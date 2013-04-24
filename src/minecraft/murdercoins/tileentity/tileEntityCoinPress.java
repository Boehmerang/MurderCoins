package murdercoins.tileentity;

import java.util.ArrayList;

import mekanism.api.ITubeConnection;
import murdercoins.common.Config;
import murdercoins.common.MurderCoins;
import murdercoins.common.helpers.IItemDust;
import murdercoins.items.itemCoinMold;
import murdercoins.items.itemDDust;
import murdercoins.items.itemMeltedBucket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;
import net.minecraftforge.oredict.OreDictionary;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.block.IElectricityStorage;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.ElectricItemHelper;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityRunnable;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class tileEntityCoinPress extends TileEntityElectricityRunnable implements IInventory, ISidedInventory, IPacketReceiver, IElectricityStorage, ITankContainer, ITubeConnection
{
	public int processTicks = 0;
	public int tankWarmingTicks = 0;										// # of ticks machine has been in "warming state"
	public int ticksToWarm = 100;
	public int drainBucketTicks = 0;										// # variable for draining buckets
	public int ticksToDrainBucket = 20;										// # of ticks it takes to drain a bucket
	public int ticksWithoutPower = 0;										// # of ticks machine has not had power
	public int ticksTillFreeze = 1200;										// # of ticks until machine enters "Frozen" state
	public int meltingTicks = 500;									// Ticks it takes to melt a gold ingot
	public int goldStored = 0;												// Amount of gold currently stored
	public int maxGold = 20 * LiquidContainerRegistry.BUCKET_VOLUME;		// Max amount of gold the press can store
	public int goldPerBucket = LiquidContainerRegistry.BUCKET_VOLUME;		// How much gold is in each bucket.
	private int playersUsing = 0;											// Number of players using the machine
	
	public double joulesStored = 0.0D;										// Current amount of Joules stored
	public static double maxJoules = 1500000.0D;							// Maximum amount of Joules the machine can store
	public static double joulesPerSmelt = 50000.0D;							// Joules required to melt a gold ingot
	public static double unfreezeJoules = 150000.0D;						// Joules needed to unfreeze the tank.
	private double tankJoules = 10.0D;										// Joules per tick to keep the gold tank liquid

	public boolean isFrozen = false;										// If the machine's tank is Frozen.
	
	private ItemStack[] inventory = new ItemStack[8];						// Machine inventory
	
	public LiquidTank 			tank 										= new LiquidTank(this.maxGold);
	
	Config						configLoader								= new Config();

	@Override
	public void updateEntity()
	{
		this.tank.setLiquid(MurderCoins.goldLiquid);
		this.tank.getLiquid().amount = this.goldStored;
		
		this.meltingTicks = Config.CPprocessTicks;
		this.ticksToWarm = Config.CPticksToWarm;
		this.ticksTillFreeze = Config.CPticksTillFreeze;
		this.joulesPerSmelt = Config.CPjoulesPerUse;
		this.tankJoules = Config.CPtankJoules;
		this.unfreezeJoules = Config.CPunfreezeJoules;
		super.updateEntity();
		/**
		 * Attempts to charge from battery in slot 1.
		 */
		this.setJoules(this.getJoules() + ElectricItemHelper.dechargeItem(this.inventory[0], this.getMaxJoules() - this.getJoules(), this.getVoltage()));
		/**
		 * Trys to press the coins, if it can, it press the coins, and then checks to see if the molds break.
		 */
		if (!this.worldObj.isRemote)
		{
			/*
			 * 		Checks to see if the machine is currently in "Frozen" state, if the machine is "Frozen" it will check available power, and if there
			 * 		is enough power available, it will enter the "Warming" state.
			 */
			if(this.isFrozen == true)
			{
				if (this.getJoules() > this.unfreezeJoules)
				{
					if(this.tankWarmingTicks == 0)
					{
					this.tankWarmingTicks = (this.getGold()/1000)*ticksToWarm;
					}
					else if(this.tankWarmingTicks > 1)
					{
						this.tankWarmingTicks --;
						if(this.tankWarmingTicks == 1)
						{
							this.isFrozen = false;
							this.tankWarmingTicks = 0;
						}
					}
				}
			}
			/*
			 * checks to see if there is gold in tank, and if there is enough power to warm it. If not the
			 * machine will enter "Frozen" status.
			 */
			else if (this.getGold()>0)
			{
				if(this.getJoules() < this.tankJoules)
				{
					this.ticksWithoutPower++;
					if(this.ticksWithoutPower == this.ticksTillFreeze)
					{
						this.isFrozen = true;
						this.ticksWithoutPower = 0;
					}
				}
			}

			/*
			 * Fills internal Tank from Buckets.
			 */
			if(this.inventory[6] != null && this.getGold() < this.maxGold && this.isFrozen == false)
			{
				if(this.inventory[2] != null)
				{
					if (this.inventory[2].stackSize >= 16)
					{
						return;
					}
				}
				if(this.drainBucketTicks == 0)
				{
					this.drainBucketTicks = this.ticksToDrainBucket;
				}
				else if (this.drainBucketTicks > 0)
				{
					this.drainBucketTicks--;
					if (this.drainBucketTicks < 1)
			    	{
						
						//this.tank.setCapacity(this.tank.getCapacity()-this.goldPerBucket);
						this.setGold(goldPerBucket, true);
						this.decrStackSize(6, 1);
						this.getEmptyBucket();
						this.drainBucketTicks=0;
			    	}
				}
			}
			/*
			 * Checks to see if it can process, and then presses the coins.
			 */
			if (this.canProcess() && this.getJoules() >= this.joulesPerSmelt)
			{
				if (this.processTicks == 0)
				{
					this.processTicks = meltingTicks;
				}
				else if (this.processTicks > 0)
				{
					this.processTicks--;

					if (this.processTicks < 1)
					{
						if (this.inventory[5] == null)
						{
							pressCoins(false);
							breakMolds();
							this.processTicks = 0;
						}
						else
						{
							pressCoins(true);
							breakMolds();
							this.processTicks = 0;
						}
					}
				}
				else
				{
					this.processTicks = 0;
				}
			}
			if (this.playersUsing > 0)
			{
				if (this.ticks % 3 == 0)
				{
					if(this.getGold() > 0)
					{
						this.setJoules(this.getJoules() - this.tankJoules );
					}
					PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
				}
			}
		}
	}

	//	Sets the amount of gold stored (additive or subtractive)
	public void setGold(int goldAmount, boolean add)
	{
		if(add)
		{
		this.goldStored += goldAmount;
		this.tank.getLiquid().amount = this.goldStored;
		}
		else
		{
			this.goldStored -= goldAmount;
			//this.tank.setCapacity(this.tank.getLiquid().amount - goldAmount);
			this.tank.getLiquid().amount = this.goldStored;
		}
		System.out.println(this.tank.getLiquid().amount);
		if (this.goldStored >= this.maxGold)this.goldStored = this.maxGold;
	}
	//	Returns the amount of gold stored.
	public int getGold()
	{
		return this.goldStored;
	}

	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		return direction == ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite();
	}

	
	@Override
	public ElectricityPack getRequest()
	{
		return new ElectricityPack((this.getMaxJoules() - this.getJoules()) / this.getVoltage(), this.getVoltage());
	}

	@Override
	public void onReceive(ElectricityPack electricityPack)
	{
		/**
		 * Creates an explosion if the voltage is too high.
		 */
		if (UniversalElectricity.isVoltageSensitive)
		{
			if (electricityPack.voltage > this.getVoltage())
			{
				this.worldObj.createExplosion(null, this.xCoord, this.yCoord, this.zCoord, 1.5f, true);
				return;
			}
		}

		this.setJoules(this.getJoules() + electricityPack.getWatts());
	}
	@Override
	public Packet getDescriptionPacket()
	{
		return PacketManager.getPacket("MurderCoins", this, this.processTicks, this.getJoules(),this.goldStored, this.isFrozen, this.tankWarmingTicks);
	}

	@Override
	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			this.processTicks = dataStream.readInt();
			this.setJoules(dataStream.readDouble());
			this.goldStored = dataStream.readInt();
			this.isFrozen = dataStream.readBoolean();
			this.tankWarmingTicks = dataStream.readInt();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void openChest()
	{
		if (!this.worldObj.isRemote)
		{
			PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 15);
		}
		this.playersUsing++;
	}

	@Override
	public void closeChest()
	{
		this.playersUsing--;
	}

	/**
	 * @return Is this machine able to process its specific task?
	 */
	public boolean canProcess()
	{
		if(this.isFrozen == true)
		{
			return false;
		}
		if (this.inventory[3] == null)
		{
			this.processTicks = 0;
			return false;
		}
		if (this.inventory[4] == null)
		{
			this.processTicks = 0;
			return false;
		}
		if (this.inventory[3] != null)
		{
			if (this.inventory[3].getItem() != MurderCoins.itemCoinMold)
			{
			this.processTicks = 0;
			return false;
			}
		}
		if (this.inventory[4] != null)
		{
			if (this.inventory[4].getItem() != MurderCoins.itemCoinMold)
			{
			this.processTicks = 0;
			return false;
			}
		}
		if (this.getGold()<=0)
		{
			this.processTicks = 0;
			return false;
		}
		if (inventory[7] != null)
		{
			if(inventory[7].stackSize >= 64)
			{
				inventory[7].stackSize = 64;
				this.processTicks = 0;
				return false;
			}
		}
		if(inventory[5]==null)
		{
			if(inventory[7] != null)
			{
				if(inventory[7].getItem() != MurderCoins.itemGoldCoin)
				{
				this.processTicks = 0;
				return false;
				}
			}
		}
		if (MurderCoins.MekanismLoaded == false)
		{
		if (inventory[5]!=null)
		{
			if(inventory[5].getItem() == MurderCoins.itemDiamondDust)
			{
				if(inventory[7] != null)
				{
					if(inventory[7].getItem() != MurderCoins.itemDiamondCoin)
					{
					this.processTicks = 0;
					return false;
					}
				}
			}
			if(inventory[5].getItem() == MurderCoins.itemEmeraldDust)
			{
				if(inventory[7] != null)
				{
					if(inventory[7].getItem() != MurderCoins.itemEmeraldCoin)
					{
					this.processTicks = 0;
					return false;
					}
				}
			}
		}
		}
		else if(MurderCoins.MekanismLoaded == true)
		{
			ArrayList<ItemStack> tList = OreDictionary.getOres("dustDiamond");
			for (int i = 0; i < tList.size(); i++)
			{
				ItemStack tStack = tList.get(i);
				tStack = tStack.copy();
				tStack.stackSize = 1;
				if(inventory[5].isItemEqual(tStack))
				{
					if(inventory[7] != null)
					{
						if(inventory[7].getItem() != MurderCoins.itemDiamondCoin)
						{
						this.processTicks = 0;
						return false;
						}
					}
				}
				if(inventory[5].getItem() == MurderCoins.itemEmeraldDust)
				{
					if(inventory[7] != null)
					{
						if(inventory[7].getItem() != MurderCoins.itemEmeraldCoin)
						{
						this.processTicks = 0;
						return false;
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * Turn one item from the furnace source stack into the appropriate smelted item in the furnace
	 * result stack
	 */
	public void pressCoins(boolean isDust)
	{
		if (!canProcess())
		{
			return;
		}
		if(MurderCoins.MekanismLoaded == false)
		{
			if (!isDust)
			{
				ItemStack itemstack = new ItemStack(MurderCoins.itemGoldCoin, 4);
				if (this.inventory[7] == null)
				{
					this.inventory[7] = itemstack;
				}
				else if (this.inventory[7].isItemEqual(new ItemStack(MurderCoins.itemGoldCoin)))
				{
					this.inventory[7].stackSize += 4;
				}
				this.setGold(goldPerBucket, false);
				this.setJoules(this.getJoules() - this.joulesPerSmelt);
			}
			else if (this.inventory[5].isItemEqual(new ItemStack(MurderCoins.itemDiamondDust)))
			{
				ItemStack itemstack = new ItemStack(MurderCoins.itemDiamondCoin, 4);
				if (this.inventory[7] == null)
				{
					this.inventory[7] = itemstack;
				}
				else if (this.inventory[7].isItemEqual(new ItemStack(MurderCoins.itemDiamondCoin)))
				{
					this.inventory[7].stackSize += 4;
				}
				this.decrStackSize(5, 1);
				this.setGold(goldPerBucket, false);
				this.setJoules(this.getJoules() - this.joulesPerSmelt);
			}
			else if (this.inventory[5].isItemEqual(new ItemStack(MurderCoins.itemEmeraldDust)))
			{
				ItemStack itemstack = new ItemStack(MurderCoins.itemEmeraldCoin, 4);
				if (this.inventory[7] == null)
				{
					this.inventory[7] = itemstack;
				}
				else if (this.inventory[7].isItemEqual(new ItemStack(MurderCoins.itemEmeraldCoin)))
				{
					this.inventory[7].stackSize += 4;
				}
			this.decrStackSize(5, 1);
			this.setGold(goldPerBucket, false);
			this.setJoules(this.getJoules() - this.joulesPerSmelt);
			}
		}
		else
		{
			ArrayList<ItemStack> tList = OreDictionary.getOres("dustDiamond");
			for (int i = 0; i < tList.size(); i++)
			{
				ItemStack tStack = tList.get(i);
				tStack = tStack.copy();
				tStack.stackSize = 1;
				if (!isDust)
				{
					ItemStack itemstack = new ItemStack(MurderCoins.itemGoldCoin, 4);
					if (this.inventory[7] == null)
					{
						this.inventory[7] = itemstack;
					}
					else if (this.inventory[7].isItemEqual(new ItemStack(MurderCoins.itemGoldCoin)))
					{
						this.inventory[7].stackSize += 4;
					}
					this.setGold(goldPerBucket, false);
					this.setJoules(this.getJoules() - this.joulesPerSmelt);
				}
				else if (this.inventory[5].isItemEqual(new ItemStack(MurderCoins.itemEmeraldDust)))
				{
					ItemStack itemstack = new ItemStack(MurderCoins.itemEmeraldCoin, 4);
					if (this.inventory[7] == null)
					{
						this.inventory[7] = itemstack;
					}
					else if (this.inventory[7].isItemEqual(new ItemStack(MurderCoins.itemEmeraldCoin)))
					{
						this.inventory[7].stackSize += 4;
					}
				this.decrStackSize(5, 1);
				this.setGold(goldPerBucket, false);
				this.setJoules(this.getJoules() - this.joulesPerSmelt);
				}
				else if (this.inventory[5].isItemEqual(tStack))
				{
					ItemStack itemstack = new ItemStack(MurderCoins.itemDiamondCoin, 4);
					if (this.inventory[7] == null)
					{
						this.inventory[7] = itemstack;
					}
					else if (this.inventory[7].isItemEqual(new ItemStack(MurderCoins.itemDiamondCoin)))
					{
						this.inventory[7].stackSize += 4;
					}
					this.decrStackSize(5, 1);
					this.setGold(goldPerBucket, false);
					this.setJoules(this.getJoules() - this.joulesPerSmelt);
				}
			}
		}
	}
	public void breakMolds()
	{
		ItemStack broken = new ItemStack(MurderCoins.brokenMold,1);
		double moldbreak = Math.random() * 10;
		if(moldbreak==5)
		{
			this.inventory[3] = null;
			this.inventory[3] = broken;
		}
		if(moldbreak ==9)
		{
			this.inventory[4] = null;
			this.inventory[4] = broken;
		}
	}

	//returns an empty bucket in slot 7.
	private void getEmptyBucket()
	{
		if(inventory[2] == null)
		{
			inventory[2] = new ItemStack(Item.bucketEmpty);
		}
		else if (inventory[2] != null)
		{
			inventory[2].stackSize += 1;
		}
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.processTicks = par1NBTTagCompound.getInteger("smeltingTicks");
		NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
		this.inventory = new ItemStack[this.getSizeInventory()];
		this.joulesStored = par1NBTTagCompound.getDouble("joulesStored");
		this.goldStored = par1NBTTagCompound.getInteger("goldStored");
		this.isFrozen = par1NBTTagCompound.getBoolean("isFrozen");
		this.tankWarmingTicks = par1NBTTagCompound.getInteger("tankWarmingTicks");
		this.ticksWithoutPower = par1NBTTagCompound.getInteger("ticksWithoutPower");

		for (int var3 = 0; var3 < var2.tagCount(); ++var3)
		{
			NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
			byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.inventory.length)
			{
				this.inventory[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("smeltingTicks", this.processTicks);
		par1NBTTagCompound.setDouble("joulesStored", this.joulesStored);
		par1NBTTagCompound.setInteger("goldStored", this.goldStored);
		par1NBTTagCompound.setBoolean("isFrozen", isFrozen);
		par1NBTTagCompound.setInteger("tankWarmingTicks", this.tankWarmingTicks);
		par1NBTTagCompound.setInteger("ticksWithoutPower", this.ticksWithoutPower);
		NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < this.inventory.length; ++var3)
		{
			if (this.inventory[var3] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				this.inventory[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}

		par1NBTTagCompound.setTag("Items", var2);
	}

	@Override
	public int getSizeInventory()
	{
		return this.inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int par1)
	{
		return this.inventory[par1];
	}

	@Override
	public ItemStack decrStackSize(int par1, int par2)
	{
		if (this.inventory[par1] != null)
		{
			ItemStack var3;

			if (this.inventory[par1].stackSize <= par2)
			{
				var3 = this.inventory[par1];
				this.inventory[par1] = null;
				return var3;
			}
			else
			{
				var3 = this.inventory[par1].splitStack(par2);

				if (this.inventory[par1].stackSize == 0)
				{
					this.inventory[par1] = null;
				}

				return var3;
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int par1)
	{
		if (this.inventory[par1] != null)
		{
			ItemStack var2 = this.inventory[par1];
			this.inventory[par1] = null;
			return var2;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		this.inventory[par1] = par2ItemStack;

		if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
		{
			par2ItemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInvName()
	{
		return LanguageRegistry.instance().getStringLocalization("goldForge");
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return true;
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring stack size) into
	 * the given slot.
	 */
	@Override
	public boolean isStackValidForSlot(int slotID, ItemStack itemStack)
	{
		//return slotID == 3 ? itemStack.getItem() instanceof ItemCoinMold :(slotID == 4 ? itemStack.getItem() instanceof ItemCoinMold: (slotID == 0 ? itemStack.getItem() instanceof IItemElectric :(slotID==6 ? itemStack.getItem() instanceof ItemMeltedBucket: false)));
		if(itemStack.getItem() instanceof IItemElectric)
		{
			return slotID == 0;
		}
		if(itemStack.getItem() instanceof IItemDust)
		{
			return slotID == 5; 
		}	
		
		return true;
	}

	/**
	 * Get the size of the side inventory.
	 */
	@Override
	public int[] getSizeInventorySide(int side)
	{
		return side == 0 ? new int[] { 2 } : (side == 1 ? new int[] { 0, 1 } : new int[] { 0 });
	}

	@Override
	public boolean func_102007_a(int slotID, ItemStack par2ItemStack, int par3)
	{
		return this.isStackValidForSlot(slotID, par2ItemStack);
	}

	@Override
	public boolean func_102008_b(int slotID, ItemStack par2ItemStack, int par3)
	{
		return slotID == 3;
	}

	@Override
	public double getJoules()
	{
		return this.joulesStored;
	}

	@Override
	public void setJoules(double joules)
	{
		this.joulesStored = Math.max(Math.min(joules, getMaxJoules()), 0);
	}

	@Override
	public double getMaxJoules()
	{
		return this.maxJoules;
	}

	@Override
	public int fill(ForgeDirection from, LiquidStack resource, boolean doFill)
	{
		return this.fill(0, resource, doFill);
	}

	@Override
	public int fill(int tankIndex, LiquidStack resource, boolean doFill)
	{
		if (resource == null || tankIndex != 0)
		{
			return 0;
		}
		else if (this.tank.getLiquid() != null && !resource.isLiquidEqual(this.tank.getLiquid()))
		{
			return 0;
		}
		return this.tank.fill(resource, doFill);
		
	}

	@Override
	public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILiquidTank[] getTanks(ForgeDirection direction) 
	{
		
		return new ILiquidTank[] { this.tank };	}

	@Override
	public ILiquidTank getTank(ForgeDirection direction, LiquidStack type) 
	{
		if (type == null)
		{
			return null;
		}
		if (type.isLiquidEqual(this.tank.getLiquid()))
		{
			return this.tank;
		}
		return null;
	}

	@Override
	public boolean canTubeConnect(ForgeDirection side) 
	{
		return true;
	}
}