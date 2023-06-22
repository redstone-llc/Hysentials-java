package cc.woverflow.hysentials.mixin;

import cc.woverflow.hysentials.util.HypixelAPIUtils;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class NetHandlerPlayClientMixin_GameChecker {
    @Inject(method = "handleDisplayScoreboard", at = @At("TAIL"))
    private void checkScoreboard(S3DPacketDisplayScoreboard packetIn, CallbackInfo ci) {
        HypixelAPIUtils.checkGameModes();
    }
}
