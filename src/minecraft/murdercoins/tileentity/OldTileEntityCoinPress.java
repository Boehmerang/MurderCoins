package murdercoins.tileentity;

import java.util.EnumSet;

import murdercoins.common.MurderCoins;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.ElectricItemHelper;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityRunnable;

import com.google.common.io.ByteArrayDataInput;

public class OldTileEntityCoinPress extends TileEntityElectricityRunnable implements IInventory, IPacketReceiver
{
	
	
	
	
	
	
	
	
	
	
	
	
	//		Power Use and Ticks per process of Gold Forge
	
	public static int GFprocessTicks;
	public static int GFfrozenTicks;
	public static double GFjoulesPerUse;
	public static double GFtankJoules;
	public static double GFunfreezeJoules;
	
	//		Power Use and Ticks per process of Coin Press
	
	public static int CPprocessTicks;
	public static int CPfrozenTicks;
	public static double CPjoulesPerUse;
	public static double CPtankJoules;
	public static double CPunfreezeJoules;
	
	//		Tick's per process of Manual Coin Press
	
	public static int MCPprocessTicks; 
	
	//		Power Use and Ticks per Process of Pulverisor
	
	public static int PprocessTicks;
	public static double PjoulesPerUse;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static final double WATTS_PER_TICK = 500.0D;
	public static final double joulesPerSmelt = 50000.0D;
	public double prevJoules = 0;

	private ItemStack[] inventory = new ItemStack[8];

	public static final int meltingTicks = 500;
	private int playersUsing = 0;
	private int recipeTicks;
	public int processTicks = 0;

	@Override
	public void initiate()
	{
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, MurderCoins.goldForge.blockID);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			this.prevJoules = this.wattsReceived;
			/**
			 * Decharge electric item.
			 */
			this.wattsReceived += ElectricItemHelper.dechargeItem(this.inventory[0], this.getWattBuffer() - this.wattsReceived, getVoltage());

			if (canPress() && this.wattsReceived >= WATTS_PER_TICK)
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
						if (this.inventory[5].getItem() == MurderCoins.itemDiamondDust || this.inventory[5].getItem() == MurderCoins.itemEmeraldDust)
						{
							pressCoins(true);
							this.processTicks = 0;
						}
						else
						{
							pressCoins(false);
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
	public ElectricityPack getRequest()
	{
		return new ElectricityPack((this.wattsReceived - this.WATTS_PER_TICK / this.getVoltage()), this.getVoltage());
	}

	@Override
	protected EnumSet<ForgeDirection> getConsumingSides()
	{
		return ElectricityNetworkHelper.getDirections(this);
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return PacketManager.getPacket("MurderCoins", this, this.wattsReceived, this.processTicks, this.disabledTicks);
	}

	@Override
	public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			if (worldObj.isRemote)
			{
				this.wattsReceived = dataStream.readDouble();
				this.processTicks = dataStream.readInt();
				this.disabledTicks = dataStream.readInt();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/*
	 * public boolean canPress() { if (inventory[3]==null) { return false; } if(inventory[4] ==
	 * null) { return false; } if (inventory[5].isItemEqual(new ItemStack(murderCoins.dDust))) { if
	 * (inventory[6].isItemEqual(new ItemStack(murderCoins.meltedBucket))) { return true; } //if
	 * (pipeConnected()) // { // return true; // } // return false; } else if
	 * (inventory[5].isItemEqual(new ItemStack(murderCoins.eDust))) { if
	 * (inventory[6].isItemEqual(new ItemStack(murderCoins.meltedBucket))){
	 *
	 * return true; } return false; } else { return false; } }
	 */
	public boolean canPress()
	{
		if (this.inventory[3] == null)
		{
			this.processTicks = 0;
			return false;
		}
		if (this.inventory[4] == null)
		{
			// if(pipeConnected())
			// {
			// return true;
			// }
			this.processTicks = 0;
			return false;
		}
		if (this.inventory[5] == null)
		{
			/*
			 * if(this.inventory[6].isItemEqual(new ItemStack(murderCoins.meltedBucket))) { return
			 * true; } /*if (this.inventory[6].isItemEqual(new ItemStack(murderCoins.meltedBucket)))
			 * { return true; } /*if (pipeConnected()) { return true; }
			 */
			return false;
		}
		if (this.inventory[6] == null)
		{
			return false;
		}
		/*
		 * else if (this.inventory[5].isItemEqual(new ItemStack(murderCoins.eDust))) { if
		 * (this.inventory[6].isItemEqual(new ItemStack(murderCoins.meltedBucket))){ return true; }
		 * this.processTicks = 0; return false; }
		 */

		return true;
	}

	public void pressCoins(boolean isDust)
	{
		if (!canPress())
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
			// this.decrStackSize(5, 1); no dust to downgrade.
			this.decrStackSize(6, 1);
			this.wattsReceived -= this.joulesPerSmelt;
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
			this.wattsReceived -= this.joulesPerSmelt;
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
			this.wattsReceived -= this.joulesPerSmelt;
		}
	}
	public void breakMolds()
	{
		ItemStack broken = new ItemStack(MurderCoins.brokenMold,1);
		double moldbreak = Math.random() * 10;
		if(moldbreak==5)
		{
			this.inventory[2] = null;
			this.inventory[2] = broken;
		}
		if(moldbreak ==9)
		{
			this.inventory[3] = null;
			this.inventory[3] = broken;
		}
	}
	/**
	 * Reads a tile entity from NBT.
	 */

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		this.processTicks = nbt.getInteger("processTicks");
		this.wattsReceived = nbt.getDouble("joulesStored");

		/* INV LOADER */
		NBTTagList var2 = nbt.getTagList("Items");
		this.inventory = new ItemStack[getSizeInventory()];
		for (int var3 = 0; var3 < var2.tagCount(); var3++)
		{
			NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
			byte var5 = var4.getByte("Slot");

			if ((var5 >= 0) && (var5 < this.inventory.length))
				this.inventory[var5] = ItemStack.loadItemStackFromNBT(var4);
		}
	}

	/**
	 * Writes a tile entity to NBT.
	 */

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("processTicks", this.processTicks);
		nbt.setDouble("joulesStored", this.wattsReceived);

		/* INV WRITER */
		NBTTagList var2 = new NBTTagList();
		for (int var3 = 0; var3 < this.inventory.length; var3++)
		{
			if (this.inventory[var3] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				this.inventory[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}
		nbt.setTag("Items", var2);
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
		return "Coin Press";
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

	public int getProcessTicks()
	{
		// TODO Auto-generated method stub

		return processTicks;
	}

	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		return true;
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return false;
	}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack)
	{
		return false;
	}

	public double getJoules()
	{
		return this.wattsReceived;
	}
	@Override
	public double getWattBuffer()
	{
		return this.getRequest().getWatts() * 10;
	}
}