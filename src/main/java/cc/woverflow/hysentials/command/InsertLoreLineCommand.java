package cc.woverflow.hysentials.command;

import cc.woverflow.hysentials.util.MUtils;
import cc.woverflow.hysentials.guis.container.GuiItem;
import cc.woverflow.hysentials.util.C;
import cc.woverflow.hysentials.util.MUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InsertLoreLineCommand extends CommandBase {
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "insertloreline";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/insertloreline <line> <value>";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("insertll", "ill");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 2) {
            MUtils.chat("§cUsage: /insertloreline <line> <value>");
            return;
        }
        int line;
        try {
            line = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            MUtils.chat("§cInvalid line number!");
            return;
        }
        if (line < 1) {
            MUtils.chat("§cInvalid line number!");
            return;
        }

        ItemStack item = Minecraft.getMinecraft().thePlayer.getHeldItem();
        if (item == null || item.getItem() == null) {
            MUtils.chat("§cYou must be holding an item!");
            return;
        }
        item = item.copy();
        StringBuilder textArguments = new StringBuilder("");

        for (int i = 1; i < args.length; i++) {
            textArguments.append(args[i] + " ");
        }

        String lineToInsert = textArguments.toString().trim();

        List<String> newLore = new ArrayList<String>();

        lineToInsert = C.translate(lineToInsert);

        if (GuiItem.getLore(item).size() != 0) {
            List<String> oldLore = GuiItem.getLore(item);

            if (oldLore.size() < line) {
                MUtils.chat("§cLine number is too high!");
                return;
            }

            line = line - 1;

            for (int i = 0; i < oldLore.size(); i++) {
                if (i == line) {
                    newLore.add(lineToInsert);
                    newLore.add(oldLore.get(i));
                } else {
                    newLore.add(oldLore.get(i));
                }
            }

            GuiItem.setLore(item, newLore);
            RenameCommand.setCreativeAction(item, Minecraft.getMinecraft().thePlayer.inventory.currentItem);
            MUtils.chat("§aSuccessfully inserted line §e" + (line + 1) + "§a!");
        } else {
            MUtils.chat("§cItem must have lore!");
        }
    }
}
