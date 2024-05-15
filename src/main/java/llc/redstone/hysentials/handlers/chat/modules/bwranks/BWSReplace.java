package llc.redstone.hysentials.handlers.chat.modules.bwranks;

import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.platform.Platform;
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
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static llc.redstone.hysentials.handlers.redworks.BwRanksUtils.*;

public class BWSReplace implements ChatReceiveModule {
    public Pattern pattern = Pattern.compile("<(.+):(.+)>");

    @SubscribeEvent()
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        if (!BUtils.isHypixelOrSBX()) return;
        if (event.type != 0 && event.type != 1) return;
        //These can probably moved around, but I cannot be bothered.
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

        IChatComponent chatComponent = event.message;
        List<IChatComponent> siblings = event.message.getSiblings();

        HashMap<String, UUID> users = new HashMap<>();

        Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().forEach(playerInfo -> {
            users.put(playerInfo.getGameProfile().getName(), playerInfo.getGameProfile().getId());
        });


        HypixelRanks hRank = null;
        BlockWAPIUtils.Rank blockwRank = null;
        String previousRankColor = null;
        UUID uuidBold = null;
        GlStateManager.disableAlpha();
        for (IChatComponent sibling : siblings) {
            String s = sibling.getFormattedText();
            if (futuristic) {
                //This is for the level prefix colors
                if (LocrawUtil.INSTANCE.getLocrawInfo() != null && !LocrawUtil.INSTANCE.getLocrawInfo().getGameType().equals(LocrawInfo.GameType.SKYBLOCK)) {
                    Pattern p = Pattern.compile("(§[0-9a-fk-or])\\[(\\d+)(.)] ");
                    Pattern p2 = Pattern.compile("(§[0-9a-fk-or])\\[(.+)(.)§[0-9a-fk-or]] ");
                    //We don't care about multicolored ones just yet
                    Matcher m1 = p.matcher(s);
                    Matcher m2 = p2.matcher(s);
                    boolean found = m1.find();
                    if (found || m2.find()) {
                        Matcher m = found ? m1 : m2;
                        String color = C.toHex(m.group(1)).replace("#", "");
                        String num = C.removeColor(m.group(2));
                        String symbol = m.group(3);
                        if (ChatConfig.levelPrefixColors) {
                            s = s.replaceFirst("(§[0-9a-fk-or])\\[(\\d+)(.)] ", "<" + color + ":" + num + ">");
                        } else {
                            //Else then §8
                            OneColor oneColor = ChatConfig.defaultLevelColor;
                            Color color1 = new Color(oneColor.getRed(), oneColor.getGreen(), oneColor.getBlue());

                            String buf = Integer.toHexString(color1.getRGB());
                            String hex = buf.substring(buf.length() - 6);

                            s = s.replaceFirst("(§[0-9a-fk-or])\\[(\\d+)(.)] ", "<" + hex + ":" + num + ">");
                        }
                    }
                }
            }


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
                        } else if (Pattern.compile(regex2).matcher(s).find(0)) {
                            blockwRank = rank;
                            if (uuidBold == null) uuidBold = user.getValue();
                            s = s.replaceAll("(§r§7|§7)" + name, replacement).replaceAll("§[7f]: ", rank.getChatColor() + ": " + italic(uuidBold) + bold(uuidBold));
                        }
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
                            }
                            if (m2.find(0)) {
                                Object[] replacement = getReplacement("§7", name, uuid, LocrawUtil.INSTANCE.getLocrawInfo().getGameType().equals(LocrawInfo.GameType.SKYBLOCK), false);
                                HypixelRanks r = (HypixelRanks) replacement[1];
                                hRank = r;
                                if (uuidBold == null) uuidBold = user.getValue();
                                s = s.replace(m2.group(0), "§f" + replacement[0].toString()).replaceAll("§[7f]:", r.getChat() + ":" + italic(uuidBold) + bold(uuidBold));
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (futuristic) {
                String rankColor = (hRank != null ? hRank.getChat() : blockwRank != null ? blockwRank.getChatColor() : "§7");
                if (rankColor.equals("§7") && previousRankColor != null) {
                    rankColor = previousRankColor;
                }
                previousRankColor = rankColor;
                s = rankColor + s;
                s = s.replaceFirst("§[7f]: ", rankColor + ": " + italic(uuidBold) + bold(uuidBold));
                if ((s.startsWith("§7") || s.startsWith("§f")) && !rankColor.equals("§7") && uuidBold != null) {
                    s = s.replaceFirst("(§7|§f)", rankColor + italic(uuidBold) + bold(uuidBold));
                }
                s = MessageFormatter.fixEmojiBug(s, rankColor);
                s = s.replaceAll("§r", "");
            }


            UTextComponent textComponent = new UTextComponent(sibling);
            textComponent.setText(s);
            if (siblings.indexOf(sibling) != -1) {
                siblings.set(siblings.indexOf(sibling), textComponent);
            }

            //I'm not too sure if this actually works or not
            Pattern actionRegex = pattern;
            Matcher actionMatcher = actionRegex.matcher(s.replaceAll("§r", ""));
            if (actionMatcher.find()) {
                String finalS = s;
                String name = actionMatcher.group(1);
                int i = name.lastIndexOf("<");
                name = name.substring(i + 1);
                int startM = finalS.indexOf("<" + name + ":" + actionMatcher.group(2) + ">");

                HysentialsSchema.Action action = BlockWAPIUtils.getAction(name, actionMatcher.group(2));

                if (action != null) {
                    String mes = finalS.substring(0, startM);
                    String mes2 = finalS.substring(startM + ("<" + name + ":" + actionMatcher.group(2) + ">").length());
                    UTextComponent messageComponent = new UTextComponent("&b" + action.getAction().getCreator() + "'s " + action.getAction().getName() + " &7(Copy)");
                    messageComponent.setHover(HoverEvent.Action.SHOW_TEXT, "§eClick to copy the action");
                    messageComponent.setClick(ClickEvent.Action.SUGGEST_COMMAND, action.getId());
                    UTextComponent all = new UTextComponent("");
                    all.appendSibling(new UTextComponent(mes)).appendSibling(messageComponent).appendSibling(new UTextComponent(mes2));
                    siblings.set(siblings.indexOf(sibling), all);
                }
            }
        }
        Minecraft.getMinecraft().thePlayer.addChatMessage(chatComponent);
        event.setCanceled(true);
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
        String msg = event.message.getFormattedText();
        if (HousingConfig.removeAsterisk && (msg.startsWith("§r§7* ") || msg.startsWith("§7* "))) {
            event.message = new UTextComponent(msg.replaceFirst("§r§7\\* ", "§7"));
            onMessageReceived(event);
            return true;
        }

        boolean futuristic = Hysentials.INSTANCE.getConfig().formattingConfig.enabled && FormattingConfig.fancyRankInChat && FormattingConfig.futuristicRanks;
        if (!futuristic) return false;
        msg = msg.replaceAll("§r", "");
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
