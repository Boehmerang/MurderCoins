package murdercoins.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import murdercoins.common.MurderCoins;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

public class itemNugBucket extends Item
{
	public itemNugBucket(int id)
	{
     super(id);
     this.setCreativeTab(murdercoins.common.MurderCoins.murderTab);
	}
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon("MurderCoins:bucketGoldDust");
	}
	/*
	public void updateIcons(IconRegister iconregister)
	{
		iconIndex = iconregister.registerIcon("MurderCoins:bucketGoldDust");
	}
	*/
}
