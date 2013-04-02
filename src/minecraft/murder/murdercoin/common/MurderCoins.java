package murder.murdercoin.common;

import murder.murdercoin.client.GuiHandler;
import murder.murdercoin.common.items.ItemCoinMold;
import murder.murdercoin.common.items.ItemDDust;
import murder.murdercoin.common.items.ItemDclump;
import murder.murdercoin.common.items.ItemDirtyDDust;
import murder.murdercoin.common.items.ItemDirtyEDust;
import murder.murdercoin.common.items.ItemECoin;
import murder.murdercoin.common.items.ItemEDust;
import murder.murdercoin.common.items.ItemEclump;
import murder.murdercoin.common.items.ItemMeltedBucket;
import murder.murdercoin.common.items.ItemNugBucket;
import murder.murdercoin.common.items.ItemdCoin;
import murder.murdercoin.common.items.ItemgCoin;
import murder.murdercoin.common.machines.forge.BlockGoldForge;
import murder.murdercoin.common.machines.forge.TileGoldForge;
import murder.murdercoin.common.machines.press.BlockCoinPress;
import murder.murdercoin.common.machines.press.TileEntityCoinPress;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import universalelectricity.prefab.network.ConnectionHandler;
import universalelectricity.prefab.network.PacketManager;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "MurderCoins", name = "Murder Coins", version = "1.0.4a", dependencies = "after:Mekanism;after:BasicComponents")
@NetworkMod(channels = "MurderCoins", clientSideRequired = true, serverSideRequired = false, connectionHandler = ConnectionHandler.class, packetHandler = PacketManager.class)
/*
 * clientPacketHandlerSpec = @SidedPacketHandler(channels = {"MurderCoins" }, packetHandler =
 * ClientPacketHandler.class), serverPacketHandlerSpec =@SidedPacketHandler(channels =
 * {"MurderCoins" }, packetHandler = ServerPacketHandler.class))
 */
public class MurderCoins
{
	public static final String modid = "MurderCoins";
	@Instance("MurderCoins")
	public static MurderCoins instance = new MurderCoins();
	private GuiHandler guiHandler = new GuiHandler();

	@SidedProxy(clientSide = "murder.murdercoin.client.ClientProxyMurderCoins", serverSide = "murder.murdercoin.common.CommonProxyMurderCoins")
	public static CommonProxyMurderCoins proxy;
	public static CreativeTabs murderTab = new TabsMurderTab(CreativeTabs.getNextID(), "MurderCoins");

	public static Item itemGoldCoin;
	public static Item itemDiamondCoin;
	public static Item itemEmeraldCoin;
	public static Item itemDiamondClump;
	public static Item dirtyDDust;
	public static Item itemDiamondDust;
	public static Item itemEmeraldClump;
	public static Item dirtyEDust;
	public static Item itemEmeraldDust;
	public static Item itemCoinMold;
	public static Item itemGoldNubBucket;
	public static Item itemMeltedGoldBuket;
	public static Item coinMold2;
	public static Block coinPress;
	public static Block goldForge;
	public static Block GoldStill;
	public static Block GoldFlowing;

	public static int generatorID = 3005;

	configMurderCoins comfigLoader = new configMurderCoins();

	@PreInit()
	public void PreInitMurderCoins(FMLPreInitializationEvent e)
	{

		// UniversalElectricity.register(this, 1, 2, 6, false);
		configMurderCoins.loadConfig(e);
		itemRegistration();

	}

	@Init
	public void load(FMLInitializationEvent event)
	{
		proxy.registerRenderThings();
		blockRegistration();
		// itemRegistration();
		addCraftingRecipes();
		addMekanismRecipes();
		networkRegisters();
		tileEntityRegisters();

	}

	public void blockRegistration()
	{

		coinPress = new BlockCoinPress(254).setUnlocalizedName("coinPress");
		GameRegistry.registerBlock(coinPress);
		LanguageRegistry.addName(coinPress, "Coin Press");
		goldForge = new BlockGoldForge(255).setUnlocalizedName("goldForge");
		GameRegistry.registerBlock(goldForge);
		LanguageRegistry.addName(goldForge, "Gold Forge");
		/*
		 * GoldStill = new BlockGoldStill(cc.GoldStillID).setUnlocalizedName("GoldStill");
		 * GameRegistry.registerBlock(GoldStill, "Gold_Still"); LanguageRegistry.addName(GoldStill,
		 * "Gold Still"); GoldFlowing = new
		 * BlockGoldFlowing(cc.GoldFlowingID).setUnlocalizedName("GoldFlowing");
		 * GameRegistry.registerBlock(GoldFlowing, "Gold_Flowing;");
		 * LanguageRegistry.addName(GoldFlowing, "Gold Flowing");
		 */
		// register itemBlocks

		// Item.itemsList[generatorID] = new ItemBlockGoldForge(generatorID-256,
		// goldForge).setItemName("goldForge").setIconIndex(32);

	}

