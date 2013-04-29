package murdercoins.client.Render;

import org.lwjgl.opengl.GL11;

import murdercoins.client.Model.ModelCoinPress;
import murdercoins.tileentity.tileEntityCoinPress;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class RenderCoinPress  extends TileEntitySpecialRenderer
{	
	public static final ModelCoinPress cpModel  = new ModelCoinPress();;
	public RenderCoinPress()
	{
		//cpModel = new ModelCoinPress();
	}
	public void renderAModelAt(tileEntityCoinPress tile, double d, double d1, double d2, float f) {

		int rotation = 0;
		if(tile.worldObj != null)
		{
			int temp = tile.getBlockMetadata();
			switch (temp)
			{
			case 0:
				rotation = 0;
				break;
			case 1:
				rotation = 0;
				break;
			case 2:
				rotation = 90;
				break;
			case 3:
				rotation = 0;
				break;
			default:
				rotation = 0;
				break;
			}				
		}
		bindTextureByName("CoinPress.png"); //texture
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d + 0.5F, (float)d1 + 1.5F, (float)d2 + 0.5F);
		GL11.glScalef(1.0F, -1F, -1F);
		GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
		cpModel.render(0.0625F);
		GL11.glPopMatrix(); //end
		}
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) 
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
		bindTextureByName("/mods/MurderCoins/textures/models/CoinPress.png"); //texture
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d0 + 0.5F, (float)d1 + 1.5F, (float)d2 + 0.5F);
		GL11.glScalef(1.0F, -1F, -1F);
		GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
		cpModel.render(0.0625F);
		GL11.glPopMatrix(); //end
	}

}
