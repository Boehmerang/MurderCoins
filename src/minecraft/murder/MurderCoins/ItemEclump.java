package murder.MurderCoins;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;


public class ItemEclump extends Item
{
	public ItemEclump(int id)
	{
     super(id);
     this.setCreativeTab(murderCoins.murderTab);
	}
	public void updateIcons(IconRegister iconregister)
	{
		iconIndex = iconregister.registerIcon("MurderCoins:eClump");
	}
}