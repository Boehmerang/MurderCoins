package murdercoins.client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import murdercoins.common.MurderCoins;
import murdercoins.container.containerBasicTrader;
import murdercoins.container.containerBasicTraderShop;
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
public class guiBasicTraderShop extends GuiContainer 
{
	private int containerWidth;
	private int containerHeight;
	private int tradeID;
	public String tradeTypeString;
	public boolean isFalse = false;
	
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
	
	private int[] ItemsArray;
	
	//Prices
	private int gCoinPrice;
	private int dCoinPrice;
	private int eCoinPrice;
	
	//buttons
	private GuiButtonMerchant nextSaleID;
	private GuiButtonMerchant prevSaleID;
	
	private GuiButton doTransaction;
	
	public guiBasicTraderShop(InventoryPlayer player_inventory, tileEntityBasicTrader tile_entity) 
	{
		super(new containerBasicTrader(tile_entity, player_inventory, true));
		this.tileentity = tile_entity;
		this.tradeID = 0;
		if (this.tileentity.getSaleType(this.tradeID) == true)
		{
			this.tradeTypeString = "Buy";
		}
		else 
		{
			this.tradeTypeString = "Sell";
		}
	}
	public void sendPacket(int type)
	{
		if (type == 1)
		{
			PacketDispatcher.sendPacketToServer(PacketManager.getPacket(   "MurderCoins", this.tileentity, this.Owner1.getText(), this.Owner2.getText(), this.Owner3.getText(),
    			this.Owner4.getText(), this.Owner5.getText(), this.Owner6.getText(), this.Owner7.getText(), this.tileentity.safeAttached,  this.tileentity.getPrice(0, 1),
    			this.tileentity.getPrice(1, 1), this.tileentity.getPrice(2, 1), this.tileentity.getPrice(3, 1), this.tileentity.getPrice(4, 1), this.tileentity.getPrice(5, 1),
    			this.tileentity.getPrice(6, 1), this.tileentity.getPrice(7, 1), this.tileentity.getPrice(8, 1),this.tileentity.getPrice(0, 2),
    			this.tileentity.getPrice(1, 2), this.tileentity.getPrice(2, 2), this.tileentity.getPrice(3, 2), this.tileentity.getPrice(4, 2), this.tileentity.getPrice(5, 2),
    			this.tileentity.getPrice(6, 2), this.tileentity.getPrice(7, 2), this.tileentity.getPrice(8, 2), this.tileentity.getPrice(0, 3),
    			this.tileentity.getPrice(1, 3), this.tileentity.getPrice(2, 3), this.tileentity.getPrice(3, 3), this.tileentity.getPrice(4, 3), this.tileentity.getPrice(5, 3),
    			this.tileentity.getPrice(6, 3), this.tileentity.getPrice(7, 3), this.tileentity.getPrice(8, 3), this.tileentity.getSaleType(0),	this.tileentity.getSaleType(1),	
    			this.tileentity.getSaleType(2),	this.tileentity.getSaleType(3),	this.tileentity.getSaleType(4),	this.tileentity.getSaleType(5),	this.tileentity.getSaleType(6),	
    			this.tileentity.getSaleType(7), this.tileentity.getSaleType(8), false, this.tradeID));
		}
		else
		{
			this.ItemsArray = this.updateItems(true);
			
			PacketDispatcher.sendPacketToServer(PacketManager.getPacket(   "MurderCoins", this.tileentity, this.Owner1.getText(), this.Owner2.getText(), this.Owner3.getText(),
	    			this.Owner4.getText(), this.Owner5.getText(), this.Owner6.getText(), this.Owner7.getText(), this.tileentity.safeAttached,  this.tileentity.getPrice(0, 1),
	    			this.tileentity.getPrice(1, 1), this.tileentity.getPrice(2, 1), this.tileentity.getPrice(3, 1), this.tileentity.getPrice(4, 1), this.tileentity.getPrice(5, 1),
	    			this.tileentity.getPrice(6, 1), this.tileentity.getPrice(7, 1), this.tileentity.getPrice(8, 1),this.tileentity.getPrice(0, 2),
	    			this.tileentity.getPrice(1, 2), this.tileentity.getPrice(2, 2), this.tileentity.getPrice(3, 2), this.tileentity.getPrice(4, 2), this.tileentity.getPrice(5, 2),
	    			this.tileentity.getPrice(6, 2), this.tileentity.getPrice(7, 2), this.tileentity.getPrice(8, 2), this.tileentity.getPrice(0, 3),
	    			this.tileentity.getPrice(1, 3), this.tileentity.getPrice(2, 3), this.tileentity.getPrice(3, 3), this.tileentity.getPrice(4, 3), this.tileentity.getPrice(5, 3),
	    			this.tileentity.getPrice(6, 3), this.tileentity.getPrice(7, 3), this.tileentity.getPrice(8, 3), this.tileentity.getSaleType(0),	this.tileentity.getSaleType(1),	
	    			this.tileentity.getSaleType(2),	this.tileentity.getSaleType(3),	this.tileentity.getSaleType(4),	this.tileentity.getSaleType(5),	this.tileentity.getSaleType(6),	
	    			this.tileentity.getSaleType(7), this.tileentity.getSaleType(8), true, this.tradeID));
		}
	}
	@Override
	public void initGui()
	{
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		Owner1 = new GuiTextField(this.fontRenderer, 181, 25, 70, 13);
		Owner1.setText(this.tileentity.botOwner1);
		Owner1.setFocused(false);
		Owner1.setMaxStringLength(50);
		
		Owner2 = new GuiTextField(this.fontRenderer, 181, 40, 70, 13);
		Owner2.setText(this.tileentity.botOwner2);
		Owner2.setFocused(false);
		Owner2.setMaxStringLength(50);
		
		Owner3 = new GuiTextField(this.fontRenderer, 181, 55, 70, 13);
		Owner3.setText(this.tileentity.botOwner3);
		Owner3.setFocused(false);
		Owner3.setMaxStringLength(50);
		
		Owner4 = new GuiTextField(this.fontRenderer, 181, 70, 70, 13);
		Owner4.setText(this.tileentity.botOwner4);
		Owner4.setFocused(false);
		Owner4.setMaxStringLength(50);
		
		Owner5 = new GuiTextField(this.fontRenderer, 181, 85, 70, 13);
		Owner5.setText(this.tileentity.botOwner5);
		Owner5.setFocused(false);
		Owner5.setMaxStringLength(50);
		
		Owner6 = new GuiTextField(this.fontRenderer, 181, 100, 70, 13);
		Owner6.setText(this.tileentity.botOwner6);
		Owner6.setFocused(false);
		Owner6.setMaxStringLength(50);
		
		Owner7 = new GuiTextField(this.fontRenderer, 181, 115, 70, 13);
		Owner7.setText(this.tileentity.botOwner7);
		Owner7.setFocused(false);
		Owner7.setMaxStringLength(50);
		
		if (this.tileentity.safeAttached)
		{
			int i = (this.width - this.xSize) / 2;
        	int j = (this.height - this.ySize) / 2;
        	this.buttonList.add(this.nextSaleID = new GuiButtonMerchant(1, i + 154, j + 52, true));
        	this.buttonList.add(this.prevSaleID = new GuiButtonMerchant(2, i + 10, j + 52, false));
        
        	this.buttonList.add(this.doTransaction = new GuiButton(9, i + 77, j + 67, 23, 15, ""));

        	this.nextSaleID.enabled = false;
        	this.prevSaleID.enabled = false;
		
			this.gCoinPrice = this.tileentity.getPrice(this.tradeID, 1);
			this.dCoinPrice = this.tileentity.getPrice(this.tradeID, 2);
			this.eCoinPrice = this.tileentity.getPrice(this.tradeID, 3);
		}

	}
	
