package murder.murdercoin.common.machines.pulverisor;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.registry.LanguageRegistry;

import murder.murdercoin.common.MurderCoins;
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
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.block.IElectricityStorage;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.ElectricItemHelper;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityRunnable;

public class TilePulverisor extends TileEntityElectricityRunnable implements IInventory, ISidedInventory, IPacketReceiver, IElectricityStorage
{
	public int processTicks = 0;
	public double joulesStored = 0.0D;
	public static double maxJoules = 1500000.0D;
	private ItemStack[] inventory = new ItemStack[3];
	private int playersUsing = 0;
	public static double joulesPerSmelt = 50000.0D;
	public static int crushingTicks = 100;
	public int facing;
	public boolean isRunning = false;
	
	public void updateEntity()
	{
		super.updateEntity();
		/**
	 	* Attempts to charge from battery in slot 1.
	 	*/
		this.setJoules(this.getJoules() + ElectricItemHelper.dechargeItem(this.inventory[0], this.getMaxJoules() - this.getJoules(), this.getVoltage()));
		/*
		 * will attempt to crush the item.
		 */
		if (!this.worldObj.isRemote)
		{
			if (this.canProcess())
			{
				if (this.getJoules() >= joulesPerSmelt)
				{
					if (this.processTicks == 0)
					{
						this.processTicks = this.crushingTicks;
						this.isRunning = true;
					}
					else if (this.processTicks > 0)
					{
						this.processTicks--;

						/**
						 * Process the item when the process timer is done.
						 */
						if (this.processTicks < 1)
						{
							if(this.inventory[1].getItem() == Item.diamond)
							{
								this.crushItem(true);
								this.processTicks = 0;
								this.setJoules(getJoules() - joulesPerSmelt);
								this.isRunning = false;
							}
							else if(this.inventory[1].getItem() == Item.emerald)
							{
								this.crushItem(false);
								this.processTicks = 0;
								this.setJoules(getJoules() - joulesPerSmelt);
								this.isRunning = false;
							}
						}
					}
					else
					{
						this.processTicks = 0;
					}
				}
				else
				{
					this.processTicks = 0;
				}
			}
			else
			{
				this.processTicks = 0;
			}
			if (this.ticks % 3 == 0 && this.playersUsing > 0)
			{
				PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
			}
		}
	}
	public int getFacing()
	{
		return facing;
	}

	public void setFacing(int facing)
	{
		this.facing = facing;
	}
	public boolean canProcess()
	{
		if (this.getJoules() >=this.joulesPerSmelt)
		{
			if(this.inventory[1] != null)
			{
				if(this.inventory[1].getItem() == Item.diamond)
				{
					if(this.inventory[2] == null)return true;
					else if (this.inventory[2]!=null)
					{
						if(this.inventory[2].getItem() == MurderCoins.itemDiamondDust) return true;
						else
						{
							this.processTicks = 0;
							return false;
						}
					}
					this.processTicks = 0;
					return false;
				}
				if(this.inventory[1].getItem() == Item.emerald)
				{
					if (this.inventory[2]==null)return true;
					else if (this.inventory[2]!=null)
					{
						if (this.inventory[2].getItem() == MurderCoins.itemEmeraldDust) return true;
						else 
						{
							this.processTicks = 0;
							return false;
						}
					}
					this.processTicks = 0;
					return false;
				}
				this.processTicks = 0;
				return false;
			}
			this.processTicks = 0;
			return false;
		}
		this.processTicks = 0;
		return false;
	}
	public void crushItem(boolean isDiamond)
	{
		if(isDiamond)
		{			
			if (this.inventory[2] != null)
			{
				if (this.inventory[2].stackSize>=64)
				{
					this.inventory[2].stackSize = 64;
					return;
				}
				else
				{
					this.inventory[2].stackSize++;
					this.decrStackSize(1, 1);
				}
			}
			else
			{
				ItemStack itemstack = new ItemStack(MurderCoins.itemDiamondDust,1);
				this.inventory[2] = itemstack;
				this.decrStackSize(1, 1);  	
			}
		}
		else
		{
			if (this.inventory[2] != null)
			{
				if (this.inventory[2].stackSize>=64)
				{
					this.inventory[2].stackSize = 64;
					return;
				}
				else
				{
					this.inventory[2].stackSize++;
					this.decrStackSize(1, 1);
				}
			}
			else
			{
				ItemStack itemstack = new ItemStack(MurderCoins.itemEmeraldDust,1);
				this.inventory[2] = itemstack;
				this.decrStackSize(1, 1);  	
			}
		}
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
		this.facing = par1NBTTagCompound.getShort("facing");

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
		par1NBTTagCompound.setShort("facing", (short) this.facing);

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
	public int[] getSizeInventorySide(int side)
	{
		return side == 0 ? new int[] { 2 } : (side == 1 ? new int[] { 0, 1 } : new int[] { 0 });
	}
	@Override
	public boolean func_102007_a(int i, ItemStack itemstack, int j) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean func_102008_b(int i, ItemStack itemstack, int j) {
		// TODO Auto-generated method stub
		return false;
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
	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		return direction == ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite();
	}
	public boolean getRunning() 
	{
		if (this.isRunning == true)return true;
		return false;
	}
}
