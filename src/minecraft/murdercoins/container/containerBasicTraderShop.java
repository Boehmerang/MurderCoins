package murdercoins.container;

import murdercoins.tileentity.tileEntityBasicTrader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.prefab.SlotSpecific;

public class containerBasicTraderShop extends Container {
    protected tileEntityBasicTrader tile_entity;

    public containerBasicTraderShop(tileEntityBasicTrader tile_entity, InventoryPlayer player_inventory)
    {
		this.tile_entity = tile_entity;

		// battery Slot
		//this.addSlotToContainer(new SlotSpecific(tile_entity, 0, 154, 6, IItemElectric.class));

		// Transaction Item
		this.addSlotToContainer(new Slot(tile_entity, 0, 80, 9));

		// Gold Coin Slot
		this.addSlotToContainer(new Slot(tile_entity, 1, 32, 34));

		// Diamond Coin Slot
		this.addSlotToContainer(new Slot(tile_entity, 2, 80, 34));
		
		// Emerald Coin Slot
		this.addSlotToContainer(new Slot(tile_entity, 3, 125, 34));
		
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
    public boolean canInteractWith(EntityPlayer player){
            return tile_entity.isUseableByPlayer(player);
    }

    protected void bindPlayerInventory(InventoryPlayer player_inventory){
            for(int i = 0; i < 3; i++){
                    for(int j = 0; j < 9; j++){
                            addSlotToContainer(new Slot(player_inventory, j + i * 9 + 9, 9 + j * 18, 85 + i * 16));
                    }
            }

            for(int i = 0; i < 9; i++){
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

			if (par1 == 0)
			{
				if (!this.mergeItemStack(var4, 1, 37, true)) { return null; }

				var3.onSlotChange(var4, var2);
			}
			else if (par1 != 1 && par1 != 0)
			{
				if (var4.getItem() instanceof IItemElectric)
				{
					Slot temp = (Slot) this.inventorySlots.get(0);
					if (!temp.getHasStack())
					{
						temp.putStack(var4);
						var3.decrStackSize(1);
					}
					if (!this.mergeItemStack(var4, 0, 0, false)) { return null; }
				}
				else if (par1 >= 1 && par1 < 28)
				{
					if (!this.mergeItemStack(var4, 32, 40, false)) { return null; }
				}
				else if (par1 >= 32 && par1 < 40 && !this.mergeItemStack(var4, 3, 30, false)) { return null; }
			}
			else if (!this.mergeItemStack(var4, 4, 31, false)) { return null; }

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
