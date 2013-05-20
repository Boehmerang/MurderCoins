package murdercoins.container;

import murdercoins.common.MurderCoins;
import murdercoins.common.helpers.iItemCoins;
import murdercoins.items.ItemPressArm;
import murdercoins.items.itemCoinMold;
import murdercoins.items.itemDCoin;
import murdercoins.items.itemECoin;
import murdercoins.items.itemGCoin;
import murdercoins.tileentity.tileEntityBasicVault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.prefab.SlotSpecific;

public class containerBasicVault extends Container
{
	protected tileEntityBasicVault tile_entity;

	public containerBasicVault(tileEntityBasicVault tile_entity, InventoryPlayer player_inventory)
	{
		this.tile_entity = tile_entity;
		
		int var3;
		int slot = 0;
		for (var3 = 0; var3 < 9; ++var3)
		{
			this.addSlotToContainer(new Slot(tile_entity, slot, 87 + var3 * 18, 8));
			slot++;
		}
		
		for (var3 = 0; var3 < 2; ++var3)
		{
			for (int var4 = 0; var4 < 9; ++var4)
			{
				this.addSlotToContainer(new Slot(tile_entity, slot /*var4 + var3 * 9 + 98*/, 87 + var4 * 18, 36 + var3 * 18));
				slot++;
			}
		}
		for (var3 = 0; var3 < 2; ++var3)
		{
			for (int var4 = 0; var4 <3; ++var4)
			{
				this.addSlotToContainer(new SlotSpecific(tile_entity, slot /*var4 + var3 * 9 + 98*/, 13 + var4 * 18, 21 + var3 * 18, itemGCoin.class));
				slot++;
			}
		}
		for (var3 = 0; var3 < 2; ++var3)
		{
			for (int var4 = 0; var4 < 3; ++var4)
			{
				this.addSlotToContainer(new SlotSpecific(tile_entity, slot /*var4 + var3 * 9 + 98*/, 13 + var4 * 18, 61 + var3 * 18, itemDCoin.class));
				slot++;
			}
		}
		for (var3 = 0; var3 < 2; ++var3)
		{
			for (int var4 = 0; var4 < 3; ++var4)
			{
				this.addSlotToContainer(new SlotSpecific(tile_entity, slot /*var4 + var3 * 9 + 98*/, 13 + var4 * 18, 100 + var3 * 18, itemECoin.class));
				slot++;
			}
		}
		

		for (var3 = 0; var3 < 3; ++var3)
		{
			for (int var4 = 0; var4 < 9; ++var4)
			{
				this.addSlotToContainer(new Slot(player_inventory, var4 + var3 * 9 + 9, 87 + var4 * 18, 84 + var3 * 18));
			}
		}

		for (var3 = 0; var3 < 9; ++var3)
		{
			this.addSlotToContainer(new Slot(player_inventory, var3, 87 + var3 * 18, 142));
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
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slotID)
	{
		ItemStack var2 = null;
		Slot var3 = (Slot) this.inventorySlots.get(slotID);

		if (var3 != null && var3.getHasStack())
		{
			ItemStack var4 = var3.getStack();
			var2 = var4.copy();

			if (slotID >= 0  && slotID < 45)
			{
				if (!this.mergeItemStack(var4, 45, 81, false)) { return null; }
			}
			/*else if (slotID >= 27 && slotID < 33)
			{
				if (!this.mergeItemStack(var4, 45, 81, false)) { return null; }
			}
			else if (slotID >= 33 && slotID < 39)
			{
				if (!this.mergeItemStack(var4, 45, 81, false)) { return null; }
			}
			else if (slotID >= 39 && slotID < 45)
			{
				if (!this.mergeItemStack(var4, 45, 81, false)) { return null; }
			}*/
			else if (slotID >= 45 && slotID < 81 && !this.mergeItemStack(var4, 9, 27, false)) { return null; }
			else if (!this.mergeItemStack(var4, 5, 40, false)) { return null; }

			if (var4.stackSize == 0)
			{
				var3.putStack((ItemStack) null);
			}
			else
			{
				var3.onSlotChanged();
			}

			if (var4.stackSize == var2.stackSize) { return null; }

			var3.onPickupFromSlot(par1EntityPlayer, var4);
		}
		return var2;
	}
}
