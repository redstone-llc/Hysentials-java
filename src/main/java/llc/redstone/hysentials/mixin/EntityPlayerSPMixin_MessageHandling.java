package llc.redstone.hysentials.mixin;

import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.guis.container.containers.club.ClubDashboard;
import llc.redstone.hysentials.guis.container.Container;
import llc.redstone.hysentials.handlers.chat.modules.misc.GlobalChatStuff;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Mixin(value = EntityPlayerSP.class, priority = Integer.MAX_VALUE)
public class EntityPlayerSPMixin_MessageHandling {
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void handleSentMessages(String message, CallbackInfo ci) {
        Hysentials.lastMessage = message;
        if (hysentials$sendMessage(message) == null) ci.cancel();

        if (GlobalChatStuff.GlobalSendMessage.onMessageSend(message) == null) ci.cancel();
        if (Hysentials.INSTANCE.getChatHandler().handleSentMessage(message) == null) ci.cancel();
        if (ClubDashboard.handleSentMessage(message) == null) ci.cancel();

    }

    @Unique
    private String hysentials$sendMessage(String message) {
        if (message.startsWith("/")) return message;
        for (Map.Entry<UUID, Function<String, String>> entry : Container.chatRequests.entrySet()) {
            if (entry.getValue() != null) {
                Container.chatRequests.remove(entry.getKey());
                return entry.getValue().apply(message);
            }
        }
        return message;
    }
}
