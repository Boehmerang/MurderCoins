package murdercoins.client.Render;

import murdercoins.client.Model.ModelGoldForge;
import murdercoins.tileentity.tileEntityGoldForge;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class RenderGoldForge extends TileEntitySpecialRenderer
{
    private ModelGoldForge modelGoldForge = new ModelGoldForge();
     
    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick)
    {
        modelGoldForge.render((tileEntityGoldForge)tileEntity, x, y, z);
    }
}