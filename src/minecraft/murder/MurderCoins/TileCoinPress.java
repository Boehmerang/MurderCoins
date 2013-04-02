package murder.MurderCoins;

import java.util.EnumSet;

import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.block.IElectricityStorage;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.electricity.IElectricityNetwork;
import universalelectricity.core.item.ElectricItemHelper;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.implement.IDisableable;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityRunnable;

import com.google.common.io.ByteArrayDataInput;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import murder.MurderCoins.BlockCoinPress;

public class TileCoinPress extends TileEntityElectricityRunnable implements IInventory, IPacketReceiver, IElectricityStorage, IDisableable
{
	public final double WATTS_PER_TICK = 500.0D;
	public final double TRANSFER_LIMIT = 1250.0D;
	public int processTicks = 0;
	public double joulesStored = 0.0D;
	private int recipeTicks = 0;
	public static double maxJoules = 1500000.0D;
	private ItemStack[] inventory = new ItemStack[8];
	private int playersUsing = 0;
	public static double joulesPerSmelt = 50000.0D;
	public static int meltingTicks = 500;
	public double prevJoules = 0;

	@Override
	public void initiate()
	{
		//ElectricityConnections.registerConnector(this, EnumSet.of(ForgeDirection.getOrientation(3)));
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, murderCoins.goldForge.blockID);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		this.prevJoules = joulesStored;
		
