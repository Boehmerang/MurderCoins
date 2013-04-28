package murdercoins.tileentity;

import java.util.Random;

import mekanism.api.ITubeConnection;
import murdercoins.common.Config;
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
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;
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

public class tileEntityGoldForge extends  TileEntityElectricityRunnable implements IInventory, ISidedInventory, IPacketReceiver, IElectricityStorage, ITankContainer, ITubeConnection
{
	
	public int 					processTicks = 0;
	public int 					tankWarmingTicks = 0;
	public int 					ticksToWarm;// = 100;
	public int 					ticksWithoutPower = 0;
	public int 					ticksTillFreeze;// = 1200;
	public int 					fillTicks = 0;
	public int 					ticksToFill = 20;
	private int 				playersUsing = 0;
	//public int 				goldStored = 0;
	public int					maxGold = 20 * LiquidContainerRegistry.BUCKET_VOLUME;
	public int 					goldPerBucket = LiquidContainerRegistry.BUCKET_VOLUME;
	public static int 			meltingTicks;// = 500;	
	
	public double 				joulesStored = 0.0D;
	public static double 		maxJoules = 1500000.0D;
	public static double 		joulesPerSmelt;// = 50000.0D;
	public static double		joulesToWarm;
	private double 				tankRunningJoules;// = 10.0D;
	
	public LiquidTank 			gFtank;//	= new LiquidTank(this.maxGold);
	
	public boolean 				isFrozen = false;
	
	private ItemStack[] 		inventory = new ItemStack[4];
	public Random random = new Random();
	
	Config						configLoader		= new Config();
	
