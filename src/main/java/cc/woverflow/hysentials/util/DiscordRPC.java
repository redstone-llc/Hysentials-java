package cc.woverflow.hysentials.util;

import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hysentials.handlers.redworks.HousingScoreboard;
import cc.woverflow.hysentials.handlers.sbb.SbbRenderer;
import cc.woverflow.hysentials.handlers.sbb.Scoreboard;
import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.DiscordEventAdapter;
import de.jcm.discordgamesdk.activity.Activity;
import de.jcm.discordgamesdk.activity.ActivityType;
import de.jcm.discordgamesdk.user.DiscordUser;
import de.jcm.discordgamesdk.user.Relationship;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Executable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static cc.woverflow.hysentials.guis.sbBoxes.SBBoxesEditor.removeHiddenCharacters;
import static org.apache.commons.lang3.StringUtils.capitalize;

public class DiscordRPC {
    public static File downloadDiscordLibrary() throws IOException {
        // Find out which name Discord's library has (.dll for Windows, .so for Linux)
        String name = "discord_game_sdk";
        String suffix;

        String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        String arch = System.getProperty("os.arch").toLowerCase(Locale.ROOT);

        if (osName.contains("windows")) {
            suffix = ".dll";
        } else if (osName.contains("linux")) {
            suffix = ".so";
        } else if (osName.contains("mac os")) {
            suffix = ".dylib";
        } else {
            throw new RuntimeException("cannot determine OS type: " + osName);
        }

		/*
		Some systems report "amd64" (e.g. Windows and Linux), some "x86_64" (e.g. Mac OS).
		At this point we need the "x86_64" version, as this one is used in the ZIP.
		 */
        if (arch.equals("amd64"))
            arch = "x86_64";

        // Path of Discord's library inside the ZIP
        String zipPath = ".lib/" + arch + "/" + name + suffix;

        // Open the URL as a ZipInputStream
        URL downloadUrl = new URL("https://dl-game-sdk.discordapp.net/2.5.6/discord_game_sdk.zip");
        HttpURLConnection connection = (HttpURLConnection) downloadUrl.openConnection();
        connection.setRequestProperty("User-Agent", "discord-game-sdk4j (https://github.com/JnCrMx/discord-game-sdk4j)");
        ZipInputStream zin = new ZipInputStream(connection.getInputStream());

        // Search for the right file inside the ZIP
        ZipEntry entry;
        while ((entry = zin.getNextEntry()) != null) {
            if (entry.getName().equals(zipPath)) {
                // Create a new temporary directory
                // We need to do this, because we may not change the filename on Windows
                File tempDir = new File(System.getProperty("java.io.tmpdir"), "java-" + name + System.nanoTime());
                if (!tempDir.mkdir())
                    throw new IOException("Cannot create temporary directory");
                tempDir.deleteOnExit();

                // Create a temporary file inside our directory (with a "normal" name)
                File temp = new File(tempDir, name + suffix);
                temp.deleteOnExit();

                // Copy the file in the ZIP to our temporary file
                Files.copy(zin, temp.toPath());

                // We are done, so close the input stream
                zin.close();

                // Return our temporary file
                return temp;
            }
            // next entry
            zin.closeEntry();
        }
        zin.close();
        // We couldn't find the library inside the ZIP
        return null;
    }

    boolean enabled = false;
    Core discordRPC;
    boolean triedReconnect = false;
    Activity activity;