		if (!this.worldObj.isRemote)
		{
			/**
			 * Decharge electric item.
			 */
			setJoules(getJoules() + ElectricItemHelper.dechargeItem(this.inventory[0], getMaxJoules() - getJoules(), getVoltage()));
			if (!this.worldObj.isRemote)
			{
				if (!this.isDisabled())
				{
					ElectricityPack electricityPack = ElectricityNetworkHelper.consumeFromMultipleSides(this, this.getConsumingSides(), this.getRequest());
					this.onReceive(electricityPack);
				}
				else
				{
					ElectricityNetworkHelper.consumeFromMultipleSides(this, new ElectricityPack());
				}
			}
			
			
			/*ForgeDirection inputDirection = ForgeDirection.getOrientation(3);
			//TileEntity inputTile = VectorHelper.getConnectorFromSide(this.worldObj, new Vector3(this), inputDirection);
			
			//IElectricityNetwork inputNetwork = ElectricityNetworkHelper.getNetworkFromTileEntity(inputTile, inputDirection);

			if (getJoules() <= getMaxJoules())
			{				
				if (this.joulesStored <= maxJoules)
				{
				//	inputNetwork.startRequesting(this, WATTS_PER_TICK/this.getVoltage() , this.getVoltage());
				//  ElectricityPack electricityPack = inputNetwork.consumeElectricity(this);
					ElectricityPack electricityPack = ElectricityNetworkHelper.consumeFromMultipleSides(this, this.getConsumingSides(), this.getRequest());
					//this.setJoules(this.getJoules() + electricityPack.getWatts());

					if (UniversalElectricity.isVoltageSensitive)
					{
						if (electricityPack.voltage > this.getVoltage())
						{
							this.worldObj.createExplosion(null, this.xCoord, this.yCoord, this.zCoord, 2f, true);
						}
					}
				}
				else
				{
					//inputNetwork.stopRequesting(this);
					ElectricityNetworkHelper.consumeFromMultipleSides(this,new ElectricityPack());
				}
			}*/
			if(canPress()&& hasEnoughPower())
			{
				if(this.processTicks == 0)
				{
					this.processTicks = meltingTicks;
				}
				else if(this.processTicks > 0)
				{
					this.processTicks--;
					
					if(this.processTicks < 1)
					{
						if(this.inventory[5].getItem()==murderCoins.dDust||this.inventory[5].getItem()==murderCoins.eDust)
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
		}
		if (!this.worldObj.isRemote)
		{
			if (this.ticks % 3 == 0)
			{
				PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
			}
		}
	}

	@Override
	public ElectricityPack getRequest()
	{
		return new ElectricityPack((this.getMaxJoules() - this.getJoules()) / this.getVoltage(), this.getVoltage());
	}
	/*	public ElectricityPack getRequest()
	{
		if (this.joulesStored < this.maxJoules)
		{
		return new ElectricityPack((getMaxJoules() - getJoules()) / getVoltage(), getVoltage());
		}
		if(this.joulesStored >= this.maxJoules)
		{
			this.joulesStored = this.maxJoules;
			return new ElectricityPack(getMaxJoules(),getVoltage());
		}
		return new ElectricityPack();	
	}*/
	@Override
	  public void onReceive(ElectricityPack electricityPack)
	  {
	    if (UniversalElectricity.isVoltageSensitive)
	    {
	      if (electricityPack.voltage > getVoltage())
	      {
	    	  this.worldObj.createExplosion(null, this.xCoord, this.yCoord, this.zCoord, 1.5f, true);
				return;
	      }
	    }

	    setJoules(getJoules() + electricityPack.getWatts());
	  }
	@Override
	protected EnumSet<ForgeDirection> getConsumingSides()
	{
		return ElectricityNetworkHelper.getDirections(this);
	}
	@Override
	public Packet getDescriptionPacket()
	{
		return PacketManager.getPacket("MurderCoins", this, new Object[] { Integer.valueOf(this.processTicks), Integer.valueOf(this.disabledTicks) });
	}

	@Override
	public void handlePacketData(INetworkManager network, int packetType,Packet250CustomPayload packet, EntityPlayer player,ByteArrayDataInput dataStream) 
	{
		try
		{
			this.setJoules(dataStream.readDouble());
			this.disabledTicks = dataStream.readInt();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
	}
	
	public boolean hasEnoughPower()
	{
		if(this.joulesStored >= joulesPerSmelt)
		{
		return true;	
		}
		else
		{
		return false;
		
		}
	}

/*	public boolean canPress()
	{	
		if (inventory[3]==null)
		{
			return false;
		}
		if(inventory[4] == null)
		{
			return false;
		}
		if (inventory[5].isItemEqual(new ItemStack(murderCoins.dDust)))
		{
			if (inventory[6].isItemEqual(new ItemStack(murderCoins.meltedBucket)))
			{
				return true;
			}
			//if (pipeConnected())
			//  {
			//  return true;
			//  }
			//			
			return false;						
		}
		else if (inventory[5].isItemEqual(new ItemStack(murderCoins.eDust)))
		{
			if (inventory[6].isItemEqual(new ItemStack(murderCoins.meltedBucket))){
				
				return true;
			}
			return false;
		}
		else
		{
			return false;
		}
	}*/
	public boolean canPress()
	{	
		if (this.inventory[3] == null)
		{
			this.processTicks = 0;
			return false;
		}
		if (this.inventory[4] == null)
		{
			//if(pipeConnected())
			//{
			//	return true;
			//}
			this.processTicks = 0;
			return false;
		}
		if (this.inventory[5] == null)
		{
			/*if(this.inventory[6].isItemEqual(new ItemStack(murderCoins.meltedBucket)))
			{
				return true;
			}
			/*if (this.inventory[6].isItemEqual(new ItemStack(murderCoins.meltedBucket)))
			{
				return true;
			}
			/*if (pipeConnected())
			 {
			  	return true;
			 }
			 */
			return false;
		}
		if (this.inventory[6]==null)
		{
			return false;
		}
		/*else if (this.inventory[5].isItemEqual(new ItemStack(murderCoins.eDust)))
		{
			if (this.inventory[6].isItemEqual(new ItemStack(murderCoins.meltedBucket))){
				return true;
			}
			this.processTicks = 0;
			return false;
		}*/
		
		
			return true;
		
	}
	public boolean pipeConnected()
	{
		return false;
	}
	public void pressCoins(boolean isDust)
	{
		if(!canPress())
		{
			return;
		}
		if(!isDust)
		{
		ItemStack itemstack = new ItemStack(murderCoins.gCoin,4);
		if(this.inventory[7]==null)
		{
			this.inventory[7] = itemstack;
		}
		else if(this.inventory[7].isItemEqual(new ItemStack(murderCoins.gCoin)))
		{
			this.inventory[7].stackSize += 4;
		}
		//this.decrStackSize(5, 1); no dust to downgrade.
    	this.decrStackSize(6, 1);
    	setJoules(getJoules() - joulesPerSmelt);
		}
		else if (this.inventory[5].isItemEqual(new ItemStack(murderCoins.dDust))) 
		{
			ItemStack itemstack = new ItemStack(murderCoins.dCoin,4);
			if(this.inventory[7]==null)
			{
				this.inventory[7] = itemstack;
			}
			else if(this.inventory[7].isItemEqual(new ItemStack(murderCoins.dCoin)))
			{
				this.inventory[7].stackSize += 4;
			}
			this.decrStackSize(5, 1);
	    	this.decrStackSize(6, 1);
	    	setJoules(getJoules() - joulesPerSmelt);
		}
		else if (this.inventory[5].isItemEqual(new ItemStack(murderCoins.eDust))) 
		{
			ItemStack itemstack = new ItemStack(murderCoins.eCoin,4);
			if(this.inventory[7]==null)
			{
				this.inventory[7] = itemstack;
			}
			else if(this.inventory[7].isItemEqual(new ItemStack(murderCoins.eCoin)))
			{
				this.inventory[7].stackSize += 4;
			}
			this.decrStackSize(5, 1);
	    	this.decrStackSize(6, 1);
	    	setJoules(getJoules() - joulesPerSmelt);
		}
	}

	/**
	 * Reads a tile entity from NBT.
	 */

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.processTicks = par1NBTTagCompound.getInteger("processTicks");
		NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
		this.inventory = new ItemStack[getSizeInventory()];
		try
		{
			this.joulesStored = par1NBTTagCompound.getDouble("joulesStored");
		}
		catch (Exception e)
		{
		}

		for (int var3 = 0; var3 < var2.tagCount(); var3++)
		{
			NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
			byte var5 = var4.getByte("Slot");

			if ((var5 >= 0) && (var5 < this.inventory.length))
				this.inventory[var5] = ItemStack.loadItemStackFromNBT(var4);
		}
	}

	/**
	 * Writes a tile entity to NBT.
	 */

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("processTicks", this.processTicks);
		NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < this.inventory.length; var3++)
		{
			if (this.inventory[var3] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)var3);
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

	@Override
	public double getJoules() {
		// TODO Auto-generated method stub
		return this.joulesStored;
	}

	@Override
	public void setJoules(double joules) {
		// TODO Auto-generated method stub
		this.joulesStored = Math.max(Math.min(joules, getMaxJoules()), 0);
	}

	@Override
	public double getMaxJoules() {
		// TODO Auto-generated method stub
		return this.maxJoules;
	}

	public int getProcessTicks() {
		// TODO Auto-generated method stub
		
		return processTicks;
	}

	@Override
	public boolean canConnect(ForgeDirection direction) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isInvNameLocalized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		// TODO Auto-generated method stub
		return false;
	}

}