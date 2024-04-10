package llc.redstone.hysentials.handlers.chat.modules.bwranks;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import llc.redstone.hysentials.config.hysentialMods.ChatConfig;
import llc.redstone.hysentials.config.hysentialMods.FormattingConfig;
import llc.redstone.hysentials.config.hysentialMods.HousingConfig;
import llc.redstone.hysentials.cosmetic.CosmeticUtilsKt;
import llc.redstone.hysentials.schema.HysentialsSchema;
import llc.redstone.hysentials.util.*;
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.command.HysentialsCommand;
import llc.redstone.hysentials.handlers.chat.ChatReceiveModule;
import llc.redstone.hysentials.handlers.redworks.BwRanks;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static llc.redstone.hysentials.handlers.redworks.BwRanksUtils.*;

public class BWSReplace implements ChatReceiveModule {
    public static List<String> diagnostics = new ArrayList<>();
    public Pattern pattern = Pattern.compile("<(.+):(.+)>");

    @SubscribeEvent()
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        if (!BUtils.isHypixelOrSBX()) return;
        if (event.type != 0 && event.type != 1) return;
        if (PartyFormatter.checkMessage(event)) {
            event.setCanceled(true);
            return;
        }
        if (GuildFormatter.checkMessage(event)) {
            event.setCanceled(true);
            return;
        }
        if (checkRegexes(event)) {
            event.setCanceled(true);
            return;
        }
        boolean futuristic = Hysentials.INSTANCE.getConfig().formattingConfig.enabled && FormattingConfig.fancyRankInChat && FormattingConfig.futuristicRanks;
        long start0 = System.currentTimeMillis();
        String message = event.message.getFormattedText();
        diagnostics.add("New message received: " + message);
        IChatComponent chatComponent = event.message;
        List<IChatComponent> siblings = event.message.getSiblings();
        HysentialsCommand.messages.add(chatComponent.toString());

        diagnostics.add("Looking for players in the map...");
        long start1 = System.currentTimeMillis();
        HashMap<String, UUID> users = new HashMap<>();

        Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().forEach(playerInfo -> {
            users.put(playerInfo.getGameProfile().getName(), playerInfo.getGameProfile().getId());
        });
        diagnostics.add("Took " + (System.currentTimeMillis() - start1) + "ms to find players in the map.");


