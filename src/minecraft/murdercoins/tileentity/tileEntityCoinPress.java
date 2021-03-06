package murdercoins.tileentity;

import java.util.ArrayList;

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
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;
import net.minecraftforge.oredict.OreDictionary;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.block.IElectricalStorage;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.ElectricItemHelper;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectrical;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class tileEntityCoinPress extends TileEntityElectrical implements IInventory, ISidedInventory, IPacketReceiver, IElectricalStorage, ITankContainer//, ITubeConnection
{
	public int processTicks = 0;
	public int tankWarmingTicks = 0;										// # of ticks machine has been in "warming state"
	public int ticksToWarm = 100;
	public int drainBucketTicks = 0;										// # variable for draining buckets
	public int ticksToDrainBucket = 20;										// # of ticks it takes to drain a bucket
	public int ticksWithoutPower = 0;										// # of ticks machine has not had power
	public int ticksTillFreeze = 1200;										// # of ticks until machine enters "Frozen" state
	public int meltingTicks = 500;											// Ticks it takes to melt a gold ingot
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
	
	public LiquidTank 			CPtank; 										
	
	Config						configLoader								= new Config();

	public tileEntityCoinPress()
	{
		this.meltingTicks = Config.CPprocessTicks;
		this.ticksToWarm = Config.CPticksToWarm;
		this.ticksTillFreeze = Config.CPticksTillFreeze;
		this.joulesPerSmelt = Config.CPjoulesPerUse;
		this.tankJoules = Config.CPtankJoules;
		this.unfreezeJoules = Config.CPunfreezeJoules;
		this.CPtank = new LiquidTank(this.maxGold);
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		/**
		 * Attempts to charge from battery in slot 1.
		 */
		this.setJoules(this.getJoules() + ElectricItemHelper.dischargeItem(this.inventory[0], (float) (this.getMaxJoules() - this.getJoules())));

		
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
					this.tankWarmingTicks = (this.getStoredGold().amount/1000)*ticksToWarm;
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
			else if ( this.CPtank.getLiquid() != null)
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
			if(this.inventory[6] != null && this.isFrozen == false)
			{
				LiquidStack liquid = LiquidDictionary.getLiquid("Gold", this.goldPerBucket);
				if( this.CPtank.getLiquid()!=null)
				{
					if( this.CPtank.getLiquid().amount < this.maxGold)
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
								this.CPtank.fill(liquid, true);
								this.decrStackSize(6, 1);
								this.getEmptyBucket();
								this.drainBucketTicks=0;
			    			}
						}
					}
				}
				else if (this.CPtank.getLiquid()==null)
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
							this.CPtank.setLiquid(liquid);
							this.decrStackSize(6, 1);
							this.getEmptyBucket();
							this.drainBucketTicks=0;
		    			}
					}
				}
				PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
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
							this.processTicks = 0;
						}
						else
						{
							pressCoins(true);
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
					if( this.CPtank.getLiquid() != null)
					{
						if ( this.CPtank.getLiquid().amount > 0)
						{
							this.setJoules(this.getJoules() - this.tankJoules );
						}
						PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
					}
				}
			}
		}
	}


	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		return direction == ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite();
	}

	
	public ElectricityPack getRequest()
	{
		return new ElectricityPack((float) ((this.getMaxJoules() - this.getJoules()) / this.getVoltage()), this.getVoltage());
	}

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
	
	public LiquidStack getStoredGold()
	{
		return CPtank.getLiquid();
	}
	@Override
	public Packet getDescriptionPacket()
	{
		if( this.CPtank.getLiquid() != null)
		{
			return PacketManager.getPacket("MurderCoins", this, this.processTicks, this.getJoules(), this.getStoredGold().itemID, this.getStoredGold().amount, this.getStoredGold().itemMeta, this.isFrozen, this.tankWarmingTicks);
		}
		else
		{
			return PacketManager.getPacket("MurderCoins", this, this.processTicks, this.getJoules(), 0, 0, 0, this.isFrozen, this.tankWarmingTicks, 0);
		}
	}

	@Override
	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			this.processTicks = dataStream.readInt();
			this.setJoules(dataStream.readDouble());
			this.CPtank.setLiquid(new LiquidStack(dataStream.readInt(), dataStream.readInt(), dataStream.readInt()));
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
			if (this.inventory[3].getItem() != MurderCoins.itemPressArm)
			{
			this.processTicks = 0;
			return false;
			}
			if (this.inventory[3].getItemDamage() >= MurderCoins.itemPressArm.getMaxDamage())
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
			if (this.inventory[4].getItemDamage() >= MurderCoins.itemCoinMold.getMaxDamage())
			{
			this.processTicks = 0;
			return false;
			}
		}
		if ( this.CPtank.getLiquid() == null)
		{
			this.processTicks = 0;
			return false;
		}
		if ( this.CPtank.getLiquid() != null)
		{
			if( this.CPtank.getLiquid().amount < this.goldPerBucket)
			{
				this.processTicks = 0;
				return false;
			}
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
			if(configLoader.gCoinsOn == true)
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
			else if(configLoader.gCoinsOn == false)
			{
				return false;
			}
		}
			if (inventory[5]!=null)
			{
				ArrayList<ItemStack> dList = OreDictionary.getOres("dustDiamond");
				ArrayList<ItemStack> eList = OreDictionary.getOres("dustEmerald");
				for (int i = 0; i < dList.size(); i++)
				{
					ItemStack dStack = dList.get(i);
					dStack = dStack.copy();
					dStack.stackSize = 1;
					if(inventory[5].isItemEqual(dStack))
					{
						if(inventory[5].stackSize < 4)
						{
							return false;
						}
						if(inventory[7] != null)
						{
							if(inventory[7].getItem() != MurderCoins.itemDiamondCoin)
							{
							this.processTicks = 0;
							return false;
							}
						}
					}
				}
				for (int i = 0; i < eList.size(); i++)
				{
					if(inventory[5].getItem() == MurderCoins.itemEmeraldDust)
					{
						ItemStack eStack = eList.get(i);
						eStack = eStack.copy();
						eStack.stackSize = 1;
						if(inventory[5].isItemEqual(eStack))
						{
							if(inventory[5].stackSize < 4)
							{
								return false;
							}
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
			this.CPtank.drain(goldPerBucket, true);
			this.damageItems();
			this.setJoules(this.getJoules() - this.joulesPerSmelt);
			return;
		}
		else
		{
			ArrayList<ItemStack> dList = OreDictionary.getOres("dustDiamond");
			ArrayList<ItemStack> eList = OreDictionary.getOres("dustEmerald");
			for (int i = 0; i < dList.size(); i++)
			{
				ItemStack eStack = eList.get(i);
				eStack = eStack.copy();
				eStack.stackSize = 1;

				if (this.inventory[5].isItemEqual(eStack))
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
					this.decrStackSize(5, 4);
					this.CPtank.drain(goldPerBucket, true);
					this.damageItems();
					this.setJoules(this.getJoules() - this.joulesPerSmelt);
					return;
				}
			}
			for (int i = 0; i < dList.size(); i++)
			{
				ItemStack dStack = dList.get(i);
				dStack = dStack.copy();
				dStack.stackSize = 1;
				if (this.inventory[5].isItemEqual(dStack))
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
					this.decrStackSize(5, 4);
					this.CPtank.drain(goldPerBucket, true);
					this.damageItems();
					this.setJoules(this.getJoules() - this.joulesPerSmelt);
					return;
				}
			}
		}
	}
	
	public void damageItems()
	{
		if (this.inventory[3].getItem() != null)
		{
			this.inventory[3].setItemDamage(this.inventory[3].getItemDamage()+1);
		}
		if (this.inventory[4].getItem() != null)
		{
			this.inventory[4].setItemDamage(this.inventory[4].getItemDamage()+1);
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
		//this.goldStored = par1NBTTagCompound.getInteger("goldStored");
		this.isFrozen = par1NBTTagCompound.getBoolean("isFrozen");
		this.tankWarmingTicks = par1NBTTagCompound.getInteger("tankWarmingTicks");
		this.ticksWithoutPower = par1NBTTagCompound.getInteger("ticksWithoutPower");

		if(par1NBTTagCompound.hasKey("liquidTank"))
    	{
			 this.CPtank.readFromNBT(par1NBTTagCompound.getCompoundTag("liquidTank"));
    	}
		
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
		//par1NBTTagCompound.setInteger("goldStored", this.goldStored);
		par1NBTTagCompound.setBoolean("isFrozen", isFrozen);
		par1NBTTagCompound.setInteger("tankWarmingTicks", this.tankWarmingTicks);
		par1NBTTagCompound.setInteger("ticksWithoutPower", this.ticksWithoutPower);
		   
		if( this.CPtank.getLiquid() != null)
	    {
		   	par1NBTTagCompound.setTag("liquidTank", this.CPtank.writeToNBT(new NBTTagCompound()));
	    }
		
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

	//@Override
	public double getJoules()
	{
		return this.joulesStored;
	}

	//@Override
	public void setJoules(double joules)
	{
		this.joulesStored = Math.max(Math.min(joules, getMaxJoules()), 0);
	}

	//@Override
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
		else if ( this.CPtank.getLiquid() != null && !resource.isLiquidEqual( this.CPtank.getLiquid()))
		{
			return 0;
		}
		return this.CPtank.fill(resource, doFill);
	}

	@Override
	public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILiquidTank[] getTanks(ForgeDirection direction) 
	{
		
		return new ILiquidTank[] { CPtank };	}

	@Override
	public ILiquidTank getTank(ForgeDirection direction, LiquidStack type) 
	{
		if (type == null)
		{
			return null;
		}
		if (type.isLiquidEqual( this.CPtank.getLiquid()))
		{
			return CPtank;
		}
		return null;
	}

	//@Override
	public boolean canTubeConnect(ForgeDirection side) 
	{
		return true;
	}
	
	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring stack size) into
	 * the given slot.
	 */
	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemStack)
	{
		ArrayList<ItemStack> dList = OreDictionary.getOres("dustDiamond");
		ArrayList<ItemStack> eList = OreDictionary.getOres("dustEmerald");
		ItemStack meltedBuck = new ItemStack(MurderCoins.bucketGold);
		ItemStack emDust = new ItemStack (MurderCoins.itemEmeraldDust);
		for (int i = 0; i < dList.size(); i++)
		{
			ItemStack dStack = dList.get(i);
			dStack = dStack.copy();
			dStack.stackSize = 1;
			ItemStack eStack = eList.get(i);
			eStack = eStack.copy();
			eStack.stackSize = 1;
			if (itemStack.isItemEqual(dStack)||itemStack.isItemEqual(eStack))
			{
				return slotID == 5;
			}
		}
		if (itemStack.isItemEqual(emDust))
		{
			return slotID == 5;
		}
		if (itemStack.isItemEqual(new ItemStack(MurderCoins.bucketGold)))//(meltedBuck))
		{
			return slotID == 6;
		}
		if(itemStack.getItem() instanceof IItemElectric)
		{
			return slotID == 0;
		}
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) 
	{
		if (side == 0)
		{
			return new int[] {0,2,7};
		}
		else if (side == 1)
		{
			return new int[] {0,5,6};
		}
		return null;
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack itemstack, int side)
	{
		return isItemValidForSlot(slotID, itemstack);
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, int side)
	{
		if (itemstack.getItem() instanceof IItemElectric && ((IItemElectric)itemstack.getItem()).getElectricityStored(itemstack) == 0) 
		{
			return slotID==0;
		}
		return (slotID == 2 || slotID == 7);
	}

	@Override
	public float getRequest(ForgeDirection direction) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getProvide(ForgeDirection direction) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getMaxEnergyStored() {
		// TODO Auto-generated method stub
		return 0;
	}
}