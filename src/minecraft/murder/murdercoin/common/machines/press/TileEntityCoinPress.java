package murder.murdercoin.common.machines.press;

import murder.murdercoin.common.MurderCoins;
import murder.murdercoin.common.items.ItemCoinMold;
import murder.murdercoin.common.items.ItemDDust;
import murder.murdercoin.common.items.ItemEDust;
import murder.murdercoin.common.items.ItemMeltedBucket;
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
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidStack;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.block.IElectricityStorage;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.ElectricItemHelper;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityRunnable;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class TileEntityCoinPress extends TileEntityElectricityRunnable implements IInventory, ISidedInventory, IPacketReceiver, IElectricityStorage, ITankContainer
{
	public int processTicks = 0;
	public double joulesStored = 0.0D;
	public static double maxJoules = 1500000.0D;
	private ItemStack[] inventory = new ItemStack[8];
	private int playersUsing = 0;
	public static double joulesPerSmelt = 50000.0D;
	public static int meltingTicks = 500;
	public int goldStored = 0;
	public int maxGold = 8 * LiquidContainerRegistry.BUCKET_VOLUME;
	public int goldPerBucket = LiquidContainerRegistry.BUCKET_VOLUME;
	

	@Override
	public void updateEntity()
	{
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
					PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
				}
			}
		}
	}

	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		//return direction == ForgeDirection.getOrientation(3);
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
		return PacketManager.getPacket("MurderCoins", this, this.processTicks, this.getJoules());
	}

	@Override
	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			this.processTicks = dataStream.readInt();
			this.setJoules(dataStream.readDouble());
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
		/*if (this.inventory[5] == null)
		 *{
		 *
		 * if(this.inventory[6].isItemEqual(new ItemStack(murderCoins.meltedBucket))) { return
		 * true; } /*if (this.inventory[6].isItemEqual(new ItemStack(murderCoins.meltedBucket)))
		 * { return true; } /*if (pipeConnected()) { return true; }
		 *
		 *	return false;
		 *}
		 */
		if (this.inventory[6] == null)
		{
			this.processTicks = 0;
			return false;
		}
		if(inventory[2]!=null)
		{
			if(inventory[2].stackSize >= 16)
			{
				inventory[2].stackSize = 16;
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
			if(inventory[7] != null)
			{
				if(inventory[7].getItem() != MurderCoins.itemGoldCoin)
				{
				this.processTicks = 0;
				return false;
				}
			}
		}
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
		/*
		 * else if (this.inventory[5].isItemEqual(new ItemStack(murderCoins.eDust))) { if
		 * (this.inventory[6].isItemEqual(new ItemStack(murderCoins.meltedBucket))){ return true; }
		 * this.processTicks = 0; return false; }
		 */

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
			// this.decrStackSize(5, 1); no dust to deduct.
			this.decrStackSize(6, 1);
			this.getEmptyBucket();
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
			this.decrStackSize(6, 1);
			this.getEmptyBucket();
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
			this.decrStackSize(6, 1);
			this.getEmptyBucket();
			this.setJoules(this.getJoules() - this.joulesPerSmelt);
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
	private void getEmptyBucket()  //returns an empty bucket in slot 7.
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
		if(slotID==0)
		{
			return itemStack.getItem() instanceof IItemElectric;
		}
		else if(slotID==1)
		{
			return itemStack.getItem() instanceof IItemElectric;
		}
		else if(slotID==2)
		{
			return false;
		}
		else if(slotID==3)
		{
			return itemStack.getItem() instanceof ItemCoinMold;
			//return false;
		}
		else if(slotID==4)
		{
			return itemStack.getItem() instanceof ItemCoinMold;
		}
		else if(slotID==6)
		{
			return itemStack.getItem() instanceof ItemMeltedBucket;
		}
		
		else if(slotID==7)
		{
			return false;
		}
		else
		{
		return true;
		}
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
}