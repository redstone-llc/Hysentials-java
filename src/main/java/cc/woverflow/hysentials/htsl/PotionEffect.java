package cc.woverflow.hysentials.htsl;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.woverflow.hysentials.util.MUtils;

public enum PotionEffect {
    SPEED(10),
    SLOWNESS(11),
    HASTE(12),
    MINING_FATIGUE(13),
    STRENGTH(14),
    INSTANT_HEALTH(15),
    INSTANT_DAMAGE(16),
    JUMP_BOOST(19),
    NAUSEA(20),
    REGENERATION(21),
    RESISTANCE(22),
    FIRE_RESISTANCE(23),
    WATER_BREATHING(24),
    INVISIBILITY(25),
    BLINDNESS(28),
    NIGHT_VISION(29),
    HUNGER(30),
    WEAKNESS(31),
    POISON(32),
    WITHER(33),
    HEALTH_BOOST(34),
    ABSORPTION(10, true);

    private final int slot;
    private final boolean isOnSecondPage;

    PotionEffect(int slot) {
        this.slot = slot;
        this.isOnSecondPage = false;
    }

    PotionEffect(int slot, boolean isOnSecondPage) {
        this.slot = slot;
        this.isOnSecondPage = isOnSecondPage;
    }

    public int getSlot() {
        return slot;
    }

    public boolean isOnSecondPage() {
        return isOnSecondPage;
    }

    public static PotionEffect fromString(String effect) {
        switch (effect.toLowerCase().replace(" ", "_")) {
            case "speed": return SPEED;
            case "slowness": return SLOWNESS;
            case "haste": return HASTE;
            case "mining_fatigue": return MINING_FATIGUE;
            case "strength": return STRENGTH;
            case "instant_health": return INSTANT_HEALTH;
            case "instant_damage": return INSTANT_DAMAGE;
            case "jump_boost": return JUMP_BOOST;
            case "nausea": return NAUSEA;
            case "regeneration": return REGENERATION;
            case "resistance": return RESISTANCE;
            case "fire_resistance": return FIRE_RESISTANCE;
            case "water_breathing": return WATER_BREATHING;
            case "invisibility": return INVISIBILITY;
            case "blindness": return BLINDNESS;
            case "night_vision": return NIGHT_VISION;
            case "hunger": return HUNGER;
            case "weakness": return WEAKNESS;
            case "poison": return POISON;
            case "wither": return WITHER;
            case "health_boost": return HEALTH_BOOST;
            case "absorption": return ABSORPTION;
            default: {
                UChat.chat("&3[HTSL] &6Warning: Unknown effect \"" + effect + "\"");
                return SPEED;
            }
        }
    }
}
