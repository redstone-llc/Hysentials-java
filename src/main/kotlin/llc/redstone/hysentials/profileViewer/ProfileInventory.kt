package llc.redstone.hysentials.profileViewer

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.InventoryBasic
import net.minecraft.item.ItemStack
import net.minecraft.util.IChatComponent

class ProfileInventory(var size: Int): InventoryBasic("Cosmetic", false, size)