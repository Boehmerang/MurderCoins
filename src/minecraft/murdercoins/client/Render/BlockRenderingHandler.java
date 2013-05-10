package murdercoins.client.Render;

import murdercoins.block.blockCoinPress;
import murdercoins.block.blockGoldForge;
import murdercoins.client.Model.ModelCoinPress;
import murdercoins.client.Model.ModelGoldForge;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;

@SideOnly(Side.CLIENT)
public class BlockRenderingHandler implements ISimpleBlockRenderingHandler
{
	public static BlockRenderingHandler instance = new BlockRenderingHandler();
	public static final int ID = RenderingRegistry.getNextAvailableRenderId();
	
	public ModelCoinPress coinPress = new ModelCoinPress();
	public ModelGoldForge goldForge = new ModelGoldForge();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) 
	{
			GL11.glPushMatrix();
			GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);

			if (block instanceof blockGoldForge)
			{   
		        GL11.glBindTexture(GL11.GL_TEXTURE_2D, FMLClientHandler.instance().getClient().renderEngine.getTexture("/mods/MurderCoins/textures/models/GoldForge.png"));
				GL11.glTranslatef((float) 0.0F, (float) 0.3F, (float) 0.0F);
				GL11.glRotatef(180f, 90f, 0f, 1f);
		        goldForge.render(0.0400F);
		        GL11.glPopMatrix();
			}
			else if (block instanceof blockCoinPress)
			{
		        GL11.glBindTexture(GL11.GL_TEXTURE_2D, FMLClientHandler.instance().getClient().renderEngine.getTexture("/mods/MurderCoins/textures/models/CoinPress.png"));
		        GL11.glTranslatef((float)-1.F, (float)1.2F, (float)0.5F);
		        GL11.glRotatef(180f, 180.0F, 1.0F, 0.0F);
				coinPress.render(0.0400F);
				GL11.glPopMatrix(); //end
			}
		
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) 
	{
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory() 
	{
		return true;
	}

	@Override
	public int getRenderId() 
	{
		return ID;
	}

}
