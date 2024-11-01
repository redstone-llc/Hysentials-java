package llc.redstone.hysentials.command;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import llc.redstone.hysentials.util.BUtils;
import llc.redstone.hysentials.util.MUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

import static llc.redstone.hysentials.command.InsertLoreLineCommand.processRedirect;
import static llc.redstone.hysentials.command.RenameCommand.setCreativeAction;
import static llc.redstone.hysentials.guis.container.GuiItem.getLore;
import static llc.redstone.hysentials.guis.container.GuiItem.setLore;

public class RemoveLoreLineCommand extends CommandBase {
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "removeloreline";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/removeloreline <line>";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("removell", "rll");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!Minecraft.getMinecraft().playerController.getCurrentGameType().isCreative() || BUtils.isSBX()) {
            if (processRedirect(this, args)) return;
            UMinecraft.getPlayer().sendChatMessage("/removeloreline " + String.join(" ", args));
            return;
        }
        if (args.length == 0) {
            UChat.chat("&cUsage: /removeloreline <line>");
            return;
        }

        int line;
        try {
            line = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            UChat.chat("&cInvalid line number!");
            return;
        }
        if (line < 1) {
            UChat.chat("&cInvalid line number!");
            return;
        }

        ItemStack item = UMinecraft.getPlayer().getHeldItem();
        if (item == null || item.getItem() == null) {
            UChat.chat("&cYou must be holding an item!");
            return;
        }
        item = item.copy();
        if (line > getLore(item).size()) {
            UChat.chat("&cLine number is too high!");
            return;
        }
        List<String> lore = getLore(item);
        lore.remove(line - 1);
        setLore(item, lore);
        RenameCommand.setCreativeAction(item, UMinecraft.getPlayer().inventory.currentItem);
        UChat.chat("&aSuccessfully removed line &e" + line + "&a!");
    }
}
