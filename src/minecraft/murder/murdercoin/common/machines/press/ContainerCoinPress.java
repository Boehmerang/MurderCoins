package murder.murdercoin.common.machines.press;

import murder.murdercoin.common.MurderCoins;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.prefab.SlotSpecific;

public class ContainerCoinPress extends Container
{
	protected TileEntityCoinPress tile_entity;

	public ContainerCoinPress(TileEntityCoinPress tile_entity, InventoryPlayer player_inventory)
	{
		this.tile_entity = tile_entity;

		// battery Slot
		this.addSlotToContainer(new SlotSpecific(tile_entity, 0, 154, 6, IItemElectric.class));
		// upgrade slots
		this.addSlotToContainer(new SlotSpecific(tile_entity, 1, 154, 26, IItemElectric.class));
		this.addSlotToContainer(new SlotSpecific(tile_entity, 2, 154, 46, IItemElectric.class));

		// CoinMoldslots
		this.addSlotToContainer(new Slot(tile_entity, 3, 28, 10));
		this.addSlotToContainer(new Slot(tile_entity, 4, 28, 38));

		// Dust Slot
		this.addSlotToContainer(new Slot(tile_entity, 5, 49, 10));
		// Bucket Slot
		this.addSlotToContainer(new SlotSpecific(tile_entity, 6, 49, 38, new ItemStack(MurderCoins.itemMeltedGoldBuket)));

		// Smelting result
		this.addSlotToContainer(new SlotFurnace(player_inventory.player, tile_entity, 7, 108, 25));
		int var3;

		for (var3 = 0; var3 < 3; ++var3)
		{
			for (int var4 = 0; var4 < 9; ++var4)
			{
				this.addSlotToContainer(new Slot(player_inventory, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
			}
		}

		for (var3 = 0; var3 < 9; ++var3)
		{
			this.addSlotToContainer(new Slot(player_inventory, var3, 8 + var3 * 18, 142));
		}

		tile_entity.openChest();
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return tile_entity.isUseableByPlayer(player);
	}

	protected void bindPlayerInventory(InventoryPlayer player_inventory)
	{
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				addSlotToContainer(new Slot(player_inventory, j + i * 9 + 9, 9 + j * 18, 85 + i * 16));

			}
		}

		for (int i = 0; i < 9; i++)
		{
			addSlotToContainer(new Slot(player_inventory, i, 6 + i * 16, 142));
		}

	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1)
	{
		ItemStack var2 = null;
		Slot var3 = (Slot) this.inventorySlots.get(par1);

		if (var3 != null && var3.getHasStack())
		{
			ItemStack var4 = var3.getStack();
			var2 = var4.copy();

			if (par1 == 2)
			{
				if (!this.mergeItemStack(var4, 3, 39, true))
				{
					return null;
				}

				var3.onSlotChange(var4, var2);
			}
			else if (par1 != 1 && par1 != 0)
			{
				if (var4.getItem() instanceof IItemElectric)
				{
					if (!this.mergeItemStack(var4, 0, 1, false))
					{
						return null;
					}
				}
				else if (par1 >= 3 && par1 < 30)
				{
					if (!this.mergeItemStack(var4, 30, 39, false))
					{
						return null;
					}
				}
				else if (par1 >= 30 && par1 < 39 && !this.mergeItemStack(var4, 3, 30, false))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(var4, 3, 39, false))
			{
				return null;
			}

			if (var4.stackSize == 0)
			{
				var3.putStack((ItemStack) null);
			}
			else
			{
				var3.onSlotChanged();
			}

			if (var4.stackSize == var2.stackSize)
			{
				return null;
			}

			var3.onPickupFromSlot(par1EntityPlayer, var4);
		}

		return var2;
	}

}
