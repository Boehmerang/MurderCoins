package murdercoins.tileentity;

import java.util.ArrayList;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.registry.LanguageRegistry;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.block.IElectricityStorage;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.ElectricItemHelper;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.implement.IRedstoneReceptor;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityAdvanced;
import universalelectricity.prefab.tile.TileEntityElectricityRunnable;
import murdercoins.common.Config;
import murdercoins.common.MurderCoins;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
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
import net.minecraftforge.oredict.OreDictionary;

public class tileEntityManPress extends TileEntityElectricityRunnable implements IInventory, ISidedInventory, IPacketReceiver, ITankContainer, IRedstoneReceptor
{
	public int processTicks = 0;
	private int playersUsing = 0;
	public int goldStored = 0;
	public int maxGold = 8 * LiquidContainerRegistry.BUCKET_VOLUME;
	public int goldPerBucket = LiquidContainerRegistry.BUCKET_VOLUME;
	public static int meltingTicks = 1500;
	
	public double joulesStored = 0.0D;
	public static double maxJoules = 1500000.0D;
	public static double joulesPerSmelt = 50000.0D;
	
	public boolean hasPower = false;
	public boolean didRun = false;
	
	
	private ItemStack[] inventory = new ItemStack[6];
	
	Config						configLoader								= new Config();

