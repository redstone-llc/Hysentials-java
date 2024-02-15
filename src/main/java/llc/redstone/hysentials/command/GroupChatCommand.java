package llc.redstone.hysentials.command;

import llc.redstone.hysentials.util.MUtils;
import cc.polyfrost.oneconfig.utils.commands.annotations.*;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.config.HysentialsConfig;
import llc.redstone.hysentials.handlers.groupchats.GroupChat;
import llc.redstone.hysentials.util.DuoVariable;
import llc.redstone.hysentials.websocket.Request;
import llc.redstone.hysentials.websocket.Socket;
import llc.redstone.hysentials.config.HysentialsConfig;
import net.minecraft.client.Minecraft;

@Command(value = "groupchat", aliases = {"grc", "group"}, customHelpMessage = {
    "§9§m                                                     ",
    "§aGroup Commands:",
    "§e/group join <name> §8- §bAccepts a group invitation",
    "§e/group chat <group> <chat message> §8- §bSend a chat message to your group chat channel.",
    "§e/group create <name> §8- §bCreates a group with the specified name.",
    "§e/group settings <group> <allInvite/silence/filter/private> <value> §8- §bChange group settings.",
    "§e/group demote <group> <player> §8- §bDemotes the player to the previous rank.",
    "§e/group disband <group> §8- §bDisbands the group.",
    "§e/group help §8- §bPrints this help message.",
    "§e/group invite <group> <player> §8- §bInvites the player to your group.",
    "§e/group kick <group> <player> <reason> §8- §bKicks the player from your group.",
    "§e/group leave <group> §8- §bLeaves your current group.",
    "§e/group promote <group> <player> §8- §bPromotes the player to the next rank.",
    "§e/group online <group> §8- §bShow the current online members of your group.",
    "§e/group rename <group> <name> §8- §bRenames the group.",
    "§e/group transfer <group> <player> §8- §bTransfers ownership of the group to another player.",
    "§e/group hide <group> §8- §bHide the group from displaying messages in chat.",
    "§e/group unhide <group> §8- §bAllow the group to display messages in chat.",
    "§e/group color <group> <color> §8- §bChange the prefix color of the group.",
    "§9§m                                                     "
})
public class GroupChatCommand {
    @SubCommand(description = "Join a group chat.", aliases = "join")
    public void join(String name) {
        Socket.CLIENT.sendText(new Request(
            "method", "groupJoin",
            "name", name,
            "username", Minecraft.getMinecraft().thePlayer.getName(),
            "serverId", Socket.serverId
        ).toString());
    }

    @SubCommand(description = "Chat in a group chat.", aliases = "chat")
    public void chat(String group, @Greedy String message) {
        Socket.CLIENT.sendText(new Request(
            "method", "groupChat",
            "name", group,
            "username", Minecraft.getMinecraft().thePlayer.getName(),
            "serverId", Socket.serverId,
            "message", message
        ).toString());
        Socket.awaiting.add(new DuoVariable<>("groupChat", jsonObject -> {
            if (jsonObject.has("displayName")) return;
            MUtils.chat(HysentialsConfig.chatPrefix + " " + jsonObject.getString("message"));
        }));
    }

    @SubCommand(description = "Create a group chat.", aliases = "create")
    public void create(String name) {
        Socket.CLIENT.sendText(new Request(
            "method", "groupCreate",
            "name", name,
            "owner", Minecraft.getMinecraft().thePlayer.getName(),
            "serverId", Socket.serverId
        ).toString());
        Socket.awaiting.add(new DuoVariable<>("groupCreate", jsonObject -> {
            MUtils.chat(HysentialsConfig.chatPrefix + " " + jsonObject.getString("message"));
        }));
    }

    @SubCommand(description = "Set the settings of the group.", aliases = {"settings", "s"})
    public void settings( String name, @Description(autoCompletesTo = {"allInvite", "silence", "filter", "private"}) String setting, @Description(autoCompletesTo = {"true", "false"}) String value) {
        Socket.CLIENT.sendText(new Request(
            "method", "groupSettings",
            "name", name,
            "username", Minecraft.getMinecraft().thePlayer.getName(),
            "setting", setting,
            "valuez", value,
            "serverId", Socket.serverId
        ).toString());
        Socket.awaiting.add(new DuoVariable<>("groupSettings", jsonObject -> {
            MUtils.chat(HysentialsConfig.chatPrefix + " " + jsonObject.getString("message"));
        }));
    }

    @SubCommand(description = "Demote a player in the group.", aliases = {"demote", "d"})
    public void demote(String name, String player) {
        Socket.CLIENT.sendText(new Request(
            "method", "groupDemote",
            "name", name,
            "username", Minecraft.getMinecraft().thePlayer.getName(),
            "player", player,
            "serverId", Socket.serverId
        ).toString());
        Socket.awaiting.add(new DuoVariable<>("groupDemote", jsonObject -> {
            MUtils.chat(HysentialsConfig.chatPrefix + " " + jsonObject.getString("message"));
        }));
    }

    @SubCommand(description = "Disband a group.", aliases = {"disband", "dis"})
    public void disband(String name) {
        Socket.CLIENT.sendText(new Request(
            "method", "groupDisband",
            "name", name,
            "username", Minecraft.getMinecraft().thePlayer.getName(),
            "serverId", Socket.serverId
        ).toString());
        Socket.awaiting.add(new DuoVariable<>("groupDisband", jsonObject -> {
            MUtils.chat(HysentialsConfig.chatPrefix + " " + jsonObject.getString("message"));
        }));
    }

