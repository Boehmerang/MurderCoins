package murdercoins.client.Model;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import murdercoins.tileentity.tileEntityGoldForge;
import net.minecraft.client.model.ModelBase;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ModelGoldForge  extends ModelBase
{
    private IModelCustom modelGoldForge;
     
    public ModelGoldForge()
    {
        modelGoldForge = AdvancedModelLoader.loadModel("/mods/OBJTutorial/models/tutBox.obj");
    }
    private void render()
    {
    	modelGoldForge.renderAll();
    }
    public void render(tileEntityGoldForge box, double x, double y, double z)
	{
		// Push a blank matrix onto the stack
		GL11.glPushMatrix();

		// Move the object into the correct position on the block (because the OBJ's origin is the center of the object)
		GL11.glTranslatef((float)x + 0.5f, (float)y + 0.5f, (float)z + 0.5f);
		
		// Scale our object to about half-size in all directions (the OBJ file is a little large)
		GL11.glScalef(0.5f, 0.5f, 0.5f);

		// Bind the texture, so that OpenGL properly textures our block.
		FMLClientHandler.instance().getClient().renderEngine.bindTexture("/mods/OBJTutorial/textures/models/TutBox.png");

		// Render the object, using modelTutBox.renderAll();
		this.render();

		// Pop this matrix from the stack.
		GL11.glPopMatrix();
	}
}