package murdercoins.client.Render;

import murdercoins.client.Model.ModelCoinPress;
import murdercoins.tileentity.tileEntityCoinPress;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class RenderCoinPressItem implements IItemRenderer
{
	public ModelCoinPress cpModel;
	
	public RenderCoinPressItem()
	{
		cpModel = new ModelCoinPress();
	}
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		// TODO Auto-generated method stub
		TileEntityRenderer.instance.renderTileEntityAt(new tileEntityCoinPress(), 0.0D, 0.0D, 0.0D, 0.0F);
	}

}
