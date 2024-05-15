package llc.redstone.hysentials.hook;

import cc.polyfrost.oneconfig.utils.MathUtils;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.config.HysentialsConfig;
import net.minecraft.entity.boss.BossStatus;

public class BossStatusHook {
    private static float lerpedBossHealth;
    private static long percentSetTime;

    public static void onStatusSet() {
        lerpedBossHealth = getPercent();
        percentSetTime = System.currentTimeMillis();
    }

    public static float getPercent() {
        long l = System.currentTimeMillis() - percentSetTime;
        float f = MathUtils.clamp((float)l / HysentialsConfig.bossBarHUD.lerpSpeed, 0.0f, 1.0f);
        return lerp(f, lerpedBossHealth, BossStatus.healthScale);
    }

    private static float lerp(float pct, float start, float end) {
        return start + pct * (end - start);
    }
}
