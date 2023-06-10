package cc.woverflow.hysentials.guis.misc;

import cc.woverflow.hysentials.handlers.sbb.SbbRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
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
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = Minecraft.getMinecraft().thePlayer.getHeldItem();
        if (item.getTagCompound() != null) {
            NBTTagCompound tag = item.getTagCompound();
            NBTTagCompound extraAttributes = tag.getCompoundTag("ExtraAttributes");
            if (extraAttributes != null && extraAttributes.hasKey("HOUSING_MENU")) {
                if (event.action.equals(PlayerInteractEvent.Action.RIGHT_CLICK_AIR) && Minecraft.getMinecraft().thePlayer.isSneaking() && SbbRenderer.housingScoreboard.getHousingName() != null) {
                    new PlayerInventory(event.entityPlayer).open();
                }
            }
        }
    }
}
