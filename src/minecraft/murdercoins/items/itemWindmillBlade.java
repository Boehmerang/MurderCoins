package murdercoins.items;

import murdercoins.common.MurderCoins;
import murdercoins.common.helpers.IItemTurbines;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class itemWindmillBlade extends Item implements IItemTurbines
{
	public itemWindmillBlade(int id)
	{
     super(id);
     this.setCreativeTab(MurderCoins.murderTab);
	}
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon("MurderCoins:windmillBlades");
	}
	@Override
	public void RegWithDictionary(ItemStack item, String itemname) {
		// TODO Auto-generated method stub
		
	}
}