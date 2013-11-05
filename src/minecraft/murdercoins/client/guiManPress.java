package murdercoins.client;

import murdercoins.container.containerManPress;
import murdercoins.tileentity.tileEntityManPress;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class guiManPress extends GuiContainer
{
	public static final ResourceLocation text = new ResourceLocation("murdercoins","textures/manpressgui.png");
	private int containerWidth;
	private int containerHeight;
	private tileEntityManPress tileentity;

    public guiManPress(InventoryPlayer player_inventory, tileEntityManPress tile_entity)
    {
        super(new containerManPress(tile_entity, player_inventory));
        this.tileentity = tile_entity;
    }

    protected void drawGuiContainerForegroundLayer(int i, int j)
    {
    	//String capacityInfo = ElectricityDisplay.getDisplay(this.tileentity.getJoules(), ElectricUnit.JOULES);
	    String displayText = "";
        fontRenderer.drawString("Coin Press", 82, 6, 0xffffff);
        /*fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 6, ySize - 96 , 0xffffff);
        if (this.tileentity.isDisabled())
		{
			displayText = "Disabled!";
		}*/
		if (this.tileentity.processTicks > 0)
		{
			displayText = "Working";
		}
		else
		{
			displayText = "Idle";
		}

		this.fontRenderer.drawString("Status:  " + displayText, 71, 45, 0xffffff);
		//this.fontRenderer.drawString(ElectricityDisplay.getDisplay(this.tileentity.getVoltage(), ElectricUnit.VOLTAGE), 82, 56, 0xffffff);
		//this.fontRenderer.drawString(capacityInfo, /*82, 56,*/30,68, 0xffffff);
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		//ResourceLocation text = new ResourceLocation("/mods/MurderCoins/textures/manPressGui.png");
		this.mc.renderEngine.bindTexture(text);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		containerWidth = (this.width - this.xSize) / 2;
		containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);

		if (this.tileentity.processTicks > 0)
		{
			int scale = (int) (((double) this.tileentity.processTicks / (double) this.tileentity.meltingTicks) * 23);
			this.drawTexturedModalRect(containerWidth + 77, containerHeight + 24, 176, 0, 23 - scale, 20);
		}
	}
}
