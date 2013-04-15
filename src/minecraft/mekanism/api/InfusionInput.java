package mekanism.api;

import net.minecraft.item.ItemStack;

public class InfusionInput
{
	/** The type of this infusion */
	public InfusionType infusionType;

	/** How much infuse it takes to perform this operation */
	public int infuseStored;

	/** The input ItemStack */
	public ItemStack inputSlot;

	public InfusionInput(InfusionType infusiontype, int required, ItemStack itemstack)
	{
		infusionType = infusiontype;
		infuseStored = required;
		inputSlot = itemstack;
	}

	public static InfusionInput getInfusion(InfusionType type, int stored, ItemStack itemstack)
	{
		return new InfusionInput(type, stored, itemstack);
	}
}
