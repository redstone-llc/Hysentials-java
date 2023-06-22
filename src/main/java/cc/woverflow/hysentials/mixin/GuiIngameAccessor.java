package cc.woverflow.hysentials.mixin;

import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiIngame.class)
public interface GuiIngameAccessor {

    @Accessor("displayedTitle")
    void setDisplayedTitle(String title);

    @Accessor("displayedSubTitle")
    void setDisplayedSubTitle(String subtitle);
}