    public void register() {
        CreateParams params = new CreateParams();

        params.setClientID(1116856903255982260L);
        params.setFlags(CreateParams.Flags.NO_REQUIRE_DISCORD);
        params.registerEventHandler(new DiscordEventAdapter() {
            //add things later
        });
        try {
            discordRPC = new Core(params);
            System.out.println("started sdk");
            enabled = true;

            Thread callBacks = new Thread(() -> {
                while (enabled) {
                    discordRPC.runCallbacks();
                    try {
                        Thread.sleep(16);//run callbacks at 60fps
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Thread.currentThread().interrupt();
            });
            callBacks.start();
        } catch (Exception e) {
            System.out.println("An error occurred while trying to start the core, is Discord running?");
            enabled = false;
        }
    }

    World currentWorld;
    Instant lastGameChange = Instant.now();
    int maxPlayers = 0;
    public void updateRPC() {
        try (Activity activity = new Activity()) {
            activity.setType(ActivityType.PLAYING);
            activity.setDetails("Playing on Hypixel");
            activity.assets().setSmallImage("hysentials");
            activity.assets().setSmallText("Hysentials");
            if (Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().theWorld != currentWorld) {
                currentWorld = Minecraft.getMinecraft().theWorld;
                lastGameChange = Instant.now();
                maxPlayers = 0;
            }
            if (ScoreboardWrapper.getLines(true).size() > 2) {
                String sline1 = C.removeColor(removeHiddenCharacters(ScoreboardWrapper.getLines(true).get(2).toString()));
                String sline2 = C.removeColor(removeHiddenCharacters(ScoreboardWrapper.getLines(true).get(3).toString()));
                if (sline1.startsWith("Players: ") || sline2.startsWith("Players: ")) {
                    String line = (sline1.startsWith("Players: ") ? sline1 : sline2);
                    String[] split = line.split("/");
                    if (split.length == 2) {
                        try {
                            maxPlayers = Integer.parseInt(split[1]);
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
            }

            activity.timestamps().setStart(lastGameChange);

            if (LocrawUtil.INSTANCE.isInGame() && LocrawUtil.INSTANCE.getLocrawInfo() != null) {
                activity.setState(capitalize(LocrawUtil.INSTANCE.getLocrawInfo().getGameType().name().toLowerCase()));
                activity.assets().setLargeText(capitalize(LocrawUtil.INSTANCE.getLocrawInfo().getGameType().name().toLowerCase()));
                activity.assets().setLargeImage(LocrawUtil.INSTANCE.getLocrawInfo().getGameType().name().toLowerCase());
                activity.party().size().setCurrentSize(Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().size());
                activity.party().size().setMaxSize(maxPlayers == 0 ? Minecraft.getMinecraft().getNetHandler().currentServerMaxPlayers : maxPlayers);
            } else {
                if (ScoreboardWrapper.getTitle() != null && !C.removeColor(ScoreboardWrapper.getTitle()).equals("HYPIXEL")) {
                    if (SbbRenderer.housingScoreboard.getHousingName() != null) {
                        activity.setState(C.removeColor(SbbRenderer.housingScoreboard.getHousingCreator()) + "'s Housing");
                        activity.assets().setLargeImage("housing");
                        activity.assets().setLargeText(C.removeColor(SbbRenderer.housingScoreboard.getHousingName()));
                        activity.party().size().setCurrentSize(Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().size());
                        activity.party().size().setMaxSize(maxPlayers == 0 ? Minecraft.getMinecraft().getNetHandler().currentServerMaxPlayers : maxPlayers);
                    } else {
                        activity.assets().setLargeImage(C.removeColor(ScoreboardWrapper.getTitle()).toLowerCase().replace(" ", "").replace("_", "").replace("games", ""));
                        activity.assets().setLargeText(capitalize(C.removeColor(ScoreboardWrapper.getTitle().toLowerCase())) + " Lobby");
                        activity.setState(capitalize(C.removeColor(ScoreboardWrapper.getTitle().toLowerCase())) + " Lobby");
                    }
                } else {
                    activity.setState("In Lobby");
                    activity.assets().setLargeImage("classiclobby");
                    activity.assets().setLargeText("Main Lobby");
                }
            }
            discordRPC.activityManager().updateActivity(activity);
            triedReconnect = false;
        } catch (Exception e) {
            e.printStackTrace();
            if (!triedReconnect)
                reconnect();
            else
                try {
                    enabled = false;
                    discordRPC.close();
                } catch (Throwable ignored) {
                }
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_AQUA + "Hycord > "
                + EnumChatFormatting.RED + "A problem has repeatedly occurred and HyCord has automatically been disabled," +
                " to attempt to enable HyCord please do /hycord reconnect."));
        }
    }

    public void reconnect() {
        triedReconnect = true;
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_AQUA + "Hycord > "
            + EnumChatFormatting.RED + "Disconnected from Discord attempting to reconnect..."));
        try {
            enabled = false;
            discordRPC.close();
        } catch (Throwable ignored) {
        }
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_AQUA + "Hycord > "
            + EnumChatFormatting.YELLOW + "Started sdk"));
        register();
    }
}
