package murdercoins.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import murdercoins.common.MurderCoins;
import murdercoins.common.helpers.IItemDust;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class itemEDust  extends Item implements IItemDust
{
	public itemEDust(int id)
	{
     super(id);
     this.setCreativeTab(murdercoins.common.MurderCoins.murderTab);
	}
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon("MurderCoins:eDust");
	}
	@Override
	public void RegWithDictionary(ItemStack item, String itemname) {
		// TODO Auto-generated method stub
		
	}
}