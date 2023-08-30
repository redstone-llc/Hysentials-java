package cc.woverflow.hysentials.guis.misc;

import cc.polyfrost.oneconfig.utils.commands.annotations.SubCommand;
import cc.woverflow.hysentials.guis.container.Container;
import cc.woverflow.hysentials.guis.container.GuiItem;
import cc.woverflow.hysentials.handlers.sbb.SbbRenderer;
import cc.woverflow.hysentials.util.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import scala.Int;

import java.util.Arrays;
import java.util.List;

import static cc.woverflow.hysentials.command.RenameCommand.setCreativeAction;

public class PlayerInventory extends Container {
    public static PlayerInventory instance;
    EntityPlayer player;

    public PlayerInventory(EntityPlayer player) {
        super(player.getName() + "'s Inventory", 3);
        this.player = player;
        instance = this;
    }

    @Override
    public void setItems() {
        if (player == null) {
            return;
        }
        if (player.getHeldItem() != null) {
            setItem(10, GuiItem.fromStack(player.getHeldItem()));
        } else {
            setItem(10, GuiItem.fromStack(
                GuiItem.makeColorfulItem(Material.STAINED_GLASS_PANE, "&cNo Held Item", 1, 8,
                    "&7Player is not holding",
                    "&7an item!")
            ));
        }
        ItemStack[] armor = player.inventory.armorInventory;
        if (armor[3] != null) {
            setItem(13, GuiItem.fromStack(armor[3]));
        } else {
            setItem(13, GuiItem.fromStack(
                GuiItem.makeColorfulItem(Material.STAINED_GLASS_PANE, "&cNo Helmet", 1, 8,
                    "&7Player does not have",
                    "&7a helmet equipped!")
            ));
        }
        if (armor[2] != null) {
            setItem(14, GuiItem.fromStack(armor[2]));
        } else {
            setItem(14, GuiItem.fromStack(
                GuiItem.makeColorfulItem(Material.STAINED_GLASS_PANE, "&cNo Chestplate", 1, 8,
                    "&7Player does not have",
                    "&7a chestplate equipped!")
            ));
        }
        if (armor[1] != null) {
            setItem(15, GuiItem.fromStack(armor[1]));
        } else {
            setItem(15, GuiItem.fromStack(
                GuiItem.makeColorfulItem(Material.STAINED_GLASS_PANE, "&cNo Leggings", 1, 8,
                    "&7Player does not have",
                    "&7leggings equipped!")
            ));
        }
        if (armor[0] != null) {
            setItem(16, GuiItem.fromStack(armor[0]));
        } else {
            setItem(16, GuiItem.fromStack(
                GuiItem.makeColorfulItem(Material.STAINED_GLASS_PANE, "&cNo Boots", 1, 8,
                    "&7Player does not have",
                    "&7boots equipped!")
            ));
        }

        for (int i = 0; i < 27; i++) {
            if (i == 10 || i == 13 || i == 14 || i == 15 || i == 16) continue;
            setItem(i, GuiItem.fromStack(
                GuiItem.makeColorfulItem(Material.STAINED_GLASS_PANE, "&1&1&5", 1, 7)
            ));
        }
    }

    @Override
    public void handleMenu(MouseEvent event) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        super.closeInventory(player);
        instance = null;
    }

    @Override
    public void setClickActions() {
        setDefaultAction((event) -> {
            event.getEvent().cancel();
            if (event.getButton() != 0) {
                return;
            }
            if (event.getSlot() == 10 || event.getSlot() == 13 || event.getSlot() == 14 || event.getSlot() == 15 || event.getSlot() == 16) {
                ItemStack item = getStackInSlot(event.getSlot());
                if (item == null || item.getItem() == null) {
                    return;
                }
                if (item.getItemDamage() == 8) {
                    return;
                }
                if (Minecraft.getMinecraft().playerController.getCurrentGameType().isCreative()) {
                    int i = Minecraft.getMinecraft().thePlayer.inventory.getFirstEmptyStack();
                    if (i < 0) {
                        return;
                    }
                    Minecraft.getMinecraft().thePlayer.inventory.setInventorySlotContents(i, item);
                    setCreativeAction(item, i);
                }
            }
        });
    }

    public void tick() {
        if (player == null) {
            return;
        }

        update();
    }
}
