package murdercoins.tileentity;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.registry.LanguageRegistry;

import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityAdvanced;
import murdercoins.common.Config;
import murdercoins.common.MurderCoins;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class tileEntityBasicVault extends TileEntityAdvanced implements IInventory, ISidedInventory, IPacketReceiver
{
	
	private int playersUsing = 0;
	public ItemStack[] inventory = new ItemStack[45];
	public String vaultOwner1 = " ";
	public String vaultOwner2 = " ";
	public String vaultOwner3 = " ";
	public String vaultOwner4 = " ";
	public String vaultOwner5 = " ";
	public String vaultOwner6 = " ";
	public String vaultOwner7 = " ";
	public EntityPlayer Owner1;
	
	public tileEntityBasicVault()
	{
		this.vaultOwner2 = "Squidicuz";
	}
	public void setOwners(String playerName, int ownerNumber)
	{
		switch (ownerNumber)
		{
			case 1:
				this.vaultOwner1 = playerName;
				//System.out.println(this.vaultOwner1);
				break;
			case 2:
				this.vaultOwner2 = playerName;
				//System.out.println(this.vaultOwner2);
				break;
			case 3:
				this.vaultOwner3 = playerName;
				//System.out.println(this.vaultOwner3);
				break;
			case 4:
				this.vaultOwner4 = playerName;
				//System.out.println(this.vaultOwner4);
				break;
			case 5:
				this.vaultOwner5 = playerName;
				//System.out.println(this.vaultOwner5);
				break;
			case 6:
				this.vaultOwner6 = playerName;
				//System.out.println(this.vaultOwner6);
				break;
			case 7:
				this.vaultOwner7 = playerName;
				//System.out.println(this.vaultOwner7);
				break;
			default:
				break;
		}
		
	}
	public String getOwners(int owner)
	{
		switch(owner)
		{
		case 1:
			return this.vaultOwner1;
		case 2:
			return this.vaultOwner2;
		case 3:
			return this.vaultOwner3;
		case 4:
			return this.vaultOwner4;
		case 5:
			return this.vaultOwner5;
		case 6:
			return this.vaultOwner6;
		case 7:
			return this.vaultOwner7;
		default:
			return this.vaultOwner1;
		}
	}
	
	@Override
	public void updateEntity()
	{	
		super.updateEntity();
		
		if (!this.worldObj.isRemote)
		{
			
			if (this.ticks % 3 == 0 && this.playersUsing > 0)
			{
				PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
			}
			this.onInventoryChanged();
		}
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return PacketManager.getPacket("MurderCoins", this, this.vaultOwner1, this.vaultOwner2, this.vaultOwner3, this.vaultOwner4, this.vaultOwner5, this.vaultOwner6);
	}
	@Override
	public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream) 
	{
		try
		{
		this.vaultOwner1 = dataStream.readUTF();
		this.vaultOwner2 = dataStream.readUTF();
		this.vaultOwner3 = dataStream.readUTF();
		this.vaultOwner4 = dataStream.readUTF();
		this.vaultOwner5 = dataStream.readUTF();
		this.vaultOwner6 = dataStream.readUTF();
		}
		catch (Exception e)
		{
			this.vaultOwner1 = " ";
			this.vaultOwner2 = " ";
			this.vaultOwner3 = " ";
			this.vaultOwner4 = " ";
			this.vaultOwner5 = " ";
			this.vaultOwner6 = " ";
			System.out.print("Failed to sync owners of Vault at: " + Integer.toString(this.xCoord) + ", " + Integer.toString(this.yCoord) + ", " + Integer.toString(this.zCoord) + ".");
		}
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) 
	{
		return new int[] {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44};
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack itemstack, int side) 
	{
		return isStackValidForSlot(slotID, itemstack);
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j)
	{
		return false;
	}

	@Override
	public int getSizeInventory() 
	{
		return this.inventory.length;
	}

	public void putStackInSlot(int slotID, ItemStack stack)
	{
		this.inventory[slotID] = stack;
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
		return LanguageRegistry.instance().getStringLocalization("basicVault");
	}

	@Override
	public boolean isInvNameLocalized() 
	{
		return true;
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

	@Override
	public boolean isStackValidForSlot(int slotID, ItemStack itemstack) 
	{
		if(slotID >=27 && slotID <= 32) 
		{
			if (itemstack.getItem() == MurderCoins.itemGoldCoin)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else if (slotID >= 33 && slotID <= 38) 
		{
			if (itemstack.isItemEqual(new ItemStack(MurderCoins.itemDiamondCoin)))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else if (slotID >= 39 && slotID <= 44) 
		{
			if (itemstack.getItem() == MurderCoins.itemEmeraldCoin)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else if (slotID >=0 && slotID <= 8)
		{
			return false;
		}
		else if (slotID >=9 && slotID <= 17)
		{
			if (this.inventory[slotID-9] != null)
			{
				if (itemstack.getItem() == this.inventory[slotID-9].getItem())
				{
					return true;
				}
				else 
				{
					return false;
				}
			}
			else if(this.inventory[slotID-9] == null)
			{
				return false;
			}
		}
		else if (slotID >=18 && slotID <= 26)
		{
			if(this.inventory[slotID-18] != null)
			{
				if (itemstack.getItem() == this.inventory[slotID-18].getItem())
				{
					return true;
				}
				else 
				{
					return false;
				}
			}
			else if (this.inventory[slotID - 18] == null)
			{
				return false;
			}
		}
		return false;
	}
	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.vaultOwner1 = par1NBTTagCompound.getString("owner1");
		this.vaultOwner2 = par1NBTTagCompound.getString("owner2");
		this.vaultOwner3 = par1NBTTagCompound.getString("owner3");
		this.vaultOwner4 = par1NBTTagCompound.getString("owner4");
		this.vaultOwner5 = par1NBTTagCompound.getString("owner5");
		this.vaultOwner6 = par1NBTTagCompound.getString("owner6");
		NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
		this.inventory = new ItemStack[this.getSizeInventory()];

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
		par1NBTTagCompound.setString("owner1", this.vaultOwner1);
		par1NBTTagCompound.setString("owner2", this.vaultOwner2);
		par1NBTTagCompound.setString("owner3", this.vaultOwner3);
		par1NBTTagCompound.setString("owner4", this.vaultOwner4);
		par1NBTTagCompound.setString("owner5", this.vaultOwner5);
		par1NBTTagCompound.setString("owner6", this.vaultOwner6);
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

	
	public Boolean safeHasCoins(int type)
	{
		switch (type)
		{
		case 1:
			if ( getStackInSlot(27) != null)
				return true;
			if ( getStackInSlot(28) != null)
				return true;
			if ( getStackInSlot(29) != null)
				return true;
			if ( getStackInSlot(30) != null)
				return true;
			if ( getStackInSlot(31) != null)
				return true;
			if ( getStackInSlot(32) != null)
				return true;
		case 2:
			if ( getStackInSlot(33) != null)
				return true;
			if ( getStackInSlot(34) != null)
				return true;
			if ( getStackInSlot(35) != null)
				return true;
			if ( getStackInSlot(36) != null)
				return true;
			if ( getStackInSlot(37) != null)
				return true;
			if ( getStackInSlot(38) != null)
				return true;
		case 3:
			if ( getStackInSlot(39) != null)
				return true;
			if ( getStackInSlot(40) != null)
				return true;
			if ( getStackInSlot(41) != null)
				return true;
			if ( getStackInSlot(42) != null)
				return true;
			if ( getStackInSlot(43) != null)
				return true;
			if ( getStackInSlot(44) != null)
				return true;
		}
		
		return false;
	}
	

}
