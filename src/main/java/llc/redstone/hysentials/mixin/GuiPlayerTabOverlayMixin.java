package llc.redstone.hysentials.mixin;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import llc.redstone.hysentials.handlers.lobby.TabChanger;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;
import java.util.List;

@Mixin(GuiPlayerTabOverlay.class)
public class GuiPlayerTabOverlayMixin {


    /*
    Put Hysentials ranks above hypixel ranks
     */
    @Redirect(method = "renderPlayerlist", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Ordering;sortedCopy(Ljava/lang/Iterable;)Ljava/util/List;"))
    private List<NetworkPlayerInfo> renderPlayerlist(Ordering<NetworkPlayerInfo> instance, Iterable<NetworkPlayerInfo> elements) {
        return TabChanger.ordering.sortedCopy(elements);
    }
}
