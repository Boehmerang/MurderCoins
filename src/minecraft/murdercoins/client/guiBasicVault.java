package murdercoins.client;

import murdercoins.container.containerBasicVault;
import murdercoins.container.containerPulverisor;
import murdercoins.tileentity.tileEntityBasicVault;
import murdercoins.tileentity.tileEntityPulverisor;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import universalelectricity.prefab.network.PacketManager;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class guiBasicVault extends GuiContainer 
{
	private int containerWidth;
	private int containerHeight;
	private GuiTextField Owner1;
	private GuiTextField Owner2;
	private GuiTextField Owner3;
	private GuiTextField Owner4;
	private GuiTextField Owner5;
	private GuiTextField Owner6;
	private GuiTextField Owner7;
	private tileEntityBasicVault tileentity;
	
	
	public guiBasicVault(InventoryPlayer player_inventory, tileEntityBasicVault tile_entity) 
	{
		super(new containerBasicVault(tile_entity, player_inventory));
		this.tileentity = tile_entity;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		Owner1 = new GuiTextField(this.fontRenderer, 181, 25, 70, 13);
		Owner1.setText(this.tileentity.vaultOwner1);
		Owner1.setFocused(false);
		Owner1.setMaxStringLength(50);
		
		Owner2 = new GuiTextField(this.fontRenderer, 181, 40, 70, 13);
		Owner2.setText(this.tileentity.vaultOwner2);
		Owner2.setFocused(false);
		Owner2.setMaxStringLength(50);
		
		Owner3 = new GuiTextField(this.fontRenderer, 181, 55, 70, 13);
		Owner3.setText(this.tileentity.vaultOwner3);
		Owner3.setFocused(false);
		Owner3.setMaxStringLength(50);
		
		Owner4 = new GuiTextField(this.fontRenderer, 181, 70, 70, 13);
		Owner4.setText(this.tileentity.vaultOwner4);
		Owner4.setFocused(false);
		Owner4.setMaxStringLength(50);
		
		Owner5 = new GuiTextField(this.fontRenderer, 181, 85, 70, 13);
		Owner5.setText(this.tileentity.vaultOwner5);
		Owner5.setFocused(false);
		Owner5.setMaxStringLength(50);
		
		Owner6 = new GuiTextField(this.fontRenderer, 181, 100, 70, 13);
		Owner6.setText(this.tileentity.vaultOwner6);
		Owner6.setFocused(false);
		Owner6.setMaxStringLength(50);
		
		Owner7 = new GuiTextField(this.fontRenderer, 181, 115, 70, 13);
		Owner7.setText(this.tileentity.vaultOwner7);
		Owner7.setFocused(false);
		Owner7.setMaxStringLength(50);
	}
    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
	@Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }
	
	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j) 
	{
		this.drawString(this.fontRenderer, "OWNERS" , 185, 10, 4210752);
		this.Owner1.drawTextBox();
		this.Owner2.drawTextBox();
		this.Owner3.drawTextBox();
		this.Owner4.drawTextBox();
		this.Owner5.drawTextBox();
		this.Owner6.drawTextBox();
		this.Owner7.drawTextBox();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		this.mc.renderEngine.bindTexture("/mods/MurderCoins/textures/basicVaultGui.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		containerWidth = ((this.width - this.xSize) / 2);
		containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize+80, this.ySize);

	}
    /**
     * Called when the mouse is clicked.
     */
    @Override
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        //this.Owner1.mouseClicked(par1 - this.containerWidth, par2 - this.containerHeight, par3);
        this.Owner2.mouseClicked(par1 - this.containerWidth, par2 - this.containerHeight, par3);
        this.Owner3.mouseClicked(par1 - this.containerWidth, par2 - this.containerHeight, par3);
        this.Owner4.mouseClicked(par1 - this.containerWidth, par2 - this.containerHeight, par3);
        this.Owner5.mouseClicked(par1 - this.containerWidth, par2 - this.containerHeight, par3);
        this.Owner6.mouseClicked(par1 - this.containerWidth, par2 - this.containerHeight, par3);
        this.Owner7.mouseClicked(par1 - this.containerWidth, par2 - this.containerHeight, par3);
    }
    /**
     * Fired when a key is typed. This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e).
     */
    @Override
    protected void keyTyped(char par1, int par2)
    {
        if (this.Owner1.textboxKeyTyped(par1, par2))
        {
        	this.tileentity.setOwners(this.Owner1.getText(), 1);
        	PacketDispatcher.sendPacketToServer(PacketManager.getPacket("MurderCoins", this.tileentity, this.Owner1.getText(), this.Owner2.getText(), this.Owner3.getText(), this.Owner4.getText(), this.Owner5.getText(), this.Owner6.getText(), this.Owner7.getText()));
        }
        else if (this.Owner2.textboxKeyTyped(par1, par2))
        {
        	this.tileentity.setOwners(this.Owner2.getText(), 2);
        	PacketDispatcher.sendPacketToServer(PacketManager.getPacket("MurderCoins", this.tileentity, this.Owner1.getText(), this.Owner2.getText(), this.Owner3.getText(), this.Owner4.getText(), this.Owner5.getText(), this.Owner6.getText(), this.Owner7.getText()));
        }
        else if (this.Owner3.textboxKeyTyped(par1, par2))
        {
        	this.tileentity.setOwners(this.Owner3.getText(), 3);
        	PacketDispatcher.sendPacketToServer(PacketManager.getPacket("MurderCoins", this.tileentity, this.Owner1.getText(), this.Owner2.getText(), this.Owner3.getText(), this.Owner4.getText(), this.Owner5.getText(), this.Owner6.getText(), this.Owner7.getText()));
        }
        else if (this.Owner4.textboxKeyTyped(par1, par2))
        {
        	this.tileentity.setOwners(this.Owner4.getText(), 4);
        	PacketDispatcher.sendPacketToServer(PacketManager.getPacket("MurderCoins", this.tileentity, this.Owner1.getText(), this.Owner2.getText(), this.Owner3.getText(), this.Owner4.getText(), this.Owner5.getText(), this.Owner6.getText(), this.Owner7.getText()));
        }
        else if (this.Owner5.textboxKeyTyped(par1, par2))
        {
        	this.tileentity.setOwners(this.Owner5.getText(), 5);
        	PacketDispatcher.sendPacketToServer(PacketManager.getPacket("MurderCoins", this.tileentity, this.Owner1.getText(), this.Owner2.getText(), this.Owner3.getText(), this.Owner4.getText(), this.Owner5.getText(), this.Owner6.getText(), this.Owner7.getText()));
        }
        else if (this.Owner6.textboxKeyTyped(par1, par2))
        {
        	this.tileentity.setOwners(this.Owner6.getText(), 6);
        	PacketDispatcher.sendPacketToServer(PacketManager.getPacket("MurderCoins", this.tileentity, this.Owner1.getText(), this.Owner2.getText(), this.Owner3.getText(), this.Owner4.getText(), this.Owner5.getText(), this.Owner6.getText(), this.Owner7.getText()));
        }
        else if (this.Owner7.textboxKeyTyped(par1, par2))
        {
        	this.tileentity.setOwners(this.Owner7.getText(), 7);
        	PacketDispatcher.sendPacketToServer(PacketManager.getPacket("MurderCoins", this.tileentity, this.Owner1.getText(), this.Owner2.getText(), this.Owner3.getText(), this.Owner4.getText(), this.Owner5.getText(), this.Owner6.getText(), this.Owner7.getText()));
        }
        else
    	{
        	super.keyTyped(par1, par2);
    	}
    }
}
