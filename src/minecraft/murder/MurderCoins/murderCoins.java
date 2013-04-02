package murder.MurderCoins;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemBlock;
import universalelectricity.core.UniversalElectricity;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import universalelectricity.prefab.network.ConnectionHandler;
import universalelectricity.prefab.network.PacketManager;

@Mod(modid="MurderCoins", name="Murder Coins", version="1.0.4a", dependencies="after:Mekanism;after:BasicComponents")
@NetworkMod(channels = "MurderCoins", clientSideRequired = true, serverSideRequired = false, connectionHandler = ConnectionHandler.class, packetHandler = PacketManager.class)
/*clientPacketHandlerSpec = @SidedPacketHandler(channels = {"MurderCoins" }, packetHandler = ClientPacketHandler.class),
serverPacketHandlerSpec =@SidedPacketHandler(channels = {"MurderCoins" }, packetHandler = ServerPacketHandler.class))*/

public class murderCoins 
{
	public static final String modid = "MurderCoins";
	@Instance("MurderCoins")
	public static murderCoins instance = new murderCoins();
	private GuiHandler guiHandler = new GuiHandler();
	
	@SidedProxy(clientSide = "murder.MurderCoins.ClientProxyMurderCoins", serverSide = "murder.MurderCoins.CommonProxyMurderCoins")
	 public static CommonProxyMurderCoins proxy;
	 public static CreativeTabs murderTab = new TabsMurderTab(CreativeTabs.getNextID(),"MurderCoins");
	 
	 public static Item gCoin;
	 public static Item dCoin;
	 public static Item eCoin;
	 public static Item dClump;
	 public static Item dirtyDDust;
	 public static Item dDust;
	 public static Item eClump;
	 public static Item dirtyEDust;
	 public static Item eDust;
	 public static Item coinMold;
	 public static Item nugBucket;
	 public static Item meltedBucket;
	 public static Item coinMold2;
	 public static Block coinPress;
	 public static Block goldForge;
	 public static Block GoldStill;
	 public static Block GoldFlowing;
	 
	 
	 public static int generatorID = 3005;
	 
	 
	 configMurderCoins cc = new configMurderCoins();

