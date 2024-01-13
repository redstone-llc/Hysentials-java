package llc.redstone.hysentials.htsl;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import llc.redstone.hysentials.util.MUtils;

public enum Enchantment {
    PROTECTION(10),
    FIRE_PROTECTION(11),
    FEATHER_FALLING(12),
    BLAST_PROTECTION(13),
    PROJECTILE_PROTECTION(14),
    RESPIRATION(15),
    AQUA_AFFINITY(16),
    THORNS(19),
    DEPTH_STRIDER(20),
    SHARPNESS(21),
    SMITE(22),
    BANE_OF_ARTHROPODS(23),
    KNOCKBACK(24),
    FIRE_ASPECT(25),
    LOOTING(28),
    EFFICIENCY(29),
    SILK_TOUCH(30),
    UNBREAKING(31),
    FORTUNE(32),
    POWER(33),
    PUNCH(34),
    FLAME(10, true),
    INFINITY(11, true);

    private final int slot;
    private final boolean isOnSecondPage;

    Enchantment(int slot) {
        this.slot = slot;
        this.isOnSecondPage = false;
    }

    Enchantment(int slot, boolean isOnSecondPage) {
        this.slot = slot;
        this.isOnSecondPage = isOnSecondPage;
    }

    public int getSlot() {
        return slot;
    }

    public boolean isOnSecondPage() {
        return isOnSecondPage;
    }

    public static Enchantment fromString(String enchantment) {
        switch (enchantment) {
            case "protection": return PROTECTION;
            case "fire_protection": return FIRE_PROTECTION;
            case "feather_falling": return FEATHER_FALLING;
            case "blast_protection": return BLAST_PROTECTION;
            case "projectile_protection": return PROJECTILE_PROTECTION;
            case "respiration": return RESPIRATION;
            case "aqua_affinity": return AQUA_AFFINITY;
            case "thorns": return THORNS;
            case "depth_strider": return DEPTH_STRIDER;
            case "sharpness": return SHARPNESS;
            case "smite": return SMITE;
            case "bane_of_arthropods": return BANE_OF_ARTHROPODS;
            case "knockback": return KNOCKBACK;
            case "fire_aspect": return FIRE_ASPECT;
            case "looting": return LOOTING;
            case "efficiency": return EFFICIENCY;
            case "silk_touch": return SILK_TOUCH;
            case "unbreaking": return UNBREAKING;
            case "fortune": return FORTUNE;
            case "power": return POWER;
            case "punch": return PUNCH;
            case "flame": return FLAME;
            case "infinity": return INFINITY;
            default: {
                UChat.chat("&3[HTSL] &6Warning: Unknown enchantment \"" + enchantment + "\"");
                return PROTECTION;
            }
        }
    }
}
