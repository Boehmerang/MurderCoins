package murdercoins.client;

import murdercoins.container.containerCoinPress;
import murdercoins.tileentity.tileEntityCoinPress;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;

public class guiCoinPress extends GuiContainer
{
	public static final ResourceLocation text = new ResourceLocation("murdercoins","textures/coinpressgui.png");
	private int containerWidth;
	private int containerHeight;
	private tileEntityCoinPress tileentity;

    public guiCoinPress(InventoryPlayer player_inventory, tileEntityCoinPress tile_entity)
    {
        super(new containerCoinPress(tile_entity, player_inventory));
        this.tileentity = tile_entity;
    }

    protected void drawGuiContainerForegroundLayer(int i, int j)
    {
    	String capacityInfo = ElectricityDisplay.getDisplay((float) this.tileentity.getJoules(), ElectricUnit.JOULES);
	    String displayText = "";
        fontRenderer.drawString("Coin Press", 74, 6, 0xffffff);
        //fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 6, ySize - 96 , 0xffffff);
       /* if ((this.tileentity).isDisabled())
		{
			displayText = "Disabled!";
		}
        else*/ if (this.tileentity.isFrozen == true)
        {
        	if(this.tileentity.tankWarmingTicks>0)
        	{
        		displayText="Warming";
        	}
        	else
        	{
        		displayText = "Frozen";
        	}
        }
		else if (this.tileentity.processTicks > 0)
		{
			displayText = "Working";
		}
		else
		{
			displayText = "Idle";
		}

		this.fontRenderer.drawString("Status: " + displayText, 65, 45, 0xffffff);
		this.fontRenderer.drawString(ElectricityDisplay.getDisplay(this.tileentity.getVoltage(), ElectricUnit.VOLTAGE), 72, 56, 0xffffff);
		this.fontRenderer.drawString(capacityInfo, /*82, 56,*/30,68, 0xffffff);
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		//ResourceLocation text = new ResourceLocation("MurderCoins","coinPressGui.png");
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
		if (this.tileentity.getJoules() > 0)
		{
			int scale2 = (int) (((double) this.tileentity.joulesStored / (double) this.tileentity.getMaxJoules()) * 96);
			this.drawTexturedModalRect(containerWidth + 28, containerHeight + 67, 2, 168, 0 + scale2, 9);
		}
		if (this.tileentity.CPtank.getLiquid().amount > 0)
		{
			int scale2 = (int) (((double) this.tileentity.getStoredGold().amount / (double) this.tileentity.maxGold) * 44);
			this.drawTexturedModalRect(containerWidth + 12, containerHeight + 10/*54*/, 185, /*65*/21 ,16,44-scale2);
		}
		if (this.tileentity.getStoredGold().amount == 0)
		{
			//int scale2 = (int) (((double) this.tileentity.goldStored / (double) this.tileentity.maxGold) * 44);
			this.drawTexturedModalRect(containerWidth + 12, containerHeight + 10/*54*/, 185, /*65*/21 ,16,44);
		}
	}
}
