package llc.redstone.hysentials.handlers.chat.modules.bwranks;

import llc.redstone.hysentials.config.hysentialmods.FormattingConfig;

public class MessageFormatter {
    public static String replaceWhite(String message, String color) {
        if (FormattingConfig.hexRendering()) {
            return fixEmojiBug(message, color);
        }
        return message;
    }

    public static String fixEmojiBug(String message, String color) {
        if (FormattingConfig.hexRendering()) {
            return message.replaceAll("(§r§f|§f§f)([\\x00-\\x7F])", color + "$2");
        }
        return message;
    }
}
