package murder.murdercoin.common.items;

import murder.murdercoin.common.MurderCoins;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

public class ItemEDust  extends Item 
{
	public ItemEDust(int id)
	{
     super(id);
     this.setCreativeTab(MurderCoins.murderTab);
	}
	public void updateIcons(IconRegister iconregister)
	{
		iconIndex = iconregister.registerIcon("MurderCoins:eDust");
	}
}