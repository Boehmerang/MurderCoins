package murdercoins.client;

import murdercoins.container.containerBasicVault;
import murdercoins.container.containerPulverisor;
import murdercoins.tileentity.tileEntityBasicVault;
import murdercoins.tileentity.tileEntityPulverisor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class guiBasicVault extends GuiContainer 
{
	private int containerWidth;
	private int containerHeight;
	private tileEntityBasicVault tileentity;
	
	
	public guiBasicVault(InventoryPlayer player_inventory, tileEntityBasicVault tile_entity) 
	{
		super(new containerBasicVault(tile_entity, player_inventory));
		this.tileentity = tile_entity;
	}
	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j) 
	{

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		this.mc.renderEngine.bindTexture("/mods/MurderCoins/textures/basicVaultGui.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		containerWidth = (this.width - this.xSize) / 2;
		containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0,
				this.xSize, this.ySize);

	}
}
