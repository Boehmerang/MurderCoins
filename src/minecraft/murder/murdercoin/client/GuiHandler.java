package murder.murdercoin.client;

import murder.murdercoin.common.machines.forge.GoldForgeContainer;
import murder.murdercoin.common.machines.forge.TileGoldForge;
import murder.murdercoin.common.machines.press.ContainerCoinPress;
import murder.murdercoin.common.machines.press.TileEntityCoinPress;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler{

		@Override
		public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
            TileEntity tile_entity = world.getBlockTileEntity(x, y, z);
            
            
            if(tile_entity instanceof TileGoldForge)
            	{
   
                    return new GoldForgeContainer((TileGoldForge) tile_entity, player.inventory);
            	}        
            
            if(tile_entity instanceof TileEntityCoinPress)
            {
            	return new ContainerCoinPress((TileEntityCoinPress) tile_entity, player.inventory);
            }

			return null;
		}


		@Override
		public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		     
            TileEntity tile_entity = world.getBlockTileEntity(x, y, z);
           
           
            if(tile_entity instanceof TileGoldForge){
   
            	return new goldForgeGui(player.inventory,(TileGoldForge) tile_entity);
            }
            if(tile_entity instanceof TileEntityCoinPress)
            {
            	return new GuiCoinPress(player.inventory,(TileEntityCoinPress) tile_entity);
            } 
   
    return null;
		}
}