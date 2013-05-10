package murdercoins.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import murdercoins.common.MurderCoins;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class itemCoinMold extends Item
{
	
	private Icon moldIcon;
	private Icon damagedMoldIcon;
	
	public itemCoinMold(int id)
	{
	
     super(id);
     this.setCreativeTab(murdercoins.common.MurderCoins.murderTab);
	}
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon("MurderCoins:coinMold");
		this.damagedMoldIcon = iconRegister.registerIcon("MurderCoins:brokenMold");
	}
	
	@Override
	 public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
	{
		return Item.ingotIron.itemID == par2ItemStack.itemID ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
	}
	@Override
	public Icon getIconFromDamage(int damage)
	{
		if (damage == this.getMaxDamage())
		{
			return this.damagedMoldIcon;
		}
		return itemIcon;
		
	}
	/*
	public void updateIcons(IconRegister iconregister)
	{
		iconIndex = iconregister.registerIcon("MurderCoins:coinMold");
	}
	*/
}
