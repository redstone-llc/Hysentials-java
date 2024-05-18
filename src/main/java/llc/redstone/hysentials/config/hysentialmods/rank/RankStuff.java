package llc.redstone.hysentials.config.hysentialmods.rank;

import cc.polyfrost.oneconfig.config.core.OneColor;
import org.jetbrains.annotations.NotNull;

public class RankStuff {
    public OneColor nametagColor;
    public OneColor chatMessageColor;

    public RankStuff(OneColor nametagColor, OneColor chatMessageColor) {
        this.nametagColor = nametagColor;
        this.chatMessageColor = chatMessageColor;
    }


    public @NotNull OneColor getNametagColor() {
        return nametagColor;
    }
    public @NotNull OneColor getChatMessageColor() {
        return chatMessageColor;
    }
}
