package murdercoins.common.helpers;

import net.minecraft.item.ItemStack;

/*
 * 			Interface applied to all dust items for use with hoppers.
 * 
 */

public interface IItemDust 
{
	/*
	 * 	Called to register the dust with the ore Dictionary
	 */
	public void RegWithDictionary(ItemStack item, String itemname);

}