	public void itemRegistration()
	{
		itemGoldCoin = new ItemgCoin(comfigLoader.gCoinID).setUnlocalizedName("gCoin");
		LanguageRegistry.addName(itemGoldCoin, "Gold Coin(s)");
		itemDiamondCoin = new ItemdCoin(comfigLoader.dCoinID).setUnlocalizedName("dCoin");
		LanguageRegistry.addName(itemDiamondCoin, "Diamond Coin(s)");
		itemCoinMold = new ItemCoinMold(comfigLoader.coinMoldID).setUnlocalizedName("coinMold");
		LanguageRegistry.addName(itemCoinMold, "Coin Mold");
		coinMold2 = itemCoinMold;
		itemGoldNubBucket = new ItemNugBucket(comfigLoader.nugBucketID).setUnlocalizedName("nugBucket");
		LanguageRegistry.addName(itemGoldNubBucket, "Bucket of GoldNuggets");
		itemMeltedGoldBuket = new ItemMeltedBucket(comfigLoader.meltedBucketID).setUnlocalizedName("meltedBucket").setContainerItem(Item.bucketEmpty).setContainerItem(itemCoinMold);
		LanguageRegistry.addName(itemMeltedGoldBuket, "Bucket of melted Gold");
		itemDiamondClump = new ItemDclump(comfigLoader.dClumpID).setUnlocalizedName("dClump");
		LanguageRegistry.addName(itemDiamondClump, "Diamond Clump");
		itemEmeraldClump = new ItemEclump(comfigLoader.eClumpID).setUnlocalizedName("eClump");
		LanguageRegistry.addName(itemEmeraldClump, "Emerald Clump");
		dirtyDDust = new ItemDirtyDDust(comfigLoader.dirtyDDustID).setUnlocalizedName("dirtyDDust");
		LanguageRegistry.addName(dirtyDDust, "Dirty Diamond Dust");
		dirtyEDust = new ItemDirtyEDust(comfigLoader.dirtyEDustID).setUnlocalizedName("dirtyEDust");
		LanguageRegistry.addName(dirtyEDust, "Dirty Emerald Dust");
		itemDiamondDust = new ItemDDust(comfigLoader.dDustID).setUnlocalizedName("dDust");
		LanguageRegistry.addName(itemDiamondDust, "Diamond Dust");
		itemEmeraldCoin = new ItemECoin(comfigLoader.eCoinID).setUnlocalizedName("eCoin");
		LanguageRegistry.addName(itemEmeraldCoin, "Emerald Coin");
		itemEmeraldDust = new ItemEDust(comfigLoader.eDustID).setUnlocalizedName("eDust");
		LanguageRegistry.addName(itemEmeraldDust, "Emerald Dust");
	}

	public void addCraftingRecipes()
	{
		GameRegistry.addSmelting(itemGoldNubBucket.itemID, new ItemStack(itemMeltedGoldBuket, 1), 0.1f);
		GameRegistry.addShapelessRecipe(new ItemStack(itemGoldNubBucket, 1), Item.bucketEmpty, Item.goldNugget, Item.goldNugget, Item.goldNugget, Item.goldNugget, Item.goldNugget, Item.goldNugget, Item.goldNugget, Item.goldNugget);
		GameRegistry.addShapelessRecipe(new ItemStack(itemGoldCoin, 4), itemCoinMold, itemMeltedGoldBuket);
		GameRegistry.addRecipe(new ItemStack(itemDiamondCoin, 1), new Object[] { 
			"XXX", "XYX", "XXX", 
			'X', itemGoldCoin, 
			'Y', Item.diamond });
		GameRegistry.addRecipe(new ItemStack(itemCoinMold, 1), new Object[] { 
			"YXY", "XXX", "YXY", 
			'X', Item.ingotIron, 
			'Y', Item.bucketEmpty });

	}

	public void addMekanismRecipes()
	{
		mekanism.api.RecipeHelper.addPurificationChamberRecipe(new ItemStack(Item.diamond, 1), new ItemStack(itemDiamondClump, 3));
		mekanism.api.RecipeHelper.addPurificationChamberRecipe(new ItemStack(Item.emerald), new ItemStack(itemEmeraldClump, 3));
		mekanism.api.RecipeHelper.addCrusherRecipe(new ItemStack(itemDiamondClump, 1), new ItemStack(dirtyDDust, 1));
		mekanism.api.RecipeHelper.addCrusherRecipe(new ItemStack(itemEmeraldClump, 1), new ItemStack(dirtyEDust, 1));
		mekanism.api.RecipeHelper.addEnrichmentChamberRecipe(new ItemStack(dirtyDDust, 1), new ItemStack(itemDiamondDust, 1));
		mekanism.api.RecipeHelper.addEnrichmentChamberRecipe(new ItemStack(dirtyEDust, 1), new ItemStack(itemEmeraldDust, 1));

	}

	public void networkRegisters()
	{
		NetworkRegistry.instance().registerGuiHandler(this, guiHandler);
	}

	public void tileEntityRegisters()
	{
		GameRegistry.registerTileEntity(TileGoldForge.class, "TileGoldForge");
		GameRegistry.registerTileEntity(TileEntityCoinPress.class, "TileCoinPress");
	}
}
