package murder.MurderCoins;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;

public class coinPressGUI extends GuiContainer
{
	

	private int containerWidth;
	private int containerHeight;
	private TileCoinPress tileentity2;
	
    public coinPressGUI(InventoryPlayer player_inventory, TileCoinPress tile_entity)
    {
        super(new coinPressContainer(tile_entity, player_inventory));
        this.tileentity2 = tile_entity;
    }
  /*  @Override
    protected void drawGuiContainerForegroundLayer(int i, int j)
    {
    	String capacityInfo = ElectricityDisplay.getDisplay(this.tileentity.getJoules(), ElectricUnit.JOULES);
	    String displayText = "";
        fontRenderer.drawString("Coin Press", 82, 6, 0xffffff);
        //fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 6, ySize - 96 , 0xffffff);
        if (this.tileentity.isDisabled())
		{
			displayText = "Disabled!";
		}
		else if (this.tileentity.processTicks > 0)
		{
			displayText = "Working";
		}
		else
		{
			displayText = "Idle";
		}

		this.fontRenderer.drawString("Status:  " + displayText, 82, 45, 0xffffff);
		//this.fontRenderer.drawString(ElectricityDisplay.getDisplay(this.tile_entity.getVoltage(), ElectricUnit.VOLTAGE), 82, 56, 0xffffff);
		this.fontRenderer.drawString(capacityInfo, 82, 56, 0xffffff);
		
	}*/
    @Override
    protected void drawGuiContainerForegroundLayer(int i, int j)
    {
    	String capacityInfo = ElectricityDisplay.getDisplay(this.tileentity2.getJoules(), ElectricUnit.JOULES);
	    String displayText = "";
        fontRenderer.drawString("Coin Press", 6, 6, 0xffffff);
        //fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 6, ySize - 96 , 0xffffff);
        if (this.tileentity2.isDisabled())
		{
			displayText = "Disabled!";
		}
		else if (this.tileentity2.processTicks > 0)
		{
			displayText = "Working";
		}
		else
		{
			displayText = "Idle";
		}

		this.fontRenderer.drawString("Status:  " + displayText, 82, 45, 0xffffff);
		//this.fontRenderer.drawString(ElectricityDisplay.getDisplay(this.tileentity.getVoltage(), ElectricUnit.VOLTAGE), 82, 56, 0xffffff);
		this.fontRenderer.drawString(capacityInfo, 82, 56, 0xffffff);
		
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		this.mc.renderEngine.bindTexture("/mods/MurderCoins/textures/coinPressGui.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		containerWidth = (this.width - this.xSize) / 2;
		containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);

		if (this.tileentity2.processTicks > 0)
		{
			int scale = (int) (((double) this.tileentity2.processTicks / (double) this.tileentity2.meltingTicks) * 23);
			this.drawTexturedModalRect(containerWidth + 77, containerHeight + 24, 176, 0, 23 - scale, 20);
		}
	}
}