	public int[] updateItems(Boolean changedinGUI)
	{
		if(changedinGUI)
		{
			return this.doFakeTransaction(this.tradeID, this.tileentity.getSaleType(this.tradeID));
		}
		return new int[] {this.tileentity.inventory[1].itemID, this.tileentity.inventory[1].stackSize, this.tileentity.inventory[2].itemID, this.tileentity.inventory[2].stackSize,
				this.tileentity.inventory[3].itemID, this.tileentity.inventory[3].stackSize, this.tileentity.inventory[4].itemID, this.tileentity.inventory[4].stackSize };
	}
	
	 /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
		if (this.tileentity.safeAttached)
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
			else if (par1GuiButton == this.doTransaction)
			{
				//this.doTransaction(this.tradeID, this.tileentity.getSaleType(this.tradeID));
				//this.tileentity.processTransaction(this.tradeID);
				this.tileentity.tradeWanted = this.tradeID;
				this.tileentity.processWanted = true;
				sendPacket(2);
				flag = true;
				this.updateScreen();
			}
		}
	}
    
    public int[] doFakeTransaction(int slotID, Boolean type)
    {
    	int gPrice = this.tileentity.getPrice(slotID, 1), dPrice = this.tileentity.getPrice(slotID, 2), ePrice = this.tileentity.getPrice(slotID, 3);
    	ItemStack stack = this.tileentity.attachedSafe.getStackInSlot(slotID).copy();
    	int[] tempArray = new int[] {0,0,0,0,0,0,0,0};
    	if (type)  //if it's a sale.
    	{
    		if(this.tileentity.inventory[4] == null)
    			{
    				tempArray[0] = stack.itemID;
    				tempArray[1] = stack.stackSize;
    			}
    		else if (this.tileentity.inventory[4] != null && this.tileentity.inventory[4].isItemEqual(stack))
    		{
    			if (this.tileentity.inventory[4].stackSize == 64)
    				{	tempArray[1] = 64;	 }
    			else if (this.tileentity.inventory[4].stackSize + stack.stackSize <= 64)
    			{
    				tempArray[1] = this.tileentity.inventory[4].stackSize + stack.stackSize;
    			}
    		}
    		if (gPrice > 0)
    		{
    			tempArray[2] = MurderCoins.itemGoldCoin.itemID;
    			if (this.tileentity.inventory[1] != null)
    			tempArray[3] = this.tileentity.inventory[1].stackSize - gPrice;
    		}
    		else if (gPrice == 0)
    		{
    			tempArray[2] = MurderCoins.itemGoldCoin.itemID;
    			if (this.tileentity.inventory[1] != null)
    			tempArray[3] = this.tileentity.inventory[1].stackSize;
    		}
    		if (dPrice > 0)
    		{
    			if (this.tileentity.inventory[2] != null)
    			{
    				tempArray[4] = MurderCoins.itemDiamondCoin.itemID;
    				tempArray[5] = this.tileentity.inventory[2].stackSize - dPrice;
    			}
    		}
    		else if (dPrice == 0)
    		{
    			if (this.tileentity.inventory[2] != null)
    			{
    				tempArray[4] = MurderCoins.itemDiamondCoin.itemID;
    				tempArray[5] = this.tileentity.inventory[2].stackSize;
    			}
    		}
    		if (ePrice > 0)
    		{
    			if (this.tileentity.inventory[3] != null)
    			{
    				tempArray[6] = MurderCoins.itemEmeraldCoin.itemID;
    				tempArray[7] = this.tileentity.inventory[3].stackSize - ePrice;
    			}
    		}
    		else if (ePrice == 0)
    		{
    			tempArray[6] = MurderCoins.itemEmeraldCoin.itemID;
    			if (this.tileentity.inventory[3] != null)
    			tempArray[7] = this.tileentity.inventory[3].stackSize;
    		}
    		return tempArray;
    	}
    	return new int[] {};
    }
    
    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
		if (this.tileentity.safeAttached == true)
		{
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
			if (this.tileentity.getSaleType(this.tradeID) == true)
			{
        	this.tradeTypeString = "Buy";
			}
			if (this.tileentity.getSaleType(this.tradeID) == false)
        	{
        	this.tradeTypeString = "Sell";
        	}
			if (this.tileentity.canTransact(this.tradeID, this.tileentity.attachedSafe.getStackInSlot(this.tradeID)))
			{
				this.doTransaction.enabled = true;
			}
			else if (!this.tileentity.canTransact(this.tradeID, this.tileentity.attachedSafe.getStackInSlot(this.tradeID)))
			{
				this.doTransaction.enabled = false;
			}
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
        
		RenderHelper.enableGUIStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glEnable(GL11.GL_LIGHTING);
        itemRenderer.zLevel = 100.0F;
        //ItemStack tradeItem = this.tileentity.attachedSafe.getStackInSlot(this.tradeID);
        if (this.tileentity.attachedSafe!=null)
        {
        	if (this.tileentity.attachedSafe.getStackInSlot(this.tradeID) != null)//(tradeItem != null)
        	{
        		ItemStack tradeItem = this.tileentity.attachedSafe.getStackInSlot(this.tradeID);
        		itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, tradeItem, k + 112, l + 17);
        		itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, tradeItem, k + 112, l + 17);
        	}       
        }
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
		if (this.tileentity.safeAttached)
		{
			this.drawString(this.fontRenderer, "Trade# " + Integer.toString(this.tradeID + 1) + " of 9", 4, 4, 0xffffff);
		
			this.drawCenteredString(this.fontRenderer, Integer.toString(this.gCoinPrice), 47, 28, 0xffffff);
			this.drawCenteredString(this.fontRenderer, Integer.toString(this.dCoinPrice), 68, 28, 0xffffff);
			this.drawCenteredString(this.fontRenderer, Integer.toString(this.eCoinPrice), 89, 28, 0xffffff);
			this.drawCenteredString(this.fontRenderer, this.tradeTypeString, 88, 71, 0xffffff);
		}
		if(!this.tileentity.safeAttached)
			this.drawCenteredString(this.fontRenderer, "No Trader Safe attached!", this.xSize/2, 74, 0xFF5555);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		this.mc.renderEngine.bindTexture("/mods/MurderCoins/textures/basicTraderShopGui.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		containerWidth = ((this.width - this.xSize) / 2);
		containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize+80, this.ySize);
		
		if (this.tileentity.getSaleType(this.tradeID) == false && this.tileentity.safeAttached)
		{
			this.drawTexturedModalRect(containerWidth + 99, containerHeight + 17, 1, 168, 9, 18);
			this.drawTexturedModalRect(containerWidth + 99, containerHeight + 41, 1, 168, 9, 18);
		}
		if (this.tileentity.getSaleType(this.tradeID) == true && this.tileentity.safeAttached)
		{
			this.drawTexturedModalRect(containerWidth + 99, containerHeight + 17, 13, 168, 9, 18);
			this.drawTexturedModalRect(containerWidth + 99, containerHeight + 41, 13, 168, 9, 18);
		}
	}
    /**
     * Called when the mouse is clicked.
     */
    @Override
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        /*
        this.Owner1.mouseClicked(par1 - this.containerWidth, par2 - this.containerHeight, par3);
        this.Owner2.mouseClicked(par1 - this.containerWidth, par2 - this.containerHeight, par3);
        this.Owner3.mouseClicked(par1 - this.containerWidth, par2 - this.containerHeight, par3);
        this.Owner4.mouseClicked(par1 - this.containerWidth, par2 - this.containerHeight, par3);
        this.Owner5.mouseClicked(par1 - this.containerWidth, par2 - this.containerHeight, par3);
        this.Owner6.mouseClicked(par1 - this.containerWidth, par2 - this.containerHeight, par3);
        this.Owner7.mouseClicked(par1 - this.containerWidth, par2 - this.containerHeight, par3);
        */
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
        	sendPacket(1);
        }
        else if (this.Owner2.textboxKeyTyped(par1, par2))
        {
        	this.tileentity.setOwners(this.Owner2.getText(), 2);
        	sendPacket(1);
        }
        else if (this.Owner3.textboxKeyTyped(par1, par2))
        {
        	this.tileentity.setOwners(this.Owner3.getText(), 3);
        	sendPacket(1);
        }
        else if (this.Owner4.textboxKeyTyped(par1, par2))
        {
        	this.tileentity.setOwners(this.Owner4.getText(), 4);
        	sendPacket(1);
        }
        else if (this.Owner5.textboxKeyTyped(par1, par2))
        {
        	this.tileentity.setOwners(this.Owner5.getText(), 5);
        	sendPacket(1);
        }
        else if (this.Owner6.textboxKeyTyped(par1, par2))
        {
        	this.tileentity.setOwners(this.Owner6.getText(), 6);
        	sendPacket(1);
        }
        else if (this.Owner7.textboxKeyTyped(par1, par2))
        {
        	this.tileentity.setOwners(this.Owner7.getText(), 7);
        	sendPacket(1);
        }
        else
    	{
        	super.keyTyped(par1, par2);
    	}
    }
}
