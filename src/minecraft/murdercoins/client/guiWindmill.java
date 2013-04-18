package murdercoins.client;

import murdercoins.container.containerPulverisor;
import murdercoins.container.containerWindmillBase;
import murdercoins.tileentity.tileEntityPulverisor;
import murdercoins.tileentity.tileEntityWindmillBase;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class guiWindmill extends GuiContainer 
{
	private int containerWidth;
	private int containerHeight;
	private tileEntityWindmillBase tileentity;
	
	
	public guiWindmill(InventoryPlayer player_inventory, tileEntityWindmillBase tile_entity) 
	{
		super(new containerWindmillBase(tile_entity, player_inventory));
		this.tileentity = tile_entity;
	}
	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		String capacityInfo = ElectricityDisplay.getDisplayShort(this.tileentity.getOutput(), ElectricUnit.JOULES);
		String displayText = "";
		fontRenderer.drawString("Basic Windmill", 62, 6, 0xffffff);
		if (this.tileentity.isDisabled())
		{
			displayText = "Disabled!";
		}
		else if (this.tileentity.isGenerating == true) {
			displayText = "Working";
		}
		else
		{
			displayText = "Idle";
		}

		this.fontRenderer.drawString("Status:  " + displayText, 55, 45,	0xffffff);
		this.fontRenderer.drawString(ElectricityDisplay.getDisplay(this.tileentity.getVoltage(), ElectricUnit.VOLTAGE), 56, 56, 0xffffff);
		this.fontRenderer.drawString("Output:  " + capacityInfo, 30, 68, 0xffffff);
		}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2,
			int par3) {
		this.mc.renderEngine
				.bindTexture("/mods/MurderCoins/textures/windmillGui.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		containerWidth = (this.width - this.xSize) / 2;
		containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0,
				this.xSize, this.ySize);

		if (this.tileentity.currentOutput > 0) {
			int scale = (int) (((double) this.tileentity.currentOutput / (double) this.tileentity.Max_Watts_Produced) * 96);
			//this.drawTexturedModalRect(containerWidth + 77, containerHeight + 24, 176, 0, 0 + scale, 20);
			this.drawTexturedModalRect(containerWidth + 28, containerHeight + 67, 2, 168, 0 + scale, 9);
		}
		/*
		if (this.tileentity.getOutput() > 0)
		{
			int scale2 = (int) (((double) this.tileentity.currentOutput / (double) this.tileentity.prevOutput) * 96);
			this.drawTexturedModalRect(containerWidth + 28, containerHeight + 67, 2, 168, 0 + scale2, 9);
		}
		*/
	}
}
