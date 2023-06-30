package cc.woverflow.hysentials.handlers.redworks;

import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.handlers.sbb.SbbRenderer;
import cc.woverflow.hysentials.util.*;
import cc.woverflow.hysentials.websocket.Socket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class BwRanks {
    private int tick;
    public static HashMap<NetworkPlayerInfo, DuoVariable<String,String>> playerTeamMap = new HashMap<>();

    public static HashMap<String, String> replacementMap = new HashMap<>();
    public static HashMap<UUID, ScorePlayerTeam> customTeamMap = new HashMap<>();
    public static boolean hasRank = true;

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        this.tick = 79;
        playerTeamMap.clear();
        replacementMap.clear();
        customTeamMap.clear();
    }
    public static boolean hidingGuildList = false;
    public static boolean hidingLastMessage = false;
    public static List<String> lines = new ArrayList<>();

    public boolean debug = false;
    public static boolean initializedRpc = false;
    int tick2 = 0;
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        final LocrawInfo locraw = LocrawUtil.INSTANCE.getLocrawInfo();
        if (event.phase != TickEvent.Phase.START || Minecraft.getMinecraft().thePlayer == null || !HypixelUtils.INSTANCE.isHypixel() || locraw == null) {
            return;
        }
        if (++this.tick == 80) {
            Multithreading.runAsync(() -> {
                BlockWAPIUtils.getOnline(); // Update online cache
                if (++tick2 == 30) {
                    replacementMap.clear();
                    tick2 = 0;
                }
            });
            this.tick = 0;
        }
        if (tick % 5 == 0) {
            if (Minecraft.getMinecraft().fontRendererObj instanceof ImageIconRenderer && !HysentialsConfig.futuristicRanks) {
                Minecraft.getMinecraft().fontRendererObj = Hysentials.minecraftFont;
            } else if (!(Minecraft.getMinecraft().fontRendererObj instanceof ImageIconRenderer) && HysentialsConfig.futuristicRanks) {
                Minecraft.getMinecraft().fontRendererObj = Hysentials.INSTANCE.imageIconRenderer;
            }


            Multithreading.runAsync(() -> {
                //Discord RPC
                if (Socket.cachedServerData.has("rpc") && Socket.cachedServerData.getBoolean("rpc")) {
                    if (!initializedRpc && HypixelUtils.INSTANCE.isHypixel()) {
                        if (Hysentials.INSTANCE.discordRPC == null) {
                            try {
                                DiscordCore.init();
                                Hysentials.INSTANCE.discordRPC = new DiscordRPC();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        Hysentials.INSTANCE.discordRPC.register();
                        initializedRpc = true;
                    }
                    Hysentials.INSTANCE.discordRPC.updateRPC();
                }

                HashMap<NetworkPlayerInfo, String> displayMap = Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().stream().collect(HashMap::new, (map, playerInfo) -> {
                    String display = playerInfo.getDisplayName() == null ? "" : playerInfo.getDisplayName().getFormattedText();
                    if (display.equals("") && playerInfo.getPlayerTeam() != null) {
                        display = playerInfo.getPlayerTeam().getColorPrefix() + playerInfo.getGameProfile().getName() + playerInfo.getPlayerTeam().getColorSuffix();
                    }
                    String originalDisplay = display;
                    display = display.replace(display.substring(display.indexOf(playerInfo.getGameProfile().getName()) + playerInfo.getGameProfile().getName().length()), "");

                    String newDisplay = BwRanksUtils.getMessage(display, playerInfo.getGameProfile().getName(), playerInfo.getGameProfile().getId(), true, true);
                    map.put(playerInfo, originalDisplay);
                    if (!display.equals("") && !newDisplay.equals(display)) {
                        replacementMap.put(display.substring(0, display.indexOf(playerInfo.getGameProfile().getName()) + playerInfo.getGameProfile().getName().length()), newDisplay);
                    }
                }, HashMap::putAll);

                for (NetworkPlayerInfo player : displayMap.keySet()) {
                    BlockWAPIUtils.Rank rank = null;
                    if (Hysentials.INSTANCE.getOnlineCache().getOnlinePlayers().containsKey(player.getGameProfile().getId())) {
                        try {
                            rank = BlockWAPIUtils.Rank.valueOf(Hysentials.INSTANCE.getOnlineCache().getRankCache().get(player.getGameProfile().getId()).toUpperCase());
                        } catch (Exception ignored) {
                            rank = BlockWAPIUtils.Rank.DEFAULT;
                        }
                    }

                    if (rank != null && !rank.equals(BlockWAPIUtils.Rank.DEFAULT) && !customTeamMap.containsKey(player.getGameProfile().getId()) && SbbRenderer.housingScoreboard.getHousingName() == null) {
                        ScorePlayerTeam customTeam = Minecraft.getMinecraft().theWorld.getScoreboard().createTeam("AA" + randomString(10));
                        customTeam.setNamePrefix(displayMap.get(player).substring(0, displayMap.get(player).indexOf(player.getGameProfile().getName())));
                        customTeam.setNameSuffix(displayMap.get(player).substring(displayMap.get(player).indexOf(player.getGameProfile().getName()) + player.getGameProfile().getName().length()));
                        customTeamMap.put(player.getGameProfile().getId(), customTeam);
                        Minecraft.getMinecraft().theWorld.getScoreboard().addPlayerToTeam(player.getGameProfile().getName(), customTeam.getTeamName());
                    }
                }
            });
        }
    }

    //generate random string
    private static final Random rand = new Random();

    public static String randomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder result = new StringBuilder();
        while (length > 0) {
            result.append(characters.charAt(rand.nextInt(characters.length())));
            length--;
        }
        return result.toString();
    }

    public static Pattern pattern = Pattern.compile("\\[[A-Za-z§0-9+]+] ");

    public static boolean hasRank(String prefix) {
        return pattern.matcher(prefix).find();
    }

    private void oldCode() {
        List<DuoVariable<String,String>> teams = new ArrayList<>();
        Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().forEach(playerInfo -> {
            String name = playerInfo.getGameProfile().getName();
            String display = playerInfo.getDisplayName() == null ? "" : playerInfo.getDisplayName().getFormattedText();
            String prefix = display.substring(0, display.indexOf(name));
            String suffix = display.substring(display.indexOf(name) + name.length());

            DuoVariable<String, String> team = new DuoVariable<>(prefix, suffix);
            playerTeamMap.put(playerInfo, team);

            if (!teams.contains(team)) {
                teams.add(team);
            }
        });
        List<String> prefixes = new ArrayList<>();
        for (DuoVariable<String,String> team : teams) {
            String prefix = team.getFirst();
            if (prefix == null) {
                prefix = "";
            }
            prefixes.add(prefix);
        }
        hasRank = prefixes.stream().anyMatch(BwRanks::hasRank);
        for (NetworkPlayerInfo player : playerTeamMap.keySet()) {
            long start = System.currentTimeMillis();
            String name = player.getGameProfile().getName();
            DuoVariable<String,String> team = playerTeamMap.get(player);
            String prefix = team.getFirst();
            String suffix = team.getSecond();
            if (prefix == null) {
                prefix = "";
            }

            if (suffix == null) {
                suffix = "";
            }
            String display = prefix + name + suffix;
            String replacement = BwRanksUtils.getMessage(display, name, player.getGameProfile().getId(), true, hasRank);
            replacementMap.put(display, replacement);

            BlockWAPIUtils.Rank rank = null;
            if (Hysentials.INSTANCE.getOnlineCache().getOnlinePlayers().containsKey(player.getGameProfile().getId())) {
                try {
                    rank = BlockWAPIUtils.Rank.valueOf(Hysentials.INSTANCE.getOnlineCache().getRankCache().get(player.getGameProfile().getId()).toUpperCase());
                } catch (Exception ignored) {
                    rank = BlockWAPIUtils.Rank.DEFAULT;
                }
            }

            if (rank != null && !customTeamMap.containsKey(player.getGameProfile().getId())) {
                ScorePlayerTeam customTeam = Minecraft.getMinecraft().theWorld.getScoreboard().createTeam("AA" + randomString(10));
                customTeam.setNamePrefix(team.getFirst());
                customTeam.setNameSuffix(team.getSecond());
                customTeamMap.put(player.getGameProfile().getId(), customTeam);
                Minecraft.getMinecraft().theWorld.getScoreboard().addPlayerToTeam(name, customTeam.getTeamName());
                Minecraft.getMinecraft().theWorld.getScoreboard().removePlayerFromTeam(name, player.getPlayerTeam());
            }
        }
    }
}
