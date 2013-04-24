package murdercoins.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class itemGoldForge extends ItemBlock
{
public itemGoldForge(int par1, Block block)
{
super(par1);
}

public String getItemNameIS(ItemStack itemstack)
{
	return itemstack.getItem().getUnlocalizedName();
}

public int getMetadata(int par1)
{
return par1;
}
}