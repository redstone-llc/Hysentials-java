package cc.woverflow.hysentials.mixin;

import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hysentials.Hysentials;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiNewChat.class)
public abstract class GuiNewChatMixin_LocrawHider {
    @Shadow @Final private Minecraft mc;

    @Shadow @Final private List<ChatLine> chatLines;

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private float percentComplete;

    @Shadow public abstract void deleteChatLine(int id);

    @Inject(method = "printChatMessageWithOptionalDeletion", at = @At("HEAD"), cancellable = true)
    private void handlePrintChatMessage(IChatComponent chatComponent, int chatLineId, CallbackInfo ci) {
        handleHytilsMessage(chatComponent, chatLineId, mc.ingameGUI.getUpdateCounter(), false, ci);
    }

    @Inject(method = "setChatLine", at = @At("HEAD"), cancellable = true)
    private void handleSetChatLine(IChatComponent chatComponent, int chatLineId, int updateCounter, boolean displayOnly, CallbackInfo ci) {
        handleHytilsMessage(chatComponent, chatLineId, updateCounter, displayOnly, ci);
    }

    private void handleHytilsMessage(IChatComponent chatComponent, int chatLineId, int updateCounter, boolean displayOnly, CallbackInfo ci) {
        if (HypixelUtils.INSTANCE.isHypixel() && chatComponent.getUnformattedTextForChat().startsWith("{") && chatComponent.getUnformattedTextForChat().endsWith("}")) {
            percentComplete = 1.0F;
            if (chatLineId != 0) {
                deleteChatLine(chatLineId);
            }
            if (!displayOnly) {
                chatLines.add(0, new ChatLine(updateCounter, chatComponent, chatLineId));
                while (this.chatLines.size() > (100 + (Hysentials.INSTANCE.isPatcher ? 32667 : 0)))
                {
                    this.chatLines.remove(this.chatLines.size() - 1);
                }
            }
            ci.cancel();
        }
    }


}
