package llc.redstone.hysentials.handlers.chat.modules.bwranks;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.config.hysentialMods.ChatConfig;
import llc.redstone.hysentials.handlers.redworks.BwRanksUtils;
import llc.redstone.hysentials.util.MUtils;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static llc.redstone.hysentials.handlers.redworks.BwRanksUtils.removePrefix;

public class PartyFormatter {

    private static boolean lineSeperator = false;
    private static final List<String> middle = new ArrayList<>();
    private static final List<UTextComponent> components = new ArrayList<>();

    private static final Pattern checkPrefix = Pattern.compile(":.+: ");

    private static final Pattern partyNotif = Pattern.compile("(§[0-9a-fk-or].+ |§[0-9a-fk-or])(.+) (§ehas been removed from the party.|§ejoined the party.|§chas already been invited to the party.|§ehas disconnected, they have §c5 §eminutes to rejoin before they are removed from the party.|§ewas removed from your party because they disconnected.|§ehas disbanded the party!|§aenabled Private Game|§cdisabled Private Game|§cis already in the party.)");
    private static final Pattern partyInvite = Pattern.compile("(§[0-9a-fk-or].+ |§[0-9a-fk-or])(.+) §einvited (§[0-9a-fk-or].+ |§[0-9a-fk-or])(.+) §eto the party! They have §c60 §eseconds to accept.");
    private static final Pattern partyJoined = Pattern.compile("(§eYou have joined )(.+)('s §eparty!)");
    private static final Pattern partyTransfer = Pattern.compile("(§eThe party was transferred to )(.+)( §eby )(.+)");
    private static final Pattern partyPromote1 = Pattern.compile("(.+)(§e has promoted )(.+)( §eto Party Leader)");
    private static final Pattern partyPromote2 = Pattern.compile("(.+)(§eis now a Party Moderator)");
    private static final Pattern partyPromote3 = Pattern.compile("(.+)(§e has promoted )(.+)( §eto Party Moderator)");
    private static final Pattern partyDemote = Pattern.compile("(.+)(§e has demoted )(.+)( §eto Party Member)");

    public static void reset() {
        lineSeperator = false;
        middle.clear();
        components.clear();
    }

    public static boolean checkMessage(ClientChatReceivedEvent event) {
        if (!Hysentials.INSTANCE.getConfig().chatConfig.enabled || !ChatConfig.partyFormatting) return false;

        String msg = event.message.getFormattedText().replaceAll("§r", "");
        List<String> sibilings = event.message.getSiblings().stream().map(e -> e.getFormattedText().replaceAll("§r", "")).collect(Collectors.toList());
        if (msg.startsWith("§9§m-----------------------------------------------------") && msg.contains("\n")) {
            if (sibilings.get(2).equals("§ehas invited you to join their party!\n")) {
                msg = msg.replace("§9§m-----------------------------------------------------\n", "");
                msg = msg.replace("§9§m-----------------------------------------------------", "");

                String prefixName = msg.split("\n")[1].split(" §e")[0];
                String name = removePrefix(prefixName);

                UChat.chat(ChatConfig.partyPrefix + "&9" + name + " &einvited you to join their party!");

                String command = "/party accept " + name;
                String hover = "Click to run\n" + command;
                UTextComponent comp1 = new UTextComponent("");
                comp1.setClick(ClickEvent.Action.RUN_COMMAND, command);
                comp1.setHover(HoverEvent.Action.SHOW_TEXT, hover);

                UTextComponent comp = new UTextComponent("&eYou have §c60 §eseconds to accept. §6Click here to join.");

                comp.setClick(ClickEvent.Action.RUN_COMMAND, command);
                comp.setHover(HoverEvent.Action.SHOW_TEXT, hover);
                comp1.appendSibling(comp);

                if (ChatConfig.hideLines) {
                    comp1.chat();
                } else {
                    UChat.chat("§9§m-----------------------------------------------------");
                    comp1.chat();
                    UChat.chat("§9§m-----------------------------------------------------");
                }
                event.setCanceled(true);
                reset();
                return true;
            }
        }
        if (!lineSeperator && (msg.equals("§9§m-----------------------------------------------------") || msg.equals("§9§m-----------------------------"))) {
            lineSeperator = true;
            return true;
        }
        if (lineSeperator && !(msg.equals("§9§m-----------------------------------------------------") || msg.equals("§9§m-----------------------------"))) {
            middle.add(msg.replace("§r", ""));
            components.add(new UTextComponent(event.message));
            return true;
        }

        if (lineSeperator) {
            lineSeperator = false;
            if (middle.size() == 0) {
                UChat.chat("§9§m-----------------------------------------------------");
                UChat.chat("§9§m-----------------------------------------------------");
            }

            String message = middle.get(0);

            if (handleSpecialMessages(message, event)) {
                return true;
            }

            Matcher pNMatcher = partyNotif.matcher(message);
            Matcher pIMatcher = partyInvite.matcher(message);
            Matcher pJMatcher = partyJoined.matcher(message);
            Matcher pTMatcher = partyTransfer.matcher(message);
            Matcher pP1Matcher = partyPromote1.matcher(message);
            Matcher pP2Matcher = (middle.size() > 1) ? partyPromote2.matcher(middle.get(1)) : partyPromote2.matcher("");
            Matcher pP3Matcher = partyPromote3.matcher(message);
            Matcher pDMatcher = partyDemote.matcher(message);

            if (pIMatcher.find()) {
                sendMessage("&9" + pIMatcher.group(2) + " &einvited &9" + pIMatcher.group(4) + " &eto the party! &7(60s to accept)");
            } else if (pJMatcher.find()) {
                sendMessage("&eYou have joined &9" + removePrefix(pJMatcher.group(2)) + "'s &eparty!");
            } else if (pTMatcher.find()) {
                sendMessage("&9" + removePrefix(pTMatcher.group(4)) + " &etransferred the party to &9" + removePrefix(pTMatcher.group(2)) + "&e!");
            } else if (pP1Matcher.find()) {
                sendMessage("&9" + removePrefix(pP1Matcher.group(1)) + " &ehas promoted &9" + removePrefix(pP1Matcher.group(3)) + " &eto Party Leader");
            } else if (pP2Matcher.find()) {
                sendMessage("&9" + removePrefix(pP2Matcher.group(1)) + " &eis now a Party Moderator");
            } else if (pP3Matcher.find()) {
                sendMessage("&9" + removePrefix(pP3Matcher.group(1)) + " &ehas promoted &9" + removePrefix(pP3Matcher.group(3)) + " &eto Party Moderator");
            } else if (pDMatcher.find()) {
                sendMessage("&9" + removePrefix(pDMatcher.group(1)) + " &ehas demoted &9" + removePrefix(pDMatcher.group(3)) + " &eto Party Member");
            } else if (pNMatcher.find()) {
                handlePartyNotification(pNMatcher.group(2), pNMatcher.group(3));
            } else {
                handleDefaultCase();
            }

            reset();
            return true;
        }

        Pattern partyPattern = Pattern.compile("§9Party §8> (§[0-9a-fk-or].+ |§[0-9a-fk-or])(.+)§[f7]: (.+)");

        Matcher partyMatcher = partyPattern.matcher(event.message.getFormattedText().replaceAll("§r", ""));
        if (partyMatcher.find()) {
            String name = partyMatcher.group(2);
            String prefix = "";
            if (ChatConfig.partyChatPrefix) {
                prefix = BwRanksUtils.getReplace(partyMatcher.group(1), name, null);
            }
            String mes = partyMatcher.group(3);
            MUtils.chat(ChatConfig.partyPrefix + "&9" + prefix + name + "<#c0def5>" + ": " + mes);

            return true;
        }
        return false;
    }

