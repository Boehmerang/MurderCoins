package murder.murdercoin.common.machines.press;

import murder.murdercoin.common.MurderCoins;
import murder.murdercoin.common.items.ItemCoinMold;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.prefab.SlotSpecific;
import net.minecraft.item.ItemBucket;


public class ContainerCoinPress extends Container
{
	protected TileEntityCoinPress tile_entity;

	public ContainerCoinPress(TileEntityCoinPress tile_entity, InventoryPlayer player_inventory)
	{
		this.tile_entity = tile_entity;

		// Battery Slot
		this.addSlotToContainer(new SlotSpecific(tile_entity, 0, 154, 6, IItemElectric.class));
		// Bucket slot
		this.addSlotToContainer(new SlotSpecific(tile_entity, 6, 154, 26,new ItemStack(MurderCoins.itemMeltedGoldBuket)));
		// Bucket return slot
		this.addSlotToContainer(new SlotSpecific(tile_entity, 2, 154, 46, ItemBucket.class));

		// CoinMoldslots
		this.addSlotToContainer(new SlotSpecific(tile_entity, 3, 34, 10, ItemCoinMold.class)).getSlotStackLimit();
		this.addSlotToContainer(new SlotSpecific(tile_entity, 4, 34, 38, ItemCoinMold.class)).getSlotStackLimit();

		// Dust Slot
		this.addSlotToContainer(new Slot(tile_entity, 5, 55, 25));
		// Old Bucket Slot (removed due to liquid tank)
		//this.addSlotToContainer(new SlotSpecific(tile_entity, 6, 55, 38, new ItemStack(MurderCoins.itemMeltedGoldBuket)));

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
