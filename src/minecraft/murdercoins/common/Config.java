package murdercoins.common;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class Config
{
	//		Item Id numbers.
	public static int gCoinID;
	public static int dCoinID;
	public static int eCoinID;
	public static int coinMoldID;
	public static int pressArmID;
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
	public static int manPressID;
	public static int itemGoldForgeID;
	public static int GoldStillID;
	public static int GoldFlowingID;
	public static int pulverisorID;
	public static int fakeBlockID;
	public static int cpBoundID;
	
	// 		Max Damage allowed for the Molds and Hydraulic Piston
	
	public static int moldDamage;
	public static int hPistonDamage;
	
	//		Power Use and Ticks per process of Gold Forge
	
	public static int GFprocessTicks;
	public static int GFticksToWarm;
	public static int GFticksTillFreeze;
	public static double GFjoulesPerUse;
	public static double GFtankJoules;
	public static double GFunfreezeJoules;
	
	//		Power Use and Ticks per process of Coin Press
	
	public static int CPprocessTicks;
	public static int CPticksToWarm;
	public static int CPticksTillFreeze;
	public static double CPjoulesPerUse;
	public static double CPtankJoules;
	public static double CPunfreezeJoules;
	
	//		Tick's per process of Manual Coin Press
	
	public static int MCPprocessTicks; 
	
	//		Power Use and Ticks per Process of Pulverisor
	
	public static int PprocessTicks;
	public static double PjoulesPerUse;

	public static void loadConfig(FMLPreInitializationEvent e)
	{
		Configuration config = new Configuration(e.getSuggestedConfigurationFile()); // Gets the
																						// file

		config.load(); // Loads it

		// Property gCoin; //This is a property, see below
		// gCoin = config.getItem("gCoin", 200); //This gets the property
		// gCoin.comment = "Cold Coin ID"; //This adds a comment
		gCoinID = config.getItem("gCoin", 3000).getInt();
		dCoinID = config.getItem("dCoin", 3001).getInt();
		eCoinID = config.getItem("eCoin", 3002).getInt();
		coinMoldID = config.getItem("coinMold", 3003).getInt();
		nugBucketID = config.getItem("nugBucket", 3004).getInt();
		meltedBucketID = config.getItem("meltedBucket", 205).getInt(); 
		dDustID = config.getItem("dDust", 3010).getInt();
		eDustID = config.getItem("eDust", 3011).getInt();
		pressArmID = config.getItem("brokenMold", 3012).getInt();
		coinPressID = config.getBlock("coinPress", 3013).getInt();
		goldForgeID = config.getBlock("goldForge", 3014).getInt();
		//itemGoldForgeID = config.getItem("goldForge", 3015).getInt();
		manPressID = config.getBlock("manPress", 3016).getInt();
		pulverisorID = config.getBlock("pulverisor", 3017).getInt();
		fakeBlockID = config.getBlock("fakeBlock", 3018).getInt();
		cpBoundID = config.getBlock("cpBounding", 3019).getInt();

		GoldStillID = config.getItem("GoldStill", 1001).getInt();
		GoldFlowingID = config.getItem("GoldFlowing", 1000).getInt();
		
		//		Manual Coin Press options
		
		MCPprocessTicks = config.get("Manual Coin Press", "Ticks needed to press coins", 1500).getInt();
		
		//		Pulverisor Options
		
		PprocessTicks = config.get("Pulverisor", "Ticks to Pulverise gem", 250).getInt();
		PjoulesPerUse = config.get("Pulverisor", "Joules per use", 50000.0D).getDouble(50000.0D);
		
		//		Electric Coin Press options
		
		CPprocessTicks = config.get("Coin Press", "Ticks needed to press coins", 1500).getInt();
		CPticksToWarm = config.get("Coin Press", "Ticks(multiplied by gold Stored) it takes to warm the tank", 100).getInt();
		CPticksTillFreeze = config.get("Coin Press", "Ticks until the tank will freeze", 1200).getInt();
		CPjoulesPerUse = config.get("Coin Press", "Joules used to press coins", 50000.0D).getDouble(50000.0D);
		CPtankJoules = config.get("Coin Press", "Joules used to keep tank warm", 10.0D).getDouble(10.0D);
		CPunfreezeJoules = config.get("Coin Press", "Joules(multiplied by gold Stored) needed to unfreeze the tank", 15000.0D).getDouble(15000.0D);
		
		//		Gold Forge options
		
		GFprocessTicks = config.get("Gold Forge", "Ticks needed to melt the gold", 500).getInt();
		GFticksToWarm = config.get("Gold Forge", "Ticks(multiplied by gold Stored) needed to warm the tank", 1000).getInt();
		GFticksTillFreeze = config.get("Gold Forge", "Ticks until the tank will freeze", 1200).getInt();
		GFjoulesPerUse = config.get("Gold Forge", "Joules used to press coins", 50000.0D).getDouble(50000.0D);
		GFtankJoules = config.get("Gold Forge", "Joules used to keep tank warm", 10.0D).getDouble(10.0D);
		GFunfreezeJoules = config.get("Gold Forge", "Joules(multiplied by gold Stored) needed to unfreeze the tank", 15000.0D).getDouble(15000.0D);
		
		//		Item Damage amounts
		
		moldDamage = config.get("Item Damage", "Max Damage for Coin Mold", 50).getInt();
		hPistonDamage = config.get("Item Damage", "Max Damage for Hydraulic Piston", 50).getInt();
		
		config.save(); // Saves the file

	}
}
