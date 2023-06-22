package cc.woverflow.hysentials.handlers.chat.modules.bwranks;

import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.woverflow.hysentials.util.MUtils;
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UMessage;
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.command.HysentialsCommand;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.handlers.chat.ChatReceiveModule;
import cc.woverflow.hysentials.handlers.redworks.BwRanks;
import cc.woverflow.hysentials.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lwjgl.Sys;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cc.woverflow.hysentials.guis.actionLibrary.ActionViewer.toList;
import static cc.woverflow.hysentials.handlers.redworks.BwRanksUtils.*;

public class BWSReplace implements ChatReceiveModule {
    @SubscribeEvent()
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        if (!HypixelUtils.INSTANCE.isHypixel()) return;
        if (event.type != 0 && event.type != 1) return;
        if (checkRegexes(event)) {
            event.setCanceled(true);
            return;
        }
        String message = event.message.getFormattedText();
        IChatComponent chatComponent = event.message;
        List<IChatComponent> siblings = event.message.getSiblings();
        HysentialsCommand.messages.add(chatComponent.toString());

        HashMap<String, UUID> users = new HashMap<>();

        Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().forEach(playerInfo -> {
            users.put(playerInfo.getGameProfile().getName(), playerInfo.getGameProfile().getId());
        });
        Multithreading.runAsync(() -> {
            HypixelRanks hRank = null;
            BlockWAPIUtils.Rank blockwRank = null;
            for (IChatComponent sibling : siblings) {
                String s = sibling.getFormattedText();

                if (HysentialsConfig.removeAsterisk && (s.startsWith("§r§7* ") || s.startsWith("§7* "))) {
                    s = s.replaceFirst("(§7|§r§7)\\* ", "");
                }
                if (hRank != null && (s.startsWith("§7: ") || s.startsWith("§f: "))) {
                    s = s.replaceFirst("§7: ", hRank.getChat() + ": ").replaceFirst("§f: ", hRank.getChat() + ": ");
                    hRank = null;
                }
                if (blockwRank != null && (s.startsWith("§7: ") || s.startsWith("§f: "))) {
                    s = s.replaceFirst("§7: ", blockwRank.getChat() + ": ").replaceFirst("§f: ", blockwRank.getChat() + ": ");
                    blockwRank = null;
                }
                for (Map.Entry<String, UUID> user : users.entrySet()) {
                    String name = user.getKey();
                    UUID uuid = user.getValue();
                    if (uuid.equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))) continue;
                    try {
                        BlockWAPIUtils.Rank rank = null;
                        if (Hysentials.INSTANCE.getOnlineCache().getOnlinePlayers().containsKey(uuid)) {
                            try {
                                rank = BlockWAPIUtils.Rank.valueOf(Hysentials.INSTANCE.getOnlineCache().getRankCache().get(uuid).toUpperCase());
                            } catch (Exception ignored) {
                                rank = BlockWAPIUtils.Rank.DEFAULT;
                            }
                        }
                        String regex1 = "\\[[A-Za-z§0-9+]+] " + name;
                        String regex2 = "(§r§7|§7)" + name;
                        if (rank != null && rank != BlockWAPIUtils.Rank.DEFAULT) {
                            String replacement = (rank.getPrefix(name) + name + (getPlus(uuid)));
                            if (HysentialsConfig.futuristicRanks) {
                                replacement = (rank.getPlaceholder() + name + (getPlus(uuid)));
                                if (!BwRanks.hasRank) {
                                    replacement = (rank.getPlaceholder() + name);
                                }
                            }
                            if (!BwRanks.hasRank) {
                                replacement = (rank.getColor() + name);
                            }
                            Matcher m1 = Pattern.compile(regex1).matcher(s);
                            if (m1.find(0)) {
                                blockwRank = rank;
                                s = s.replaceAll("\\[[A-Za-z§0-9+]+] " + name, replacement).replaceAll("§[7f]: ", rank.getChat() + ": ");
                            } else if (Pattern.compile(regex2).matcher(s).find(0)) {
                                blockwRank = rank;
                                s = s.replaceAll("(§r§7|§7)" + name, replacement).replaceAll("§[7f]: ", rank.getChat() + ": ");
                            }
//                  else if (Pattern.compile(regex3).matcher(s).find(0)) {
//                        didSomething = true;
//                        s = s.replaceAll("[a-f0-9§]{2}" + name, replacement).replaceAll("§[7f]: ", rank.getChat() + ": ");
//                    }
                        } else {
                            Matcher m1 = Pattern.compile(regex1).matcher(s);
                            Matcher m2 = Pattern.compile(regex2).matcher(s);
                            if (m1.find(0)) {
                                Object[] replacement = getReplacement(m1.group(0).split(" ")[0], name, uuid, false);
                                HypixelRanks r = (HypixelRanks) replacement[1];
                                hRank = r;
                                s = s.replace(m1.group(0), "§f" + replacement[0].toString()).replaceAll("§[7f]:", r.getChat() + ":");
                                HysentialsCommand.messages.add(sibling.getFormattedText() + " -> " + s);

                            }
                            if (m2.find(0)) {
                                Object[] replacement = getReplacement("§7", name, uuid, LocrawUtil.INSTANCE.getLocrawInfo().getGameType().equals(LocrawInfo.GameType.SKYBLOCK));
                                HypixelRanks r = (HypixelRanks) replacement[1];
                                hRank = r;

                                s = s.replace(m2.group(0), "§f" + replacement[0].toString()).replaceAll("§[7f]:", r.getChat() + ":");
                                HysentialsCommand.messages.add(sibling.getFormattedText() + " -> " + s);

                            }
//                    if (m3.find(0) && (!BwRanks.hasRank)) {
//                        didSomething = true;
//                        Object[] replacement = getReplacement(m3.group(0).substring(0, 2), name, uuid, true);
//                        HypixelRanks r = (HypixelRanks) replacement[1];
//                        s = s.replaceAll("[a-f0-9§]{2}" + name, replacement[0].toString()).replaceAll("§[7f]:", r.getChat() + ":");
//                    }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                UTextComponent textComponent = new UTextComponent(sibling);
                textComponent.setText(s);
                if (siblings.indexOf(sibling) != -1) {
                    siblings.set(siblings.indexOf(sibling), textComponent);
                }

                Pattern actionRegex = Pattern.compile("<(.+):(.+)>");
                Matcher actionMatcher = actionRegex.matcher(s.replaceAll("§r", ""));
                if (actionMatcher.find()) {
                    String name = actionMatcher.group(1);
                    int i = name.lastIndexOf("<");
                    name = name.substring(i + 1);
                    int start = s.indexOf("<" + name + ":" + actionMatcher.group(2) + ">");
                    String a = NetworkUtils.getString("https://hysentials.redstone.llc/api/actions");
                    JSONObject json = new JSONObject(a);
                    JSONArray actions = json.getJSONArray("actions");
                    String finalName = name;
                    JSONObject action = (JSONObject) toList(actions).stream().filter(o -> {
                        JSONObject object = ((JSONObject) o);
                        return object.getJSONObject("action").getString("creator").equals(finalName) && object.getString("id").equals(actionMatcher.group(2));
                    }).findFirst().orElse(null);

                    if (action != null) {
                        String mes = s.substring(0, start);
                        String mes2 = s.substring(start + ("<" + name + ":" + actionMatcher.group(2) + ">").length());
                        boolean isFunction = action.getJSONObject("action").getString("type").equals("function");
                        UTextComponent messageComponent = new UTextComponent("&b" + action.getJSONObject("action").getString("creator") + "'s " + capitalizeFirst(action.getJSONObject("action").getString("type")) + (isFunction ? " " : " Action ") + "&7(Copy)");
                        messageComponent.setHover(HoverEvent.Action.SHOW_TEXT, "§eClick to copy the action");
                        messageComponent.setClick(ClickEvent.Action.SUGGEST_COMMAND, action.getString("id"));
                        UTextComponent all = new UTextComponent("");
                        all.appendSibling(new UTextComponent(mes)).appendSibling(messageComponent).appendSibling(new UTextComponent(mes2));
                        siblings.set(siblings.indexOf(sibling), all);
//                    all.chat();
//                    event.setCanceled(true);
//                    return;
                    }
                }
            }
            Minecraft.getMinecraft().thePlayer.addChatMessage(chatComponent);
        });
        event.setCanceled(true);
    }

