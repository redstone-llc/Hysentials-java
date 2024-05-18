package llc.redstone.hysentials.guis.misc;

import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.config.hysentialmods.HousingConfig;
import llc.redstone.hysentials.handlers.htsl.Navigator;
import llc.redstone.hysentials.handlers.sbb.SbbRenderer;
import llc.redstone.hysentials.profileviewer.DefaultProfileGui;
import llc.redstone.hysentials.util.C;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;


public class PlayerInvHandler {
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && PlayerInventory.instance != null) {
            PlayerInventory.instance.tick();
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(EntityInteractEvent event) {
        ItemStack item = Minecraft.getMinecraft().thePlayer.getHeldItem();
        if (!(event.target instanceof EntityPlayer)) return;
        if (event.target.getUniqueID().equals(Minecraft.getMinecraft().thePlayer.getUniqueID())) return;
        if (Minecraft.getMinecraft().getNetHandler().getPlayerInfo(((EntityPlayer) event.target).getUniqueID()) == null) return;
        if (item != null && item.getTagCompound() != null) {
            NBTTagCompound tag = item.getTagCompound();
            NBTTagCompound extraAttributes = tag.getCompoundTag("ExtraAttributes");
            if (extraAttributes != null && !extraAttributes.hasKey("HOUSING_MENU") && SbbRenderer.housingScoreboard.getHousingName() != null) {
                if (Minecraft.getMinecraft().thePlayer.isSneaking() && HousingConfig.shiftRightClickInv) {
                    new PlayerInventory((EntityPlayer) event.target).open();
                    event.setCanceled(true);
                    event.setCanceled(true);
                }
                return;
            }
        } else if (SbbRenderer.housingScoreboard.getHousingName() != null) {
            if (Minecraft.getMinecraft().thePlayer.isSneaking() && HousingConfig.shiftRightClickInv) {
                new PlayerInventory((EntityPlayer) event.target).open();
                event.setCanceled(true);
                event.setCanceled(true);
            }
            return;
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.gui instanceof GuiChest) {
            GuiChest chest = (GuiChest) event.gui;
            if ( chest.inventorySlots == null || chest.inventorySlots.inventoryItemStacks == null || chest.inventorySlots.inventoryItemStacks.size() == 0 ) return;
            ItemStack first = chest.inventorySlots.getSlot(0).getStack();
            if (first == null) return;
            String name = first.hasDisplayName() ? C.removeColor(first.getDisplayName()) : "";
            if (Navigator.getContainerName() == null) return;
            String containerName = C.removeColor(Navigator.getContainerName());
            if (name.equals(containerName)) {
                event.setCanceled(true);
                EntityPlayer p = Minecraft.getMinecraft().theWorld.getPlayerEntityByName(Navigator.getContainerName());
                if (p != null) {
                    Hysentials.INSTANCE.guiDisplayHandler.setDisplayNextTick(new DefaultProfileGui(p));
                }
            }

        }

    }
}
