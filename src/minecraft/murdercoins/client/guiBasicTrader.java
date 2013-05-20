package murdercoins.client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import murdercoins.common.MurderCoins;
import murdercoins.container.containerBasicTrader;
import murdercoins.tileentity.tileEntityBasicTrader;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonMerchant;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.Icon;
import net.minecraft.village.MerchantRecipeList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import universalelectricity.prefab.network.PacketManager;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class guiBasicTrader extends GuiContainer 
{
	private int containerWidth;
	private int containerHeight;
	private int tradeID;
	public String tradeTypeString;
	public String playerUsing;
	
	private tileEntityBasicTrader tileentity;
	
	//Icons
	private Icon gCoin;
	private Icon dCoin;
	private Icon eCoin;

	//Owners
	private GuiTextField Owner1;
	private GuiTextField Owner2;
	private GuiTextField Owner3;
	private GuiTextField Owner4;
	private GuiTextField Owner5;
	private GuiTextField Owner6;
	private GuiTextField Owner7;
	
	//Prices
	private int gCoinPrice;
	private int dCoinPrice;
	private int eCoinPrice;
	
	//buttons
	private GuiButtonMerchant nextSaleID;
	private GuiButtonMerchant prevSaleID;
	
	private GuiButton gCoinUp;
	private GuiButton gCoinDn;
	private GuiButton dCoinUp;
	private GuiButton dCoinDn;
	private GuiButton eCoinUp;
	private GuiButton eCoinDn;
	
	private GuiButton tradeType;
	
	public guiBasicTrader(InventoryPlayer player_inventory, tileEntityBasicTrader tile_entity) 
	{
		super(new containerBasicTrader(tile_entity, player_inventory, false));
		this.tileentity = tile_entity;
		this.playerUsing = player_inventory.player.username;
		this.tradeID = 0;
		if (this.tileentity.getSaleType(this.tradeID) == true)
		{
			this.tradeTypeString = "Sell";
		}
		else 
		{
			this.tradeTypeString = "Buy";
		}
		
	}
	public void sendPacket()
	{
    	PacketDispatcher.sendPacketToServer(PacketManager.getPacket(   "MurderCoins", this.tileentity, this.Owner1.getText(), this.Owner2.getText(), this.Owner3.getText(),
    			this.Owner4.getText(), this.Owner5.getText(), this.Owner6.getText(), this.Owner7.getText(), this.tileentity.safeAttached, this.tileentity.getPrice(0, 1),
    			this.tileentity.getPrice(1, 1), this.tileentity.getPrice(2, 1), this.tileentity.getPrice(3, 1), this.tileentity.getPrice(4, 1), this.tileentity.getPrice(5, 1),
    			this.tileentity.getPrice(6, 1), this.tileentity.getPrice(7, 1), this.tileentity.getPrice(8, 1),this.tileentity.getPrice(0, 2),
    			this.tileentity.getPrice(1, 2), this.tileentity.getPrice(2, 2), this.tileentity.getPrice(3, 2), this.tileentity.getPrice(4, 2), this.tileentity.getPrice(5, 2),
    			this.tileentity.getPrice(6, 2), this.tileentity.getPrice(7, 2), this.tileentity.getPrice(8, 2), this.tileentity.getPrice(0, 3),
    			this.tileentity.getPrice(1, 3), this.tileentity.getPrice(2, 3), this.tileentity.getPrice(3, 3), this.tileentity.getPrice(4, 3), this.tileentity.getPrice(5, 3),
    			this.tileentity.getPrice(6, 3), this.tileentity.getPrice(7, 3), this.tileentity.getPrice(8, 3), this.tileentity.getSaleType(0),	this.tileentity.getSaleType(1),	
    			this.tileentity.getSaleType(2),	this.tileentity.getSaleType(3),	this.tileentity.getSaleType(4),	this.tileentity.getSaleType(5),	this.tileentity.getSaleType(6),	
    			this.tileentity.getSaleType(7), this.tileentity.getSaleType(8), false, this.tradeID	));
	}
	@Override
	public void initGui()
	{
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		Owner1 = new GuiTextField(this.fontRenderer, 181, 20, 70, 13);
		Owner1.setText(this.tileentity.botOwner1);
		Owner1.setFocused(false);
		Owner1.setMaxStringLength(50);
		
		Owner2 = new GuiTextField(this.fontRenderer, 181, 48, 70, 13);
		Owner2.setText(this.tileentity.botOwner2);
		Owner2.setFocused(false);
		Owner2.setMaxStringLength(50);
		
		Owner3 = new GuiTextField(this.fontRenderer, 181, 63, 70, 13);
		Owner3.setText(this.tileentity.botOwner3);
		Owner3.setFocused(false);
		Owner3.setMaxStringLength(50);
		
		Owner4 = new GuiTextField(this.fontRenderer, 181, 78, 70, 13);
		Owner4.setText(this.tileentity.botOwner4);
		Owner4.setFocused(false);
		Owner4.setMaxStringLength(50);
		
		Owner5 = new GuiTextField(this.fontRenderer, 181, 93, 70, 13);
		Owner5.setText(this.tileentity.botOwner5);
		Owner5.setFocused(false);
		Owner5.setMaxStringLength(50);
		
		Owner6 = new GuiTextField(this.fontRenderer, 181, 108, 70, 13);
		Owner6.setText(this.tileentity.botOwner6);
		Owner6.setFocused(false);
		Owner6.setMaxStringLength(50);
		
		Owner7 = new GuiTextField(this.fontRenderer, 181, 123, 70, 13);
		Owner7.setText(this.tileentity.botOwner7);
		Owner7.setFocused(false);
		Owner7.setMaxStringLength(50);
		
		
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.buttonList.add(this.nextSaleID = new GuiButtonMerchant(1, i + 154, j + 52, true));
        this.buttonList.add(this.prevSaleID = new GuiButtonMerchant(2, i + 10, j + 52, false));
        
        this.buttonList.add(this.gCoinUp = new GuiButton(3, i + 22, j + 38, 8, 7, "+"));
        this.buttonList.add(this.gCoinDn = new GuiButton(4, i + 50, j + 38, 8, 7, "-"));
        this.buttonList.add(this.dCoinUp = new GuiButton(5, i + 70, j + 38, 8, 7, "+"));
        this.buttonList.add(this.dCoinDn = new GuiButton(6, i + 98, j + 38, 8, 7, "-"));
        this.buttonList.add(this.eCoinUp = new GuiButton(7, i + 115, j + 38, 8, 7, "+"));
        this.buttonList.add(this.eCoinDn = new GuiButton(8, i + 143, j + 38, 8, 7, "-"));
        this.buttonList.add(this.tradeType = new GuiButton(9, i + 113, j + 12, 23, 15, ""));

        this.nextSaleID.enabled = false;
        this.prevSaleID.enabled = false;
        this.gCoinUp.enabled = false;
        this.gCoinDn.enabled = false;
        this.dCoinUp.enabled = false;
        this.dCoinDn.enabled = false;
        this.eCoinUp.enabled = false;
        this.eCoinDn.enabled = false;
		
		this.gCoinPrice = this.tileentity.getPrice(this.tradeID, 1);
		this.dCoinPrice = this.tileentity.getPrice(this.tradeID, 2);
		this.eCoinPrice = this.tileentity.getPrice(this.tradeID, 3);
	}
	
	 /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        boolean flag = false;

        if (par1GuiButton == this.nextSaleID)
        {
            ++this.tradeID;
    		this.gCoinPrice = this.tileentity.getPrice(this.tradeID, 1);
    		this.dCoinPrice = this.tileentity.getPrice(this.tradeID, 2);
    		this.eCoinPrice = this.tileentity.getPrice(this.tradeID, 3);
            flag = true;
        }
        else if (par1GuiButton == this.prevSaleID)
        {
            --this.tradeID;
            
    		this.gCoinPrice = this.tileentity.getPrice(this.tradeID, 1);
    		this.dCoinPrice = this.tileentity.getPrice(this.tradeID, 2);
    		this.eCoinPrice = this.tileentity.getPrice(this.tradeID, 3);
            flag = true;
        }
        else if (par1GuiButton == this.gCoinUp)
        {
        	
        	this.tileentity.setPrice(this.gCoinPrice + 1, this.tradeID, 1);
            this.gCoinPrice = this.tileentity.getPrice(this.tradeID, 1);
            flag = true;
            sendPacket();
        }
        else if (par1GuiButton == this.gCoinDn)
        {
        	this.tileentity.setPrice(this.gCoinPrice - 1, this.tradeID, 1);
        	this.gCoinPrice = this.tileentity.getPrice(this.tradeID, 1);
            flag = true;
            sendPacket();
        }
        else if (par1GuiButton == this.dCoinUp)
        {
            this.tileentity.setPrice(this.dCoinPrice + 1, this.tradeID, 2);
            this.dCoinPrice = this.tileentity.getPrice(this.tradeID, 2);
            flag = true;
            sendPacket();
        }
        else if (par1GuiButton == this.dCoinDn)
        {
            this.tileentity.setPrice(this.dCoinPrice - 1, this.tradeID, 2);
            this.dCoinPrice = this.tileentity.getPrice(this.tradeID, 2);
            flag = true;
            sendPacket();
        }
        else if (par1GuiButton == this.eCoinUp)
        {
        	this.tileentity.setPrice(this.eCoinPrice + 1, this.tradeID, 3);
            this.eCoinPrice = this.tileentity.getPrice(this.tradeID, 3);
            flag = true;
            sendPacket();
        }
        else if (par1GuiButton == this.eCoinDn)
        {
        	this.tileentity.setPrice(this.eCoinPrice - 1, this.tradeID, 3);
            this.eCoinPrice = this.tileentity.getPrice(this.tradeID, 3);
            flag = true;
            sendPacket();
        }
        else if (par1GuiButton == this.tradeType)
        {
        	this.tileentity.setSaleType(this.tradeID);
    		if (this.tileentity.getSaleType(this.tradeID) == true)
    		{
    			this.tradeTypeString = "Buy";
    		}
    		else 
    		{
    			this.tradeTypeString = "Sell";
    		}
    		flag = true;
    		sendPacket();
        }
        
        
        if (flag)
        {
        	//PacketDispatcher.sendPacketToServer(PacketManager.getPacket("MurderCoins", this.tileentity, this.Owner1.getText(), this.Owner2.getText(), this.Owner3.getText(), this.Owner4.getText(), this.Owner5.getText(), this.Owner6.getText(), this.Owner7.getText(), this.gCoinPrice, this.dCoinPrice, this.eCoinPrice));

           /* ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);

            try
            {
                dataoutputstream.writeInt(this.tradeID);
                this.mc.getNetHandler().addToSendQueue(new Packet250CustomPayload("MurderCoins", bytearrayoutputstream.toByteArray()));
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }*/
        }
        
    }
    
    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
 
        if (this.tradeID == 0)
        {
            this.prevSaleID.enabled = false;
            this.nextSaleID.enabled = true;
        }
        if (this.tradeID > 0 && this.tradeID < 8)
        {
            this.prevSaleID.enabled = true;
            this.nextSaleID.enabled = true;
        }
        if (this.tradeID == 8)
        {
            this.prevSaleID.enabled = true;
            this.nextSaleID.enabled = false;
        }
        
        if (this.gCoinPrice == 0)
        {
        	this.gCoinUp.enabled = true;
        	this.gCoinDn.enabled = false;
        }
        if (this.dCoinPrice == 0)
        {
        	this.dCoinUp.enabled = true;
        	this.dCoinDn.enabled = false;
        }
        if (this.eCoinPrice == 0)
        {
        	this.eCoinUp.enabled = true;
        	this.eCoinDn.enabled = false;
        }
        if (this.gCoinPrice > 0 && this.gCoinPrice < 64)
        {
        	this.gCoinUp.enabled = true;
        	this.gCoinDn.enabled = true;
        }
        if (this.dCoinPrice > 0 && this.dCoinPrice < 64)
        {
        	this.dCoinUp.enabled = true;
        	this.dCoinDn.enabled = true;
        }
        if (this.eCoinPrice > 0 && this.eCoinPrice < 64)
        {
        	this.eCoinUp.enabled = true;
        	this.eCoinDn.enabled = true;
        }
        if (this.gCoinPrice == 64)
        {
        	this.gCoinUp.enabled = false;
        	this.gCoinDn.enabled = true;
        }
        if (this.dCoinPrice == 64)
        {
        	this.dCoinUp.enabled = false;
        	this.dCoinDn.enabled = true;
        }
        if (this.eCoinPrice == 64)
        {
        	this.eCoinUp.enabled = false;
        	this.eCoinDn.enabled = true;
        }
        if (this.tileentity.getSaleType(this.tradeID) == true)
        {
        	this.tradeTypeString = "Sell";
        }
        if (this.tileentity.getSaleType(this.tradeID) == false)
        {
        	this.tradeTypeString = "Buy";
        }
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
    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
    	super.drawScreen(par1, par2, par3);
        int k = ((this.width - this.xSize) / 2);
        int l = ((this.height - this.ySize) / 2);
        
		ItemStack gCoins = new ItemStack(MurderCoins.itemGoldCoin);
		ItemStack dCoins = new ItemStack(MurderCoins.itemDiamondCoin);
		ItemStack eCoins = new ItemStack(MurderCoins.itemEmeraldCoin);
		RenderHelper.enableGUIStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glEnable(GL11.GL_LIGHTING);
        itemRenderer.zLevel = 100.0F;
        itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, gCoins, k + 32, l + 34);
        itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, gCoins, k + 32, l + 34);
        itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, dCoins, k + 80, l + 34);
        itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, dCoins, k + 80, l + 34);
        itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, eCoins, k + 125, l + 34);
        itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, eCoins, k + 125, l + 34);
        if (this.tileentity.attachedSafe != null)
        {
        	if (this.tileentity.attachedSafe.getStackInSlot(this.tradeID) != null)
        	{
        		ItemStack tradeItem = this.tileentity.attachedSafe.getStackInSlot(this.tradeID);
        		itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, tradeItem, k + 80, l + 9);
        		itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, tradeItem, k + 80, l + 9);
        	}
        }
    }
	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j) 
	{

		this.drawCenteredString(this.fontRenderer, "Owner" , 215, 10, 0x66FF00/*4210752*/);
		this.drawCenteredString(this.fontRenderer, "Operators" , 215, 36, 0xFFFF00);
		this.Owner1.drawTextBox();
		this.Owner2.drawTextBox();
		this.Owner3.drawTextBox();
		this.Owner4.drawTextBox();
		this.Owner5.drawTextBox();
		this.Owner6.drawTextBox();
		this.Owner7.drawTextBox();
		
		this.drawString(this.fontRenderer, "Trade# " + Integer.toString(this.tradeID + 1) + " of 9", 4, 4, 0xffffff);
		if(!this.tileentity.safeAttached)
			this.drawString(this.fontRenderer, "Please attach a Trader Safe!", 4, 75, 0xFF5555);
		this.drawCenteredString(this.fontRenderer, Integer.toString(this.gCoinPrice), 40, 52, 0xffffff);
		this.drawCenteredString(this.fontRenderer, Integer.toString(this.dCoinPrice), 88, 52, 0xffffff);
		this.drawCenteredString(this.fontRenderer, Integer.toString(this.eCoinPrice), 133, 52, 0xffffff);
		this.drawCenteredString(this.fontRenderer, this.tradeTypeString, 125, 15, 0xffffff);
		


		//this.drawCenteredString(par1FontRenderer, par2Str, par3, par4, par5)
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		this.mc.renderEngine.bindTexture("/mods/MurderCoins/textures/basicTraderGui.png");
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
        if (this.playerUsing.equalsIgnoreCase(this.tileentity.botOwner1))
        {
        	this.Owner2.mouseClicked(par1 - this.containerWidth, par2 - this.containerHeight, par3);
        	this.Owner3.mouseClicked(par1 - this.containerWidth, par2 - this.containerHeight, par3);
        	this.Owner4.mouseClicked(par1 - this.containerWidth, par2 - this.containerHeight, par3);
        	this.Owner5.mouseClicked(par1 - this.containerWidth, par2 - this.containerHeight, par3);
        	this.Owner6.mouseClicked(par1 - this.containerWidth, par2 - this.containerHeight, par3);
        	this.Owner7.mouseClicked(par1 - this.containerWidth, par2 - this.containerHeight, par3);
        }
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
        	sendPacket();
        }
        else if (this.Owner2.textboxKeyTyped(par1, par2))
        {
        	this.tileentity.setOwners(this.Owner2.getText(), 2);
        	sendPacket();
        }
        else if (this.Owner3.textboxKeyTyped(par1, par2))
        {
        	this.tileentity.setOwners(this.Owner3.getText(), 3);
        	sendPacket();
        }
        else if (this.Owner4.textboxKeyTyped(par1, par2))
        {
        	this.tileentity.setOwners(this.Owner4.getText(), 4);
        	sendPacket();
        }
        else if (this.Owner5.textboxKeyTyped(par1, par2))
        {
        	this.tileentity.setOwners(this.Owner5.getText(), 5);
        	sendPacket();
        }
        else if (this.Owner6.textboxKeyTyped(par1, par2))
        {
        	this.tileentity.setOwners(this.Owner6.getText(), 6);
        	sendPacket();
        }
        else if (this.Owner7.textboxKeyTyped(par1, par2))
        {
        	this.tileentity.setOwners(this.Owner7.getText(), 7);
        	sendPacket();
        }
        else
    	{
        	super.keyTyped(par1, par2);
    	}
    }
}
