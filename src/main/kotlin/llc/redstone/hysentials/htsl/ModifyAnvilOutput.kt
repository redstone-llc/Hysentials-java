package llc.redstone.hysentials.htsl

import cc.polyfrost.oneconfig.utils.Multithreading
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiRepair
import net.minecraft.inventory.ContainerRepair
import net.minecraft.inventory.InventoryCraftResult
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import java.util.concurrent.TimeUnit
import llc.redstone.hysentials.handlers.htsl.Navigator.click

object ModifyAnvilOutput {
    @JvmStatic
    fun modifyOutput(input: String) {
        val mc = Minecraft.getMinecraft()
        if (mc.currentScreen is GuiRepair) {
            val container = mc.thePlayer.openContainer as ContainerRepair
            try {
                val outputSlotField = ContainerRepair::class.java.getDeclaredField("field_82852_f")
                outputSlotField.isAccessible = true
                val outputSlot = outputSlotField.get(container) as InventoryCraftResult
                val stackResultField = InventoryCraftResult::class.java.getDeclaredField("field_70467_a")
                stackResultField.isAccessible = true
                val outputSlotItem = stackResultField.get(outputSlot) as Array<ItemStack>
                outputSlotItem[0] = ItemStack(Item.getItemById(339)).setStackDisplayName(input)
                stackResultField.set(outputSlot, outputSlotItem)
                mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, 2, 0, 0, mc.thePlayer)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}