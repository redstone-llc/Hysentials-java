package llc.redstone.hysentials.cosmetic

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.InventoryBasic
import net.minecraft.item.ItemStack
import net.minecraft.util.IChatComponent
import org.newdawn.slick.tests.xml.Inventory

class CosmeticInventory: InventoryBasic("Cosmetic", false, 9*4)

class CosmeticPurchaseInventory: InventoryBasic("CosmeticPurchase", false, 1)