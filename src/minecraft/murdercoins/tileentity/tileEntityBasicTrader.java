package murdercoins.tileentity;

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
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityAdvanced;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class tileEntityBasicTrader 	extends TileEntityAdvanced implements IInventory, ISidedInventory, IPacketReceiver
{
		//Normal Stuff
		private int playersUsing = 0;
		private ItemStack[] inventory = new ItemStack[5];
		
		//Stuff for Safe interaction
		public Boolean safeAttached;
		public TileEntity attachedSafe;
		
		//Owners
		public String botOwner1 = " ";
		public String botOwner2 = " ";
		public String botOwner3 = " ";
		public String botOwner4 = " ";
		public String botOwner5 = " ";
		public String botOwner6 = " ";
		public String botOwner7 = " ";
		
		//Prices
		public int slot1GPrice;
		public int slot1DPrice;
		public int slot1EPrice;
		public int slot2GPrice;
		public int slot2DPrice;
		public int slot2EPrice;
		public int slot3GPrice;
		public int slot3DPrice;
		public int slot3EPrice;
		public int slot4GPrice;
		public int slot4DPrice;
		public int slot4EPrice;
		public int slot5GPrice;
		public int slot5DPrice;
		public int slot5EPrice;
		public int slot6GPrice;
		public int slot6DPrice;
		public int slot6EPrice;
		public int slot7GPrice;
		public int slot7DPrice;
		public int slot7EPrice;
		public int slot8GPrice;
		public int slot8DPrice;
		public int slot8EPrice;
		public int slot9GPrice;
		public int slot9DPrice;
		public int slot9EPrice;
		
		public tileEntityBasicTrader()
		{
			this.safeAttached = false;
		}
		public void setOwners(String playerName, int ownerNumber)
		{
			switch (ownerNumber)
			{
				case 1:
					this.botOwner1 = playerName;
					System.out.println(this.botOwner1);
					break;
				case 2:
					this.botOwner2 = playerName;
					System.out.println(this.botOwner2);
					break;
				case 3:
					this.botOwner3 = playerName;
					System.out.println(this.botOwner3);
					break;
				case 4:
					this.botOwner4 = playerName;
					System.out.println(this.botOwner4);
					break;
				case 5:
					this.botOwner5 = playerName;
					System.out.println(this.botOwner5);
					break;
				case 6:
					this.botOwner6 = playerName;
					System.out.println(this.botOwner6);
					break;
				case 7:
					this.botOwner7 = playerName;
					System.out.println(this.botOwner7);
					break;
				default:
					break;
			}
			
		}
		
		public void setPrice(int price, int slotID, int type)
		{
			switch (type)
			{
			case 1:
				switch (slotID)
				{
				case 0:
					this.slot1GPrice = price;
					break;
				case 1:
					this.slot2GPrice = price;
					break;
				case 2:
					this.slot3GPrice = price;
					break;
				case 3:
					this.slot4GPrice = price;
					break;
				case 4:
					this.slot5GPrice = price;
					break;
				case 5:
					this.slot6GPrice = price;
					break;
				case 6:
					this.slot7GPrice = price;
					break;
				case 7:
					this.slot8GPrice = price;
					break;
				case 8:
					this.slot9GPrice = price;
					break;
				}
				break;
			case 2:
				switch (slotID)
				{
				case 0:
					this.slot1DPrice = price;
					break;
				case 1:
					this.slot2DPrice = price;
					break;
				case 2:
					this.slot3DPrice = price;
					break;
				case 3:
					this.slot4DPrice = price;
					break;
				case 4:
					this.slot5DPrice = price;
					break;
				case 5:
					this.slot6DPrice = price;
					break;
				case 6:
					this.slot7DPrice = price;
					break;
				case 7:
					this.slot8DPrice = price;
					break;
				case 8:
					this.slot9DPrice = price;
					break;
				}
				break;
			case 3:
				switch (slotID)
				{
				case 0:
					this.slot1EPrice = price;
					break;
				case 1:
					this.slot2EPrice = price;
					break;
				case 2:
					this.slot3EPrice = price;
					break;
				case 3:
					this.slot4EPrice = price;
					break;
				case 4:
					this.slot5EPrice = price;
					break;
				case 5:
					this.slot6EPrice = price;
					break;
				case 6:
					this.slot7EPrice = price;
					break;
				case 7:
					this.slot8EPrice = price;
					break;
				case 8:
					this.slot9EPrice = price;
					break;
				}
				break;
			}
		}
		
		
		public  int getPrice(int slotID, int type)
		{
			switch (type)
			{
			case 1:
				switch (slotID)
				{
				case 0:
					return this.slot1GPrice;
				case 1:
					return this.slot2GPrice;
				case 2:
					return this.slot3GPrice;
				case 3:
					return this.slot4GPrice;
				case 4:
					return this.slot5GPrice;
				case 5:
					return this.slot6GPrice;
				case 6:
					return this.slot7GPrice;
				case 7:
					return this.slot8GPrice;
				case 8:
					return this.slot9GPrice;
				}	
				break;
			case 2:
				switch (slotID)
				{
				case 0:
					return this.slot1DPrice;
				case 1:
					return this.slot2DPrice;
				case 2:
					return this.slot3DPrice;
				case 3:
					return this.slot4DPrice;
				case 4:
					return this.slot5DPrice;
				case 5:
					return this.slot6DPrice;
				case 6:
					return this.slot7DPrice;
				case 7:
					return this.slot8DPrice;
				case 8:
					return this.slot9DPrice;
				}		
				break;
			case 3:
				switch (slotID)
				{
				case 0:
					return this.slot1EPrice;
				case 1:
					return this.slot2EPrice;
				case 2:
					return this.slot3EPrice;
				case 3:
					return this.slot4EPrice;
				case 4:
					return this.slot5EPrice;
				case 5:
					return this.slot6EPrice;
				case 6:
					return this.slot7EPrice;
				case 7:
					return this.slot8EPrice;
				case 8:
					return this.slot9EPrice;
				}
				break;
			default:
				return 0;
			}
			return 0;
		}
		
		public String getOwners(int owner)
		{
			switch(owner)
			{
			case 1:
				return this.botOwner1;
			case 2:
				return this.botOwner2;
			case 3:
				return this.botOwner3;
			case 4:
				return this.botOwner4;
			case 5:
				return this.botOwner5;
			case 6:
				return this.botOwner6;
			case 7:
				return this.botOwner7;
			default:
				return this.botOwner1;
			}
		}
		
		@Override
		public void updateEntity()
		{	
			super.updateEntity();
			if (!this.safeAttached)
			{
				for(ForgeDirection orientation : ForgeDirection.VALID_DIRECTIONS) 
				{
					TileEntity tileEntity = VectorHelper.getTileEntityFromSide(worldObj, new Vector3(xCoord, yCoord, zCoord), orientation);

					if(tileEntity instanceof tileEntityBasicVault) 
					{
						this.attachedSafe = tileEntity;
						this.safeAttached = true;

					}
				}
			}
		}

		@Override
		public Packet getDescriptionPacket()
		{
			return PacketManager.getPacket("MurderCoins", this, this.botOwner1, this.botOwner2, this.botOwner3, this.botOwner4, this.botOwner5, this.botOwner6, this.botOwner7, this.safeAttached);//,
					//this.slot1GPrice, this.slot2GPrice, this.slot3GPrice, this.slot4GPrice, this.slot5GPrice, this.slot6GPrice, this.slot7GPrice, this.slot8GPrice, this.slot9GPrice,
					//this.slot1DPrice, this.slot2DPrice, this.slot3DPrice, this.slot4DPrice, this.slot5DPrice, this.slot6DPrice, this.slot7DPrice, this.slot8DPrice, this.slot9DPrice,
					//this.slot1EPrice, this.slot2EPrice, this.slot3EPrice, this.slot4EPrice, this.slot5EPrice, this.slot6EPrice, this.slot7EPrice, this.slot8EPrice, this.slot9EPrice);
		}
		@Override
		public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream) 
		{
			try
			{
			this.botOwner1 = dataStream.readUTF();
			this.botOwner2 = dataStream.readUTF();
			this.botOwner3 = dataStream.readUTF();
			this.botOwner4 = dataStream.readUTF();
			this.botOwner5 = dataStream.readUTF();
			this.botOwner6 = dataStream.readUTF();
			this.botOwner7 = dataStream.readUTF();
			this.safeAttached = dataStream.readBoolean();
			/*
			this.slot1GPrice = dataStream.readInt();
			this.slot2GPrice = dataStream.readInt();
			this.slot3GPrice = dataStream.readInt();
			this.slot4GPrice = dataStream.readInt();
			this.slot5GPrice = dataStream.readInt();
			this.slot6GPrice = dataStream.readInt();
			this.slot7GPrice = dataStream.readInt();
			this.slot8GPrice = dataStream.readInt();
			this.slot9GPrice = dataStream.readInt();
			this.slot1DPrice = dataStream.readInt();
			this.slot2DPrice = dataStream.readInt();
			this.slot3DPrice = dataStream.readInt();
			this.slot4DPrice = dataStream.readInt();
			this.slot5DPrice = dataStream.readInt();
			this.slot6DPrice = dataStream.readInt();
			this.slot7DPrice = dataStream.readInt();
			this.slot8DPrice = dataStream.readInt();
			this.slot9DPrice = dataStream.readInt();
			this.slot1EPrice = dataStream.readInt();
			this.slot2EPrice = dataStream.readInt();
			this.slot3EPrice = dataStream.readInt();
			this.slot4EPrice = dataStream.readInt();
			this.slot5EPrice = dataStream.readInt();
			this.slot6EPrice = dataStream.readInt();
			this.slot7EPrice = dataStream.readInt();
			this.slot8EPrice = dataStream.readInt();
			this.slot9EPrice = dataStream.readInt();
			*/
			}
			catch (Exception e)
			{
				this.botOwner1 = " ";
				this.botOwner2 = " ";
				this.botOwner3 = " ";
				this.botOwner4 = " ";
				this.botOwner5 = " ";
				this.botOwner6 = " ";
				this.botOwner7 = " ";
				System.out.print("Failed to sync owners of basicTraderBot at: " + Integer.toString(this.xCoord) + ", " + Integer.toString(this.yCoord) + ", " + Integer.toString(this.zCoord) + ".");
			}
		}

		@Override
		public boolean isStackValidForSlot(int slotID, ItemStack itemStack) 
		{
			if(itemStack.getItem() instanceof IItemElectric)
			{
				return slotID == 0;
			}
			return false;
		}
		@Override
		public int[] getAccessibleSlotsFromSide(int side) 
		{
			return new int[] {0,1,2,3,4};
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
			return LanguageRegistry.instance().getStringLocalization("basicTrader");
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

		/**
		 * Reads a tile entity from NBT.
		 */
		@Override
		public void readFromNBT(NBTTagCompound par1NBTTagCompound)
		{
			super.readFromNBT(par1NBTTagCompound);
			this.botOwner1 = par1NBTTagCompound.getString("owner1");
			this.botOwner2 = par1NBTTagCompound.getString("owner2");
			this.botOwner3 = par1NBTTagCompound.getString("owner3");
			this.botOwner4 = par1NBTTagCompound.getString("owner4");
			this.botOwner5 = par1NBTTagCompound.getString("owner5");
			this.botOwner6 = par1NBTTagCompound.getString("owner6");
			this.botOwner7 = par1NBTTagCompound.getString("owner7");
			this.slot1GPrice = par1NBTTagCompound.getInteger("slot1GPrice");
			this.slot2GPrice = par1NBTTagCompound.getInteger("slot2GPrice");
			this.slot3GPrice = par1NBTTagCompound.getInteger("slot3GPrice");
			this.slot4GPrice = par1NBTTagCompound.getInteger("slot4GPrice");
			this.slot5GPrice = par1NBTTagCompound.getInteger("slot5GPrice");
			this.slot6GPrice = par1NBTTagCompound.getInteger("slot6GPrice");
			this.slot7GPrice = par1NBTTagCompound.getInteger("slot7GPrice");
			this.slot8GPrice = par1NBTTagCompound.getInteger("slot8GPrice");
			this.slot9GPrice = par1NBTTagCompound.getInteger("slot9GPrice");
			this.slot1DPrice = par1NBTTagCompound.getInteger("slot1DPrice");
			this.slot2DPrice = par1NBTTagCompound.getInteger("slot2DPrice");
			this.slot3DPrice = par1NBTTagCompound.getInteger("slot3DPrice");
			this.slot4DPrice = par1NBTTagCompound.getInteger("slot4DPrice");
			this.slot5DPrice = par1NBTTagCompound.getInteger("slot5DPrice");
			this.slot6DPrice = par1NBTTagCompound.getInteger("slot6DPrice");
			this.slot7DPrice = par1NBTTagCompound.getInteger("slot7DPrice");
			this.slot8DPrice = par1NBTTagCompound.getInteger("slot8DPrice");
			this.slot9DPrice = par1NBTTagCompound.getInteger("slot9DPrice");
			this.slot1EPrice = par1NBTTagCompound.getInteger("slot1EPrice");
			this.slot2EPrice = par1NBTTagCompound.getInteger("slot2EPrice");
			this.slot3EPrice = par1NBTTagCompound.getInteger("slot3EPrice");
			this.slot4EPrice = par1NBTTagCompound.getInteger("slot4EPrice");
			this.slot5EPrice = par1NBTTagCompound.getInteger("slot5EPrice");
			this.slot6EPrice = par1NBTTagCompound.getInteger("slot6EPrice");
			this.slot7EPrice = par1NBTTagCompound.getInteger("slot7EPrice");
			this.slot8EPrice = par1NBTTagCompound.getInteger("slot8EPrice");
			this.slot9EPrice = par1NBTTagCompound.getInteger("slot9EPrice");
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
			par1NBTTagCompound.setString("owner1", this.botOwner1);
			par1NBTTagCompound.setString("owner2", this.botOwner2);
			par1NBTTagCompound.setString("owner3", this.botOwner3);
			par1NBTTagCompound.setString("owner4", this.botOwner4);
			par1NBTTagCompound.setString("owner5", this.botOwner5);
			par1NBTTagCompound.setString("owner6", this.botOwner6);
			par1NBTTagCompound.setString("owner7", this.botOwner7);
			par1NBTTagCompound.setInteger("slot1GPrice", this.slot1GPrice);
			par1NBTTagCompound.setInteger("slot2GPrice", this.slot2GPrice);
			par1NBTTagCompound.setInteger("slot3GPrice", this.slot3GPrice);
			par1NBTTagCompound.setInteger("slot4GPrice", this.slot4GPrice);
			par1NBTTagCompound.setInteger("slot5GPrice", this.slot5GPrice);
			par1NBTTagCompound.setInteger("slot6GPrice", this.slot6GPrice);
			par1NBTTagCompound.setInteger("slot7GPrice", this.slot7GPrice);
			par1NBTTagCompound.setInteger("slot8GPrice", this.slot8GPrice);
			par1NBTTagCompound.setInteger("slot9GPrice", this.slot9GPrice);
			par1NBTTagCompound.setInteger("slot1DPrice", this.slot1DPrice);
			par1NBTTagCompound.setInteger("slot2DPrice", this.slot2DPrice);
			par1NBTTagCompound.setInteger("slot3DPrice", this.slot3DPrice);
			par1NBTTagCompound.setInteger("slot4DPrice", this.slot4DPrice);
			par1NBTTagCompound.setInteger("slot5DPrice", this.slot5DPrice);
			par1NBTTagCompound.setInteger("slot6DPrice", this.slot6DPrice);
			par1NBTTagCompound.setInteger("slot7DPrice", this.slot7DPrice);
			par1NBTTagCompound.setInteger("slot8DPrice", this.slot8DPrice);
			par1NBTTagCompound.setInteger("slot9DPrice", this.slot9DPrice);
			par1NBTTagCompound.setInteger("slot1EPrice", this.slot1EPrice);
			par1NBTTagCompound.setInteger("slot2EPrice", this.slot2EPrice);
			par1NBTTagCompound.setInteger("slot3EPrice", this.slot3EPrice);
			par1NBTTagCompound.setInteger("slot4EPrice", this.slot4EPrice);
			par1NBTTagCompound.setInteger("slot5EPrice", this.slot5EPrice);
			par1NBTTagCompound.setInteger("slot6EPrice", this.slot6EPrice);
			par1NBTTagCompound.setInteger("slot7EPrice", this.slot7EPrice);
			par1NBTTagCompound.setInteger("slot8EPrice", this.slot8EPrice);
			par1NBTTagCompound.setInteger("slot9EPrice", this.slot9EPrice);
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

