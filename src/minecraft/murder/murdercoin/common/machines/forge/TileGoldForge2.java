package murder.murdercoin.common.machines.forge;

import java.util.EnumSet;

import murder.murdercoin.common.MurderCoins;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.block.IElectricityStorage;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.ElectricItemHelper;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.implement.IDisableable;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityRunnable;

import com.google.common.io.ByteArrayDataInput;

/*
 * 
 * 		Old version of the GoldForge, kept for reference.
 * 		Does not work right, but some functions will be
 * 		transfered to new tile.
 * 
 */
public class TileGoldForge2 extends TileEntityElectricityRunnable implements IInventory, IPacketReceiver, IElectricityStorage, IDisableable//, ISidedInventory
{
	public final double WATTS_PER_TICK = 500.0D;
	public double WattsReceived;
	public final double TRANSFER_LIMIT = 1250.0D;
	public int processTicks = 0;
	public double joulesStored = 0.0D;
	private int recipeTicks = 0;
	public static double maxJoules = 1500000.0D;
	private ItemStack[] inventory = new ItemStack[4];
	private int playersUsing = 0;
	public static double joulesPerSmelt = 50000.0D;
	public static int meltingTicks = 500;
	public double prevJoules = 0;


	@Override
	public void initiate()
	{
		//ElectricityConnections.registerConnector(this, EnumSet.of(ForgeDirection.getOrientation(3)));
		
		this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, MurderCoins.goldForge.blockID);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		//if (!this.worldObj.isRemote)
		if(!isDisabled())
		{
			
			//
			//  Decharge electric item.
			// 
			setJoules(getJoules() + ElectricItemHelper.dechargeItem(this.inventory[0], getMaxJoules() - getJoules(), getVoltage()));		
			
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
				if (!this.worldObj.isRemote)//(inputNetwork != null)
				{
					//double joulesNeeded = getMaxJoules()-getJoules();
					//inputNetwork.startRequesting(inputTile, getMaxJoules() - getJoules() , getVoltage());	
					ElectricityPack electricityPack = ElectricityNetworkHelper.consumeFromMultipleSides(this, this.getConsumingSides(), this.getRequest());
					//inputNetwork.startRequesting(inputTile, getRequest());
					onReceive(electricityPack);
					
					this.WattsReceived = electricityPack.getWatts();
					//setJoules(getJoules() + this.WattsReceived);
					
					if (UniversalElectricity.isVoltageSensitive)
					{
						if (electricityPack.voltage > this.getVoltage())
						{
							this.worldObj.createExplosion(null, this.xCoord, this.yCoord, this.zCoord, 2f, true);
							return;
						}
						
				}
				else
				{
					//inputNetwork.stopRequesting(inputTile);
					ElectricityNetworkHelper.consumeFromMultipleSides(this,new ElectricityPack());
				}
			}*/
				
			if(canMelt()&& hasEnoughPower())
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
						if(this.inventory[1].getItem()==Item.goldNugget)
						{
					    meltGold(true);
						setJoules(getJoules()-joulesPerSmelt);
					    this.processTicks = 0;
						}
						else
						{
							meltGold(false);
							setJoules(getJoules()-joulesPerSmelt);
							this.processTicks = 0;
						}
					}
				}
				else
				{
					this.processTicks = 0;
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
		}
	}

	@Override
	public ElectricityPack getRequest()
	{
		return new ElectricityPack((getMaxJoules() - getJoules()) / getVoltage(), getVoltage());
	}
	@Override
	public void onReceive(ElectricityPack electricityPack)
	{
	/**
	 * Creates an explosion if the voltage is too high.
	 */
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
	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			setJoules(dataStream.readDouble());
			disabledTicks = dataStream.readInt();
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

	public boolean canMelt()
	{	
		if (inventory[1] == null)
		{
			this.processTicks = 0;
			return false;
		}
		if (inventory[2] == null)
		{
			//if(pipeConnected())
			//{
			//	return true;
			//}
			this.processTicks = 0;
			return false;
		}
		if (inventory[1].isItemEqual(new ItemStack(Item.ingotGold)))
		{
			if (inventory[2].isItemEqual(new ItemStack(Item.bucketEmpty)))
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
		else if (inventory[1].isItemEqual(new ItemStack(Item.goldNugget)))
		{
			if (inventory[2].isItemEqual(new ItemStack(Item.bucketEmpty))){
				if (inventory[1].stackSize >=8)
				{
					return true;
				}
				
				return false;
			}
			return false;
		}
		else
		{
			return false;
		}
	}
	public boolean pipeConnected()
	{
		return false;
	}
	public void meltGold(boolean isNuggets)
	{
		if(!canMelt())
		{
			return;
		}
		if(!isNuggets)
		{
		ItemStack itemstack = new ItemStack(MurderCoins.itemMeltedGoldBuket,1);
		if(this.inventory[3]==null)
		{
			this.inventory[3] = itemstack;
		}
		else if(this.inventory[3].isItemEqual(new ItemStack(MurderCoins.itemMeltedGoldBuket)))
		{
			this.inventory[3].stackSize += 1;
		}
		this.decrStackSize(1, 1);
    	this.decrStackSize(2, 1);
    	setJoules(getJoules() - joulesPerSmelt);
		}
		else 
		{
			ItemStack itemstack = new ItemStack(MurderCoins.itemMeltedGoldBuket,1);
			if(this.inventory[3]==null)
			{
				this.inventory[3] = itemstack;
			}
			else if(this.inventory[3].isItemEqual(new ItemStack(MurderCoins.itemMeltedGoldBuket)))
			{
				this.inventory[3].stackSize += 1;
			}
			this.decrStackSize(1, 8);
	    	this.decrStackSize(2, 1);
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
		return "Gold Forge";
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
		return direction == ForgeDirection.getOrientation(3);
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

/*	@Override
	public int[] getSizeInventorySide(int var1) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean func_102007_a(int slot, ItemStack itemstack, int j) {
		 TODO Auto-generated method stub
		if(slot == 0)
		{
			return ((IItemElectric) itemstack.getItem()).getReceiveRequest(itemstack).getWatts() <= 0;
		}
		else if (slot == 1)
		{
			if (itemstack.isItemEqual(new ItemStack(Item.bucketEmpty)))
			{
			return true;
			}
		}
		return false;
	}

	@Override
	public boolean func_102008_b(int i, ItemStack itemstack, int j) {
		// TODO Auto-generated method stub
		return false;
	} 	*/
	
}