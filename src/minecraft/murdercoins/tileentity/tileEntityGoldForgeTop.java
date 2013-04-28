package murdercoins.tileentity;

import net.minecraftforge.common.ForgeDirection;
import universalelectricity.prefab.tile.TileEntityConductor;

public class tileEntityGoldForgeTop extends TileEntityConductor
{
	public tileEntityGoldForgeTop()
	{
		this.channel = "MurderCoins";
	}
	@Override
	public double getResistance() {
		// TODO Auto-generated method stub
		return 0.05;
	}

	@Override
	public double getCurrentCapcity() {
		// TODO Auto-generated method stub
		return 200;
	}
	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		return direction == ForgeDirection.getOrientation(this.worldObj.getBlockMetadata(xCoord, yCoord-1, zCoord) + 2).getOpposite() || direction == ForgeDirection.DOWN;
	}
}
