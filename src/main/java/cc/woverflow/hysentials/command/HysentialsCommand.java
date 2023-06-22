package cc.woverflow.hysentials.command;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.woverflow.hysentials.util.MUtils;
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.polyfrost.oneconfig.utils.commands.annotations.*;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.guis.actionLibrary.ActionLibrary;
import cc.woverflow.hysentials.guis.club.ClubDashboard;
import cc.woverflow.hysentials.handlers.htsl.CodeEditor;
import cc.woverflow.hysentials.handlers.imageicons.ImageIcon;
import cc.woverflow.hysentials.handlers.npc.QuestNPC;
import cc.woverflow.hysentials.htsl.compiler.Compiler;
import cc.woverflow.hysentials.util.JsonData;
import cc.woverflow.hysentials.util.ScoreboardWrapper;
import cc.woverflow.hysentials.websocket.Socket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static cc.woverflow.hysentials.handlers.npc.QuestNPC.checkPosition;

@Command(value = "hysentials", aliases = {"hs"})
public class HysentialsCommand {
    public static List<String> messages = new ArrayList<>();
    public static boolean collecting = false;
    @SubCommand(aliases = {"reload"}, description = "Reloads the mod")
    public void handleReload() {
        ImageIcon.reloadIcons();
        Hysentials.INSTANCE.sbBoxes = new JsonData("./config/hysentials/lines.json", new JSONObject().put("lines", new JSONArray()));
        Hysentials.INSTANCE.rankColors = new JsonData("./config/hysentials/colors.json", new JSONObject());
        MUtils.chat("§aReloaded Hysentials!");
    }

    @SubCommand(aliases = {"online"}, description = "Shows online players")
    public void handleOnline() {
        Hysentials.INSTANCE.sendMessage("§aOnline Players:");
        for (Map.Entry<UUID, String> player : Hysentials.INSTANCE.getOnlineCache().onlinePlayers.entrySet()) {
            UChat.chat("&8 - &a" + player.getValue());
        }
    }

    @SubCommand(description = "HTSL Editor", aliases = "editor")
    private static void editor(String name) {
        new CodeEditor().openGui(name);
    }

    @SubCommand(description = "open config", aliases = "config")
    private static void config() {
        Hysentials.INSTANCE.getConfig().openGui();
    }

