package murdercoins.client;

import murdercoins.container.containerPulverisor;
import murdercoins.tileentity.tileEntityPulverisor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class guiPulverisor extends GuiContainer 
{
	private int containerWidth;
	private int containerHeight;
	private tileEntityPulverisor tileentity;
	
	
	public guiPulverisor(InventoryPlayer player_inventory, tileEntityPulverisor tile_entity) 
	{
		super(new containerPulverisor(tile_entity, player_inventory));
		this.tileentity = tile_entity;
	}
	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		String capacityInfo = ElectricityDisplay.getDisplay(this.tileentity.getJoules(), ElectricUnit.JOULES);
		String displayText = "";
		fontRenderer.drawString("Pulverisor", 62, 6, 0xffffff);
		if (this.tileentity.isDisabled())
		{
			displayText = "Disabled!";
		}
		else if (this.tileentity.processTicks > 0) {
			displayText = "Working";
		}
		else
		{
			displayText = "Idle";
		}

		this.fontRenderer.drawString("Status:  " + displayText, 55, 45,	0xffffff);
		this.fontRenderer.drawString(ElectricityDisplay.getDisplay(this.tileentity.getVoltage(), ElectricUnit.VOLTAGE), 56, 56, 0xffffff);
		this.fontRenderer.drawString(capacityInfo, 30, 68, 0xffffff);
		}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2,
			int par3) {
		this.mc.renderEngine
				.bindTexture("/mods/MurderCoins/textures/pulverisorGui.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		containerWidth = (this.width - this.xSize) / 2;
		containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0,
				this.xSize, this.ySize);

		if (this.tileentity.processTicks > 0) {
			int scale = (int) (((double) this.tileentity.processTicks / (double) this.tileentity.crushingTicks) * 23);
			this.drawTexturedModalRect(containerWidth + 77, containerHeight + 24, 176, 0, 23 - scale, 20);
		}
		if (this.tileentity.getJoules() > 0)
		{
			int scale2 = (int) (((double) this.tileentity.joulesStored / (double) this.tileentity.getMaxJoules()) * 96);
			this.drawTexturedModalRect(containerWidth + 28, containerHeight + 67, 2, 168, 0 + scale2, 9);
		}
	}
}
