package murdercoins.client;

import murdercoins.container.containerGoldForge;
import murdercoins.tileentity.tileEntityGoldForge;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class guiGoldForge extends GuiContainer {
	
	public static final ResourceLocation text = new ResourceLocation("murdercoins","textures/goldforgegui.png");
	private int containerWidth;
	private int containerHeight;
	private tileEntityGoldForge tileentity;

	public guiGoldForge(InventoryPlayer player_inventory, tileEntityGoldForge tile_entity) 
	{
		super(new containerGoldForge(tile_entity, player_inventory));
		this.tileentity = tile_entity;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		String capacityInfo = ElectricityDisplay.getDisplay((float) this.tileentity.getJoules(), ElectricUnit.JOULES);
		String displayText = "";
		fontRenderer.drawString("Gold Forge", 32, 6, 0xffffff);
		//fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 6,ySize - 96, 0xffffff);
		/*if (this.tileentity.isDisabled())
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
		else if (this.tileentity.processTicks > 0) {
			displayText = "Working";
		}
		else
		{
			displayText = "Idle";
		}

		this.fontRenderer.drawString("Status:  " + displayText, 25, 45,	0xffffff);
		this.fontRenderer.drawString(ElectricityDisplay.getDisplay(this.tileentity.getVoltage(), ElectricUnit.VOLTAGE), 31, 56, 0xffffff);
		this.fontRenderer.drawString(capacityInfo, 30, 68, 0xffffff);
		}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		//ResourceLocation text = new ResourceLocation("/mods/MurderCoins/textures/goldForgeGui.png");
		this.mc.renderEngine.bindTexture(text);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		containerWidth = (this.width - this.xSize) / 2;
		containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0,
				this.xSize, this.ySize);

		if (this.tileentity.processTicks > 0) {
			int scale = (int) (((double) this.tileentity.processTicks / (double) this.tileentity.meltingTicks) * 23);
			this.drawTexturedModalRect(containerWidth + 77, containerHeight + 24, 176, 0, 23 - scale, 20);
		}
		if (this.tileentity.getJoules() > 0)
		{
			int scale2 = (int) (((double) this.tileentity.joulesStored / (double) this.tileentity.getMaxJoules()) * 96);
			this.drawTexturedModalRect(containerWidth + 28, containerHeight + 67, 2, 168, 0 + scale2, 9);
		}
		if (this.tileentity.getGold() == 0)
		{
			//int scale2 = (int) (((double) this.tileentity.goldStored / (double) this.tileentity.maxGold) * 44);
			this.drawTexturedModalRect(containerWidth + /*12*/108, containerHeight + 10/*54*/, 185, /*65*/21 ,16,44);
		}
		if (this.tileentity.getGold() > 0)
		{
			int scale2 = (int) (((double) this.tileentity.getGold() / (double) this.tileentity.maxGold) * 44);
			this.drawTexturedModalRect(containerWidth + /*12*/108, containerHeight + 10/*54*/, 185, /*65*/21 ,16,44-scale2);
		}
	
	}
}
