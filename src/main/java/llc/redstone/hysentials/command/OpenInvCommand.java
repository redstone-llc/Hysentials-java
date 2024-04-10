package llc.redstone.hysentials.command;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import llc.redstone.hysentials.util.MUtils;
import llc.redstone.hysentials.guis.misc.PlayerInventory;
import llc.redstone.hysentials.handlers.sbb.SbbRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static llc.redstone.hysentials.command.InsertLoreLineCommand.processRedirect;

public class OpenInvCommand extends CommandBase {
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "openinv";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/openinv <player>";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("inv");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (SbbRenderer.housingScoreboard.getHousingName() == null) {
            if (processRedirect(this, args)) return;
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/openinv " + String.join(" ", args));
            return;
        }
        if (args.length == 0) {
            UChat.chat("&cUsage: /openinv <player>");
            return;
        }
        for (EntityPlayer player : Minecraft.getMinecraft().theWorld.playerEntities) {
            if (player.getName().equalsIgnoreCase(args[0])) {
                new PlayerInventory(player).open();
                return;
            }
        }
        UChat.chat("&cCouldn't find a player by " + args[0] + ", make sure they are within your render distance to view their inventory!");
    }
}
