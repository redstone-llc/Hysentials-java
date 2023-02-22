package cc.woverflow.hysentials.mixin;

import cc.woverflow.hysentials.handlers.chat.modules.bwranks.BwRanksChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.regex.Pattern;

import static cc.woverflow.hysentials.handlers.imageicons.ImageIcon.shiftRenderText;

/*
This class is used for image symbols in chat.
 */
@Mixin(value = GuiNewChat.class, priority = Integer.MIN_VALUE)
public abstract class RenderChatMixin extends Gui {
    @Shadow
    @Final
    private Minecraft mc;
    @Shadow
    @Final
    private List<ChatLine> drawnChatLines;

    @Shadow
    private int scrollPos;
    @Shadow
    @Final
    private List<ChatLine> chatLines;
    @Shadow
    private boolean isScrolled;

    @Shadow
    public abstract boolean getChatOpen();

    @Shadow
    public abstract float getChatScale();

    @Shadow
    public abstract int getLineCount();

    @Shadow
    public abstract int getChatWidth();

    @Redirect(method = "drawChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"))
    private int redirectDrawString(FontRenderer instance, String text, float x, float y, int color) {
        text = BwRanksChat.onMessageReceivedS(text);
        shiftRenderText(instance, text, x, y, color, true);
        return 0;
    }

    private static final Pattern pattern = Pattern.compile(":([a-z_\\-0-9]+):", 2);

}