	 @PreInit()
	 public void PreInitMurderCoins(FMLPreInitializationEvent e)
	 {
		  
		//UniversalElectricity.register(this, 1, 2, 6, false);
		configMurderCoins.loadConfig(e);
	    itemRegistration();
	   
	 
	 }
	 @Init
	 public void load(FMLInitializationEvent event)
	 {
		 proxy.registerRenderThings();
		 blockRegistration();
		 //itemRegistration();
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
		 LanguageRegistry.addName(goldForge,"Gold Forge");
		/* GoldStill = new BlockGoldStill(cc.GoldStillID).setUnlocalizedName("GoldStill");
		 GameRegistry.registerBlock(GoldStill, "Gold_Still");
		 LanguageRegistry.addName(GoldStill, "Gold Still");
		 GoldFlowing = new BlockGoldFlowing(cc.GoldFlowingID).setUnlocalizedName("GoldFlowing");
		 GameRegistry.registerBlock(GoldFlowing, "Gold_Flowing;");
		 LanguageRegistry.addName(GoldFlowing, "Gold Flowing");*/
		 //register itemBlocks
		 
		 //Item.itemsList[generatorID] = new ItemBlockGoldForge(generatorID-256, goldForge).setItemName("goldForge").setIconIndex(32);
		 
	 }
	 public void itemRegistration() 
	 {
		 gCoin = new ItemgCoin(cc.gCoinID).setUnlocalizedName("gCoin");
		 LanguageRegistry.addName(gCoin, "Gold Coin(s)");
		 dCoin = new ItemdCoin(cc.dCoinID).setUnlocalizedName("dCoin");
		 LanguageRegistry.addName(dCoin, "Diamond Coin(s)");
		 coinMold = new ItemCoinMold(cc.coinMoldID).setUnlocalizedName("coinMold");
		 LanguageRegistry.addName(coinMold, "Coin Mold");
		 coinMold2 = coinMold;
		 nugBucket = new ItemNugBucket(cc.nugBucketID).setUnlocalizedName("nugBucket");
		 LanguageRegistry.addName(nugBucket, "Bucket of GoldNuggets");
		 meltedBucket = new ItemMeltedBucket(cc.meltedBucketID).setUnlocalizedName("meltedBucket").setContainerItem(Item.bucketEmpty).setContainerItem(coinMold);
		 LanguageRegistry.addName(meltedBucket, "Bucket of melted Gold");
		 dClump = new ItemDclump(cc.dClumpID).setUnlocalizedName("dClump");
		 LanguageRegistry.addName(dClump, "Diamond Clump");
		 eClump = new ItemEclump(cc.eClumpID).setUnlocalizedName("eClump"); 
		 LanguageRegistry.addName(eClump, "Emerald Clump");
		 dirtyDDust = new ItemDirtyDDust(cc.dirtyDDustID).setUnlocalizedName("dirtyDDust");
		 LanguageRegistry.addName(dirtyDDust, "Dirty Diamond Dust");
		 dirtyEDust = new ItemDirtyEDust(cc.dirtyEDustID).setUnlocalizedName("dirtyEDust"); 
		 LanguageRegistry.addName(dirtyEDust, "Dirty Emerald Dust");
		 dDust = new ItemDDust(cc.dDustID).setUnlocalizedName("dDust");
		 LanguageRegistry.addName(dDust, "Diamond Dust");
		 eCoin = new ItemECoin(cc.eCoinID).setUnlocalizedName("eCoin");
		 LanguageRegistry.addName(eCoin, "Emerald Coin");
		 eDust = new ItemEDust(cc.eDustID).setUnlocalizedName("eDust"); 
		 LanguageRegistry.addName(eDust, "Emerald Dust");
	 }
	 public void addCraftingRecipes()
	 {
		 GameRegistry.addSmelting(nugBucket.itemID,new ItemStack(meltedBucket,1), 0.1f);
		 GameRegistry.addShapelessRecipe(new ItemStack(nugBucket,1), Item.bucketEmpty, Item.goldNugget,Item.goldNugget,Item.goldNugget,Item.goldNugget,Item.goldNugget,Item.goldNugget,Item.goldNugget,Item.goldNugget);
		 GameRegistry.addShapelessRecipe(new ItemStack(gCoin,4), coinMold, meltedBucket);
		 GameRegistry.addRecipe(new ItemStack(dCoin, 1), new Object[]
				 {
			 		"XXX","XYX","XXX",
			 		'X', gCoin, 'Y', Item.diamond
				 }); 
		 GameRegistry.addRecipe(new ItemStack(coinMold, 1), new Object[]
				 {
			 		"YXY","XXX","YXY",
			 		'X', Item.ingotIron, 'Y', Item.bucketEmpty
				 }); 

	 }
	 public void addMekanismRecipes()
	 {
		 mekanism.api.RecipeHelper.addPurificationChamberRecipe(new ItemStack(Item.diamond,1) , new ItemStack(dClump,3));
		 mekanism.api.RecipeHelper.addPurificationChamberRecipe(new ItemStack(Item.emerald), new ItemStack(eClump,3));
		 mekanism.api.RecipeHelper.addCrusherRecipe(new ItemStack(dClump,1) , new ItemStack(dirtyDDust,1));
		 mekanism.api.RecipeHelper.addCrusherRecipe(new ItemStack(eClump,1) , new ItemStack(dirtyEDust,1));
	 	 mekanism.api.RecipeHelper.addEnrichmentChamberRecipe(new ItemStack(dirtyDDust,1) , new ItemStack(dDust,1));
		 mekanism.api.RecipeHelper.addEnrichmentChamberRecipe(new ItemStack(dirtyEDust,1) , new ItemStack(eDust,1));
		
	 }
	 public void networkRegisters()
	 {
		 NetworkRegistry.instance().registerGuiHandler(this, guiHandler);
	 }
	 public void tileEntityRegisters()
	 {
		 GameRegistry.registerTileEntity(TileGoldForge.class,"TileGoldForge");
		 GameRegistry.registerTileEntity(TileCoinPress.class,"TileCoinPress");
	 }
}
