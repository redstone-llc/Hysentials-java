package cc.polyfrost.oneconfig.internal.hud;

import cc.polyfrost.oneconfig.hud.Hud;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HudCore {
    public static final ConcurrentHashMap<Map.Entry<Field, Object>, Hud> huds = new ConcurrentHashMap<>();
    public static boolean editing = false;
}
