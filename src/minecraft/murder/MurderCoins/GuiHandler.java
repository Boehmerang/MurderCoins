package murder.MurderCoins;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.src.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.Mod.Instance; 

public class GuiHandler implements IGuiHandler{

		@Override
		public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
            TileEntity tile_entity = world.getBlockTileEntity(x, y, z);
            
            
            if(tile_entity instanceof TileGoldForge)
            	{
   
                    return new GoldForgeContainer((TileGoldForge) tile_entity, player.inventory);
            	}        
            
            if(tile_entity instanceof TileCoinPress)
            {
            	return new coinPressContainer((TileCoinPress) tile_entity, player.inventory);
            }

			return null;
		}


		@Override
		public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		     
            TileEntity tile_entity = world.getBlockTileEntity(x, y, z);
           
           
            if(tile_entity instanceof TileGoldForge){
   
            	return new goldForgeGui(player.inventory,(TileGoldForge) tile_entity);
            }
            if(tile_entity instanceof TileCoinPress)
            {
            	return new coinPressGUI(player.inventory,(TileCoinPress) tile_entity);
            } 
   
    return null;
		}
}