package murdercoins.client.Render;

import murdercoins.client.Model.ModelGoldForge;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderGoldForge extends TileEntitySpecialRenderer
{
    public static final ModelGoldForge modelGoldForge = new ModelGoldForge();
    public static int gfModelID = RenderingRegistry.getNextAvailableRenderId();
    public RenderGoldForge()
    {
    	//modelGoldForge = new ModelGoldForge();
    }
     
    @Override
    public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float tick)
    {
		
		int rotation = 0;
		if(tileentity.worldObj != null)
		{
			int temp = tileentity.getBlockMetadata();
			switch (temp)
			{
				case 0:
					rotation = 180;
					break;
				case 1:
					rotation = 0;
					break;
				case 2:
					rotation = 90;
					break;
				case 3:
					rotation = 270;
					break;
				default:
					rotation = 0;
					break;
			}				
		}
		bindTextureByName("/mods/MurderCoins/textures/models/GoldForge.png"); //texture
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d0 + 0.5F, (float)d1 + 1.5F, (float)d2 + 0.5F);
		GL11.glScalef(1.0F, -1F, -1F);
		GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
		modelGoldForge.render(0.0625F);
		GL11.glPopMatrix(); //end
    }

}