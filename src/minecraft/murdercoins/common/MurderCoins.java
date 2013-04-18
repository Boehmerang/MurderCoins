package murdercoins.common;

import java.io.File;

import murdercoins.block.blockCoinPress;
import murdercoins.block.blockGoldForge;
import murdercoins.block.blockGoldStill;
import murdercoins.block.blockManPress;
import murdercoins.block.blockPulverisor;
import murdercoins.block.blockWindmillBase;
import murdercoins.client.guiHandler;
import murdercoins.items.itemBrokenMold;
import murdercoins.items.itemCoinMold;
import murdercoins.items.itemDCoin;
import murdercoins.items.itemDDust;
import murdercoins.items.itemECoin;
import murdercoins.items.itemEDust;
import murdercoins.items.itemGCoin;
import murdercoins.items.itemMeltedBucket;
import murdercoins.items.itemNugBucket;
import murdercoins.items.itemWindmillBlade;
import murdercoins.items.itemWindmillTurbine;
import murdercoins.tileentity.tileEntityCoinPress;
import murdercoins.tileentity.tileEntityGoldForge;
import murdercoins.tileentity.tileEntityManPress;
import murdercoins.tileentity.tileEntityPulverisor;
import murdercoins.tileentity.tileEntityWindmillBase;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.server.MinecraftServer;
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

@Mod(modid = "MurderCoins", name = "Murder Coins", version = "1.1.1a", dependencies = "after:Mekanism;after:BasicComponents")
@NetworkMod(channels = "MurderCoins", clientSideRequired = true, serverSideRequired = false, connectionHandler = ConnectionHandler.class, packetHandler = PacketManager.class)
/*
 * clientPacketHandlerSpec = @SidedPacketHandler(channels = {"MurderCoins" },
 * packetHandler =
 * ClientPacketHandler.class), serverPacketHandlerSpec
 * =@SidedPacketHandler(channels =
 * {"MurderCoins" }, packetHandler = ServerPacketHandler.class))
 */
public class MurderCoins
{
	public static final String				modid				= "MurderCoins";
	@Instance("MurderCoins")
	public static MurderCoins				instance			= new MurderCoins();
	private guiHandler						guiHandler			= new guiHandler();
	
	@SidedProxy(clientSide = "murdercoins.client.ClientProxy", serverSide = "murdercoins.common.CommonProxy")
	public static CommonProxy	proxy;
	public static CreativeTabs				murderTab			= new tabMurderCoins(CreativeTabs.getNextID(), "MurderCoins");
	private static final String[]			LANGUAGES_SUPPORTED	= new String[] { "en_US" };
	public static final String				LANGUAGE_PATH		= "/mods/MurderCoins/language";
	
	public static Item						itemGoldCoin;
	public static Item						itemDiamondCoin;
	public static Item						itemEmeraldCoin;
	public static Item						itemDiamondClump;
	public static Item						dirtyDDust;
	public static Item						itemDiamondDust;
	public static Item						itemEmeraldClump;
	public static Item						dirtyEDust;
	public static Item						itemEmeraldDust;
	public static Item						itemCoinMold;
	public static Item						itemGoldNugBucket;
	public static Item						itemMeltedGoldBuket;
	public static Item						brokenMold;
	public static Item 						itemWindmillBlade;
	public static Item 						itemWindmillTurbine;
	
	public static Block						coinPress;
	public static Block						manualCoinPress;
	public static Block						goldForge;
	public static Block						GoldStill;
	public static Block						GoldFlowing;
	public static Block						pulverisor;
	public static Block 					WindmillBase;
	
	
	public static LiquidStack				goldLiquid;
	public static boolean					MekanismLoaded		= false;
	
	Config						configLoader		= new Config();
	
	@PreInit()
	public void PreInitMurderCoins(FMLPreInitializationEvent e)
	{
		// UniversalElectricity.register(this, 1, 2, 6, false);
		if (Loader.isModLoaded("Mekanism"))
			MekanismLoaded = true;
		Config.loadConfig(e);
		itemRegistration();
	}
	