	public tileEntityGoldForge()
	{
		this.meltingTicks = Config.GFprocessTicks;
		this.ticksToWarm = Config.GFticksToWarm;
		this.ticksTillFreeze = Config.GFticksTillFreeze;
		this.joulesPerSmelt = Config.GFjoulesPerUse;
		this.tankRunningJoules = Config.GFtankJoules;
		this.joulesToWarm = Config.GFunfreezeJoules;
		gFtank	= new LiquidTank(this.maxGold);
		//gFtank.setLiquid(MurderCoins.goldLiquid);
	}
	
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		/**
		 * Attempts to charge from battery in slot 1.
		 */
		this.setJoules(this.getJoules() + ElectricItemHelper.dechargeItem(this.inventory[0], this.getMaxJoules() - this.getJoules(), this.getVoltage()));
		/**
		 * Trys to melt the gold.
		 */
		if (!this.worldObj.isRemote)
		{
			/*
			 * 		Checks to see if the machine is currently in "Frozen" state, if the machine is "Frozen" it will check available power, and if there
			 * 		is enough power available, it will enter the "Warming" state.
			 */
			if(this.isFrozen == true)
			{
				if (this.getJoules()> this.joulesToWarm)
				{
					if(this.tankWarmingTicks == 0)
					{
					this.tankWarmingTicks = (gFtank.getLiquid().amount/1000)*ticksToWarm;
					}
					else if(this.tankWarmingTicks > 1)
					{
						this.tankWarmingTicks --;
						if(this.tankWarmingTicks == 1)
						{
							this.isFrozen = false;
							this.setJoules(this.getJoules() - this.joulesToWarm);
							this.tankWarmingTicks = 0;
						}
					}
				}
			}
			/*if(gFtank.getLiquid() != null)
            {
                  	for(ForgeDirection orientation : ForgeDirection.VALID_DIRECTIONS)
                    {
                    	if(gFtank.getLiquid() == null || gFtank.getLiquid().amount <= 0)
                    	{
                    		break;
                    	}
       
                        TileEntity tileEntity = VectorHelper.getTileEntityFromSide(worldObj, new Vector3(xCoord, yCoord, zCoord), orientation);

                        if(tileEntity instanceof ITankContainer)
                        {
                        	int fill = ((ITankContainer)tileEntity).fill(orientation.getOpposite(), gFtank.getLiquid(), false);
                        	if(fill > 0)
                        	{
                        		fill = ((ITankContainer)tileEntity).fill(orientation.getOpposite(), gFtank.getLiquid(), true);
                        		gFtank.drain(fill, true);
                        		                         
                        	}
                        }
                  }
            }*/
			if(this.gFtank.getLiquid() != null) 
			{
				//for(ForgeDirection orientation : ForgeDirection.VALID_DIRECTIONS) 
				//{
					ForgeDirection orientation = ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite();
					TileEntity tileEntity = VectorHelper.getTileEntityFromSide(worldObj, new Vector3(xCoord, yCoord, zCoord), orientation);

					if(tileEntity instanceof ITankContainer) 
					{	
						if(this.gFtank.getLiquid() == null || this.gFtank.getLiquid().amount <= 0) 
						{
							return;
						}
						this.gFtank.drain(((ITankContainer)tileEntity).fill(orientation.getOpposite(), this.gFtank.getLiquid(), true), true);
						//this.goldStored = this.tank.getLiquid().amount;
					//}
				}
			}
			/*int originalVolume = 0;

			if (this.tank.getLiquid() != null)
			{
				originalVolume = this.tank.getLiquid().amount;

				if (ticks % (random.nextInt(4) * 5 + 10) >= 0)
				{
					this.drain(ForgeDirection.DOWN, this.goldPerBucket,  true);
					this.goldStored = this.tank.getLiquid().amount;
				}

				if ((this.tank.getLiquid() == null && originalVolume != 0) || (this.tank.getLiquid() != null && this.tank.getLiquid().amount != originalVolume))
				{
					this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
				}
			}*/

			/*
			 * checks to see if there is gold in tank, and if there is enough power to warm it. If not the
			 * machine will enter "Frozen" status.
			 */
			if (this.gFtank.getLiquid() != null)
			{
				if (this.gFtank.getLiquid().amount > 0)
				{
					if(this.getJoules() < this.tankRunningJoules)
					{
						this.ticksWithoutPower++;
						if(this.ticksWithoutPower == this.ticksTillFreeze)
						{
							this.isFrozen = true;
							this.ticksWithoutPower = 0;
						}
					}
				}
			}
			/*
			 * 	Checks to see if machine can process, if yes - it melts the gold and adds it to the tank.
			 */

			if (this.canProcess())
			{
				if (this.getJoules() >= joulesPerSmelt)
				{
					if (this.processTicks == 0)
					{
						this.processTicks = this.meltingTicks;
					}
					else if (this.processTicks > 0)
					{
						this.processTicks--;

						/**
						 * Process the item when the process timer is done.
						 */
						if (this.processTicks < 1)
						{
							if(this.inventory[1].getItem() == Item.goldNugget)
							{
								this.smeltItem(true);
								this.processTicks = 0;
								this.setJoules(getJoules() - joulesPerSmelt);
							}
							else
							{
								this.smeltItem(false);
								this.processTicks = 0;
								this.setJoules(getJoules() - joulesPerSmelt);
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
			if(this.inventory[2] != null  && this.gFtank.getLiquid() != null && this.isFrozen == false)
			{
					if (this.gFtank.getLiquid().amount >= this.goldPerBucket)
					{
						if(this.fillTicks == 0)
						{
							this.fillTicks = this.ticksToFill;
						}
						else if (this.fillTicks > 0)
						{
							this.fillTicks--;
							if (this.fillTicks < 1)
							{
								if(this.inventory[3] != null)
								{
									if (this.inventory[3].stackSize >= 16)
									{
										return;
									}
								}
								//this.setGold(goldPerBucket, false);
								this.gFtank.drain(goldPerBucket, true);
								System.out.println("drained gold");
								ItemStack itemstack = new ItemStack(MurderCoins.bucketGold);
								if(this.inventory[3]==null)
								{
									this.inventory[3] = itemstack;
								}
								else if(this.inventory[3].isItemEqual(new ItemStack(MurderCoins.bucketGold)))
								{
									this.inventory[3].stackSize += 1;
								}
								else if(this.inventory[3].stackSize >16)
								{
									this.inventory[3].stackSize = 16;
								}
								this.decrStackSize(2, 1);
							}
						}
					}
			}
			if (this.ticks % 3 == 0 && this.playersUsing > 0)
			{
				PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
				if(this.gFtank.getLiquid() != null)
				{
					this.setJoules(this.getJoules() - this.tankRunningJoules );
				}
			}
		}
	}

	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		return direction == ForgeDirection.UP;//ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite();
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
		if (this.gFtank.getLiquid() != null)
		{
			return PacketManager.getPacket("MurderCoins", this, this.processTicks, this.getJoules(),this.gFtank.getLiquid().itemID, this.gFtank.getLiquid().amount, this.gFtank.getLiquid().itemMeta, this.isFrozen, this.tankWarmingTicks,this.getGold());
		}
		else
		{
			return PacketManager.getPacket("MurderCoins", this, this.processTicks, this.getJoules(), 0, 0, 0, this.isFrozen, this.tankWarmingTicks,this.getGold());
		}
	}

	@Override
	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			this.processTicks = dataStream.readInt();
			this.setJoules(dataStream.readDouble());
			this.gFtank.setLiquid(new LiquidStack(dataStream.readInt(), dataStream.readInt(), dataStream.readInt()));
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
		if(inventory[1] == null)
		{
			this.processTicks = 0;
			return false;
		}
		if(gFtank.getLiquid() != null)
		{
			if (gFtank.getLiquid().amount >= this.maxGold)
			{
				gFtank.getLiquid().amount = this.maxGold;
				return false;
			}
		}
		if (inventory[1].isItemEqual(new ItemStack(Item.goldNugget)))
		{
			return true;
		}
		if(inventory[1].isItemEqual(new ItemStack(Item.ingotGold)))
		{
			return true;
		}

		return true;
	}

	/**
	 * Turn one item from the furnace source stack into the appropriate smelted item in the furnace
	 * result stack
	 */
	public void smeltItem(boolean isNuggets)
	{
		{
			LiquidStack liquid = LiquidDictionary.getLiquid("Gold", this.goldPerBucket);
			if(!canProcess())
			{
				return;
			}
			if(!isNuggets)
			{
				this.decrStackSize(1, 1);
				this.gFtank.fill(liquid, true);
				System.out.println("filled gold");
				
			}
			else
			{
				this.decrStackSize(1, 8);
				this.gFtank.fill(liquid, true);
				System.out.println("filled gold");
			}
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
		if(par1NBTTagCompound.hasKey("liquidTank"))
    	{
			this.gFtank.readFromNBT(par1NBTTagCompound.getCompoundTag("liquidTank"));
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
	    if(this.gFtank.getLiquid() != null)
        {
	    	par1NBTTagCompound.setTag("liquidTank", this.gFtank.writeToNBT(new NBTTagCompound()));
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

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring stack size) into
	 * the given slot.
	 */
	@Override
	public boolean isStackValidForSlot(int slotID, ItemStack itemStack)
	{
		//return slotID == 1 ? itemStack.isItemEqual(new ItemStack(Item.ingotGold)) : (slotID == 0 ? itemStack.getItem() instanceof IItemElectric : (slotID == 2 ? itemStack.isItemEqual(new ItemStack(Item.bucketEmpty)): false));
		if(itemStack.getItem() instanceof IItemElectric)
		{
			return slotID == 0;
		}
		else if(itemStack.isItemEqual(new ItemStack(Item.ingotGold))||itemStack.isItemEqual(new ItemStack(Item.goldNugget)))
		{
			return slotID == 1;
		}
		else if(slotID==2)
		{
			return true;
		}
		return slotID == 2;
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
	/*
	 * 				Read the amount of gold in the tank and send it to the gui.
	 */
	public int getGold()
	{
		if(this.gFtank.getLiquid() == null)
		{
			return 0;
		}
		else if (this.gFtank.getLiquid() != null)
		{
			if(this.gFtank.getLiquid().amount > 0)
			{
				return this.gFtank.getLiquid().amount;
			}
			return 0;
		}
		return 0;
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
		return 0;
	}

	@Override
	public int fill(int tankIndex, LiquidStack resource, boolean doFill) 
	{
		return 0;
	}

	@Override
	public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) 
	{
		return this.drain(0, maxDrain, doDrain);
	}
	@Override
	public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain) 
	{
		if (tankIndex != 0 || gFtank.getLiquid() == null)
		{
			return null;
		}
		LiquidStack stack = gFtank.getLiquid();
		if (maxDrain < stack.amount)
		{
			stack = this.getStack(stack, maxDrain);
		}
		return gFtank.drain(maxDrain, doDrain);
	}
	public static LiquidStack getStack(LiquidStack stack, int vol)
	{
		if (stack == null)
		{
			return null;
		}
		return new LiquidStack(stack.itemID, vol, stack.itemMeta);
	}
	@Override
	public ILiquidTank[] getTanks(ForgeDirection direction) 
	{
		return new ILiquidTank[] { gFtank };
	}

	@Override
	public ILiquidTank getTank(ForgeDirection direction, LiquidStack type) 
	{
		if (type == null)
		{
			return null;
		}
		if (type.isLiquidEqual(gFtank.getLiquid()))
		{
			return gFtank;
		}
		return null;
	}
	
	@Override
	public boolean canTubeConnect(ForgeDirection side) 
	{
		
		return side == ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite();
	}
}