package murder.MurderCoins;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

//import universalelectricity.components.common.BasicComponents;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import universalelectricity.core.electricity.ElectricityDisplay;
import murder.MurderCoins.BlockGoldForge;
import murder.MurderCoins.TileGoldForge;

@SideOnly(Side.CLIENT)
public class goldForgeGui extends GuiContainer {

	private int containerWidth;
	private int containerHeight;
	private TileGoldForge tileentity;

	public goldForgeGui(InventoryPlayer player_inventory,
			TileGoldForge tile_entity) {
		super(new GoldForgeContainer(tile_entity, player_inventory));
		this.tileentity = tile_entity;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		String capacityInfo = ElectricityDisplay.getDisplay(
				this.tileentity.getJoules(), ElectricUnit.JOULES);
		String displayText = "";
		fontRenderer.drawString("Gold Forge", 6, 6, 0xffffff);
		fontRenderer.drawString(
				StatCollector.translateToLocal("container.inventory"), 6,
				ySize - 96, 0xffffff);
		if (this.tileentity.isDisabled()) {
			displayText = "Disabled!";
		} else if (this.tileentity.processTicks > 0) {
			displayText = "Working";
		} else {
			displayText = "Idle";
		}

		this.fontRenderer.drawString("Status:  " + displayText, 82, 45,
				0xffffff);
		this.fontRenderer.drawString(ElectricityDisplay.getDisplay(
				this.tileentity.getVoltage(), ElectricUnit.VOLTAGE), 82, 56,
				0xffffff);
		this.fontRenderer.drawString(capacityInfo, 82, 68, 0xffffff);

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2,
			int par3) {
		this.mc.renderEngine
				.bindTexture("/mods/MurderCoins/textures/goldForgeGui.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		containerWidth = (this.width - this.xSize) / 2;
		containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0,
				this.xSize, this.ySize);

		if (this.tileentity.processTicks > 0) {
			int scale = (int) (((double) this.tileentity.processTicks / (double) this.tileentity.meltingTicks) * 23);
			this.drawTexturedModalRect(containerWidth + 77,
					containerHeight + 24, 176, 0, 23 - scale, 20);
		}
		if (this.tileentity.getJoules() > 0) {
			int scale2 = (int) (((double) this.tileentity.getJoules() / (double) this.tileentity
					.getMaxJoules()) * 48);
			this.drawTexturedModalRect(containerWidth + 118,
					containerHeight + 70, 176, 31, /* 48 - scale2 */
					0 + scale2, 8);
		}
	}
}

/*
 * @Override protected void drawGuiContainerBackgroundLayer(float f, int i, int
 * j){
 * 
 * int picture =
 * mc.renderEngine.getTexture("/mods/murder/MurderCoins/goldForgeGui.png");
 * 
 * GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
 * 
 * this.mc.renderEngine.bindTexture(picture);
 * 
 * int x = (width - xSize) / 2;
 * 
 * int y = (height - ySize) / 2;
 * 
 * this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize); } }
 * 
 * 
 * /** Draw the foreground layer for the GuiContainer (everything in front of
 * the items)
 */
/*
 * @Override protected void drawGuiContainerForegroundLayer(int par1, int par2)
 * { this.fontRenderer.drawString(this.tileEntity.getInvName(), 69, 6, 4210752);
 * this.fontRenderer.drawString("Input:", 14, 28, 4210752); String displayText =
 * "";
 * 
 * if (this.tileEntity.isDisabled()) { displayText = "Disabled!"; } else if
 * (this.tileEntity.smeltingTicks > 0) { displayText = "Working"; } else {
 * displayText = "Idle"; }
 * 
 * this.fontRenderer.drawString("Status: " + displayText, 82, 45, 4210752);
 * this.fontRenderer.drawString("Voltage: " +
 * ElectricInfo.getDisplayShort(this.tileEntity.getVoltage(),
 * ElectricUnit.VOLTAGE), 82, 56, 4210752);
 * this.fontRenderer.drawString("Require: " +
 * ElectricInfo.getDisplayShort(this.tileEntity.WATTS_PER_TICK * 20,
 * ElectricUnit.WATT), 82, 68, 4210752);
 * this.fontRenderer.drawString(StatCollector
 * .translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752); }
 * 
 * /** Draw the background layer for the GuiContainer (everything behind the
 * items)
 */
/*
 * @Override protected void drawGuiContainerBackgroundLayer(float par1, int
 * par2, int par3) { int var4 =
 * this.mc.renderEngine.getTexture("/mods/murder/MurderCoins/goldForgeGui.png");
 * GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
 * this.mc.renderEngine.bindTexture(var4); containerWidth = (this.width -
 * this.xSize) / 2; containerHeight = (this.height - this.ySize) / 2;
 * this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize,
 * this.ySize);
 * 
 * if (this.tileEntity.smeltingTicks > 0) { int scale = (int) (((double)
 * this.tileEntity.smeltingTicks / (double)
 * this.tileEntity.SMELTING_TIME_REQUIRED) * 23);
 * this.drawTexturedModalRect(containerWidth + 77, containerHeight + 24, 176, 0,
 * 23 - scale, 20); } }
 */

