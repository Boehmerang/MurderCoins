package murdercoins.common;

import murdercoins.container.containerBasicTrader;
import murdercoins.container.containerBasicVault;
import murdercoins.container.containerCoinPress;
import murdercoins.container.containerGoldForge;
import murdercoins.container.containerManPress;
import murdercoins.container.containerPulverisor;
import murdercoins.tileentity.tileEntityBasicTrader;
import murdercoins.tileentity.tileEntityBasicVault;
import murdercoins.tileentity.tileEntityCoinPress;
import murdercoins.tileentity.tileEntityGoldForge;
import murdercoins.tileentity.tileEntityManPress;
import murdercoins.tileentity.tileEntityPulverisor;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class CommonProxy
{
	public void registerRenderThings()
	{
	}
	
	public GuiScreen getClientGui(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		return null;
	}
	
	public Container getServerGui(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		TileEntity tile_entity = world.getBlockTileEntity(x, y, z);
		switch (ID)
		{
		case 1:
            return new containerGoldForge((tileEntityGoldForge) tile_entity, player.inventory);
		case 2:
        	return new containerCoinPress((tileEntityCoinPress) tile_entity, player.inventory);
		case 3:
			return new containerManPress((tileEntityManPress) tile_entity, player.inventory);
		case 4:
			return new containerPulverisor((tileEntityPulverisor) tile_entity, player.inventory);
		case 5:
			return new containerBasicVault((tileEntityBasicVault) tile_entity, player.inventory);
		case 6:
			return new containerBasicTrader((tileEntityBasicTrader) tile_entity, player.inventory, false);
		case 7:
			return new containerBasicTrader((tileEntityBasicTrader) tile_entity, player.inventory, true);
		default:
			return null;
		}
		
		
		//return null;
	}
}