    public static String capitalizeFirst(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    boolean lineSeperator = false;
    List<String> middle = new ArrayList<>();
    List<UTextComponent> components = new ArrayList<>();

    public boolean checkRegexes(ClientChatReceivedEvent event) {
//        HysentialsCommand.messages.add(event.message.getFormattedText().replaceAll("§r", ""));
        String msg = event.message.getFormattedText().replaceAll("§r", "");
        if (!lineSeperator && msg.equals("§9§m-----------------------------------------------------")) {
            lineSeperator = true;
            return true;
        }
        if (lineSeperator && !msg.equals("§9§m-----------------------------------------------------")) {
            middle.add(msg.replace("§r", ""));
            components.add(new UTextComponent(event.message));
            return true;
        }
        if (lineSeperator && msg.equals("§9§m-----------------------------------------------------")) {
            lineSeperator = false;
            if (middle.size() == 0) {
                MUtils.chat("§9§m-----------------------------------------------------");
                MUtils.chat("§9§m-----------------------------------------------------");
            }
            if (middle.get(0).equals("§cThe party was disbanded because all invites expired and the party was empty.")) {
                MUtils.chat(":party: &9Party &ewas disbanded because all invites expired and the party was empty.");
                event.setCanceled(true);
                middle.clear();
                return true;
            }
            Pattern partyNotif = Pattern.compile("(§[0-9a-fk-or].+ |§[0-9a-fk-or])(.+) (§ehas been removed from the party.|§ejoined the party.|§chas already been invited to the party.|§ehas disconnected, they have §c5 §eminutes to rejoin before they are removed from the party.|§ewas removed from your party because they disconnected.|§ehas disbanded the party!)");
            Matcher pNMatcher = partyNotif.matcher(middle.get(0));

            Pattern partyInvite = Pattern.compile("(§[0-9a-fk-or].+ |§[0-9a-fk-or])(.+) §einvited (§[0-9a-fk-or].+ |§[0-9a-fk-or])(.+) §eto the party! They have §c60 §eseconds to accept.");
            Matcher pIMatcher = partyInvite.matcher(middle.get(0));
            if (pIMatcher.find()) {
                MUtils.chat(":party: &9" + pIMatcher.group(2) + " &einvited &9" + pIMatcher.group(4) + " &eto the party! &7(60s to accept)");
            } else if (pNMatcher.find()) {
                switch (pNMatcher.group(3)) {
                    case "§ehas been removed from the party.": {
                        MUtils.chat(":party: &9" + pNMatcher.group(2) + " &ehas been removed from the party.");
                        break;
                    }
                    case "§ejoined the party.": {
                        MUtils.chat(":party: &9" + pNMatcher.group(2) + " &ejoined the party.");
                        break;
                    }
                    case "§chas already been invited to the party.": {
                        MUtils.chat(":party: &c" + pNMatcher.group(2) + " &ehas already been invited to the party.");
                        break;
                    }
                    case "§ehas disconnected, they have §c5 §eminutes to rejoin before they are removed from the party.": {
                        MUtils.chat(":party: &9" + pNMatcher.group(2) + " &edisconnected. &7(5 mins until kick)");
                    }
                    case "§ewas removed from your party because they disconnected.": {
                        MUtils.chat(":party: &9" + pNMatcher.group(2) + " &ewas removed from your party because they disconnected.");
                    }
                    case "§ehas disbanded the party!": {
                        MUtils.chat(":party: &9" + pNMatcher.group(2) + " &ehas disbanded the party!");
                    }
                }
            } else {
                UChat.chat("§9§m-----------------------------------------------------");
                for (UTextComponent s : components) {
                    s.chat();
                }
                UChat.chat("§9§m-----------------------------------------------------");
            }
            components.clear();
            middle.clear();
            return true;
        }

        Pattern partyPattern = Pattern.compile("§9Party §8> (§[0-9a-fk-or].+ |§[0-9a-fk-or])(.+)§[f7]: (.+)");

        Matcher partyMatcher = partyPattern.matcher(event.message.getFormattedText().replaceAll("§r", ""));
        if (partyMatcher.find()) {
            Multithreading.runAsync(() -> {
                try {
                    String name = partyMatcher.group(2);
                    String prefix = partyMatcher.group(1);
                    prefix = prefix;
                    String message = partyMatcher.group(3);
//                    MUtils.chat(":party: " + replacement[0].toString() + chat + ": " + message);
                    MUtils.chat(":party: &9" + name + "<#c0def5>" + ": " + message);
                } catch (Exception e) {
                    System.out.println("Error in party chat\n" + e.getMessage());
                }
            });
            return true;
        }

        Pattern guildPattern = Pattern.compile("§2Guild > (§[0-9a-fk-or].+ |§[0-9a-fk-or])(.+)()§[f7]: (.+)");
        Pattern guildPattern2 = Pattern.compile("§2Guild > (§[0-9a-fk-or].+ |§[0-9a-fk-or])(.+)( §[0-9a-fk-or].+)§[f7]: (.+)");
        Matcher guildMatcher = guildPattern.matcher(event.message.getFormattedText().replaceAll("§r", ""));
        Matcher guildMatcher2 = guildPattern2.matcher(event.message.getFormattedText().replaceAll("§r", ""));
        boolean guildMatch = guildMatcher.find();
        boolean guildMatch2 = guildMatcher2.find();
        if (guildMatch2 || guildMatch) {
            Matcher m = guildMatch2 ? guildMatcher2 : guildMatcher;
            Multithreading.runAsync(() -> {
                try {
                    String name = m.group(2);
                    String prefix = m.group(1);
                    String tag = m.group(3);
                    String message = m.group(4);
//                    MUtils.chat(":guild: " + replacement[0].toString() + chat + ": " + message);
                    MUtils.chat(":guild: &2" + name + tag + "<#c6f5c0>" + ": " + message);
                } catch (Exception e) {
                    System.out.println("Error in guild chat\n" + e.getMessage());
                    e.printStackTrace();
                }
            });
            return true;
        }

        Pattern messageRegex = Pattern.compile("§d(To|From) (§[0-9a-fk-or].+ |§[0-9a-fk-or])(.+)§7: (.+)");
        Matcher messageMatcher = messageRegex.matcher(event.message.getFormattedText().replaceAll("§r", ""));

        if (messageMatcher.find()) {
            Multithreading.runAsync(() -> {
                try {
                    String toFrom = messageMatcher.group(1);
                    String name = messageMatcher.group(3);
                    String prefix = messageMatcher.group(2);
                    String message = C.removeColor(messageMatcher.group(4));

                    if (toFrom.equals("To")) {
                        MUtils.chat(":to: <#d96cb2>" + name + "<#e3a8ce>" + ": " + message);
                    } else {
                        MUtils.chat(":from: <#d96cb2>" + name + "<#e3a8ce>" + ": " + message);
                    }
                    event.setCanceled(true);

                } catch (Exception e) {
                    System.out.println("Error in message chat\n" + e.getMessage());
                }
            });
            return true;
        }

        Pattern joinNotification = Pattern.compile("(§aFriend|§2Guild) > (.+) §e(left|joined|)\\.");
        Matcher jnMatcher = joinNotification.matcher(event.message.getFormattedText().replaceAll("§r", ""));

        if (jnMatcher.find()) {
            String type = jnMatcher.group(1);
            String name = jnMatcher.group(2);
            String action = jnMatcher.group(3);

            if (type.equals("§aFriend")) {
                type = ":friend:<#79d930>";
            } else {
                type = ":guild:&2";
            }
            MUtils.chat(type + " " + name.substring(2) + " §e" + action + ".");
            event.setCanceled(true);
            return true;
        }


        return false;
    }

    private static List<String> getUsernames(List<String> lines) {
        List<String> usernames = new ArrayList<>();
        for (String line : lines) {
            String[] split = line.split("● ");
            if (split.length > 1) {
                for (String s : split) {
                    if (s.contains("]")) {
                        String username = s.split("]")[1];
                        if (username.startsWith(" ")) {
                            username = username.substring(1);
                        }
                        usernames.add(ChatColor.Companion.stripControlCodes(username));
                    } else {
                        String[] split2 = s.split(" §r§7");
                        if (split2.length == 1) {
                            continue;
                        }
                        String username = split2[1];
                        if (username.startsWith(" ")) {
                            username = username.substring(1);
                        }
                        usernames.add(ChatColor.Companion.stripControlCodes(username));
                    }
                }
            }
        }
        return usernames;
    }
}
