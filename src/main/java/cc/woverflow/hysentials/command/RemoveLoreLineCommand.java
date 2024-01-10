package cc.woverflow.hysentials.command;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.woverflow.hysentials.util.BUtils;
import cc.woverflow.hysentials.util.MUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

import static cc.woverflow.hysentials.command.RenameCommand.setCreativeAction;
import static cc.woverflow.hysentials.guis.container.GuiItem.getLore;
import static cc.woverflow.hysentials.guis.container.GuiItem.setLore;

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
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/removeloreline " + String.join(" ", args));
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

        ItemStack item = Minecraft.getMinecraft().thePlayer.getHeldItem();
        if (item == null || item.getItem() == null) {
            MUtils.chat("&cYou must be holding an item!");
            return;
        }
        item = item.copy();
        if (line > getLore(item).size()) {
            MUtils.chat("&cLine number is too high!");
            return;
        }
        List<String> lore = getLore(item);
        lore.remove(line - 1);
        setLore(item, lore);
        setCreativeAction(item, Minecraft.getMinecraft().thePlayer.inventory.currentItem);
        MUtils.chat("&aSuccessfully removed line &e" + line + "&a!");
    }
}