	@Init
	public void load(FMLInitializationEvent event)
	{
		proxy.registerRenderThings();
		blockRegistration();
		// itemRegistration();
		addCraftingRecipes();
		if (MekanismLoaded == true)
		{
			addMekanismRecipes();
		}
		else
		{
			addChipperandDusts();
		}
		networkRegisters();
		tileEntityRegisters();
		chestHooks();
		goldLiquid = LiquidDictionary.getOrCreateLiquid("MoltenGold", new LiquidStack(GoldStill.blockID, 1));
		LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid("MoltenGold", LiquidContainerRegistry.BUCKET_VOLUME), new ItemStack(this.itemMeltedGoldBuket), new ItemStack(Item.bucketEmpty)));
	
		/**
		 * Handle language support
		 */
		int languages = 0;
		
		for (String language : LANGUAGES_SUPPORTED)
		{
			LanguageRegistry.instance().loadLocalization(LANGUAGE_PATH + language + ".properties", language, false);
			
			if (LanguageRegistry.instance().getStringLocalization("children", language) != "")
			{
				try
				{
					String[] children = LanguageRegistry.instance().getStringLocalization("children", language).split(",");
					
					for (String child : children)
					{
						if (child != "" && child != null)
						{
							LanguageRegistry.instance().loadLocalization(LANGUAGE_PATH + language + ".properties", child, false);
							languages++;
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		int unofficialLanguages = 0;
		unofficialLanguages = langLoad();
		
		System.out.println("MurderCoins" + ": Loaded " + languages + " Official and " + unofficialLanguages + " unofficial languages");
	}
	
	private void addChipperandDusts()
	{
		// TODO Auto-generated method stub
		
	}
	
	private void chestHooks()
	{
		// Adds Gold to the Mine-shaft Corridor chest spawns.
		ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemGoldCoin), 3, 10, 35));
		ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemDiamondCoin), 1, 5, 25));
		ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemEmeraldCoin), 1, 2, 15));
		// Adds Gold to the Desert pyramid chest spawns.
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemGoldCoin), 3, 10, 35));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemDiamondCoin), 1, 5, 25));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemEmeraldCoin), 1, 2, 15));
		// Adds Gold to the Jungle pyramid chest spawns.
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemGoldCoin), 3, 10, 35));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemDiamondCoin), 1, 5, 25));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemEmeraldCoin), 1, 2, 15));
		// Adds Gold to the Stronghold Corridor chest spawns.
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemGoldCoin), 3, 10, 35));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemDiamondCoin), 1, 5, 25));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemEmeraldCoin), 1, 2, 15));
		// Adds Gold to the Stronghold Library chest spawns.
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemGoldCoin), 3, 10, 35));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemDiamondCoin), 1, 5, 25));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemEmeraldCoin), 1, 2, 15));
		// Adds Gold to the Stronghold Crossing chest spawns.
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemGoldCoin), 3, 10, 35));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemDiamondCoin), 1, 5, 25));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemEmeraldCoin), 1, 2, 15));
		// Adds Gold to the Dungeon chest spawns.
		ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemGoldCoin), 3, 10, 35));
		ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemDiamondCoin), 1, 5, 25));
		ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemEmeraldCoin), 1, 2, 15));
		// Adds Gold to Bonus Chest
		ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemGoldCoin), 3, 10, 35));
		ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemDiamondCoin), 1, 5, 25));
		ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(MurderCoins.itemEmeraldCoin), 1, 2, 15));
	}
	
	public void blockRegistration()
	{
		coinPress = new blockCoinPress(configLoader.coinPressID).setUnlocalizedName("coinPress");
		GameRegistry.registerBlock(coinPress);
		LanguageRegistry.addName(coinPress, "Coin Press");
		goldForge = new blockGoldForge(configLoader.goldForgeID).setUnlocalizedName("goldForge");
		GameRegistry.registerBlock(goldForge);
		LanguageRegistry.addName(goldForge, "Gold Forge");
		manualCoinPress = new blockManPress(configLoader.manPressID).setUnlocalizedName("manPress");
		GameRegistry.registerBlock(manualCoinPress);
		LanguageRegistry.addName(manualCoinPress, "Manual Coin Press");
		
		GoldStill = new blockGoldStill(configLoader.GoldStillID).setUnlocalizedName("GoldStill");
		GameRegistry.registerBlock(GoldStill, "Gold_Still");
		LanguageRegistry.addName(GoldStill, "Gold Still");
		/*
		 * GoldFlowing = new
		 * BlockGoldFlowing(cc.GoldFlowingID).setUnlocalizedName("GoldFlowing");
		 * GameRegistry.registerBlock(GoldFlowing, "Gold_Flowing;");
		 * LanguageRegistry.addName(GoldFlowing, "Gold Flowing");
		 */
		pulverisor = new blockPulverisor(configLoader.pulverisorID).setUnlocalizedName("puvlerisor");
		GameRegistry.registerBlock(pulverisor);
		LanguageRegistry.addName(pulverisor, "Pulverisor");
		WindmillBase = new blockWindmillBase(4000).setUnlocalizedName("Windmill");
		GameRegistry.registerBlock(WindmillBase, "Windmill");
		LanguageRegistry.addName(WindmillBase, "Windmill");
		
	}
	
	public void itemRegistration()
	{
		itemGoldCoin = new itemGCoin(configLoader.gCoinID).setUnlocalizedName("gCoin");
		LanguageRegistry.addName(itemGoldCoin, "Gold Coin(s)");
		itemDiamondCoin = new itemDCoin(configLoader.dCoinID).setUnlocalizedName("dCoin");
		LanguageRegistry.addName(itemDiamondCoin, "Diamond Coin(s)");
		itemEmeraldCoin = new itemECoin(configLoader.eCoinID).setUnlocalizedName("eCoin");
		LanguageRegistry.addName(itemEmeraldCoin, "Emerald Coin(s)");
		itemCoinMold = new itemCoinMold(configLoader.coinMoldID).setUnlocalizedName("coinMold").setMaxStackSize(1);
		LanguageRegistry.addName(itemCoinMold, "Coin Mold");
		brokenMold = new itemBrokenMold(configLoader.brokenMoldID).setUnlocalizedName("brokenMold").setMaxStackSize(1);
		LanguageRegistry.addName(brokenMold, "Broken Coin Mold");
		itemGoldNugBucket = new itemNugBucket(configLoader.nugBucketID).setUnlocalizedName("nugBucket").setMaxStackSize(16);
		LanguageRegistry.addName(itemGoldNugBucket, "Bucket of GoldNuggets");
		itemMeltedGoldBuket = new itemMeltedBucket(configLoader.meltedBucketID).setUnlocalizedName("meltedBucket").setMaxStackSize(16);
		LanguageRegistry.addName(itemMeltedGoldBuket, "Bucket of melted Gold");
		itemEmeraldDust = new itemEDust(configLoader.eDustID).setUnlocalizedName("eDust");
		LanguageRegistry.addName(itemEmeraldDust, "Emerald Dust");
		if (MekanismLoaded == false)
		{
			itemDiamondDust = new itemDDust(configLoader.dDustID).setUnlocalizedName("dDust");
			LanguageRegistry.addName(itemDiamondDust, "Diamond Dust");
		}
		itemWindmillBlade = new itemWindmillBlade(4001).setUnlocalizedName("wBlade");
		LanguageRegistry.addName(itemWindmillBlade, "Windmill Blades");
		itemWindmillTurbine = new itemWindmillTurbine(4002).setUnlocalizedName("wTurbine");
		LanguageRegistry.addName(itemWindmillTurbine, "Windmill Turbine");
	
	}
	
	public void addCraftingRecipes()
	{
		GameRegistry.addSmelting(itemGoldNugBucket.itemID, new ItemStack(itemMeltedGoldBuket, 1), 0.1f);
		GameRegistry.addShapelessRecipe(new ItemStack(itemGoldNugBucket, 1), Item.bucketEmpty, Item.goldNugget, Item.goldNugget, Item.goldNugget, Item.goldNugget, Item.goldNugget, Item.goldNugget, Item.goldNugget, Item.goldNugget);
		GameRegistry.addShapelessRecipe(new ItemStack(itemCoinMold, 1), brokenMold, Item.ingotIron);
		GameRegistry.addRecipe(new ItemStack(itemCoinMold, 1), new Object[] { "YXY", "XXX", "YXY", 'X', Item.ingotIron, 'Y', Item.bucketEmpty });
		GameRegistry.addRecipe(new ItemStack(manualCoinPress, 1), new Object[] { "YTY", "XRX", "YTY", 'T', Block.pistonBase, 'Y', Item.ingotIron, 'R', Item.redstoneRepeater, 'X', new ItemStack(Item.redstone, 1) });
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(this.coinPress, 1), new Object[] { "XTX", "OVO", "XTX", 'T', new ItemStack(Block.pistonBase, 1), 'X', "plateSteel", 'O', "basicCircuit", 'V', "motor" }));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(this.goldForge, 1), new Object[] { "XTX", "OVO", "XTX", 'V', new ItemStack(Block.furnaceIdle, 1), 'X', "plateSteel", 'O', "basicCircuit", 'T', new ItemStack(Item.redstone, 1) }));
	}
	
	public void addMekanismRecipes()
	{
		/*
		 * mekanism.api.RecipeHelper.addPurificationChamberRecipe(new
		 * ItemStack(Item.diamond, 1), new ItemStack(itemDiamondClump, 2));
		 * mekanism.api.RecipeHelper.addPurificationChamberRecipe(new
		 * ItemStack(Item.emerald), new ItemStack(itemEmeraldClump, 2));
		 * mekanism.api.RecipeHelper.addCrusherRecipe(new
		 * ItemStack(itemDiamondClump, 1), new ItemStack(dirtyDDust, 1));
		 * mekanism.api.RecipeHelper.addCrusherRecipe(new
		 * ItemStack(itemEmeraldClump, 1), new ItemStack(dirtyEDust, 1));
		 * mekanism.api.RecipeHelper.addEnrichmentChamberRecipe(new
		 * ItemStack(dirtyDDust, 1), new ItemStack(itemDiamondDust, 1));
		 * mekanism.api.RecipeHelper.addEnrichmentChamberRecipe(new
		 * ItemStack(dirtyEDust, 1), new ItemStack(itemEmeraldDust, 1));
		 */
		mekanism.api.RecipeHelper.addCrusherRecipe(new ItemStack(Item.emerald, 1), new ItemStack(itemEmeraldDust, 1));
	}
	
	public void networkRegisters()
	{
		NetworkRegistry.instance().registerGuiHandler(this, guiHandler);
	}
	
	public void tileEntityRegisters()
	{
		GameRegistry.registerTileEntity(tileEntityGoldForge.class, "TileGoldForge");
		GameRegistry.registerTileEntity(tileEntityCoinPress.class, "TileCoinPress");
		GameRegistry.registerTileEntity(tileEntityManPress.class, "TileManPress");
		GameRegistry.registerTileEntity(tileEntityPulverisor.class, "TilePulverisor");
		GameRegistry.registerTileEntity(tileEntityWindmillBase.class, "TileWindmillBase");
	}
	
	public static File[] ListLanguages()
	{
		String folderDir = "";
		if (MinecraftServer.getServer().isDedicatedServer())
		{
			folderDir = "mods/" + "BiotechLanguages";
		}
		else if (!MinecraftServer.getServer().isDedicatedServer())
		{
			folderDir = Minecraft.getMinecraftDir() + File.separator + "mods" + File.separator + "BiotechLanguages";
		}
		
		File folder = new File(folderDir);
		
		if (!folder.exists())
			folder.mkdirs();
		
		String files;
		File[] listOfFiles = folder.listFiles();
		
		return listOfFiles;
	}
	
	public static int langLoad()
	{
		int unofficialLanguages = 0;
		try
		{
			for (File langFile : ListLanguages())
			{
				if (langFile.exists())
				{
					String name = langFile.getName();
					if (name.endsWith(".lang"))
					{
						String lang = name.substring(0, name.length() - 4);
						LanguageRegistry.instance().loadLocalization(langFile.toString(), lang, false);
						unofficialLanguages++;
					}
				}
			}
		}
		catch (Exception e)
		{
		}
		return unofficialLanguages;
	}
}