    @SubCommand(description = "Invite a player to a group chat.", aliases = {"invite", "i"})
    public void invite(String name, String player) {
        Socket.CLIENT.sendText(new Request(
            "method", "groupInvite",
            "name", name,
            "username", Minecraft.getMinecraft().thePlayer.getName(),
            "player", player,
            "serverId", Socket.serverId
        ).toString());
        Socket.awaiting.add(new DuoVariable<>("groupInvite", jsonObject -> {
            MUtils.chat(HysentialsConfig.chatPrefix + " " + jsonObject.getString("message"));
        }));
    }

    @SubCommand(description = "Kick a player from a group chat.", aliases = {"kick", "k"})
    public void kick(String name, String player, String reason) {
        Socket.CLIENT.sendText(new Request(
            "method", "groupKick",
            "name", name,
            "username", Minecraft.getMinecraft().thePlayer.getName(),
            "player", player,
            "reason", reason,
            "serverId", Socket.serverId
        ).toString());
        Socket.awaiting.add(new DuoVariable<>("groupKick", jsonObject -> {
            MUtils.chat(HysentialsConfig.chatPrefix + " " + jsonObject.getString("message"));
        }));
    }

    @SubCommand(description = "Leave a group chat.", aliases = {"leave", "l"})
    public void leave(String name) {
        Socket.CLIENT.sendText(new Request(
            "method", "groupLeave",
            "name", name,
            "username", Minecraft.getMinecraft().thePlayer.getName(),
            "serverId", Socket.serverId
        ).toString());
        Socket.awaiting.add(new DuoVariable<>("groupLeave", jsonObject -> {
            MUtils.chat(HysentialsConfig.chatPrefix + " " + jsonObject.getString("message"));
        }));
    }

    @SubCommand(description = "Promote a player in the group.", aliases = {"promote", "p"})
    public void promote(String name, String player) {
        Socket.CLIENT.sendText(new Request(
            "method", "groupPromote",
            "name", name,
            "username", Minecraft.getMinecraft().thePlayer.getName(),
            "player", player,
            "serverId", Socket.serverId
        ).toString());
        Socket.awaiting.add(new DuoVariable<>("groupPromote", jsonObject -> {
            MUtils.chat(HysentialsConfig.chatPrefix + " " + jsonObject.getString("message"));
        }));
    }

    @SubCommand(description = "Display online players in a group.", aliases = {"online", "o"})
    public void online(String name) {
        Socket.CLIENT.sendText(new Request(
            "method", "groupOnline",
            "name", name,
            "username", Minecraft.getMinecraft().thePlayer.getName(),
            "serverId", Socket.serverId
        ).toString());
        Socket.awaiting.add(new DuoVariable<>("groupOnline", jsonObject -> {
            MUtils.chat(jsonObject.getString("message"));
        }));
    }

    @SubCommand(description = "Renames a group.", aliases = {"rename", "r"})
    public void rename(String name, String newName) {
        Socket.CLIENT.sendText(new Request(
            "method", "groupRename",
            "name", name,
            "username", Minecraft.getMinecraft().thePlayer.getName(),
            "newName", newName,
            "serverId", Socket.serverId
        ).toString());
        Socket.awaiting.add(new DuoVariable<>("groupRename", jsonObject -> {
            MUtils.chat(HysentialsConfig.chatPrefix + " " + jsonObject.getString("message"));
        }));
    }

    @SubCommand(aliases = {"transfer", "t"})
    public void transfer(String name, String player) {
        Socket.CLIENT.sendText(new Request(
            "method", "groupTransfer",
            "name", name,
            "username", Minecraft.getMinecraft().thePlayer.getName(),
            "player", player,
            "serverId", Socket.serverId
        ).toString());
        Socket.awaiting.add(new DuoVariable<>("groupTransfer", jsonObject -> {
            MUtils.chat(HysentialsConfig.chatPrefix + " " + jsonObject.getString("message"));
        }));
    }

    @SubCommand(aliases = {"hide", "h"})
    public void hide(String name) {
        Socket.CLIENT.sendText(new Request(
            "method", "groupHide",
            "name", name,
            "username", Minecraft.getMinecraft().thePlayer.getName(),
            "serverId", Socket.serverId
        ).toString());
        Socket.awaiting.add(new DuoVariable<>("groupHide", jsonObject -> {
            MUtils.chat(HysentialsConfig.chatPrefix + " " + jsonObject.getString("message"));
            if (Hysentials.INSTANCE.isChatting) {
                GroupChat.hideTab(name);
            }
        }));
    }

    @SubCommand(aliases = {"unhide", "uh"})
    public void unhide(String name) {
        Socket.CLIENT.sendText(new Request(
            "method", "groupUnhide",
            "name", name,
            "username", Minecraft.getMinecraft().thePlayer.getName(),
            "serverId", Socket.serverId
        ).toString());
        Socket.awaiting.add(new DuoVariable<>("groupUnhide", jsonObject -> {
            MUtils.chat(HysentialsConfig.chatPrefix + " " + jsonObject.getString("message"));
        }));
    }

    @SubCommand(description = "Set the color of the groups prefix.", aliases = "color")
    public void color( String name, String color) {
        Socket.CLIENT.sendText(new Request(
            "method", "groupColor",
            "name", name,
            "username", Minecraft.getMinecraft().thePlayer.getName(),
            "color", color,
            "serverId", Socket.serverId
        ).toString());
        Socket.awaiting.add(new DuoVariable<>("groupColor", jsonObject -> {
            MUtils.chat(HysentialsConfig.chatPrefix + " " + jsonObject.getString("message"));
        }));
    }
}
