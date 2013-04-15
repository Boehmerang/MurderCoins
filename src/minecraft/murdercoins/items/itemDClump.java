package murdercoins.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import murdercoins.common.MurderCoins;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

public class itemDClump extends Item
{
	public itemDClump(int id)
	{
     super(id);
     this.setCreativeTab(MurderCoins.murderTab);
	}
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon("MurderCoins:dClump");
	}
	/*
	 * public void updateIcons(IconRegister iconregister)
	 *
	{
		iconIndex = iconregister.registerIcon("MurderCoins:dClump");
	}
	*/
}
