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
import net.minecraftforge.fml.relauncher.ReflectionHelper

object ModifyAnvilOutput {
    @JvmStatic
    fun modifyOutput(input: String) {
        val mc = Minecraft.getMinecraft()
        if (mc.currentScreen is GuiRepair) {
            val container = mc.thePlayer.openContainer as ContainerRepair
            try {
                val outputSlotField = ReflectionHelper.findField(ContainerRepair::class.java, "field_82852_f", "outputSlot")
                outputSlotField.isAccessible = true
                val outputSlot = outputSlotField.get(container) as InventoryCraftResult
                val stackResultField = ReflectionHelper.findField(InventoryCraftResult::class.java, "field_70465_c", "stackResult")
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