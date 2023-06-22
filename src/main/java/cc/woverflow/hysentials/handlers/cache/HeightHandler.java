package cc.woverflow.hysentials.handlers.cache;

import cc.polyfrost.oneconfig.events.event.LocrawEvent;
import cc.polyfrost.oneconfig.events.event.WorldLoadEvent;
import cc.polyfrost.oneconfig.libs.caffeine.cache.Cache;
import cc.polyfrost.oneconfig.libs.caffeine.cache.Caffeine;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.util.HypixelAPIUtils;
import com.google.gson.JsonObject;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class HeightHandler {
    public static HeightHandler INSTANCE = new HeightHandler();

    private boolean printException = true;
    private JsonObject jsonObject = null;
    private final AtomicInteger counter = new AtomicInteger(0);
    private final ThreadPoolExecutor POOL = new ThreadPoolExecutor(
        50, 50,
        0L, TimeUnit.SECONDS,
        new LinkedBlockingQueue<>(), (r) -> new Thread(
        r,
        String.format("%s Cache Thread (Handler %s) %s", Hysentials.MOD_NAME, getClass().getSimpleName(), counter.incrementAndGet())
    )
    );

    public final Cache<String, Integer> cache = Caffeine.newBuilder().executor(POOL).maximumSize(100).build();

    private int currentHeight = -2;

    public int getHeight() {
        if (currentHeight != -2) return currentHeight;
        if (LocrawUtil.INSTANCE.getLocrawInfo() == null || jsonObject == null || Hysentials.INSTANCE.getLobbyChecker().playerIsInLobby())
            return -1;
        try {
            LocrawInfo locraw = LocrawUtil.INSTANCE.getLocrawInfo();
            if (cc.woverflow.hysentials.util.HypixelAPIUtils.isBedwars) {
                if (locraw.getMapName() != null && !locraw.getMapName().trim().isEmpty()) {
                    String map = locraw.getMapName().toLowerCase(Locale.ENGLISH).replace(" ", "_");
                    if (jsonObject.getAsJsonObject("bedwars").has(map)) {
                        Integer cached = cache.getIfPresent(map);
                        if (cached == null) {
                            cache.put(map, (jsonObject.getAsJsonObject("bedwars").get(map).getAsInt()));
                            currentHeight = Objects.requireNonNull(cache.getIfPresent(map));
                            return currentHeight;
                        } else {
                            currentHeight = cached;
                            return cached;
                        }
                    }
                }
            } else if (HypixelAPIUtils.isBridge) {
                currentHeight = 100;
                return currentHeight;
            }
            currentHeight = -1;
            return -1;
        } catch (Exception e) {
            if (printException) {
                e.printStackTrace();
                printException = false;
            }
            return -1;
        }
    }


    public void initialize() {
        try {
            jsonObject = NetworkUtils.getJsonElement("https://maps.pinkulu.com").getAsJsonObject();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Subscribe
    public void onLocraw(LocrawEvent e) {
        currentHeight = -2;
        printException = true;
        getHeight();
    }

    @Subscribe
    public void onWorldLoad(WorldLoadEvent e) {
        currentHeight = -2;
        printException = true;
    }
}