    @SubCommand(description = "test command", aliases = "test")
    private static void test(String command, @Greedy String args) {
        if (Minecraft.getMinecraft().thePlayer.getName().equals("EndKloon") || Minecraft.getMinecraft().thePlayer.getName().equals("Sin_ender")) {
            switch (command.toLowerCase()) {
                case "socket": {
                    Hysentials.INSTANCE.sendMessage("§aSocket is " + (Socket.CLIENT.isOpen() ? "connected" : "disconnected"));
                    break;
                }
                case "size": {
                    Hysentials.INSTANCE.sendMessage("§aSize: " + Minecraft.getMinecraft().fontRendererObj.getStringWidth(args));
                    break;
                }
                case "rankicon2": {
                    Hysentials.INSTANCE.sendMessage("§aTranslated Message: " + Hysentials.INSTANCE.getOnlineCache().playerDisplayNames.get(UUID.fromString(args)));
                    break;
                }
                case "alluuids": {
                    Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().forEach(playerInfo -> {
                        UTextComponent textComponent = new UTextComponent("§a" + playerInfo.getGameProfile().getName() + ": " + playerInfo.getGameProfile().getId());
                        textComponent.setClick(ClickEvent.Action.SUGGEST_COMMAND, playerInfo.getGameProfile().getId().toString());
                        UChat.chat(textComponent);
                    });
                    break;
                }
                case "openmenu": {
                    new ActionLibrary().open(Minecraft.getMinecraft().thePlayer);
                    break;
                }
                case "getdisplayname": {
                    Hysentials.INSTANCE.sendMessage("§aTranslated Message: " + Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().stream().filter(playerInfo -> playerInfo.getGameProfile().getName().equalsIgnoreCase(args)).findFirst().get().getDisplayName().getFormattedText());
                    break;
                }

                case "averagefps": {
                    Hysentials.INSTANCE.sendMessage("&aGetting average FPS...");
                    Multithreading.runAsync(() -> {
                        int total = 0;
                        for (int i = 0; i < 100; i++) {
                            total += Minecraft.getDebugFPS();
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        Hysentials.INSTANCE.sendMessage("&aAverage FPS: " + total / 100);
                    });
                    break;
                }

                case "locraw": {
                    System.out.println(ScoreboardWrapper.getLines(true).get(1));
                    System.out.println(ScoreboardWrapper.getLines(true).get(2));

                    System.out.println(ScoreboardWrapper.getLines(false).get(2));
                    System.out.println(ScoreboardWrapper.getLines(false).get(3));

                    Hysentials.INSTANCE.sendMessage("&a" + LocrawUtil.INSTANCE.getLocrawInfo().toString());
                    break;
                }

                case "fontnormal": {
                    Minecraft.getMinecraft().fontRendererObj = Hysentials.minecraftFont;
                    break;
                }

                case "fontcustom": {
                    Minecraft.getMinecraft().fontRendererObj = Hysentials.INSTANCE.imageIconRenderer;
                    break;
                }

                case "collect": {
                    collecting = !collecting;
                    if (collecting) {
                        messages = new ArrayList<>();
                        Hysentials.INSTANCE.sendMessage("&aCollecting messages...");
                    } else {
                        Hysentials.INSTANCE.sendMessage("&aCollected " + messages.size() + " messages!");
                        try {
                            FileUtils.writeLines(new File("./messages.txt"), messages);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }

                case "import": {
                    try {
                        String codeToBeCompiled = null;
                        File file = new File("./config/hysentials/htsl/" + args + ".htsl");
                        if (!file.exists()) {
                            File defaultFile = new File("./config/hysentials/htsl/" + args + ".txt");
                            if (defaultFile.exists()) {
                                file = defaultFile;
                                codeToBeCompiled = FileUtils.readFileToString(file);
                            } else {
                                try {
                                    JSONObject club = ClubDashboard.getClub();
                                    String otherCode = NetworkUtils.getString("https://hysentials.redstone.llc/api/club/action?clubID=" + (club != null ? club.getString("id") : null) + "&id=" + args);
                                    JSONObject otherJson = new JSONObject(otherCode);
                                    if (otherJson.has("action")) {
                                        codeToBeCompiled = otherJson.getJSONObject("action").getJSONObject("action").getString("code");
                                    } else {
                                        String code = NetworkUtils.getString("https://hysentials.redstone.llc/api/action?id=" + args);
                                        JSONObject json = new JSONObject(code);
                                        if (json.has("action")) {
                                            codeToBeCompiled = json.getJSONObject("action").getJSONObject("action").getString("code");
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            codeToBeCompiled = FileUtils.readFileToString(file);
                        }
                        if (codeToBeCompiled != null) {
                            new Compiler(codeToBeCompiled);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }

                case "spawnnpc": {
                    if (Minecraft.getMinecraft().theWorld != null) {
                        if (!Socket.cachedData.has("questNPC")) {
                            QuestNPC.pos = checkPosition();
                            try {
                                QuestNPC.profile =  QuestNPC.createGameProfileWithSkin(
                                    "Quest NPC"
                                );
                                QuestNPC.isSpawned = true;
                                QuestNPC.player = new EntityOtherPlayerMP(Minecraft.getMinecraft().theWorld, QuestNPC.profile);
                                QuestNPC.player.setPosition(QuestNPC.pos.getX() + 0.5, QuestNPC.pos.getY() - 2, QuestNPC.pos.getZ() + 0.5);
                                Minecraft.getMinecraft().theWorld.addEntityToWorld(QuestNPC.player.getEntityId(), QuestNPC.player);
                                NetworkPlayerInfo info = new NetworkPlayerInfo(QuestNPC.profile);
                                ResourceLocation skin = new ResourceLocation("textures/npc/708d06fa5114ec9c25a1c22a054a44e6b28334c2d7ad581afd635138d3982094.png");
                                Field locationSkin = NetworkPlayerInfo.class.getDeclaredField("field_178865_e");
                                locationSkin.setAccessible(true);
                                locationSkin.set(info, skin);
                                Field playerInfo = AbstractClientPlayer.class.getDeclaredField("field_175157_a");
                                playerInfo.setAccessible(true);
                                playerInfo.set(QuestNPC.player, info);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    @SubCommand(aliases = "reconnect")
    private static void reconnect() {
        if (Socket.CLIENT != null && Socket.CLIENT.isOpen())
            Socket.CLIENT.close();
        Socket.createSocket();
    }

//    @SubCommand(description = "Sets your API key.", aliases = "setkey")
//    private static void key(@Description("API Key") String apiKey) {
//        Multithreading.runAsync(() -> {
//            if (HypixelAPIUtils.isValidKey(apiKey)) {
//                HytilsConfig.apiKey = apiKey;
//                HytilsReborn.INSTANCE.getConfig().save();
//                Hysentials.INSTANCE.sendMessage(EnumChatFormatting.GREEN + "Saved API key successfully!");
//            } else {
//                Hysentials.INSTANCE.sendMessage(EnumChatFormatting.RED + "Invalid API key! Please try again.");
//            }
//        });
//    }
//
//    @SubCommand(description = "", aliases = "collect")
//    private static void collect() {
//        collecting = !collecting;
//        if (!collecting) {
//            MUtils.chat("Collected data: " + ArrayUtils.toString(messages));
//        }
//        if (collecting) {
//            messages = new ArrayList<>();
//        }
//        //disabled enabled message
//        MUtils.chat(EnumChatFormatting.GRAY + "Collecting " + (collecting ? EnumChatFormatting.GREEN + "enabled" : EnumChatFormatting.RED + "disabled"));
//    }
//
    @SubCommand(description = "Link your discord account to your minecraft account.", aliases = "link")
    private static void link() {
        if (Socket.linking) {
            Multithreading.runAsync(() -> {
                JSONObject response = Socket.data;
                response.put("server", false);
                Socket.CLIENT.send(response.toString());
                Socket.linking = false;
                Socket.data = null;
                Socket.linked = true;
                MUtils.chat(HysentialsConfig.chatPrefix + " §aSuccessfully linked your discord account to your minecraft account!");
            });
        }
    }

    @SubCommand(description = "Import actions", aliases = "import")
    public static void handleImport(String id) {
        try {
            String codeToBeCompiled = null;
            File file = new File("./config/hysentials/htsl/" + id + ".htsl");
            if (!file.exists()) {
                File defaultFile = new File("./config/hysentials/htsl/" + id + ".txt");
                if (defaultFile.exists()) {
                    file = defaultFile;
                    codeToBeCompiled = FileUtils.readFileToString(file);
                } else {
                    try {
                        JSONObject club = ClubDashboard.getClub();
                        String otherCode = NetworkUtils.getString("https://hysentials.redstone.llc/api/club/action?clubID=" + (club != null ? club.getString("id") : null) + "&id=" + id);
                        JSONObject otherJson = new JSONObject(otherCode);
                        if (otherJson.has("action")) {
                            codeToBeCompiled = otherJson.getJSONObject("action").getJSONObject("action").getString("code");
                        } else {
                            String code = NetworkUtils.getString("https://hysentials.redstone.llc/api/action?id=" + id);
                            JSONObject json = new JSONObject(code);
                            if (json.has("action")) {
                                codeToBeCompiled = json.getJSONObject("action").getJSONObject("action").getString("code");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                codeToBeCompiled = FileUtils.readFileToString(file);
            }
            if (codeToBeCompiled != null) {
                new Compiler(codeToBeCompiled);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
