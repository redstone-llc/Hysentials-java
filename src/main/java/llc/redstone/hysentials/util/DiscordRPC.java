package llc.redstone.hysentials.util;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRichPresence;
import net.minecraft.world.World;

import java.time.Instant;

public class DiscordRPC {

    boolean enabled = false;
    boolean triedReconnect = false;
    public void register() {
        club.minnced.discord.rpc.DiscordRPC lib = club.minnced.discord.rpc.DiscordRPC.INSTANCE;
        String applicationId = "1116856903255982260";
        String steamId = "";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = (user) -> System.out.println("Ready!");
        lib.Discord_Initialize(applicationId, handlers, true, steamId);
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
        presence.details = "Testing RPC";
        lib.Discord_UpdatePresence(presence);
        // in a worker thread
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                lib.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        }, "RPC-Callback-Handler").start();
    }

    World currentWorld;
    Instant lastGameChange = Instant.now();
    int maxPlayers = 0;
    public void updateRPC() {
//        try (Activity activity = new Activity()) {
//            activity.setType(ActivityType.PLAYING);
//            activity.setDetails("Playing on Hypixel");
//            activity.assets().setSmallImage("hysentials");
//            activity.assets().setSmallText("Hysentials");
//            if (Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().theWorld != currentWorld) {
//                currentWorld = Minecraft.getMinecraft().theWorld;
//                lastGameChange = Instant.now();
//                maxPlayers = 0;
//            }
//            if (ScoreboardWrapper.getLines(true).size() > 2) {
//                String sline1 = C.removeColor(removeHiddenCharacters(ScoreboardWrapper.getLines(true).get(2).toString()));
//                String sline2 = C.removeColor(removeHiddenCharacters(ScoreboardWrapper.getLines(true).get(3).toString()));
//                if (sline1.startsWith("Players: ") || sline2.startsWith("Players: ")) {
//                    String line = (sline1.startsWith("Players: ") ? sline1 : sline2);
//                    String[] split = line.split("/");
//                    if (split.length == 2) {
//                        try {
//                            maxPlayers = Integer.parseInt(split[1]);
//                        } catch (NumberFormatException ignored) {
//                        }
//                    }
//                }
//            }
//
//            activity.timestamps().setStart(lastGameChange);
//
//            if (LocrawUtil.INSTANCE.isInGame() && LocrawUtil.INSTANCE.getLocrawInfo() != null) {
//                activity.setState(capitalize(LocrawUtil.INSTANCE.getLocrawInfo().getGameType().name().toLowerCase()));
//                activity.assets().setLargeText(capitalize(LocrawUtil.INSTANCE.getLocrawInfo().getGameType().name().toLowerCase()));
//                activity.assets().setLargeImage(LocrawUtil.INSTANCE.getLocrawInfo().getGameType().name().toLowerCase());
//                activity.party().size().setCurrentSize(Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().size());
//                activity.party().size().setMaxSize(maxPlayers == 0 ? Minecraft.getMinecraft().getNetHandler().currentServerMaxPlayers : maxPlayers);
//            } else {
//                if (ScoreboardWrapper.getTitle() != null && !C.removeColor(ScoreboardWrapper.getTitle()).equals("HYPIXEL")) {
//                    if (SbbRenderer.housingScoreboard.getHousingName() != null) {
//                        activity.setState(C.removeColor(SbbRenderer.housingScoreboard.getHousingCreator()) + "'s Housing");
//                        activity.assets().setLargeImage("housing");
//                        activity.assets().setLargeText(C.removeColor(SbbRenderer.housingScoreboard.getHousingName()));
//                        activity.party().size().setCurrentSize(Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().size());
//                        activity.party().size().setMaxSize(maxPlayers == 0 ? Minecraft.getMinecraft().getNetHandler().currentServerMaxPlayers : maxPlayers);
//                    } else {
//                        activity.assets().setLargeImage(C.removeColor(ScoreboardWrapper.getTitle()).toLowerCase().replace(" ", "").replace("_", "").replace("games", ""));
//                        activity.assets().setLargeText(capitalize(C.removeColor(ScoreboardWrapper.getTitle().toLowerCase())) + " Lobby");
//                        activity.setState(capitalize(C.removeColor(ScoreboardWrapper.getTitle().toLowerCase())) + " Lobby");
//                    }
//                } else {
//                    activity.setState("In Lobby");
//                    activity.assets().setLargeImage("classiclobby");
//                    activity.assets().setLargeText("Main Lobby");
//                }
//            }
//            discordRPC.activityManager().updateActivity(activity);
//            triedReconnect = false;
//        } catch (Exception e) {
//            e.printStackTrace();
//            if (!triedReconnect)
//                reconnect();
//            else
//                try {
//                    enabled = false;
//                    discordRPC.close();
//                } catch (Throwable ignored) {
//                }
//        }
    }

    public void reconnect() {
        triedReconnect = true;
        try {
            enabled = false;
        } catch (Throwable ignored) {
        }
        register();
    }
}