        HypixelRanks hRank = null;
        BlockWAPIUtils.Rank blockwRank = null;
        UUID uuidBold = null;
        for (IChatComponent sibling : siblings) {
            String s = sibling.getFormattedText();
            diagnostics.add("Checking sibling: " + s);

            if (HousingConfig.removeAsterisk && (s.startsWith("§r§7* ") || s.startsWith("§7* "))) {
                s = s.replaceFirst("(§7|§r§7)\\* ", "");
            }

            if (futuristic) {
                if (hRank != null && (s.startsWith("§7: ") || s.startsWith("§f: "))) {
                    s = s.replaceFirst("§[7f]: ", hRank.getChat() + ": " + italic(uuidBold) + bold(uuidBold));
//                    hRank = null;
                    diagnostics.add("Added chat formatting to sibling. (Hypixel)");
                }
                if (blockwRank != null && (s.startsWith("§7: ") || s.startsWith("§f: "))) {
                    s = s.replaceFirst("§[7f]: ", blockwRank.getChatColor() + ": " + italic(uuidBold) + bold(uuidBold));
//                    blockwRank = null;
                    diagnostics.add("Added chat formatting to sibling. (BlockW)");
                }
                if ((s.startsWith("§7") || s.startsWith("§f")) && blockwRank != null && uuidBold != null) {
                    s = s.replaceFirst("(§7|§f)", blockwRank.getChatColor() + italic(uuidBold) + bold(uuidBold));
                }
                Pattern p = Pattern.compile("(§[0-9a-fk-or])\\[(\\d+)(.)] ");
                Pattern p2 = Pattern.compile("(§[0-9a-fk-or])\\[(.+)(.)§[0-9a-fk-or]] ");
                //We don't care about multicolored ones just yet
                Matcher m1 = p.matcher(s);
                Matcher m2 = p2.matcher(s);
                boolean found = m1.find();
                if (found || m2.find()){
                    Matcher m = found ? m1 : m2;
                    String color = C.toHex(m.group(1)).replace("#", "");
                    String num = C.removeColor(m.group(2));
                    String symbol = m.group(3);
                    if (ChatConfig.levelPrefixColors) {
                        s = s.replaceFirst("(§[0-9a-fk-or])\\[(\\d+)(.)] ", "<" + color + ":" + num + ">");
                    } else {
                        //Else then §8
                        String hex = ChatConfig.defaultLevelColor.getHex();
                        hex = hex.replace("#", "");
                        s = s.replaceFirst("(§[0-9a-fk-or])\\[(\\d+)(.)] ", "<" + hex + ":" + num + ">");
                    }
                }
            }
            diagnostics.add("Starting loop for players...");
            long start2 = System.currentTimeMillis();
            for (Map.Entry<String, UUID> user : users.entrySet()) {
                String name = user.getKey();
                UUID uuid = user.getValue();
                if (uuid.equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))) continue;
                try {
                    BlockWAPIUtils.Rank rank = BlockWAPIUtils.getRank(uuid);
                    String regex1 = "\\[[A-Za-z§0-9+]+] " + name;
                    String regex2 = "(§r§7|§7)" + name;
                    if (rank != null && rank != BlockWAPIUtils.Rank.DEFAULT) {
                        String replacement = (rank.getPrefix() + name);
                        if (futuristic) {
                            replacement = (rank.getPlaceholder() + name);
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
                            if (uuidBold == null) uuidBold = user.getValue();
                            s = s.replaceAll("\\[[A-Za-z§0-9+]+] " + name, replacement).replaceAll("§[7f]: ", rank.getChatColor() + ": " + italic(uuidBold) + bold(uuidBold));
                            diagnostics.add("Used regex1 to replace " + name + " with " + replacement + " (BlockW)");
                        } else if (Pattern.compile(regex2).matcher(s).find(0)) {
                            blockwRank = rank;
                            if (uuidBold == null) uuidBold = user.getValue();
                            s = s.replaceAll("(§r§7|§7)" + name, replacement).replaceAll("§[7f]: ", rank.getChatColor() + ": " + italic(uuidBold) + bold(uuidBold));
                            diagnostics.add("Used regex2 to replace " + name + " with " + replacement + " (BlockW)");
                        }
//                  else if (Pattern.compile(regex3).matcher(s).find(0)) {
//                        didSomething = true;
//                        s = s.replaceAll("[a-f0-9§]{2}" + name, replacement).replaceAll("§[7f]: ", rank.getChat() + ": ");
//                    }
                    } else {
                        if (futuristic) {
                            Matcher m1 = Pattern.compile(regex1).matcher(s);
                            Matcher m2 = Pattern.compile(regex2).matcher(s);

                            if (m1.find(0)) {
                                Object[] replacement = getReplacement(m1.group(0).split(" ")[0], name, uuid, false, false);
                                HypixelRanks r = (HypixelRanks) replacement[1];
                                hRank = r;
                                if (uuidBold == null) uuidBold = user.getValue();
                                s = s.replace(m1.group(0), "§f" + replacement[0].toString()).replaceAll("§[7f]:", r.getChat() + ":" + italic(uuidBold) + bold(uuidBold));
                                HysentialsCommand.messages.add(sibling.getFormattedText() + " -> " + s);
                                diagnostics.add("Used regex1 to replace " + name + " with " + replacement[0].toString() + " (Hypixel)");
                            }
                            if (m2.find(0)) {
                                Object[] replacement = getReplacement("§7", name, uuid, LocrawUtil.INSTANCE.getLocrawInfo().getGameType().equals(LocrawInfo.GameType.SKYBLOCK), false);
                                HypixelRanks r = (HypixelRanks) replacement[1];
                                hRank = r;
                                if (uuidBold == null) uuidBold = user.getValue();
                                s = s.replace(m2.group(0), "§f" + replacement[0].toString()).replaceAll("§[7f]:", r.getChat() + ":" + italic(uuidBold) + bold(uuidBold));
                                HysentialsCommand.messages.add(sibling.getFormattedText() + " -> " + s);
                                diagnostics.add("Used regex2 to replace " + name + " with " + replacement[0].toString() + " (Hypixel)");
                            }
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
            diagnostics.add("Looped through players in " + (System.currentTimeMillis() - start2) + "ms");
            UTextComponent textComponent = new UTextComponent(sibling);
            textComponent.setText(s);
            if (siblings.indexOf(sibling) != -1) {
                siblings.set(siblings.indexOf(sibling), textComponent);
            }
            Pattern actionRegex = pattern;
            Matcher actionMatcher = actionRegex.matcher(s.replaceAll("§r", ""));
            diagnostics.add("Looking for actions...");
            if (actionMatcher.find()) {
                diagnostics.add("Found action!");
                String finalS = s;
                String name = actionMatcher.group(1);
                int i = name.lastIndexOf("<");
                name = name.substring(i + 1);
                int startM = finalS.indexOf("<" + name + ":" + actionMatcher.group(2) + ">");
                diagnostics.add("Action: " + name + ":" + actionMatcher.group(2));

                HysentialsSchema.Action action = BlockWAPIUtils.getAction(name, actionMatcher.group(2));

                if (action != null) {
                    String mes = finalS.substring(0, startM);
                    String mes2 = finalS.substring(startM + ("<" + name + ":" + actionMatcher.group(2) + ">").length());
                    boolean isFunction = action.getAction().getType().equals("function");
                    UTextComponent messageComponent = new UTextComponent("&b" + action.getAction().getCreator() + "'s " + action.getAction().getName() + " &7(Copy)");
                    messageComponent.setHover(HoverEvent.Action.SHOW_TEXT, "§eClick to copy the action");
                    messageComponent.setClick(ClickEvent.Action.SUGGEST_COMMAND, action.getId());
                    UTextComponent all = new UTextComponent("");
                    all.appendSibling(new UTextComponent(mes)).appendSibling(messageComponent).appendSibling(new UTextComponent(mes2));
                    siblings.set(siblings.indexOf(sibling), all);
                }
            }
        }
        if (uuidBold != null) {
            BwRanks.replacementMap.put(chatComponent.getFormattedText().replaceAll("§r", ""), new DuoVariable<>(uuidBold, chatComponent.getFormattedText().replaceAll("§r", "")));
        }
        Minecraft.getMinecraft().thePlayer.addChatMessage(chatComponent);
        event.setCanceled(true);
        diagnostics.add("Finished in " + (System.currentTimeMillis() - start0) + "ms");
    }

    private static String bold(UUID id) {
        boolean bold = id != null && CosmeticUtilsKt.equippedCosmetic(id, "bold messages");
        return bold ? "§l" : "";
    }

    private static String italic(UUID id) {
        boolean italic = id != null && CosmeticUtilsKt.equippedCosmetic(id, "italic messages");
        return italic ? "§o" : "";
    }

    public boolean checkRegexes(ClientChatReceivedEvent event) {
        boolean futuristic = Hysentials.INSTANCE.getConfig().formattingConfig.enabled && FormattingConfig.fancyRankInChat && FormattingConfig.futuristicRanks;
        if (!futuristic) return false;
        String msg = event.message.getFormattedText().replaceAll("§r", "");

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
                        UTextComponent textComponent = new UTextComponent(":to: <#d96cb2>" + name + "<#e3a8ce>" + ": " + message);
                        textComponent.setClick(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + name + " ");
                        textComponent.chat();
                    } else {
                        UTextComponent textComponent = new UTextComponent(":from: <#d96cb2>" + name + "<#e3a8ce>" + ": " + message);
                        textComponent.setClick(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + name + " ");
                        textComponent.chat();
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
                type = GuildFormatter.prefix() + "&2";
            }
            UChat.chat(type + " " + name.substring(2) + " §e" + action + ".");
            event.setCanceled(true);
            return true;
        }


        return false;
    }
}
