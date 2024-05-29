package llc.redstone.hysentials.util;

import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.HysentialsUtilsKt;
import llc.redstone.hysentials.config.hysentialmods.FormattingConfig;
import llc.redstone.hysentials.config.hysentialmods.rank.RankStuff;
import llc.redstone.hysentials.cosmetic.CosmeticGui;
import llc.redstone.hysentials.cosmetics.AbstractCosmetic;
import llc.redstone.hysentials.cosmetics.Cosmetic;
import llc.redstone.hysentials.handlers.imageicons.ImageIcon;
import llc.redstone.hysentials.handlers.redworks.BwRanksUtils;
import llc.redstone.hysentials.schema.HysentialsSchema;
import llc.redstone.hysentials.websocket.Socket;
import com.google.gson.*;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BlockWAPIUtils {
    public static List<HysentialsSchema.Action> actions = new ArrayList<>();
    public static List<HysentialsSchema.Cosmetic> cosmetics = new ArrayList<>();
    public static HysentialsSchema.Club currentHousingsClub;

    /**
     * Update the actions and cosmetics caches.
     */
    public static void getOnline() {
        if (Socket.CLIENT == null || !Socket.CLIENT.isOpen()) return;
        try {
            JsonElement cosmetics = null;
            try {
                if (!(Minecraft.getMinecraft().currentScreen instanceof CosmeticGui)) {
                    cosmetics = NetworkUtils.getJsonElement(HysentialsUtilsKt.getHYSENTIALS_API() + "/cosmetic", true);
                }

                JsonElement a = NetworkUtils.getJsonElement(HysentialsUtilsKt.getHYSENTIALS_API() + "/actions", true);
                JsonObject json = a.getAsJsonObject();

                json.getAsJsonArray("actions").forEach(action -> {
                    HysentialsSchema.Action a1 = HysentialsSchema.Action.Companion.deserialize(action.getAsJsonObject());
                    actions.add(a1);
                });

                if (cosmetics == null) return;
                JsonObject object = cosmetics.getAsJsonObject();
                JsonArray array = object.getAsJsonArray("cosmetics");
                BlockWAPIUtils.cosmetics = new ArrayList<>();
                for (JsonElement cosmeticObj : array) {
                    HysentialsSchema.Cosmetic cosmetic = HysentialsSchema.Cosmetic.Companion.deserialize(cosmeticObj.getAsJsonObject());
                    BlockWAPIUtils.cosmetics.add(cosmetic);
                }

            } catch (Exception ignored) {
                ignored.printStackTrace();
            }

        } catch (Exception e) {
            return;
        }
    }

    public static void setCosmeticInstance() {
        for (HysentialsSchema.Cosmetic cosmetic : cosmetics) {
            for (Cosmetic c: AbstractCosmetic.cosmetics) {

            }
        }
    }

    public static boolean isOnline(UUID uuid) {
        return Socket.cachedUsers.containsKey(uuid.toString());
    }

    public static boolean hasCosmetic(UUID uuid, String cosmetic) {
        if (cosmetics == null) return false;
        return cosmetics.stream().anyMatch(c -> c.getUsers().contains(uuid.toString()) && c.getName().equals(cosmetic));
    }

    public static boolean equippedCosmetic(UUID uuid, String cosmetic) {
        if (cosmetics == null) return false;
        return cosmetics.stream().anyMatch(c -> c.getUsers().contains(uuid.toString()) && c.getEquipped().contains(cosmetic) && c.getName().equals(cosmetic));
    }

    public static List<HysentialsSchema.Cosmetic> getCosmetics() {
        if (cosmetics == null) return new ArrayList<>();
        return cosmetics;
    }

    public static List<HysentialsSchema.Cosmetic> getCosmetic(String type) {
        if (cosmetics == null) return new ArrayList<>();
        return cosmetics.stream().filter(c -> c.getType().equals(type)).collect(Collectors.toList());
    }

    public static String getRequest(String endpoint) {
        return HysentialsUtilsKt.getHYSENTIALS_API() + "/" + endpoint + "?key=" + Socket.serverId + "&uuid=" + Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString();
    }

    public static HysentialsSchema.Action getAction(String id, String creator) {
        if (actions == null) return null;
        return actions.stream().filter(a -> a.getId().equals(id) && a.getAction().getCreator().equals(creator)).findFirst().orElse(null);
    }

    public static Rank getRank(String uuid) {
        return getRank(UUID.fromString(uuid));
    }

    public static Rank getRank(UUID uuid) {
        Rank rank;
        try {
            rank = Rank.valueOf(Socket.cachedUsers.get(uuid.toString()).getRank().toUpperCase());
        } catch (Exception e) {
            rank = Rank.DEFAULT;
        }
        return rank;
    }

    public static String getUsername(UUID uuid) {
        return Socket.cachedUsers.get(uuid.toString()).getUsername();
    }

    public static boolean isWearingType(UUID uuid, String type) {
        try {
            return getCosmetics().stream().anyMatch(c -> c.getEquipped().contains(uuid.toString()) && (c.getSubType() == null ? c.getType() : c.getSubType()).equals(type));
        } catch (Exception e) {
            return false;
        }
    }

    public static String getDisplayName(HysentialsSchema.User user) {
        String prefix = "";
        if (BwRanksUtils.futuristicRanks(null)) {
            prefix = Rank.valueOF(user.getRank().toUpperCase()).getPlaceholder();
        } else {
            prefix = Rank.valueOF(user.getRank().toUpperCase()).getPrefix();
        }
        return prefix + user.getUsername() + (user.getHasPlus() ? " §6[+]" : "");
    }

    public enum Rank {
        ADMIN(6, "1", "§c[ADMIN] ", "§c", "admin"),
        TEAM(5, "5", "§6[TEAM] ", "§6", "team"),
        MOD(4, "2", "§2[MOD] ", "§2", "mod"),
        HELPER(3, "3", "§9[HELPER] ", "§9", "helper"),
        CREATOR("§3[§fC§3] ", 2, "4", "§3[§fCREATOR§3] ", "§3", "creator"),
        DEFAULT(1, "replace", "§7", "§7", null, "default");

        public final int index;
        private final String id;
        private final String prefix;
        private final String simplePrefix;
        private final String color;
        private final String hex;

        private String placeholder;

        Rank(int index, String id, String prefix, String color, String hex) {
            this.index = index;
            this.id = id;
            this.prefix = prefix;
            this.color = color;
            this.hex = hex;
            this.simplePrefix = (prefix.equals(color) ? color : color + "[" + C.removeColor(prefix.replaceAll("[\\[\\]]", "")).charAt(0) + "] ");
        }

        Rank(String simplePrefix, int index, String id, String prefix, String color, String hex) {
            this.index = index;
            this.id = id;
            this.prefix = prefix;
            this.color = color;
            this.hex = hex;
            this.simplePrefix = simplePrefix;
        }

        Rank(int index, String id, String prefix, String color, String placeholder, String hex) {
            this.index = index;
            this.id = id;
            this.prefix = prefix;
            this.color = color;
            this.placeholder = placeholder;
            this.hex = hex;
            this.simplePrefix = (prefix.equals(color) ? color : color + "[" + C.removeColor(prefix.replaceAll("[\\[\\]]", "")).charAt(0) + "] ");
        }

        public String getId() {
            return id;
        }

        /*
        * Get the non-futuristic prefix of the rank, if simplistic ranks are enabled, return the simple prefix, else return the normal prefix.
         */
        public String getPrefix() {
            return (FormattingConfig.simplisticRankInChat ? simplePrefix : prefix);
        }

        /*
        * Checks if futuristic ranks are enabled, if so, return the placeholder, else return the prefix.
         */
        public String getPrefixCheck() {
            if (BwRanksUtils.futuristicRanks(null)) {
                return getPlaceholder();
            }
            return getPrefix();
        }

        /*
        * Get the color of the rank, if futuristic ranks are enabled, return the nametag, else return the color.
         */
        public String getColor() {
            if (BwRanksUtils.futuristicRanks(null)) {
                return getNametagColor();
            }
            return color;
        }

        /*
        * Get the nametag color of the rank, if futuristic ranks are enabled, return the nametag color, else return the color.
         */
        public String getNametagColor() {
            if (Hysentials.INSTANCE.getConfig().formattingConfig.enabled && FormattingConfig.hexColors) {
                RankStuff rank = FormattingConfig.getRank(hex);
                return "<#" + rank.nametagColor.getHex() + ">";
            } else {
                return color;
            }
        }

        /*
        * Get the chat color of the rank, if futuristic ranks are enabled, return the chat message color else return white.
         */
        public String getChatColor() {
            if (Hysentials.INSTANCE.getConfig().formattingConfig.enabled && FormattingConfig.hexColors) {
                RankStuff rank = FormattingConfig.getRank(hex);
                return "<#" + rank.chatMessageColor.getHex() + ">";
            } else {
                return "§f";
            }
        }

        /*
        * Get the placeholder of the rank, if it's null, return the nametag color and the color of the rank.
         */
        public String getPlaceholder() {
            if (placeholder == null && ImageIcon.getIcon(name().toLowerCase()) != null) {
                return "§f:" + ImageIcon.getIcon(name().toLowerCase()).getName() + ": " + (hex.isEmpty() ? color : getNametagColor());
            }
            return placeholder;
        }

        /*
        * Get the rank by the name of the rank.
         */
        public static Rank valueOF(String s) {
            if (s == null) return null;
            for (Rank rank : values()) {
                if (rank.name().equalsIgnoreCase(s)) {
                    return rank;
                }
            }
            return null;
        }
    }
}
