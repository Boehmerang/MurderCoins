package murder.MurderCoins;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class configMurderCoins
{

	 public static int gCoinID;
	 public static int dCoinID;
	 public static int eCoinID;
	 public static int coinMoldID;
	 public static int nugBucketID;
	 public static int meltedBucketID;
	 public static int goldForgeID;
	 public static int dClumpID;
	 public static int eClumpID;
	 public static int dirtyDDustID;
	 public static int dirtyEDustID;
	 public static int dDustID;
	 public static int eDustID;
	 public static int coinPressID;
	 public static int itemGoldForgeID;
	 public static int GoldStillID;
	 public static int GoldFlowingID;
	 
	 
	 public static void loadConfig(FMLPreInitializationEvent e){
		 Configuration config = new Configuration(e.getSuggestedConfigurationFile()); //Gets the file

		 config.load(); //Loads it

		 //Property gCoin; //This is a property, see below
		 //gCoin = config.getItem("gCoin", 200); //This gets the property
		 //gCoin.comment = "Cold Coin ID"; //This adds a comment
		 gCoinID = config.getItem("gCoin", 200).getInt(); //This gets the value
		 //Property dCoin; //This is a property, see below
		 //dCoin = config.getItem("dCoin", 201); //This gets the property
		 //dCoin.comment = "Diamond Coin ID"; //This adds a comment
		 dCoinID = config.getItem("dCoin", 201).getInt(); //This gets the value		 
		 //Property coinMold; //This is a property, see below
		 //coinMold = config.getItem("coinMold", 202); //This gets the property
		 //coinMold.comment = "Coin Mold Id"; //This adds a comment
		 coinMoldID = config.getItem("coinMold", 202).getInt(); //This gets the value	
		 //Property nugBucket; //This is a property, see below
		 //nugBucket = config.getItem("nugBucket", 3003); //This gets the property
		 //nugBucket.comment = "Nugget Bucket ID"; //This adds a comment
		 nugBucketID = config.getItem("nugBucket", 203).getInt(); //This gets the value
		 //Property meltedBucket;
		 //meltedBucket = config.getItem("meltedBucket", 3004); //This gets the property
		 //meltedBucket.comment = "Bucket of Gold ID"; //This adds a comment
		 meltedBucketID = config.getItem("meltedBucket", 204).getInt(); //This gets the value
		 dClumpID = config.getItem("dClump", 206).getInt();
		 eClumpID = config.getItem("eClump", 207).getInt();
		 dirtyDDustID = config.getItem("dirtyDDust", 208).getInt();
		 dirtyEDustID = config.getItem("dirtyEDust", 209).getInt();
		 dDustID = config.getItem("dDust", 210).getInt();
		 eDustID = config.getItem("eDust", 211).getInt();
		 coinPressID = config.getItem("coinPress",312).getInt();
		 goldForgeID = config.getItem("goldForge", 313).getInt();
		 itemGoldForgeID = config.getItem("goldForge", 314).getInt();
		 eCoinID = config.getItem("eCoin", 315).getInt();
		 GoldStillID = config.getItem("GoldStill", 1001).getInt();
		 GoldFlowingID = config.getItem("GoldFlowing", 1000).getInt();
		 
		 config.save(); //Saves the file

		 }

}
