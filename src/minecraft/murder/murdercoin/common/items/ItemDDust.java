package murder.murdercoin.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import murder.murdercoin.common.MurderCoins;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

public class ItemDDust extends Item
{
	public ItemDDust(int id)
	{
     super(id);
     this.setCreativeTab(MurderCoins.murderTab);
	}
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon("MurderCoins:dDust");
	}
	/*
	public void updateIcons(IconRegister iconregister)
	{
		iconIndex = iconregister.registerIcon("MurderCoins:dDust");
	}
	*/
}