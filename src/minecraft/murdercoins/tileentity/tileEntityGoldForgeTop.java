package murdercoins.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.tile.TileEntityElectrical;

public class tileEntityGoldForgeTop extends TileEntityElectrical
{
	TileEntity mainTE;	
	public tileEntityGoldForgeTop()
	{
	
	}
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		this.worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, 3, 4, 1);
	}

	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		if(this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == 1)
		{
			return direction == ForgeDirection.getOrientation(this.worldObj.getBlockMetadata(xCoord, yCoord-1, zCoord) + 2).getOpposite() || direction == ForgeDirection.DOWN;
		}
		return false;
	}
	//@Override
	public ElectricityPack getRequest()
	{
		ForgeDirection orientation = ForgeDirection.getOrientation(0);
		TileEntity tileentity = VectorHelper.getTileEntityFromSide(worldObj, new Vector3(xCoord, yCoord, zCoord), orientation);
		if (tileentity instanceof tileEntityGoldForge)
		{
			return new ElectricityPack((float) ((((tileEntityGoldForge)tileentity).getMaxJoules() - ((tileEntityGoldForge)tileentity).getJoules()) / ((tileEntityGoldForge)tileentity).getVoltage()), ((tileEntityGoldForge)tileentity).getVoltage());
		}
		return new ElectricityPack();
	}

	//@Override
	public void onReceive(ElectricityPack electricityPack)
	{
		/**
		 * Creates an explosion if the voltage is too high.
		 */	
		ForgeDirection orientation = ForgeDirection.getOrientation(0);
		TileEntity tileentity = VectorHelper.getTileEntityFromSide(worldObj, new Vector3(xCoord, yCoord, zCoord), orientation);
		if (UniversalElectricity.isVoltageSensitive)
		{
			if (electricityPack.voltage > ((tileEntityGoldForge)tileentity).getVoltage())
			{
				this.worldObj.createExplosion(null, this.xCoord, this.yCoord, this.zCoord, 1.5f, true);
				return;
			}
		}
		if (tileentity instanceof tileEntityGoldForge)
		{
			((tileEntityGoldForge)tileentity).setJoules(((tileEntityGoldForge)tileentity).getJoules() + electricityPack.getWatts());
		}
	}
	@Override
	public float getRequest(ForgeDirection direction) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public float getProvide(ForgeDirection direction) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public float getMaxEnergyStored() {
		// TODO Auto-generated method stub
		return 0;
	}
}
