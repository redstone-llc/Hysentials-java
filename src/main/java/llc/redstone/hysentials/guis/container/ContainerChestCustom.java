
package llc.redstone.hysentials.guis.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerChestCustom extends Container {
    private IInventory lowerChestInventory;
    private int numRows;

    public ContainerChestCustom(IInventory iInventory, IInventory iInventory2, EntityPlayer entityPlayer) {
        this.lowerChestInventory = iInventory2;
        this.numRows = iInventory2.getSizeInventory() / 9;
//        iInventory2.openInventory(entityPlayer);
        int i = (this.numRows - 4) * 18;

        int j;
        int k;
        for(j = 0; j < this.numRows; ++j) {
            for(k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(iInventory2, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for(j = 0; j < 3; ++j) {
            for(k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(iInventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
            }
        }

        for(j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(iInventory, j, 8 + j * 18, 161 + i));
        }

    }

    @Override
    public void putStackInSlot(int slotID, ItemStack stack) {

    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.lowerChestInventory.isUseableByPlayer(playerIn);
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemStack = null;
        Slot slot = (Slot)this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index < this.numRows * 9) {
                if (!this.mergeItemStack(itemStack2, this.numRows * 9, this.inventorySlots.size(), true)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemStack2, 0, this.numRows * 9, false)) {
                return null;
            }

            if (itemStack2.stackSize == 0) {
                slot.putStack((ItemStack)null);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemStack;
    }

    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        this.lowerChestInventory.closeInventory(playerIn);
    }

    public IInventory getLowerChestInventory() {
        return this.lowerChestInventory;
    }
}
