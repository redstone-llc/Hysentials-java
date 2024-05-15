package llc.redstone.hysentials.handlers.chat.modules.bwranks;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.Multithreading;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.config.hysentialMods.ChatConfig;
import llc.redstone.hysentials.config.hysentialMods.FormattingConfig;
import llc.redstone.hysentials.handlers.redworks.BwRanksUtils;
import llc.redstone.hysentials.util.MUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuildFormatter {
    private static Pattern guildPattern = Pattern.compile("§2Guild > (§[0-9a-fk-or].+ |§[0-9a-fk-or])(.+)()§[f7]: (.+)");
    private static Pattern guildPattern2 = Pattern.compile("§2Guild > (§[0-9a-fk-or].+ |§[0-9a-fk-or])(.+)( §[0-9a-fk-or].+)§[f7]: (.+)");

    public static String prefix() {
        String prefix = ChatConfig.guildPrefix;
        if (prefix.startsWith(":") && prefix.endsWith(":") && !FormattingConfig.fancyRendering()) {
            prefix = "§2Guild > ";
        }
        return prefix;
    }

    public static boolean checkMessage(ClientChatReceivedEvent event) {
        if (!Hysentials.INSTANCE.getConfig().chatConfig.enabled || !ChatConfig.guildFormatting) return false;

        Matcher guildMatcher = guildPattern.matcher(event.message.getFormattedText().replaceAll("§r", ""));
        Matcher guildMatcher2 = guildPattern2.matcher(event.message.getFormattedText().replaceAll("§r", ""));
        boolean guildMatch = guildMatcher.find();
        boolean guildMatch2 = guildMatcher2.find();
        if (guildMatch2 || guildMatch) {
            Matcher m = guildMatch2 ? guildMatcher2 : guildMatcher;
            try {
                String name = m.group(2);
                String prefix = m.group(1);
                String tag = (ChatConfig.guildChatSuffix ? m.group(3) : "");
                String message = m.group(4);

                String hex = (FormattingConfig.hexRendering()) ? "<#c6f5c0>" : "";
                if (ChatConfig.guildChatPrefix) {
                    UChat.chat(prefix() + "&2" + BwRanksUtils.getReplace(prefix, name, null) + tag + hex + ": " + message);
                } else {
                    UChat.chat(prefix() + "&2" + name + tag + hex + ": " + message);
                }
            } catch (Exception e) {
                System.out.println("Error in guild chat\n" + e.getMessage());
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
