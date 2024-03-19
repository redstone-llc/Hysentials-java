package llc.redstone.hysentials.command;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.Multithreading;
import llc.redstone.hysentials.schema.HysentialsSchema;
import llc.redstone.hysentials.util.BUtils;
import llc.redstone.hysentials.util.MUtils;
import cc.polyfrost.oneconfig.utils.commands.annotations.*;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.config.HysentialsConfig;
import llc.redstone.hysentials.handlers.groupchats.GroupChat;
import llc.redstone.hysentials.util.DuoVariable;
import llc.redstone.hysentials.utils.GroupUtils;
import llc.redstone.hysentials.websocket.Request;
import llc.redstone.hysentials.websocket.Socket;
import llc.redstone.hysentials.config.HysentialsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GroupChatCommand extends CommandBase {
    String selectedGroup = null;
    boolean confirmedLeave = false;
    boolean confirmedDelete = false;
    @Override
    public String getCommandName() {
        return "groupchat";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/groupchat";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("group");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            UChat.chat(getHelp());
            return;
        }
        String command = args[0];
        if (command.equalsIgnoreCase("help")) {
            UChat.chat(getHelp());
            return;
        }
        args = Arrays.copyOfRange(args, 1, args.length);
        switch (command) {
            case "create": {
                if (args.length < 1) {
                    UChat.chat("§cUsage: /groupchat create [name]");
                    return;
                }
                String name = args[0];
                sendAction("create",
                    "name", name
                );
                break;
            }
            case "invite": {
                if (args.length < 2) {
                    if (selectedGroup == null) {
                        UChat.chat("§cUsage: /groupchat invite [name] <player>");
                        return;
                    }
                    String player = args[0];
                    sendAction("invite",
                        "groupId", selectedGroup,
                        "player", player
                    );
                    return;
                }
                String name = args[0];
                HysentialsSchema.Group group = GroupUtils.getGroupByName(name);
                if (group == null) {
                    Hysentials.INSTANCE.sendMessage("§cGroup not found");
                    return;
                }
                String player = args[1];
                sendAction("invite",
                    "groupId", group.getId(),
                    "player", player
                );
                break;
            }
            case "join": {
                if (args.length < 1) {
                    UChat.chat("§cUsage: /groupchat join <groupid>");
                    return;
                }
                String id = args[0];
                sendAction("join",
                    "groupId", id,
                    "inviter", "todo",
                    "inviterName", "todo"
                );
                break;
            }
            case "leave": {
                if (args.length < 21) {
                    if (selectedGroup == null) {
                        UChat.chat("§cUsage: /groupchat leave [name]");
                        return;
                    }
                    if (confirmedLeave) {
                        confirmedLeave = false;
                        sendAction("leave",
                            "groupId", selectedGroup
                        );
                        selectedGroup = null;
                        return;
                    }
                    confirmedLeave = true;
                    UChat.chat("§cAre you sure you want to leave " + selectedGroup + "? Type §e/groupchat §cleave again to confirm.");
                    Multithreading.runAsync(() -> {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        confirmedLeave = false;
                    });
                    return;
                }
                String name = args[0];
                HysentialsSchema.Group group = GroupUtils.getGroupByName(name);
                if (group == null) {
                    Hysentials.INSTANCE.sendMessage("§cGroup not found");
                    return;
                }
                sendAction("leave",
                    "groupId", group.getId()
                );
                break;
            }
            case "delete": {
                if (args.length < 1) {
                    if (selectedGroup == null) {
                        UChat.chat("§cUsage: /groupchat delete [name]");
                        return;
                    }
                    if (confirmedDelete) {
                        confirmedDelete = false;
                        sendAction("delete",
                            "groupId", selectedGroup
                        );
                        selectedGroup = null;
                        return;
                    }
                    confirmedDelete = true;
                    UChat.chat("§cAre you sure you want to delete " + selectedGroup + "? Type §e/groupchat delete §cagain to confirm.");
                    Multithreading.runAsync(() -> {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        confirmedDelete = false;
                    });
                    return;
                }
                String name = args[0];
                HysentialsSchema.Group group = GroupUtils.getGroupByName(name);
                if (group == null) {
                    Hysentials.INSTANCE.sendMessage("§cGroup not found");
                    return;
                }
                if (confirmedDelete) {
                    confirmedDelete = false;
                    sendAction("delete",
                        "groupId", group.getId()
                    );
                    selectedGroup = null;
                    return;
                }
                confirmedDelete = true;
                UChat.chat("§cAre you sure you want to delete " + name + "? Type §e/groupchat delete <name> §cagain to confirm.");
                Multithreading.runAsync(() -> {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    confirmedDelete = false;
                });
                break;
            }
            case "list": {
                if (Socket.cachedGroups.isEmpty()) {
                    Hysentials.INSTANCE.sendMessage("§cYou are not in any group chats or the cache is empty");
                    return;
                }
                Hysentials.INSTANCE.sendMessage("&aYou are in the following group chats:");
                for (HysentialsSchema.Group group : Socket.cachedGroups) {
                    if (group.getOwner().equals(Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString()))
                        UChat.chat("   - &6" + group.getName() + " &7(" + group.getMembers().size() + " members)");
                    else
                        UChat.chat("   - &a" + group.getName() + " &7(" + group.getMembers().size() + " members)");
                }
                break;
            }
//            case "members": {
//                HysentialsSchema.Group group = GroupUtils.getGroupById(selectedGroup);
//                if (args.length > 0) {
//                    group = GroupUtils.getGroupByName(args[0]);
//                    if (group == null) {
//                        Hysentials.INSTANCE.sendMessage("§cGroup not found");
//                        return;
//                    }
//                }
//                Hysentials.INSTANCE.sendMessage("&aMembers of " + group.getName() + ":");
//                for (String member : group.getMembers()) {
//
//                }
//
//            }
            case "message":
            case "chat": {
                if (args.length < 1) {
                    UChat.chat("§cUsage: /groupchat chat <message>");
                    return;
                }
                if (selectedGroup == null) {
                    Hysentials.INSTANCE.sendMessage("§cYou do not have a group chat selected. Use §e/groupchat <name> §cto select a group chat.");
                    return;
                }
                String message = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
                sendAction("message",
                    "groupId", selectedGroup,
                    "message", message
                );
                break;
            }
            case "settings": {
                HysentialsSchema.Group group = GroupUtils.getGroupById(selectedGroup);
                if (args.length > 0) {
                    group = GroupUtils.getGroupByName(args[0]);
                    if (group == null) {
                        Hysentials.INSTANCE.sendMessage("§cGroup not found");
                        return;
                    }
                    args = Arrays.copyOfRange(args, 1, args.length);
                }
                if (args.length < 2) {
                    Hysentials.INSTANCE.sendMessage("§cUsage: /groupchat settings [name] <setting> <value>");
                    return;
                }
                String setting = args[0];
                if (Arrays.asList("allInvite", "saveLast").contains(setting)) {
                    Hysentials.INSTANCE.sendMessage("§cYou cannot change this setting");
                    return;
                }
                String value = args[1];
                sendAction("settings",
                    "groupId", group.getId(),
                    "setting", setting,
                    "value", value
                );
            }
            default: {
                HysentialsSchema.Group group = GroupUtils.getGroupByName(command);
                if (group == null) {
                    Hysentials.INSTANCE.sendMessage("§cGroup not found");
                    return;
                }
                selectedGroup = group.getId();
                Hysentials.INSTANCE.sendMessage("§aSelected group chat: " + group.getName());
                break;
            }
        }
    }

    public static void sendAction(String action, Object... args) {
        Socket.CLIENT.sendText(new Request(
            "method", "group",
            "action", action,
            "uuid", Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString(),
            "serverId", Socket.serverId,
            args
        ).toString());
    }

    private String getHelp() {
         return "§9&m                                                               \n" +
                "&aGroup Commands &c[BETA]\n" +
                "§6/group <name> §7- Select a group chat\n" +
                "§6/group help §7- Show this help message\n" +
                "§6/group create <name> §7- Create a new group chat\n" +
                "§6/group invite [name] <player> §7- Invite player to group\n" +
                "§6/group join <name> §7- Join a group chat\n" +
                "§6/group leave [name] §7- Leave a group chat\n" + //Require confirmation if name is not provided aka they have selected a group
                "§6/group delete [name] §7- Delete a group chat\n" + //Require confirmation
                "§6/group list §7- List all group chats that you are in\n" +
                "§6/group chat <message> §7- Send a message to a group\n" +
                "§9&m                                                               ";

    }
}