	@Override
	public void updateEntity()
	{
		
		this.meltingTicks = Config.MCPprocessTicks;
		
		super.updateEntity();
		/**
		 * Trys to press the coins, if it can, it press the coins, and then checks to see if the molds break.
		 */
		if (!this.worldObj.isRemote)
		{
			if (this.canProcess())
			{
				//if(this.didRun = true)return;
				if (this.processTicks == 0)
				{
					this.processTicks = meltingTicks;
				}
				else if (this.processTicks > 0)
				{
					this.processTicks--;

					if (this.processTicks < 1)
					{
						if (this.inventory[3] == null)
						{
							pressCoins(false);
							this.processTicks = 0;
							//this.didRun = true;
						}
						else
						{
							pressCoins(true);
							this.processTicks = 0;
							//this.didRun = true;
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
					PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
				}
			}
		}
	}

	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		return false;
	}

	@Override
	public ElectricityPack getRequest()
	{
		return new ElectricityPack();
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

	
	}
	@Override
	public Packet getDescriptionPacket()
	{
		return PacketManager.getPacket("MurderCoins", this, this.processTicks);
	}

	@Override
	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			this.processTicks = dataStream.readInt();
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
		if (this.inventory[1] == null)
		{
			this.processTicks = 0;
			return false;
		}
		if (this.inventory[2] == null)
		{
			this.processTicks = 0;
			return false;
		}
		if (this.inventory[1] != null)
		{
			if (this.inventory[1].getItem() != MurderCoins.itemPressArm)
			{
			this.processTicks = 0;
			return false;
			}
			if (this.inventory[1].getItemDamage() >= MurderCoins.itemPressArm.getMaxDamage())
			{
			this.processTicks = 0;
			return false;
			}
		}
		if (this.inventory[2] != null)
		{
			if (this.inventory[2].getItem() != MurderCoins.itemCoinMold)
			{
			this.processTicks = 0;
			return false;
			}
			if (this.inventory[2].getItemDamage() >= MurderCoins.itemCoinMold.getMaxDamage())
			{
			this.processTicks = 0;
			return false;
			}
		}
		if (this.inventory[4] == null)
		{
			return false;
		}
		if(inventory[0]!=null)
		{
			if(inventory[0].stackSize >= 16)
			{
				return false;
			}
		}
		if (inventory[5] != null)
		{
			if(inventory[5].stackSize >= 64)
			{
				inventory[5].stackSize = 64;
				return false;
			}
		}
		if(inventory[3]==null)
		{
			if(inventory[5] != null)
			{
				if(inventory[5].getItem() != MurderCoins.itemGoldCoin)
				{
				this.processTicks = 0;
				return false;
				}
			}
		}

		if (inventory[3]!=null)
		{
			if(inventory[3].getItem() == MurderCoins.itemDiamondDust)
			{
				if(inventory[5] != null)
				{
					if(inventory[5].getItem() != MurderCoins.itemDiamondCoin)
					{
					this.processTicks = 0;
					return false;
					}
				}
			}
			if(inventory[3].getItem() == MurderCoins.itemEmeraldDust)
			{
				if(inventory[5] != null)
				{
					if(inventory[5].getItem() != MurderCoins.itemEmeraldCoin)
					{
					this.processTicks = 0;
					return false;
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
			if (this.inventory[5] == null)
			{
				this.inventory[5] = itemstack;
			}
			else if (this.inventory[5].isItemEqual(new ItemStack(MurderCoins.itemGoldCoin)))
			{
				this.inventory[5].stackSize += 4;
			}
			this.decrStackSize(4, 1);
			this.damageItems();
			this.getEmptyBucket();
		}
		if(MurderCoins.MekanismLoaded == false)
		{
			if (this.inventory[3].isItemEqual(new ItemStack(MurderCoins.itemEmeraldDust)))
			{
				ItemStack itemstack = new ItemStack(MurderCoins.itemEmeraldCoin, 4);
				if (this.inventory[5] == null)
				{
				this.inventory[5] = itemstack;
				}
				else if (this.inventory[5].isItemEqual(new ItemStack(MurderCoins.itemEmeraldCoin)))
				{
					this.inventory[5].stackSize += 4;
				}
				this.decrStackSize(3, 1);
				this.decrStackSize(4, 1);
				this.damageItems();
				this.getEmptyBucket();
			}
			else if (this.inventory[3].isItemEqual(new ItemStack(MurderCoins.itemDiamondDust)))
			{
				ItemStack itemstack = new ItemStack(MurderCoins.itemDiamondCoin, 4);
				if (this.inventory[5] == null)
				{
					this.inventory[5] = itemstack;
				}
				else if (this.inventory[5].isItemEqual(new ItemStack(MurderCoins.itemDiamondCoin)))
				{
					this.inventory[5].stackSize += 4;
				}
				this.decrStackSize(3, 1);
				this.decrStackSize(4, 1);
				this.damageItems();
				this.getEmptyBucket();
			}
		}
		else
		{
			if (this.inventory[3].isItemEqual(new ItemStack(MurderCoins.itemEmeraldDust)))
			{
				ItemStack itemstack = new ItemStack(MurderCoins.itemEmeraldCoin, 4);
				if (this.inventory[5] == null)
				{
				this.inventory[5] = itemstack;
				}
				else if (this.inventory[5].isItemEqual(new ItemStack(MurderCoins.itemEmeraldCoin)))
				{
					this.inventory[5].stackSize += 4;
				}
				this.decrStackSize(3, 1);
				this.decrStackSize(4, 1);
				this.damageItems();
				this.getEmptyBucket();
			}
			else 
			{
				ArrayList<ItemStack> tList = OreDictionary.getOres("dustDiamond");
				for (int i = 0; i < tList.size(); i++)
				{
					ItemStack tStack = tList.get(i);
					tStack = tStack.copy();
					tStack.stackSize = 1;
					if (this.inventory[3].isItemEqual(tStack))
					{
						ItemStack itemstack = new ItemStack(MurderCoins.itemDiamondCoin, 4);
						if (this.inventory[5] == null)
						{
							this.inventory[5] = itemstack;
						}
						else if (this.inventory[5].isItemEqual(new ItemStack(MurderCoins.itemDiamondCoin)))
						{
							this.inventory[5].stackSize += 4;
						}
						this.decrStackSize(3, 1);
						this.decrStackSize(4, 1);
						this.damageItems();
						this.getEmptyBucket();
					}
				}
			}
			
		}
	}
	
	public void damageItems()
	{
		if (this.inventory[1].getItem() != null)
		{
			this.inventory[1].setItemDamage(this.inventory[1].getItemDamage()+1);
		}
		if (this.inventory[2].getItem() != null)
		{
			this.inventory[2].setItemDamage(this.inventory[2].getItemDamage()+1);
		}
	}
	
	private void getEmptyBucket()  //returns an empty bucket in slot 7.
	{
		if(inventory[0] == null)
		{
			inventory[0] = new ItemStack(Item.bucketEmpty);
		}
		else if (inventory[0] != null)
		{
			inventory[0].stackSize += 1;
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
		par1NBTTagCompound.setDouble("joules", this.joulesStored);
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
		return LanguageRegistry.instance().getStringLocalization("manPress");
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
				return slotID == 3;
			}
		}
		if (itemStack.isItemEqual(emDust))
		{
			return slotID == 4;
		}
		if (itemStack.isItemEqual(meltedBuck))
		{
			return slotID == 4;
		}
		return false;
	}

	@Override
	public int fill(ForgeDirection from, LiquidStack resource, boolean doFill) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
		// TODO Auto-generated method stub
		return 0;
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
	public ILiquidTank[] getTanks(ForgeDirection direction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILiquidTank getTank(ForgeDirection direction, LiquidStack type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onPowerOn()
	{
		// TODO Auto-generated method stub
		//this.hasPower = true;
		System.out.println("debug");
	}

	@Override
	public void onPowerOff()
	{
		// TODO Auto-generated method stub
		//this.hasPower = false;
		//this.didRun = false;
		System.out.println("debug");
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		if (side == 0)
		{
			return new int[] {0,5};
		}
		else if (side != 0)
		{
			return new int[] {3,4};
		}
		return null;
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack itemstack, int side) 
	{
		return isStackValidForSlot(slotID, itemstack);
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, int side) 
	{
		return (slotID == 0 || slotID == 5);
	}
}