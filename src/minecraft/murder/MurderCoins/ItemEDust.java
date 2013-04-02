package murder.MurderCoins;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

public class ItemEDust  extends Item 
{
	public ItemEDust(int id)
	{
     super(id);
     this.setCreativeTab(murderCoins.murderTab);
	}
	public void updateIcons(IconRegister iconregister)
	{
		iconIndex = iconregister.registerIcon("MurderCoins:eDust");
	}
}