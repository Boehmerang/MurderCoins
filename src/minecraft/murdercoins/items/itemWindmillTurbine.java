package murdercoins.items;

import murdercoins.common.MurderCoins;
import murdercoins.common.helpers.IItemDust;
import murdercoins.common.helpers.IItemTurbines;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class itemWindmillTurbine extends Item implements IItemTurbines
{
	public itemWindmillTurbine(int id)
	{
     super(id);
     this.setCreativeTab(murdercoins.common.MurderCoins.murderTab);
	}
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon("MurderCoins:windmillTurbine");
	}
	@Override
	public void RegWithDictionary(ItemStack item, String itemname) {
		// TODO Auto-generated method stub
		
	}
}