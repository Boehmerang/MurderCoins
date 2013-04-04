package murder.murdercoin.common.items;

import murder.murdercoin.common.MurderCoins;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

public class ItemBrokenMold extends Item
{
	public ItemBrokenMold(int id)
	{
     super(id);
     this.setCreativeTab(MurderCoins.murderTab);
	}
	public void updateIcons(IconRegister iconregister)
	{
		iconIndex = iconregister.registerIcon("MurderCoins:brokenMold");
	}
}