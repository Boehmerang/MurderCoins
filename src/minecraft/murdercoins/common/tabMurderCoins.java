package murdercoins.common;

import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class tabMurderCoins extends CreativeTabs
{
	public tabMurderCoins(int position, String tabID)
	{
		super(position, tabID); // The constructor for your tab
	}

	@SideOnly(Side.CLIENT)
	public int getTabIconItemIndex() // The item it displays for your tab
	{
		return MurderCoins.itemDiamondCoin.itemID; // For this we'll use the ruby
	}

	public String getTranslatedTabLabel()
	{
		return "MurderCoins"; // The name of the tab ingame
	}
}
