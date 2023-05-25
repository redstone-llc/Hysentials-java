package cc.woverflow.hysentials.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(value = GuiNewChat.class, priority = Integer.MIN_VALUE)
public abstract class GuiNewChatMixin_GuildListHide {
    @Shadow
    @Final
    private Minecraft mc;

    @Shadow
    @Final
    private List<ChatLine> chatLines;

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private float percentComplete;

    @Shadow
    public abstract void deleteChatLine(int id);

    @Shadow
    protected abstract void setChatLine(IChatComponent chatComponent, int chatLineId, int updateCounter, boolean displayOnly);

    @Redirect(method = "setChatLine", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiUtilRenderComponents;splitText(Lnet/minecraft/util/IChatComponent;ILnet/minecraft/client/gui/FontRenderer;ZZ)Ljava/util/List;"))
    private List<IChatComponent> onSetChatLine(IChatComponent k, int s1, FontRenderer chatcomponenttext, boolean l, boolean chatcomponenttext2) {
        return GuiUtilRenderComponents.splitText(k, s1, Minecraft.getMinecraft().fontRendererObj, l, chatcomponenttext2);
    }
}
