package murder.murdercoin.common;

import mekanism.api.ItemRetriever;
import murder.murdercoin.client.GuiHandler;
import murder.murdercoin.common.items.ItemBrokenMold;
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
import murder.murdercoin.common.machines.press.BlockManPress;
import murder.murdercoin.common.machines.press.TileEntityCoinPress;
import murder.murdercoin.common.machines.press.TileEntityManPress;
import murder.murdercoin.common.machines.pulverisor.BlockPulverisor;
import murder.murdercoin.common.machines.pulverisor.TilePulverisor;
import murder.murdercoin.common.machines.still.BlockGoldStill;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.liquids.LiquidContainerData;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import universalelectricity.prefab.network.ConnectionHandler;
import universalelectricity.prefab.network.PacketManager;
import cpw.mods.fml.common.Loader;
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
	public static Item itemGoldNugBucket;
	public static Item itemMeltedGoldBuket;
	public static Item brokenMold;
	public static Block coinPress;
	public static Block manualCoinPress;
	public static Block goldForge;
	public static Block GoldStill;
	public static Block GoldFlowing;
	public static Block pulverisor;
	public static LiquidStack goldLiquid;
	public static boolean MekanismLoaded = false;

	configMurderCoins comfigLoader = new configMurderCoins();

	@PreInit()
	public void PreInitMurderCoins(FMLPreInitializationEvent e)
	{
		// UniversalElectricity.register(this, 1, 2, 6, false);
		if(Loader.isModLoaded("Mekanism")) MekanismLoaded = true;
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
		if(MekanismLoaded == true){ addMekanismRecipes();}
		else{addChipperandDusts();}
		networkRegisters();
		tileEntityRegisters();
		chestHooks();
		goldLiquid = LiquidDictionary.getOrCreateLiquid("MoltenGold", new LiquidStack(GoldStill.blockID, 1));
		LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid("MoltenGold", LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(this.itemMeltedGoldBuket), new ItemStack(Item.bucketEmpty)));
	}

	private void addChipperandDusts() {
		// TODO Auto-generated method stub

	}

	private void chestHooks()
	{
		// Adds Gold to the Mine-shaft Corridor chest spawns.
		ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemGoldCoin),3,10,35));
		ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemDiamondCoin),1,5,25));
		ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemEmeraldCoin),1,2,15));
		// Adds Gold to the Desert pyramid chest spawns.
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemGoldCoin),3,10,35));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemDiamondCoin),1,5,25));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemEmeraldCoin),1,2,15));
		// Adds Gold to the Jungle pyramid chest spawns.
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemGoldCoin),3,10,35));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemDiamondCoin),1,5,25));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemEmeraldCoin),1,2,15));
		// Adds Gold to the Stronghold Corridor chest spawns.
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemGoldCoin),3,10,35));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemDiamondCoin),1,5,25));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemEmeraldCoin),1,2,15));
		// Adds Gold to the Stronghold Library chest spawns.
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemGoldCoin),3,10,35));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemDiamondCoin),1,5,25));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemEmeraldCoin),1,2,15));
		// Adds Gold to the Stronghold Crossing chest spawns.
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemGoldCoin),3,10,35));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemDiamondCoin),1,5,25));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemEmeraldCoin),1,2,15));
		// Adds Gold to the Dungeon chest spawns.
		ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemGoldCoin),3,10,35));
		ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemDiamondCoin),1,5,25));
		ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemEmeraldCoin),1,2,15));
		//Adds Gold to Bonus Chest
		ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemGoldCoin),3,10,35));
		ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemDiamondCoin),1,5,25));
		ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemEmeraldCoin),1,2,15));
	}

	public void blockRegistration()
	{
		coinPress = new BlockCoinPress(comfigLoader.coinPressID).setUnlocalizedName("coinPress");
		GameRegistry.registerBlock(coinPress);
		LanguageRegistry.addName(coinPress, "Coin Press");
		goldForge = new BlockGoldForge(comfigLoader.goldForgeID).setUnlocalizedName("goldForge");
		GameRegistry.registerBlock(goldForge);
		LanguageRegistry.addName(goldForge, "Gold Forge");
		manualCoinPress = new BlockManPress(comfigLoader.manPressID).setUnlocalizedName("manPress");
		GameRegistry.registerBlock(manualCoinPress);
		LanguageRegistry.addName(manualCoinPress, "Manual Coin Press");

		  GoldStill = new BlockGoldStill(comfigLoader.GoldStillID).setUnlocalizedName("GoldStill");
		  GameRegistry.registerBlock(GoldStill, "Gold_Still");
		  LanguageRegistry.addName(GoldStill,"Gold Still");
		  /*GoldFlowing = new BlockGoldFlowing(cc.GoldFlowingID).setUnlocalizedName("GoldFlowing");
		 GameRegistry.registerBlock(GoldFlowing, "Gold_Flowing;");
		 LanguageRegistry.addName(GoldFlowing, "Gold Flowing");
		 */
		pulverisor = new BlockPulverisor(comfigLoader.pulverisorID).setUnlocalizedName("puvlerisor");
		GameRegistry.registerBlock(pulverisor);
		LanguageRegistry.addName(pulverisor, "Pulverisor");

	}

	public void itemRegistration()
	{
		itemGoldCoin = new ItemgCoin(comfigLoader.gCoinID).setUnlocalizedName("gCoin");
		LanguageRegistry.addName(itemGoldCoin, "Gold Coin(s)");
		itemDiamondCoin = new ItemdCoin(comfigLoader.dCoinID).setUnlocalizedName("dCoin");
		LanguageRegistry.addName(itemDiamondCoin, "Diamond Coin(s)");
		itemEmeraldCoin = new ItemECoin(comfigLoader.eCoinID).setUnlocalizedName("eCoin");
		LanguageRegistry.addName(itemEmeraldCoin, "Emerald Coin(s)");
		itemCoinMold = new ItemCoinMold(comfigLoader.coinMoldID).setUnlocalizedName("coinMold").setMaxStackSize(1);
		LanguageRegistry.addName(itemCoinMold, "Coin Mold");
		brokenMold = new ItemBrokenMold(comfigLoader.brokenMoldID).setUnlocalizedName("brokenMold").setMaxStackSize(1);
		LanguageRegistry.addName(brokenMold, "Broken Coin Mold");
		itemGoldNugBucket = new ItemNugBucket(comfigLoader.nugBucketID).setUnlocalizedName("nugBucket").setMaxStackSize(16);
		LanguageRegistry.addName(itemGoldNugBucket, "Bucket of GoldNuggets");
		itemMeltedGoldBuket = new ItemMeltedBucket(comfigLoader.meltedBucketID).setUnlocalizedName("meltedBucket").setMaxStackSize(16);
		LanguageRegistry.addName(itemMeltedGoldBuket, "Bucket of melted Gold");
		itemEmeraldDust = new ItemEDust(comfigLoader.eDustID).setUnlocalizedName("eDust");
		LanguageRegistry.addName(itemEmeraldDust, "Emerald Dust");
		if(MekanismLoaded == false)
		{
			itemDiamondDust = new ItemDDust(comfigLoader.dDustID).setUnlocalizedName("dDust");
			LanguageRegistry.addName(itemDiamondDust, "Diamond Dust");
		}
		/*
		 * removed these items to emulate more the way other mods retrieve dusts from diamonds and the like.
		 */
		//itemDiamondClump = new ItemDclump(comfigLoader.dClumpID).setUnlocalizedName("dClump");
		//LanguageRegistry.addName(itemDiamondClump, "Diamond Clump");
		//itemEmeraldClump = new ItemEclump(comfigLoader.eClumpID).setUnlocalizedName("eClump");
		//LanguageRegistry.addName(itemEmeraldClump, "Emerald Clump");
		//dirtyDDust = new ItemDirtyDDust(comfigLoader.dirtyDDustID).setUnlocalizedName("dirtyDDust");
		//LanguageRegistry.addName(dirtyDDust, "Dirty Diamond Dust");
		//dirtyEDust = new ItemDirtyEDust(comfigLoader.dirtyEDustID).setUnlocalizedName("dirtyEDust");
		//LanguageRegistry.addName(dirtyEDust, "Dirty Emerald Dust");
	}

	public void addCraftingRecipes()
	{
		GameRegistry.addSmelting(itemGoldNugBucket.itemID, new ItemStack(itemMeltedGoldBuket, 1), 0.1f);
		GameRegistry.addShapelessRecipe(new ItemStack(itemGoldNugBucket, 1), Item.bucketEmpty, Item.goldNugget, Item.goldNugget, Item.goldNugget, Item.goldNugget, Item.goldNugget, Item.goldNugget, Item.goldNugget, Item.goldNugget);
		GameRegistry.addShapelessRecipe(new ItemStack(itemCoinMold, 1), brokenMold, Item.ingotIron);
		GameRegistry.addRecipe(new ItemStack(itemCoinMold, 1), new Object[] {
			"YXY", "XXX", "YXY",
			'X', Item.ingotIron,
			'Y', Item.bucketEmpty });
		GameRegistry.addRecipe(new ItemStack(manualCoinPress, 1), new Object[] {
			"YTY", "XRX", "YTY",
			'T', Block.pistonBase,
			'Y', Item.ingotIron,
			'R', Item.redstoneRepeater,
			'X',  new ItemStack(Item.redstone, 1) });
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(this.coinPress, 1), new Object[] {
            "XTX", "OVO", "XTX",
            'T', new ItemStack(Block.pistonBase, 1),
            'X', "plateSteel",
            'O', "basicCircuit",
            'V', "motor" }));
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(this.goldForge, 1), new Object[] {
            "XTX", "OVO", "XTX",
            'V', new ItemStack(Block.furnaceIdle, 1),
            'X', "plateSteel",
            'O', "basicCircuit",
            'T', new ItemStack(Item.redstone,1) }));
	}

	public void addMekanismRecipes()
	{
		/*
		mekanism.api.RecipeHelper.addPurificationChamberRecipe(new ItemStack(Item.diamond, 1), new ItemStack(itemDiamondClump, 2));
		mekanism.api.RecipeHelper.addPurificationChamberRecipe(new ItemStack(Item.emerald), new ItemStack(itemEmeraldClump, 2));
		mekanism.api.RecipeHelper.addCrusherRecipe(new ItemStack(itemDiamondClump, 1), new ItemStack(dirtyDDust, 1));
		mekanism.api.RecipeHelper.addCrusherRecipe(new ItemStack(itemEmeraldClump, 1), new ItemStack(dirtyEDust, 1));
		mekanism.api.RecipeHelper.addEnrichmentChamberRecipe(new ItemStack(dirtyDDust, 1), new ItemStack(itemDiamondDust, 1));
		mekanism.api.RecipeHelper.addEnrichmentChamberRecipe(new ItemStack(dirtyEDust, 1), new ItemStack(itemEmeraldDust, 1));
		*/
		mekanism.api.RecipeHelper.addCrusherRecipe(new ItemStack(Item.emerald,1), new ItemStack(itemEmeraldDust, 1));
	}

	public void networkRegisters()
	{
		NetworkRegistry.instance().registerGuiHandler(this, guiHandler);
	}

	public void tileEntityRegisters()
	{
		GameRegistry.registerTileEntity(TileGoldForge.class, "TileGoldForge");
		GameRegistry.registerTileEntity(TileEntityCoinPress.class, "TileCoinPress");
		GameRegistry.registerTileEntity(TileEntityManPress.class, "TileManPress");
		GameRegistry.registerTileEntity(TilePulverisor.class, "TilePulverisor");
	}
}
