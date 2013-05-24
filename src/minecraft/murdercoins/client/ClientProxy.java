package murdercoins.client;

import murdercoins.client.Render.BlockRenderingHandler;
import murdercoins.client.Render.RenderCoinPress;
import murdercoins.client.Render.RenderGoldForge;
import murdercoins.common.CommonProxy;
import murdercoins.common.MurderCoins;
import murdercoins.tileentity.tileEntityBasicTrader;
import murdercoins.tileentity.tileEntityBasicVault;
import murdercoins.tileentity.tileEntityCoinPress;
import murdercoins.tileentity.tileEntityGoldForge;
import murdercoins.tileentity.tileEntityManPress;
import murdercoins.tileentity.tileEntityPulverisor;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
	 @Override
	  public void registerRenderThings()
	 {
		/* MinecraftForgeClient.preloadTexture("/mods/murder/MurderCoins/MurderCoins.png");
		*  MinecraftForgeClient.preloadTexture("/mods/murder/MurderCoins/goldForgeGui.png");
		   MinecraftForgeClient.preloadTexture("/mods/murder/murderCoins/coinPressGui.png");
		*/
		RenderingRegistry.registerBlockHandler(new BlockRenderingHandler());
		ClientRegistry.bindTileEntitySpecialRenderer(tileEntityCoinPress.class, new RenderCoinPress());
		ClientRegistry.bindTileEntitySpecialRenderer(tileEntityGoldForge.class, new RenderGoldForge());
		
		MurderCoins.goldLiquid.canonical().setRenderingIcon(MurderCoins.GoldStill.getBlockTextureFromSide(0));
		MurderCoins.goldLiquid.canonical().setTextureSheet("/terrain.png");
		
		LiquidDictionary.getCanonicalLiquid(MurderCoins.goldLiquid).setRenderingIcon(MurderCoins.GoldStill.getBlockTextureFromSide(0));
		LiquidDictionary.getCanonicalLiquid(MurderCoins.goldLiquid).setTextureSheet("/terrain.png");
		
		
		//MinecraftForgeClient.registerItemRenderer(MurderCoins.coinPress.blockID, new RenderCoinPressItem());

	 }
		@Override
		public GuiScreen getClientGui(int ID, EntityPlayer player, World world, int x, int y, int z) 
		{
			TileEntity tile_entity = world.getBlockTileEntity(x, y, z);
			switch (ID)
			{
			case 1:
					return new guiGoldForge(player.inventory,(tileEntityGoldForge) tile_entity);
			case 2:
            	return new guiCoinPress(player.inventory,(tileEntityCoinPress) tile_entity);
            
			case 3:
				return new guiManPress(player.inventory,(tileEntityManPress) tile_entity);
			
			case 4:
  				return new guiPulverisor(player.inventory,(tileEntityPulverisor) tile_entity);

			case 5:
				return new guiBasicVault(player.inventory,(tileEntityBasicVault) tile_entity);

			case 6:
					return new guiBasicTrader(player.inventory,(tileEntityBasicTrader) tile_entity);
			case 7:
					return new guiBasicTraderShop(player.inventory,(tileEntityBasicTrader) tile_entity);
			default:
					break;
				
			
			}
			return null;
		}
}
