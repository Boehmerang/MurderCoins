package murdercoins.tileentity;

import murdercoins.common.MurderCoins;
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
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityAdvanced;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class tileEntityBasicTrader extends TileEntityAdvanced implements IInventory, ISidedInventory, IPacketReceiver
{
		//Normal Stuff
		private int playersUsing = 0;
		public ItemStack[] inventory = new ItemStack[5];
		
		//Stuff for Safe interaction
		public Boolean safeAttached;
		public tileEntityBasicVault attachedSafe;
		
		//Owners
		public String botOwner1 = " ";
		public String botOwner2 = " ";
		public String botOwner3 = " ";
		public String botOwner4 = " ";
		public String botOwner5 = " ";
		public String botOwner6 = " ";
		public String botOwner7 = " ";
		
		//Prices
		public int slot1GPrice = 0;
		public int slot1DPrice = 0;
		public int slot1EPrice = 0;
		public int slot2GPrice = 0;
		public int slot2DPrice = 0;
		public int slot2EPrice = 0;
		public int slot3GPrice = 0;
		public int slot3DPrice = 0;
		public int slot3EPrice = 0;
		public int slot4GPrice = 0;
		public int slot4DPrice = 0;
		public int slot4EPrice = 0;
		public int slot5GPrice = 0;
		public int slot5DPrice = 0;
		public int slot5EPrice = 0;
		public int slot6GPrice = 0;
		public int slot6DPrice = 0;
		public int slot6EPrice = 0;
		public int slot7GPrice = 0;
		public int slot7DPrice = 0;
		public int slot7EPrice = 0;
		public int slot8GPrice = 0;
		public int slot8DPrice = 0;
		public int slot8EPrice = 0;
		public int slot9GPrice = 0;
		public int slot9DPrice = 0;
		public int slot9EPrice = 0;
		
		//Trade Types (is sale?)
		public Boolean slot0Trade = true;
		public Boolean slot1Trade = true;
		public Boolean slot2Trade = true;
		public Boolean slot3Trade = true;
		public Boolean slot4Trade = true;
		public Boolean slot5Trade = true;
		public Boolean slot6Trade = true;
		public Boolean slot7Trade = true;
		public Boolean slot8Trade = true;
		
		public Boolean processWanted = false;
		public int tradeWanted = 0;
		public int[] tempArray = new int[] {0,0,0,0,0,0,0,0};
		
		
		public tileEntityBasicTrader()
		{
			this.safeAttached = false;
		}
		
		@Override
		public void updateEntity()
		{	
			if (!worldObj.isRemote)
			{
				super.updateEntity();
				if (this.processWanted)
				{
					this.processTransaction(this.tradeWanted);
					this.processWanted = false;
				}
				
			}		
			for(ForgeDirection orientation : ForgeDirection.VALID_DIRECTIONS) 
			{
				//if (this.safeAttached == true) {	break;	}
				if (VectorHelper.getTileEntityFromSide(worldObj, new Vector3(this.xCoord, this.yCoord, this.zCoord), orientation) != null)
				{
					if (VectorHelper.getTileEntityFromSide(worldObj, new Vector3(this.xCoord, this.yCoord, this.zCoord), orientation) instanceof tileEntityBasicVault)
					{
						tileEntityBasicVault tileEntity = (tileEntityBasicVault)VectorHelper.getTileEntityFromSide(worldObj, new Vector3(this.xCoord, this.yCoord, this.zCoord), orientation);

						if(tileEntity instanceof tileEntityBasicVault) 
						{
							if(!this.safeAttached)
							{
								this.attachedSafe = tileEntity;
								this.attachedSafe.updateEntity();
								this.safeAttached = true;
								break;
							}
							else if (this.safeAttached)
							{
								break;
							}
						}
					}
				}
				else
				{
					if(this.safeAttached)
					{
						this.attachedSafe = null;
						this.safeAttached = false;
					}
				}
			}
			if (this.ticks % 3 == 0 && this.playersUsing > 0)
			{
				PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
			}
				
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
		
		/**
		 * Sets the (type)1-gold/2-diamond/2-emerald coin price of the item in slot (slotID) to (price)
		 * @param price
		 * @param slotID
		 * @param type
		 */
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
		
		/**
		 * Returns the price of the the item in slot (slotID) of the tradersafe.
		 * @param slotID
		 * @param type
		 * @return
		 */
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
		
		/**
		 * returns owners 1-7
		 * @param owner
		 * @return
		 */
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
		
		/**
		 * Sets the type of transaction *buy/sell* of the item in Slot (slotID)  -- Toggle switch thru the Gui
		 * @param slotID
		 */
		public void setSaleType(int slotID)
		{
			switch (slotID)
			{
				case 0:
					if (this.slot0Trade == true)
					{
						this.slot0Trade = false;
						break;
					}
					if (this.slot0Trade == false)
					{
						this.slot0Trade = true;
						break;
					}
					break;
				case 1:
					if(this.slot1Trade == true)
					{
						this.slot1Trade = false;
						break;
					}
					if (this.slot1Trade==false)
					{
						this.slot1Trade = true;
						break;
					}
				case 2:
					if(this.slot2Trade == true)
					{
						this.slot2Trade = false;
						break;
					}
					if (this.slot2Trade==false)
					{
						this.slot2Trade = true;
						break;
					}
				case 3:
					if(this.slot3Trade == true)
					{
						this.slot3Trade = false;
						break;
					}
					if (this.slot3Trade==false)
					{
						this.slot3Trade = true;
						break;
					}
				case 4:
					if(this.slot4Trade == true)
					{
						this.slot4Trade = false;
						break;
					}
					if (this.slot4Trade==false)
					{
						this.slot4Trade = true;
						break;
					}
				case 5:
					if(this.slot5Trade == true)
					{
						this.slot5Trade = false;
						break;
					}
					if (this.slot5Trade==false)
					{
						this.slot5Trade = true;
						break;
					}
				case 6:
					if(this.slot6Trade == true)
					{
						this.slot6Trade = false;
						break;
					}
					if (this.slot6Trade==false)
					{
						this.slot6Trade = true;
						break;
					}
				case 7:
					if(this.slot7Trade == true)
					{
						this.slot7Trade = false;
						break;
					}
					if (this.slot7Trade==false)
					{
						this.slot7Trade = true;
						break;
					}
				case 8:
					if(this.slot8Trade == true)
					{
						this.slot8Trade = false;
						break;
					}
					if (this.slot8Trade==false)
					{
						this.slot8Trade = true;
						break;
					}
			}
		}
		
		/**
		 * Returns the type of transaction set for the slotID (true = sell, false = buy)		
		 * @param slotID
		 * @return
		 */
		public Boolean getSaleType(int slotID)
		{
			switch (slotID)
			{
				case 0:
					return this.slot0Trade;
				case 1:
					return this.slot1Trade;
				case 2:
					return this.slot2Trade;
				case 3:
					return this.slot3Trade;
				case 4:
					return this.slot4Trade;
				case 5:
					return this.slot5Trade;
				case 6:
					return this.slot6Trade;
				case 7:
					return this.slot7Trade;
				case 8:
					return this.slot8Trade;
			}
			return true;
		}
		
		/**
		 * Checks to see if the machine has the required items/coins in the proper slots to complete the transaction.
		 * @param slotID
		 * @param stack
		 * @return
		 */
		public Boolean canTransact(int slotID, ItemStack stack)
		{
			if(this.attachedSafe.getStackInSlot(slotID) == null) {	return false; }
			int qtyNeeded = this.attachedSafe.getStackInSlot(slotID).stackSize;
			int qtyFound = 0;
				if (this.getSaleType(slotID) == true)
				{
					if (this.safeHasEnoughItems(slotID) == false)
						{	return false;	}
					if(this.getPrice(slotID, 1) >0)
					{
						if (this.inventory[1] == null)
							return false;
						if (this.inventory[1].stackSize < this.getPrice(slotID, 1))
						{
							return false;
						}
					}
					if(this.getPrice(slotID, 2) >0)
					{
						if (this.inventory[2] == null)
							return false;
						if (this.inventory[2].stackSize < this.getPrice(slotID, 2))
						{
							return false;
						}
					}
					if(this.getPrice(slotID, 3) >0)
					{
						if (this.inventory[3] == null)
							return false;
						if (this.inventory[3].stackSize < this.getPrice(slotID, 3))
						{
							return false;
						}
					}
					if (this.inventory[4] != null)
						{
							if (!this.inventory[4].isItemEqual(stack))
							{
								return false;
							}
						}
				}
				else if (this.getSaleType(slotID) == false)
				{
					if (this.attachedSafe.safeHasCoins(1) == false)
						return false;
					if (this.attachedSafe.safeHasCoins(2) == false)
						return false;
					if (this.attachedSafe.safeHasCoins(3) == false)
						return false;
					if(this.inventory[4] == null)
						return false;
					if(this.inventory[4] != null)
					{
						if(!this.inventory[4].isItemEqual(stack))
						return false;
					}
					if (this.getPrice(slotID, 1) > 0)
					{
						if (!this.safeHasEnoughCoins(slotID, 1))
							return false;
					}		
					if (this.getPrice(slotID, 2) > 0)
					{
						if (!this.safeHasEnoughCoins(slotID, 2))
							return false;
					}
					if (this.getPrice(slotID, 3) > 0)
					{
						if (!this.safeHasEnoughCoins(slotID, 3))
							return false;
					}
				}

			return true;
		}
		
		public Boolean safeHasEnoughItems(int slotID)
		{
			int qtyNeeded;
			int qtyFound = 0;
			//ItemStack stack = this.attachedSafe.inventory[slotID].copy();
			
			if (this.attachedSafe.inventory[slotID] == null){	return false;	}
			else
				{	 qtyNeeded = this.attachedSafe.getStackInSlot(slotID).stackSize;	}
			switch (slotID)
			{
				case 0:
					if (this.attachedSafe.getStackInSlot(9) == null && this.attachedSafe.getStackInSlot(18) == null)
							{	return false;	}
					else if (this.attachedSafe.getStackInSlot(9) != null)
					{
						if (this.attachedSafe.inventory[9].getItem() != this.attachedSafe.inventory[slotID].getItem())
							return false;
						else if (this.attachedSafe.inventory[9].stackSize - qtyNeeded >= 0)
							return true;
						else if (this.attachedSafe.inventory[9].stackSize > 0 && this.attachedSafe.inventory[9].stackSize - qtyNeeded < 0)
							qtyFound += this.attachedSafe.getStackInSlot(9).stackSize;
					}
					else if(this.attachedSafe.inventory[18] != null) 
					{
						if (this.attachedSafe.inventory[18].getItem() != this.attachedSafe.inventory[slotID].getItem())
							return false;
						else if(this.attachedSafe.inventory[18].stackSize - qtyNeeded >= 0)
							return true;
						else if (this.attachedSafe.inventory[18].stackSize > 0 && this.attachedSafe.inventory[18].stackSize - qtyNeeded < 0)
							qtyFound += this.attachedSafe.inventory[18].stackSize;
					}
					else if (qtyFound > qtyNeeded) 
						return true;
				
					break;
				case 1:
					if (this.attachedSafe.getStackInSlot(10) == null && this.attachedSafe.getStackInSlot(19) == null)
					{	return false;	}
					else if ( this.attachedSafe.inventory[10] != null)
					{
						if (this.attachedSafe.inventory[10].getItem() != this.attachedSafe.inventory[slotID].getItem())
							return false;
						else if (this.attachedSafe.inventory[10].stackSize >= qtyNeeded)
							return true;
						else if (this.attachedSafe.inventory[10].stackSize > 0 && this.attachedSafe.inventory[10].stackSize < qtyNeeded)
							qtyFound += this.attachedSafe.inventory[10].stackSize;
					}
					else if (  this.attachedSafe.inventory[19] != null)
					{
						if (  this.attachedSafe.inventory[19].getItem() != this.attachedSafe.inventory[slotID].getItem())
							return false;
						else if (  this.attachedSafe.inventory[19].stackSize >= qtyNeeded)
							return true;
						else if (  this.attachedSafe.inventory[19].stackSize > 0 && this.attachedSafe.inventory[19].stackSize < qtyNeeded)
							qtyFound += this.attachedSafe.getStackInSlot(19).stackSize;
					}
					else if (qtyFound > qtyNeeded) 
						return true;
					break;
				case 2:
					if (this.attachedSafe.getStackInSlot(11) == null && this.attachedSafe.getStackInSlot(20) == null)
					{	return false;	}
					else if (  this.attachedSafe.inventory[11] != null)
					{
						if (this.attachedSafe.inventory[11].getItem() != this.attachedSafe.inventory[slotID].getItem())
							return false;
						else if (this.attachedSafe.inventory[11].stackSize >= qtyNeeded)
							return true;
						else if (this.attachedSafe.inventory[11].stackSize > 0 && this.attachedSafe.inventory[11].stackSize < qtyNeeded)
							qtyFound += this.attachedSafe.inventory[11].stackSize;
					}
					else if (  this.attachedSafe.inventory[20] != null)
					{
						if (this.attachedSafe.inventory[20].getItem() != this.attachedSafe.inventory[slotID].getItem())
							return false;
						else if (this.attachedSafe.inventory[20].stackSize >= qtyNeeded)
							return true;
						else if (this.attachedSafe.inventory[20].stackSize > 0 && this.attachedSafe.inventory[20].stackSize < qtyNeeded)
							qtyFound += this.attachedSafe.inventory[20].stackSize;
					}
					else if (qtyFound > qtyNeeded) 
						return true;
					break;
				case 3:
					if (this.attachedSafe.getStackInSlot(12) == null && this.attachedSafe.getStackInSlot(21) == null)
					{	return false;	}
					else if (  this.attachedSafe.inventory[12] != null)
					{
						if (this.attachedSafe.inventory[12].getItem() != this.attachedSafe.inventory[slotID].getItem())
							return false;
						else if (this.attachedSafe.inventory[12].stackSize >= qtyNeeded)
							return true;
						else if (this.attachedSafe.inventory[12].stackSize > 0 && this.attachedSafe.inventory[12].stackSize < qtyNeeded)
							qtyFound += this.attachedSafe.inventory[12].stackSize;
					}
					else if (  this.attachedSafe.inventory[21] != null)
					{
						if (this.attachedSafe.inventory[21].getItem() != this.attachedSafe.inventory[slotID].getItem())
							return false;
						else if (this.attachedSafe.inventory[21].stackSize >= qtyNeeded)
							return true;
						else if (this.attachedSafe.inventory[21].stackSize > 0 && this.attachedSafe.inventory[21].stackSize < qtyNeeded)
							qtyFound += this.attachedSafe.inventory[21].stackSize;
					}
					else if (qtyFound > qtyNeeded) 
						return true;
					break;
				case 4:
					if (this.attachedSafe.getStackInSlot(13) == null && this.attachedSafe.getStackInSlot(22) == null)
					{	return false;	}
					else if (  this.attachedSafe.inventory[13] != null)
					{
						if (this.attachedSafe.inventory[13].getItem() != this.attachedSafe.inventory[slotID].getItem())
							return false;
						else if (this.attachedSafe.inventory[13].stackSize >= qtyNeeded)
							return true;
						else if (this.attachedSafe.inventory[13].stackSize > 0 && this.attachedSafe.inventory[13].stackSize < qtyNeeded)
							qtyFound += this.attachedSafe.inventory[13].stackSize;
					}
					else if (  this.attachedSafe.inventory[22] != null)
					{
						if (this.attachedSafe.inventory[22].getItem() != this.attachedSafe.inventory[slotID].getItem())
							return false;
						else if (this.attachedSafe.inventory[22].stackSize >= qtyNeeded)
							return true;
						else if (this.attachedSafe.inventory[22].stackSize > 0 && this.attachedSafe.inventory[22].stackSize < qtyNeeded)
							qtyFound += this.attachedSafe.inventory[22].stackSize;
					}
					else if (qtyFound > qtyNeeded) 
						return true;
					break;
				case 5:
					if (this.attachedSafe.getStackInSlot(14) == null && this.attachedSafe.getStackInSlot(23) == null)
					{	return false;	}
					else if (this.attachedSafe.inventory[14] != null)
					{
						if (this.attachedSafe.inventory[14].getItem() != this.attachedSafe.inventory[slotID].getItem())
							return false;
						else if (this.attachedSafe.inventory[14].stackSize >= qtyNeeded)
							return true;
						else if (this.attachedSafe.inventory[14].stackSize > 0 && this.attachedSafe.inventory[14].stackSize < qtyNeeded)
							qtyFound += this.attachedSafe.inventory[14].stackSize;
					}
					else if (this.attachedSafe.inventory[23] != null)
					{
						if (this.attachedSafe.inventory[23].getItem() != this.attachedSafe.inventory[slotID].getItem())
							return false;
						else if (this.attachedSafe.inventory[23].stackSize >= qtyNeeded)
							return true;
						else if (this.attachedSafe.inventory[23].stackSize > 0 && this.attachedSafe.inventory[23].stackSize < qtyNeeded)
							qtyFound += this.attachedSafe.inventory[23].stackSize;
					}
					else if (qtyFound > qtyNeeded) 
						return true;
					break;
				case 6:
					if (this.attachedSafe.getStackInSlot(15) == null && this.attachedSafe.getStackInSlot(24) == null)
					{	return false;	}
					else if (this.attachedSafe.inventory[15] != null)
					{
						if (this.attachedSafe.inventory[15].getItem() != this.attachedSafe.inventory[slotID].getItem())
							return false;
						else if (this.attachedSafe.inventory[15].stackSize >= qtyNeeded)
							return true;
						else if (this.attachedSafe.inventory[15].stackSize > 0 && this.attachedSafe.inventory[15].stackSize < qtyNeeded)
							qtyFound += this.attachedSafe.inventory[15].stackSize;
					}
					else if (this.attachedSafe.inventory[24] != null)
					{
						if (this.attachedSafe.inventory[24].getItem() != this.attachedSafe.inventory[slotID].getItem())
							return false;
						else if (this.attachedSafe.inventory[24].stackSize >= qtyNeeded)
							return true;
						else if (this.attachedSafe.inventory[24].stackSize > 0 && this.attachedSafe.inventory[24].stackSize < qtyNeeded)
							qtyFound += this.attachedSafe.inventory[24].stackSize;
					}
					else if (qtyFound > qtyNeeded) 
						return true;
					break;
				case 7:
					if (this.attachedSafe.getStackInSlot(16) == null && this.attachedSafe.getStackInSlot(25) == null)
					{	return false;	}
					else if (this.attachedSafe.inventory[16] != null)
					{
						if (this.attachedSafe.inventory[16].getItem() != this.attachedSafe.inventory[slotID].getItem())
							return false;
						else if (this.attachedSafe.inventory[16].stackSize >= qtyNeeded)
							return true;
						else if (this.attachedSafe.inventory[16].stackSize > 0 && this.attachedSafe.inventory[16].stackSize < qtyNeeded)
							qtyFound += this.attachedSafe.inventory[16].stackSize;
					}
					else if (this.attachedSafe.inventory[25] != null)
					{
						if (this.attachedSafe.inventory[25].getItem() != this.attachedSafe.inventory[slotID].getItem())
							return false;
						else if (this.attachedSafe.inventory[25].stackSize >= qtyNeeded)
							return true;
						else if (this.attachedSafe.inventory[25].stackSize > 0 && this.attachedSafe.inventory[25].stackSize < qtyNeeded)
							qtyFound += this.attachedSafe.inventory[25].stackSize;
					}
					else if (qtyFound > qtyNeeded) 
						return true;
					break;
				case 8:
					if (this.attachedSafe.getStackInSlot(17) == null && this.attachedSafe.getStackInSlot(26) == null)
					{	return false;	}
					else if (this.attachedSafe.inventory[17] != null)
					{
						if (this.attachedSafe.inventory[17].getItem() != this.attachedSafe.inventory[slotID].getItem())
							return false;
						else if (this.attachedSafe.inventory[17].stackSize >= qtyNeeded)
							return true;
						else if (this.attachedSafe.inventory[17].stackSize > 0 && this.attachedSafe.inventory[17].stackSize < qtyNeeded)
							qtyFound += this.attachedSafe.inventory[17].stackSize;
					}
					else if (this.attachedSafe.inventory[26] != null)
					{
						if (this.attachedSafe.inventory[26].getItem() != this.attachedSafe.inventory[slotID].getItem())
							return false;
						else if (this.attachedSafe.inventory[26].stackSize >= qtyNeeded)
							return true;
						else if (this.attachedSafe.inventory[26].stackSize > 0 && this.attachedSafe.inventory[26].stackSize < qtyNeeded)
							qtyFound += this.attachedSafe.inventory[26].stackSize;
					}
					else if (qtyFound > qtyNeeded) 
						return true;
					break;
			}
			return false;
		}
		

		public void processTransaction(int slotID)
		{
			Boolean saleType = this.getSaleType(slotID);
			if(!this.canTransact(slotID, this.attachedSafe.getStackInSlot(slotID)))
				return;
			if (saleType == true)  //Machine is selling the item
			{

				Boolean gPaid = false, dPaid = false, ePaid = false;
				ItemStack stack = this.attachedSafe.inventory[slotID];
				ItemStack boughtStack = stack.copy(); //this.attachedSafe.inventory[slotID].copy();
				int qty = this.attachedSafe.inventory[slotID].stackSize;
				//ItemStack gCoins = this.getStackInSlot(1).copy();
				int gAmt = this.getPrice(slotID, 1);
				//ItemStack dCoins = this.getStackInSlot(2).copy();
				int dAmt = this.getPrice(slotID, 2);
				//ItemStack eCoins = this.getStackInSlot(3).copy();
				int eAmt = this.getPrice(slotID, 3);
				if (gAmt > 0)
				{
					for (int var1 = 27; var1 < 33; var1++)
					{
						if (this.attachedSafe.inventory[var1] != null)
						{
							if (this.attachedSafe.inventory[var1].stackSize + gAmt  <= 64)
							{
								this.decrStackSize(1, gAmt);
								this.attachedSafe.inventory[var1].stackSize += gAmt;
								System.out.println("Put gCoins in slot " + Integer.toString(var1));
								this.attachedSafe.onInventoryChanged();
								gPaid = true;
								break;
							}
						}
						else if (this.attachedSafe.inventory[var1] == null)
						{
							this.decrStackSize(1, gAmt);
							this.attachedSafe.setInventorySlotContents(var1, new ItemStack(MurderCoins.itemGoldCoin, gAmt));
							System.out.println("Put gCoins in slot " + Integer.toString(var1));
							this.attachedSafe.onInventoryChanged();
							gPaid = true;
							break;
						}
					}
				}
				else if (gAmt == 0) { gPaid = true;}
				if (dAmt > 0)
				{
					for (int var1 = 33; var1 < 39; var1++)
					{
						if (this.attachedSafe.inventory[var1] != null)
						{
							if (this.attachedSafe.inventory[var1].stackSize + dAmt  <= 64)
							{
								this.decrStackSize(2, dAmt);
								this.attachedSafe.inventory[var1].stackSize += dAmt;
								this.attachedSafe.onInventoryChanged();
								dPaid = true;
								break;
							}
						}
						else if (this.attachedSafe.inventory[var1] == null)
						{
							this.decrStackSize(2, dAmt);
							this.attachedSafe.setInventorySlotContents(var1, new ItemStack(MurderCoins.itemDiamondCoin, dAmt));
							this.attachedSafe.onInventoryChanged();
							dPaid = true;
							break;
						}
					}
				}
				else if (dAmt == 0) { dPaid = true;}
				if (eAmt > 0)
				{
					for (int var1 = 39; var1 < 45; var1++)
					{
						if (this.attachedSafe.inventory[var1] != null)
						{
							if (this.attachedSafe.inventory[var1].stackSize + eAmt  <= 64)
							{
								this.decrStackSize(3, eAmt);
								this.attachedSafe.inventory[var1].stackSize += eAmt;
								ePaid = true;
								this.attachedSafe.onInventoryChanged();
								break;
							}
						}
						else if (this.attachedSafe.inventory[var1] == null)
						{
							this.decrStackSize(3, eAmt);
							this.attachedSafe.setInventorySlotContents(var1, new ItemStack(MurderCoins.itemEmeraldCoin, eAmt));
							this.attachedSafe.onInventoryChanged();
							ePaid = true;
							break;
						}
					}
				}
			
				else if (eAmt == 0) { ePaid = true;}
				
				if (gPaid && dPaid && ePaid)
				{	
					boolean didPay = false;
					if (this.inventory[4] != null && this.inventory[4].stackSize + qty <=64)
					{
						this.inventory[4].stackSize += qty;
						//PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
						
						for (int i = 1; i < 3; i ++)
						{
							if (this.attachedSafe.inventory[slotID + (i * 9)] != null)
							{
								if (this.attachedSafe.inventory[slotID + (i * 9)].stackSize - qty > 0)
								{
									this.attachedSafe.inventory[slotID + (i * 9)].stackSize -= qty;
									this.attachedSafe.onInventoryChanged();
									System.out.println("removed an item from slot:" + Integer.toString(i*9 +9));
									didPay = true;
									//PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
									break;
								}
								else if (this.attachedSafe.inventory[slotID + (i * 9)].stackSize - qty == 0)
								{
									this.attachedSafe.inventory[slotID + (i * 9)] = null;
									this.attachedSafe.onInventoryChanged();
									System.out.println("Set slot " + Integer.toString(i*9 +9) + " to null.");
									didPay = true;
									//PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
									break;
								}
							}
							
						}
						PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
					}
					else if (this.inventory[4] == null)
					{
						//this.inventory[4] = boughtStack;
						//PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);

						
						for (int i = 1; i < 3; i ++)
						{
							if (didPay) break;
							if (this.attachedSafe.getStackInSlot(slotID + (i * 9)) != null)
							{
								if (!didPay && this.attachedSafe.getStackInSlot(slotID + (i * 9)).stackSize - qty > 0)
								{
									if (!didPay && this.attachedSafe.getStackInSlot(slotID + (i * 9)).stackSize - qty > 0)
									{
										this.attachedSafe.decrStackSize((slotID + (i * 9)) + 9, qty);
										System.out.println("removed an item from slot:" + Integer.toString(i*9 +9));
										didPay = true;
										this.attachedSafe.onInventoryChanged();
										this.inventory[4] = boughtStack;
										//PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
										break;
									}
									/*else if (!didPay && this.attachedSafe.getStackInSlot(slotID + (i * 9)).stackSize - qty == 0)
									{
										this.attachedSafe.inventory[slotID + (i * 9)] = null;
										this.attachedSafe.onInventoryChanged();
										System.out.println("Slot:" + Integer.toString(i*9 +9) + " set to NULL");
										this.inventory[4] = boughtStack;
										didPay = true;
										//PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
										break;
									}*/
								}
								else if (this.attachedSafe.getStackInSlot(slotID + (i * 9)).stackSize - qty == 0)
								{
									
									this.attachedSafe.inventory[slotID + (i * 9)] = null;
									this.attachedSafe.onInventoryChanged();
									System.out.println("Slot:" + Integer.toString(i*9 +9) + " set to NULL");
									didPay = true;
									this.inventory[4] = boughtStack;
									//PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
									//break;
								}
							}
						}
						PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
					}
				}
			}
			else if (saleType == false) //Machine is buying the item.
			{
				Boolean gPaid = false, dPaid = false, ePaid = false;
				int qty = this.attachedSafe.inventory[slotID].stackSize;
				int gAmt = this.getPrice(slotID, 1);
				int dAmt = this.getPrice(slotID, 2);
				int eAmt = this.getPrice(slotID, 3);
				ItemStack soldStack = this.attachedSafe.getStackInSlot(slotID);
				ItemStack gCoins = new ItemStack(MurderCoins.itemGoldCoin, gAmt);
				ItemStack dCoins = new ItemStack(MurderCoins.itemDiamondCoin, dAmt);
				ItemStack eCoins = new ItemStack(MurderCoins.itemEmeraldCoin, eAmt);
				
				if (gAmt > 0)
				{
					for (int var1 = 27; var1 < 33; var1++)
					{
						if (this.attachedSafe.inventory[var1] != null)
						{
							if (this.attachedSafe.inventory[var1].stackSize - gAmt >= 1)
							{
								if (this.inventory[1] == null)
								{
									this.inventory[1] = gCoins;
								}
								else if (this.inventory[1] != null)
								{
									this.inventory[1].stackSize += gAmt;
								}
								this.attachedSafe.inventory[var1].stackSize -= gAmt;
								System.out.println("Took gCoins from slot " + Integer.toString(var1));
								this.attachedSafe.onInventoryChanged();
								gPaid = true;
								break;
							}
							else if (this.attachedSafe.inventory[var1].stackSize == gAmt)
							{
								if (this.inventory[1] == null)
								{
									this.inventory[1] = gCoins;
								}
								else if (this.inventory[1] != null)
								{
									this.inventory[1].stackSize += gAmt;
								}
								this.attachedSafe.inventory[var1] = null;
								System.out.println("Removed gCoins in slot " + Integer.toString(var1));
								this.attachedSafe.onInventoryChanged();
								gPaid = true;
								break;
							}
						}
					}
				}
				else if (gAmt == 0)
					{	gPaid = true;	}
				if (dAmt > 0)
				{
					if (this.inventory[2] == null)
					{
						for (int var1 = 33; var1 < 39; var1++)
						{
							if (this.attachedSafe.inventory[var1] != null)
							{
								if (this.attachedSafe.inventory[var1].stackSize - dAmt  >= 1)
								{
									if (this.inventory[2] == null)
									{
										this.inventory[2] = dCoins;
									}
									else if (this.inventory[2] != null)
									{
										this.inventory[2].stackSize += dAmt;
									}
									this.attachedSafe.inventory[var1].stackSize -= dAmt;
									this.attachedSafe.onInventoryChanged();
									dPaid = true;
									break;
								}
								else if (this.attachedSafe.inventory[var1].stackSize == dAmt)
								{
									if (this.inventory[2] == null)
									{
										this.inventory[2] = dCoins;
									}
									else if (this.inventory[2] != null)
									{
										this.inventory[2].stackSize += dAmt;
									}
									this.attachedSafe.inventory[var1] = null;
									this.attachedSafe.onInventoryChanged();
									dPaid = true;
									break;
								}
							}
						}
					}
				}
				else if (dAmt == 0) { dPaid = true;}
				if (eAmt > 0)
				{
					for (int var1 = 39; var1 < 45; var1++)
					{
						if (this.attachedSafe.inventory[var1] != null)
						{
							if (this.attachedSafe.inventory[var1].stackSize - eAmt >= 1)
							{
								if (this.inventory[3] == null)
								{
									this.inventory[3] = eCoins;
								}
								else if (this.inventory[3] != null)
								{
									this.inventory[3].stackSize += eAmt;
								}
								this.attachedSafe.inventory[var1].stackSize -= eAmt;
								ePaid = true;
								this.attachedSafe.onInventoryChanged();
								break;
							}
							else if (this.attachedSafe.inventory[var1].stackSize == eAmt)
							{
								this.attachedSafe.inventory[var1] = null;
								this.attachedSafe.onInventoryChanged();
								ePaid = true;
								break;
							}
						}
					}
				}
			
				else if (eAmt == 0) { ePaid = true;}
				
				if (gPaid && dPaid && ePaid)
				{
					if (this.inventory[4].stackSize == qty)
					{
						this.inventory[4] = null;
					}

					else if (this.inventory[4].stackSize - qty >= 1)
					{
						this.inventory[4].stackSize -= qty;
					}
				}
					
			}
		}
	
		/**
		 * Makes sure that the TraderSafe has enough coins/items for the transaction before allowing the transaction to proceed.
		 * @param slotID
		 * @param type
		 * @return
		 */
		public Boolean safeHasEnoughCoins(int slotID, int type)
		{
			int price = this.getPrice(slotID, type);
			int qty = 0;
			switch (type)
			{
			case 1:
					if(this.attachedSafe.getStackInSlot(27) != null)
					{
						if (this.attachedSafe.getStackInSlot(27).stackSize >= price) { return true; }
						else { qty += this.attachedSafe.getStackInSlot(27).stackSize;}
						
					}
					else if(this.attachedSafe.getStackInSlot(28) != null)
					{
						if (this.attachedSafe.getStackInSlot(28).stackSize >= price) { return true; }
						else { qty += this.attachedSafe.getStackInSlot(28).stackSize;}
						
					}
					else if(this.attachedSafe.getStackInSlot(29) != null)
					{
						if (this.attachedSafe.getStackInSlot(29).stackSize >= price) { return true; }
						else { qty += this.attachedSafe.getStackInSlot(29).stackSize;}
						
					}
					else if(this.attachedSafe.getStackInSlot(30) != null)
					{
						if (this.attachedSafe.getStackInSlot(30).stackSize >= price) { return true; }
						else { qty += this.attachedSafe.getStackInSlot(30).stackSize;}
						
					}
					else if(this.attachedSafe.getStackInSlot(31) != null)
					{
						if (this.attachedSafe.getStackInSlot(31).stackSize >= price) { return true; }
						else { qty += this.attachedSafe.getStackInSlot(31).stackSize;}
						
					}
					else if(this.attachedSafe.getStackInSlot(32) != null)
					{
						if (this.attachedSafe.getStackInSlot(32).stackSize >= price) { return true; }
						else { qty += this.attachedSafe.getStackInSlot(32).stackSize;}	
					}
					if (qty > price) {  return true;}
					else {break;}
					
			case 2:
				if(this.attachedSafe.getStackInSlot(33) != null)
				{
					if (this.attachedSafe.getStackInSlot(33).stackSize >= price) { return true; }
					else { qty += this.attachedSafe.getStackInSlot(33).stackSize;}
					
				}
				else if(this.attachedSafe.getStackInSlot(34) != null)
				{
					if (this.attachedSafe.getStackInSlot(34).stackSize >= price) { return true; }
					else { qty += this.attachedSafe.getStackInSlot(34).stackSize;}
					
				}
				else if(this.attachedSafe.getStackInSlot(35) != null)
				{
					if (this.attachedSafe.getStackInSlot(35).stackSize >= price) { return true; }
					else { qty += this.attachedSafe.getStackInSlot(35).stackSize;}
					
				}
				else if(this.attachedSafe.getStackInSlot(36) != null)
				{
					if (this.attachedSafe.getStackInSlot(36).stackSize >= price) { return true; }
					else { qty += this.attachedSafe.getStackInSlot(36).stackSize;}
					
				}
				else if(this.attachedSafe.getStackInSlot(37) != null)
				{
					if (this.attachedSafe.getStackInSlot(37).stackSize >= price) { return true; }
					else { qty += this.attachedSafe.getStackInSlot(37).stackSize;}
					
				}
				else if(this.attachedSafe.getStackInSlot(38) != null)
				{
					if (this.attachedSafe.getStackInSlot(38).stackSize >= price) { return true; }
					else { qty += this.attachedSafe.getStackInSlot(38).stackSize;}	
				}
				if (qty > price) {  return true;}
				else {break;}
			case 3:
				if(this.attachedSafe.getStackInSlot(39) != null)
				{
					if (this.attachedSafe.getStackInSlot(39).stackSize >= price) { return true; }
					else { qty += this.attachedSafe.getStackInSlot(39).stackSize;}
					
				}
				else if(this.attachedSafe.getStackInSlot(40) != null)
				{
					if (this.attachedSafe.getStackInSlot(40).stackSize >= price) { return true; }
					else { qty += this.attachedSafe.getStackInSlot(40).stackSize;}
					
				}
				else if(this.attachedSafe.getStackInSlot(41) != null)
				{
					if (this.attachedSafe.getStackInSlot(41).stackSize >= price) { return true; }
					else { qty += this.attachedSafe.getStackInSlot(41).stackSize;}
					
				}
				else if(this.attachedSafe.getStackInSlot(42) != null)
				{
					if (this.attachedSafe.getStackInSlot(42).stackSize >= price) { return true; }
					else { qty += this.attachedSafe.getStackInSlot(42).stackSize;}
					
				}
				else if(this.attachedSafe.getStackInSlot(43) != null)
				{
					if (this.attachedSafe.getStackInSlot(43).stackSize >= price) { return true; }
					else { qty += this.attachedSafe.getStackInSlot(43).stackSize;}
					
				}
				else if(this.attachedSafe.getStackInSlot(44) != null)
				{
					if (this.attachedSafe.getStackInSlot(44).stackSize >= price) { return true; }
					else { qty += this.attachedSafe.getStackInSlot(44).stackSize;}	
				}
				if (qty > price) {  return true;}
				else {break;}
			default:
				break;			
			}
			return false;
		}
		
	/*	
		public Boolean safeHasCoins(int type)
		{
			switch (type)
			{
			case 1:
				if ( this.attachedSafe.getStackInSlot(27) != null)
					return true;
				if ( this.attachedSafe.getStackInSlot(28) != null)
					return true;
				if ( this.attachedSafe.getStackInSlot(29) != null)
					return true;
				if ( this.attachedSafe.getStackInSlot(30) != null)
					return true;
				if ( this.attachedSafe.getStackInSlot(31) != null)
					return true;
				if ( this.attachedSafe.getStackInSlot(32) != null)
					return true;
			case 2:
				if ( this.attachedSafe.getStackInSlot(33) != null)
					return true;
				if ( this.attachedSafe.getStackInSlot(34) != null)
					return true;
				if ( this.attachedSafe.getStackInSlot(35) != null)
					return true;
				if ( this.attachedSafe.getStackInSlot(36) != null)
					return true;
				if ( this.attachedSafe.getStackInSlot(37) != null)
					return true;
				if ( this.attachedSafe.getStackInSlot(38) != null)
					return true;
			case 3:
				if ( this.attachedSafe.getStackInSlot(39) != null)
					return true;
				if ( this.attachedSafe.getStackInSlot(40) != null)
					return true;
				if ( this.attachedSafe.getStackInSlot(41) != null)
					return true;
				if ( this.attachedSafe.getStackInSlot(42) != null)
					return true;
				if ( this.attachedSafe.getStackInSlot(43) != null)
					return true;
				if ( this.attachedSafe.getStackInSlot(44) != null)
					return true;
			}
			
			return false;
		}
	*/
		

		
		public void putStackInSafeSlot (int slotID, ItemStack stack)
		{
			if(this.safeAttached == false)
				return;
			if(this.safeAttached == true)
			{
				this.attachedSafe.putStackInSlot(slotID, stack);
			}
			return;
		}
		
		
		@Override
		public Packet getDescriptionPacket()
		{
			return PacketManager.getPacket("MurderCoins", this, this.botOwner1, this.botOwner2, this.botOwner3, this.botOwner4, this.botOwner5, this.botOwner6, this.botOwner7, this.safeAttached,
					this.slot1GPrice, this.slot2GPrice, this.slot3GPrice, this.slot4GPrice, this.slot5GPrice, this.slot6GPrice, this.slot7GPrice, this.slot8GPrice, this.slot9GPrice,
					this.slot1DPrice, this.slot2DPrice, this.slot3DPrice, this.slot4DPrice, this.slot5DPrice, this.slot6DPrice, this.slot7DPrice, this.slot8DPrice, this.slot9DPrice,
					this.slot1EPrice, this.slot2EPrice, this.slot3EPrice, this.slot4EPrice, this.slot5EPrice, this.slot6EPrice, this.slot7EPrice, this.slot8EPrice, this.slot9EPrice,
					this.slot0Trade, this.slot1Trade, this.slot2Trade, this.slot3Trade, this.slot4Trade, this.slot5Trade, this.slot6Trade, this.slot7Trade, this.slot8Trade  );
		}
		@Override
		public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream) 
		{	
			int a1;
			int a2;
			int b1;
			int b2;
			int c1;
			int c2;
			int d1;
			int d2;
				if (this.worldObj.isRemote)
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
						this.slot0Trade = dataStream.readBoolean();
						this.slot1Trade = dataStream.readBoolean();
						this.slot2Trade = dataStream.readBoolean();
						this.slot3Trade = dataStream.readBoolean();
						this.slot4Trade = dataStream.readBoolean();
						this.slot5Trade = dataStream.readBoolean();
						this.slot6Trade = dataStream.readBoolean();
						this.slot7Trade = dataStream.readBoolean();
						this.slot8Trade = dataStream.readBoolean();
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
						e.printStackTrace();
					}	
			}
			else if(!this.worldObj.isRemote)
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
					this.slot0Trade = dataStream.readBoolean();
					this.slot1Trade = dataStream.readBoolean();
					this.slot2Trade = dataStream.readBoolean();
					this.slot3Trade = dataStream.readBoolean();
					this.slot4Trade = dataStream.readBoolean();
					this.slot5Trade = dataStream.readBoolean();
					this.slot6Trade = dataStream.readBoolean();
					this.slot7Trade = dataStream.readBoolean();
					this.slot8Trade = dataStream.readBoolean();
					this.processWanted = dataStream.readBoolean();
					this.tradeWanted = dataStream.readInt();
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
					e.printStackTrace();
				}
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
			return new int[] {0,1};
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
			this.botOwner1 = par1NBTTagCompound.getString("towner1");
			this.botOwner2 = par1NBTTagCompound.getString("towner2");
			this.botOwner3 = par1NBTTagCompound.getString("towner3");
			this.botOwner4 = par1NBTTagCompound.getString("towner4");
			this.botOwner5 = par1NBTTagCompound.getString("towner5");
			this.botOwner6 = par1NBTTagCompound.getString("towner6");
			this.botOwner7 = par1NBTTagCompound.getString("towner7");
			this.safeAttached = par1NBTTagCompound.getBoolean("safeAttached");
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
			this.slot0Trade = par1NBTTagCompound.getBoolean("slot0Trade");
			this.slot1Trade = par1NBTTagCompound.getBoolean("slot1Trade");
			this.slot2Trade = par1NBTTagCompound.getBoolean("slot2Trade");
			this.slot3Trade = par1NBTTagCompound.getBoolean("slot3Trade");
			this.slot4Trade = par1NBTTagCompound.getBoolean("slot4Trade");
			this.slot5Trade = par1NBTTagCompound.getBoolean("slot5Trade");
			this.slot6Trade = par1NBTTagCompound.getBoolean("slot6Trade");
			this.slot7Trade = par1NBTTagCompound.getBoolean("slot7Trade");
			this.slot8Trade = par1NBTTagCompound.getBoolean("slot8Trade");
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
			par1NBTTagCompound.setString("towner1", this.botOwner1);
			par1NBTTagCompound.setString("towner2", this.botOwner2);
			par1NBTTagCompound.setString("towner3", this.botOwner3);
			par1NBTTagCompound.setString("towner4", this.botOwner4);
			par1NBTTagCompound.setString("towner5", this.botOwner5);
			par1NBTTagCompound.setString("towner6", this.botOwner6);
			par1NBTTagCompound.setString("towner7", this.botOwner7);
			par1NBTTagCompound.setBoolean("safeAttached", this.safeAttached);
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
			par1NBTTagCompound.setBoolean("slot0Trade", this.slot0Trade);
			par1NBTTagCompound.setBoolean("slot1Trade", this.slot1Trade);
			par1NBTTagCompound.setBoolean("slot2Trade", this.slot2Trade);
			par1NBTTagCompound.setBoolean("slot3Trade", this.slot3Trade);
			par1NBTTagCompound.setBoolean("slot4Trade", this.slot4Trade);
			par1NBTTagCompound.setBoolean("slot5Trade", this.slot5Trade);
			par1NBTTagCompound.setBoolean("slot6Trade", this.slot6Trade);
			par1NBTTagCompound.setBoolean("slot7Trade", this.slot7Trade);
			par1NBTTagCompound.setBoolean("slot8Trade", this.slot8Trade);
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

