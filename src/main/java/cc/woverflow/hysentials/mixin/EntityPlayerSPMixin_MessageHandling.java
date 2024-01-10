package cc.woverflow.hysentials.mixin;

import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.guis.club.ClubDashboard;
import cc.woverflow.hysentials.guis.container.Container;
import cc.woverflow.hysentials.handlers.chat.modules.misc.GlobalChatStuff;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

@Mixin(EntityPlayerSP.class)
public class EntityPlayerSPMixin_MessageHandling {
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void handleSentMessages(String message, CallbackInfo ci) {
        if (GlobalChatStuff.GlobalSendMessage.onMessageSend(message) == null) ci.cancel();
        if (Hysentials.INSTANCE.getChatHandler().handleSentMessage(message) == null) ci.cancel();
        if (ClubDashboard.handleSentMessage(message) == null) ci.cancel();

        if (hysentials$sendMessage(message) == null) ci.cancel();
    }

    @Unique
    private String hysentials$sendMessage(String message) {
        for (Map.Entry<UUID, Function<String, String>> entry : Container.chatRequests.entrySet()) {
            if (entry.getValue() != null) {
                Container.chatRequests.remove(entry.getKey());
                return entry.getValue().apply(message);
            }
        }
        return message;
    }
}
