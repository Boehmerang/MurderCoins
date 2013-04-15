package murder.murdercoin.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import murder.murdercoin.common.MurderCoins;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

public class ItemEclump extends Item
{
	public ItemEclump(int id)
	{
     super(id);
     this.setCreativeTab(MurderCoins.murderTab);
	}
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon("MurderCoins:eClump");
	}
	/*
	public void updateIcons(IconRegister iconregister)
	{
		iconIndex = iconregister.registerIcon("MurderCoins:eClump");
	}
	*/
}