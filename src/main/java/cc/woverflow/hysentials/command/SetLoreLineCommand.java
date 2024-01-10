package cc.woverflow.hysentials.command;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.woverflow.hysentials.util.BUtils;
import cc.woverflow.hysentials.util.MUtils;
import cc.woverflow.hysentials.util.C;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cc.woverflow.hysentials.command.RenameCommand.setCreativeAction;
import static cc.woverflow.hysentials.guis.container.GuiItem.getLore;
import static cc.woverflow.hysentials.guis.container.GuiItem.setLore;

public class SetLoreLineCommand extends CommandBase {
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "setloreline";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/setloreline <line> <value>";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("setll", "sll");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (!Minecraft.getMinecraft().playerController.getCurrentGameType().isCreative() || BUtils.isSBX()) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/setloreline " + String.join(" ", args));
            return;
        }
        if (args.length < 2) {
            UChat.chat("§cUsage: /setloreline <line> <value>");
            return;
        }
        int line;
        try {
            line = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            UChat.chat("§cInvalid line number!");
            return;
        }
        if (line < 1) {
            UChat.chat("§cInvalid line number!");
            return;
        }

        StringBuilder builder = new StringBuilder("");
        ItemStack item = Minecraft.getMinecraft().thePlayer.getHeldItem();
        if (item == null || item.getItem() == null) {
            UChat.chat("§cYou must be holding an item!");
            return;
        }
        line = line - 1;

        for (int i = 1; i < args.length; i++) {
            builder.append(args[i] + " ");
        }

        String loreToBeSet = builder.toString().trim();

        List<String> oldLore = getLore(item);
        List<String> newLore = new ArrayList<String>(oldLore);
        loreToBeSet = C.translate(loreToBeSet);
        try {
            newLore.set(line, loreToBeSet);
        } catch (IndexOutOfBoundsException e) {
            for (int i = oldLore.size() - 1; i < line; i++) { // Expand new lore to proper size
                newLore.add("");
            }

            newLore.set(line, loreToBeSet);
        }

        setLore(item, newLore);
        setCreativeAction(item, Minecraft.getMinecraft().thePlayer.inventory.currentItem);
        MUtils.chat("§aLore line " + (line + 1) + " set to: " + loreToBeSet);
    }
}
