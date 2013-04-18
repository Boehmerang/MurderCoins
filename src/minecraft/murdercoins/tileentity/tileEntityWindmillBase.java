package murdercoins.tileentity;

import java.util.HashSet;
import java.util.Set;

import murdercoins.common.helpers.IItemTurbines;
import murdercoins.common.helpers.IItemWindBlades;
import net.minecraft.client.Minecraft;
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
import universalelectricity.core.block.IConductor;
import universalelectricity.core.electricity.ElectricityNetworkHelper;
import universalelectricity.core.electricity.IElectricityNetwork;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectrical;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class tileEntityWindmillBase extends TileEntityElectrical implements IInventory, ISidedInventory, IPacketReceiver 
{

	private ItemStack[] inventory = new ItemStack[3];  //Itemstack containing the inventory.
	
	/*
	 * 		Minimum and Maximum watts the windmill will produced at any point.
	 */
	public static final int Max_Watts_Produced = 10000;
	public static final int Minum_Watts_Produced = 100;
	
	private static final float Max_Multiplier = 1.0f;
	private static final float Minimum_Multiplier = 0.3f;
	public static float Current_Multiplier = 0.0f;
	
	public double prevOutput, currentOutput = 0;
	
	public IConductor electricConnection = null;

	public boolean isGenerating, prevGenerating = false;						//	Is the Windmill Generating Power?

	public final Set<EntityPlayer> playersUsing = new HashSet<EntityPlayer>();
	
	
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if(this.Current_Multiplier <= 0){this.Current_Multiplier = this.Minimum_Multiplier;}
		
		if (!this.worldObj.isRemote)
		{
			this.prevOutput = this.currentOutput;

			// Check nearby blocks and see if the conductor is full. If so, then it is connected
			ForgeDirection outputDirection = ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite();
			TileEntity outputTile = VectorHelper.getConnectorFromSide(this.worldObj, new Vector3(this.xCoord, this.yCoord, this.zCoord), outputDirection);

			IElectricityNetwork network = ElectricityNetworkHelper.getNetworkFromTileEntity(outputTile, outputDirection);
			if (network != null)
			{
				if (network.getRequest().getWatts() > 0)
				{
					this.electricConnection = (IConductor) outputTile;
				}
				else
				{
					this.electricConnection = null;
				}
			}
			
			else
			{
				this.electricConnection = null;
			}
			
			if(!this.isDisabled() && this.canProduce() == true)
			{
				this.setGenerating(true);
				if (this.electricConnection != null)
				{
					this.currentOutput = Math.min(this.currentOutput + Math.min((this.currentOutput * 0.005 + Current_Multiplier), 5), this.Max_Watts_Produced);
				}				
				
			}
			if (!this.canProduce()||this.electricConnection == null)
			{
				this.currentOutput = Math.max(this.currentOutput - 8, 0);
				this.setGenerating(false);
			}
			if (this.electricConnection != null)
			{
				if (this.currentOutput > this.Minum_Watts_Produced)
				{	
					this.electricConnection.getNetwork().startProducing(this, (this.currentOutput/this.getVoltage())/20, this.getVoltage());
				}
				else
				{
					this.electricConnection.getNetwork().stopProducing(this);
				}
			}
			
			if (this.ticks % 3 == 0)
			{
				PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj);
				for (EntityPlayer player : this.playersUsing)
				{
					PacketDispatcher.sendPacketToPlayer(getDescriptionPacket(), (Player) player);
				}
			}
		}	
	
	}
	
	/*
	 *  		Checks to see if the windmill has all parts required.
	 */
	public boolean canProduce()
	{
		if (this.inventory[1] != null && this.inventory[2] != null)
		{
			return true;
		}
		return false;
	}
	public void setGenerating(boolean on) 
	{
		if(on == true)
		{
			if(this.isGenerating != true)
			{
				this.isGenerating = true;
				System.out.println(isGenerating);
				//this.prevGenerating = this.isGenerating;
			}
		}
		else
		{
			if(this.isGenerating != false)
			{
				this.isGenerating = false;
				System.out.println(isGenerating);
				//this.prevGenerating = this.isGenerating;
			}
		}
		PacketManager.sendPacketToClients(getDescriptionPacket(), this.worldObj);
		//this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
		this.worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, 0, 0);
	}
	/*
	 *  		Allows the block to connect to a wire on what we consider the "back" of the machine.
	 */
	@Override
	public boolean canConnect(ForgeDirection direction) 
	{
		return direction == ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite();
	}
	@Override
	public Packet getDescriptionPacket()
	{
		return PacketManager.getPacket("MurderCoins", this, this.currentOutput, this.disabledTicks, this.isGenerating);
	}
	@Override
	public void handlePacketData(INetworkManager network, int packetType,Packet250CustomPayload packet, EntityPlayer player,	ByteArrayDataInput dataStream)
	{
		try
		{
			if (this.worldObj.isRemote)
			{
				this.currentOutput = dataStream.readDouble();
				this.disabledTicks = dataStream.readInt();
				this.isGenerating = dataStream.readBoolean();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

	@Override
	public int[] getSizeInventorySide(int var1) {
		// TODO Auto-generated method stub
		return null;
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
		return LanguageRegistry.instance().getStringLocalization("Windmill");
	}

	@Override
	public boolean isInvNameLocalized() 
	{
		return true;
	}

	@Override
	public int getInventoryStackLimit() 
	{
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) 
	{
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : entityplayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;

	}

	@Override
	public void openChest() 
	{
		
	}

	@Override
	public void closeChest()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		// TODO Auto-generated method stub
		return false;
	}

	public double getOutput() 
	{
			return this.currentOutput;
	}
	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.disabledTicks = par1NBTTagCompound.getInteger("smeltingTicks");
		NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
		this.inventory = new ItemStack[this.getSizeInventory()];
		this.currentOutput = par1NBTTagCompound.getDouble("joulesStored");

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
		par1NBTTagCompound.setInteger("smeltingTicks", this.disabledTicks);
		par1NBTTagCompound.setDouble("joules", this.currentOutput);
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
