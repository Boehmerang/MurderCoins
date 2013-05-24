package murdercoins.client;

import murdercoins.common.MurderCoins;
import murdercoins.container.containerBasicTrader;
import murdercoins.container.containerBasicTraderShop;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class guiHandler implements IGuiHandler{
		@Override
		public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
            return MurderCoins.proxy.getServerGui(ID, player, world, x,y,z);
			/*
			TileEntity tile_entity = world.getBlockTileEntity(x, y, z);

            if(tile_entity instanceof tileEntityGoldForge)
            {
                return new containerGoldForge((tileEntityGoldForge) tile_entity, player.inventory);
            }
            if(tile_entity instanceof tileEntityCoinPress)
            {
            	return new containerCoinPress((tileEntityCoinPress) tile_entity, player.inventory);
            }
			if(tile_entity instanceof tileEntityManPress)
			{
				return new containerManPress((tileEntityManPress) tile_entity, player.inventory);
			}
			if(tile_entity instanceof tileEntityPulverisor)
			{
				return new containerPulverisor((tileEntityPulverisor) tile_entity, player.inventory);
			}
			if(tile_entity instanceof tileEntityBasicVault)
			{
				return new containerBasicVault((tileEntityBasicVault) tile_entity, player.inventory);
			}
			if(tile_entity instanceof tileEntityBasicTrader)
			{
				switch (ID)
				{
				case 0:
					return new containerBasicTrader((tileEntityBasicTrader) tile_entity, player.inventory, false);
				case 1:
					return new containerBasicTrader((tileEntityBasicTrader) tile_entity, player.inventory, true);
				}
			}
			*/
			
			//return null;
		}

		@Override
		public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
            
			return MurderCoins.proxy.getClientGui(ID, player, world, x, y, z);
			/*TileEntity tile_entity = world.getBlockTileEntity(x, y, z);

            if(tile_entity instanceof tileEntityGoldForge){
            	return new guiGoldForge(player.inventory,(tileEntityGoldForge) tile_entity);
            }
            if(tile_entity instanceof tileEntityCoinPress)
            {
            	return new guiCoinPress(player.inventory,(tileEntityCoinPress) tile_entity);
            }
            if(tile_entity instanceof tileEntityManPress)
			{
				return new guiManPress(player.inventory,(tileEntityManPress) tile_entity);
			}
            if(tile_entity instanceof tileEntityPulverisor)
  			{
  				return new guiPulverisor(player.inventory,(tileEntityPulverisor) tile_entity);
  			}
			if(tile_entity instanceof tileEntityBasicVault)
			{
				return new guiBasicVault(player.inventory,(tileEntityBasicVault) tile_entity);
			}
			if(tile_entity instanceof tileEntityBasicTrader)
			{
				switch (ID)
				{
				case 0:
					return new guiBasicTrader(player.inventory,(tileEntityBasicTrader) tile_entity);
				case 1:
					return new guiBasicTraderShop(player.inventory,(tileEntityBasicTrader) tile_entity);
				default:
					break;
				}
			}*/
			
            //return null;
		}
}