    private static void sendMessage(String s) {
        if (ChatConfig.hideLines) {
            UChat.chat(ChatConfig.partyPrefix + s);
        } else {
            UChat.chat("§9§m-----------------------------------------------------");
            UChat.chat(ChatConfig.partyPrefix + s);
            UChat.chat("§9§m-----------------------------------------------------");
        }
    }

    private static boolean handleSpecialMessages(String message, ClientChatReceivedEvent event) {
        switch (message) {
            case "§cThe party was disbanded because all invites expired and the party was empty.":
            case "§eYou left the party.":
            case "§cYou can't party warp into limbo!":
            case "§cThe party is now muted.":
            case "§cThis party is currently muted.":
            case "§cThere are no offline players to remove.":
            case "§cYou are not in a party right now.":
                sendMessage(message);
                event.setCanceled(true);
                return true;
            default:
                return false;
        }
    }

    private static void handlePartyNotification(String playerName, String notification) {
        switch (notification) {
            case "§ehas been removed from the party.":
                sendMessage("&9" + playerName + " &ehas been removed from the party.");
                break;
            case "§ejoined the party.":
                sendMessage("&9" + playerName + " &ejoined the party.");
                break;
            case "§chas already been invited to the party.":
                sendMessage("&c" + playerName + " &ehas already been invited to the party.");
                break;
            case "§ehas disconnected, they have §c5 §eminutes to rejoin before they are removed from the party.":
                sendMessage("&9" + playerName + " &edisconnected. &7(5 mins until kick)");
                break;
            case "§ewas removed from your party because they disconnected.":
                sendMessage("&9" + playerName + " &ewas removed from your party because they disconnected.");
                break;
            case "§ehas disbanded the party!":
                sendMessage("&9" + playerName + " &ehas disbanded the party!");
                break;
            case "§aenabled Private Game":
                sendMessage("&9" + playerName + " &aenabled Private Game");
                break;
            case "§cdisabled Private Game":
                sendMessage("&9" + playerName + " &cdisabled Private Game");
                break;
            case "§cis already in the party.":
                sendMessage("&c" + playerName + " &cis already in the party.");
                break;
        }
    }

    private static void handleDefaultCase() {
        UChat.chat("§9§m-----------------------------------------------------");
        for (UTextComponent s : components) {
            s.chat();
        }
        UChat.chat("§9§m-----------------------------------------------------");
    }
}
