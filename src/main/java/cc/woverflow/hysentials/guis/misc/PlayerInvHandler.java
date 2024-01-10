package cc.woverflow.hysentials.guis.misc;

import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.cosmetic.CosmeticGui;
import cc.woverflow.hysentials.handlers.htsl.Navigator;
import cc.woverflow.hysentials.handlers.sbb.SbbRenderer;
import cc.woverflow.hysentials.profileViewer.DefaultProfileGui;
import cc.woverflow.hysentials.util.C;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.concurrent.TimeUnit;


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
                if (Minecraft.getMinecraft().thePlayer.isSneaking() && HysentialsConfig.shiftRightClickInv) {
                    new PlayerInventory((EntityPlayer) event.target).open();
                    event.setCanceled(true);
                    event.setCanceled(true);
                }
                return;
            }
        } else if (SbbRenderer.housingScoreboard.getHousingName() != null) {
            if (Minecraft.getMinecraft().thePlayer.isSneaking() && HysentialsConfig.shiftRightClickInv) {
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
