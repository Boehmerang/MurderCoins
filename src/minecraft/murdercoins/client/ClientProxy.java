package murdercoins.client;

import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import murdercoins.client.Render.RenderCoinPress;
import murdercoins.client.Render.RenderCoinPressItem;
import murdercoins.client.Render.RenderGoldForge;
import murdercoins.common.CommonProxy;
import murdercoins.common.MurderCoins;
import murdercoins.tileentity.tileEntityCoinPress;
import murdercoins.tileentity.tileEntityGoldForge;

public class ClientProxy extends CommonProxy
{
	 @Override
	  public void registerRenderThings()
	 {
		/* MinecraftForgeClient.preloadTexture("/mods/murder/MurderCoins/MurderCoins.png");
		*  MinecraftForgeClient.preloadTexture("/mods/murder/MurderCoins/goldForgeGui.png");
		   MinecraftForgeClient.preloadTexture("/mods/murder/murderCoins/coinPressGui.png");
		*/
		ClientRegistry.bindTileEntitySpecialRenderer(tileEntityCoinPress.class, new RenderCoinPress());
			
		//MinecraftForgeClient.registerItemRenderer(MurderCoins.coinPress.blockID, new RenderCoinPressItem());
	 }
		@Override
		public void InitRendering()
		{
			
		}
}
