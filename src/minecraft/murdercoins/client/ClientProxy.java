package murdercoins.client;

import net.minecraftforge.client.MinecraftForgeClient;
import murdercoins.client.Render.BlockRenderingHandler;
import murdercoins.client.Render.RenderCoinPress;
import murdercoins.client.Render.RenderCoinPressItem;
import murdercoins.client.Render.RenderGoldForge;
import murdercoins.common.CommonProxy;
import murdercoins.common.MurderCoins;
import murdercoins.tileentity.tileEntityCoinPress;
import murdercoins.tileentity.tileEntityGoldForge;
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
			
		//MinecraftForgeClient.registerItemRenderer(MurderCoins.coinPress.blockID, new RenderCoinPressItem());

	 }

}
