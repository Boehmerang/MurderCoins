package murdercoins.tileentity;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.registry.LanguageRegistry;

import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityAdvanced;
import murdercoins.common.Config;
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
	private int derp;
	private ItemStack[] inventory = new ItemStack[27];
	public String vaultOwner1 = " ";
	public String vaultOwner2 = " ";
	public String vaultOwner3 = " ";
	public String vaultOwner4 = " ";
	public String vaultOwner5 = " ";
	public String vaultOwner6 = " ";
	
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
				System.out.println(this.vaultOwner1);
				break;
			case 2:
				this.vaultOwner2 = playerName;
				System.out.println(this.vaultOwner2);
				break;
			case 3:
				this.vaultOwner3 = playerName;
				System.out.println(this.vaultOwner3);
				break;
			case 4:
				this.vaultOwner4 = playerName;
				System.out.println(this.vaultOwner4);
				break;
			case 5:
				this.vaultOwner5 = playerName;
				System.out.println(this.vaultOwner5);
				break;
			case 6:
				this.vaultOwner6 = playerName;
				System.out.println(this.vaultOwner6);
				break;
		}
		
	}
	
	@Override
	public void updateEntity()
	{	
		super.updateEntity();
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
	public boolean canExtractItem(int i, ItemStack itemstack, int j)
	{
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
	public boolean isStackValidForSlot(int i, ItemStack itemstack) 
	{
		return true;
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


}
