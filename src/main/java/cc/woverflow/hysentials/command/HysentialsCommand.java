package cc.woverflow.hysentials.command;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.polyfrost.oneconfig.utils.commands.annotations.Greedy;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.cosmetic.CosmeticGui;
import cc.woverflow.hysentials.guis.actionLibrary.ActionLibrary;
import cc.woverflow.hysentials.guis.club.ClubDashboard;
import cc.woverflow.hysentials.guis.misc.HysentialsLevel;
import cc.woverflow.hysentials.guis.quest.QuestMainGui;
import cc.woverflow.hysentials.handlers.htsl.CodeEditor;
import cc.woverflow.hysentials.handlers.imageicons.ImageIcon;
import cc.woverflow.hysentials.handlers.misc.QuestHandler;
import cc.woverflow.hysentials.handlers.npc.NPC;
import cc.woverflow.hysentials.handlers.npc.QuestNPC;
import cc.woverflow.hysentials.handlers.npc.lost.LostAdventure;
import cc.woverflow.hysentials.htsl.compiler.Compiler;
import cc.woverflow.hysentials.hyphone.HyPhoneGUI;
import cc.woverflow.hysentials.profileViewer.DefaultProfileGui;
import cc.woverflow.hysentials.quest.Quest;
import cc.woverflow.hysentials.util.JsonData;
import cc.woverflow.hysentials.util.MUtils;
import cc.woverflow.hysentials.util.SBBJsonData;
import cc.woverflow.hysentials.util.ScoreboardWrapper;
import cc.woverflow.hysentials.utils.ChatLib;
import cc.woverflow.hysentials.websocket.Socket;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static cc.woverflow.hysentials.handlers.npc.QuestNPC.checkPosition;

public class HysentialsCommand extends CommandBase {
    public static List<String> messages = new ArrayList<>();
    public static boolean collecting = false;
    @Override
    public String getCommandName() {
        return "hysentials";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/hysentials <args>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("hs");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        String command = args.length > 0 ? args[0] : "help";

        switch (command) {
            case "help": {
                helpPage(args.length > 1 ? Integer.parseInt(args[1]) : 1);
                break;
            }

            case "config": {
                Hysentials.INSTANCE.getConfig().openGui();
                break;
            }

            case "reload": {
                ImageIcon.reloadIcons();
                Hysentials.INSTANCE.sbBoxes = new SBBJsonData("./config/hysentials/lines.json", new JSONObject().put("lines", new JSONArray()));
                Hysentials.INSTANCE.rankColors = new JsonData("./config/hysentials/colors.json", new JSONObject());
                UChat.chat("§aReloaded Hysentials!");
                break;
            }

            case "reconnect": {
                if (Socket.CLIENT != null && Socket.CLIENT.isOpen()) {
                    Socket.manualDisconnect = true;
                    Socket.CLIENT.disconnect();
                }
                Socket.relogAttempts = 0;
                Socket.createSocket();
                break;
            }

            case "link": {
                if (Socket.linking) {
                    Multithreading.runAsync(() -> {
                        JSONObject response = Socket.data;
                        response.put("server", false);
                        Socket.CLIENT.sendText(response.toString());
                        Socket.linking = false;
                        Socket.data = null;
                        Socket.linked = true;
                        MUtils.chat(HysentialsConfig.chatPrefix + " §aSuccessfully linked your discord account to your minecraft account!");
                    });
                } else {
                    MUtils.chat(HysentialsConfig.chatPrefix + " §cYou are not currently linking your account! You must run /link in the discord to link your account.");
                }
                break;
            }

            case "level": {
                new HysentialsLevel(0).open();
                break;
            }

            case "online": {
                Hysentials.INSTANCE.sendMessage("§aOnline Players (" + Hysentials.INSTANCE.getOnlineCache().onlinePlayers.size() + "):");
                for (Map.Entry<UUID, String> player : Hysentials.INSTANCE.getOnlineCache().onlinePlayers.entrySet().stream().limit(25).collect(Collectors.toList())) {
                    UChat.chat("&8 - &a" + player.getValue());
                }
                if (Hysentials.INSTANCE.getOnlineCache().onlinePlayers.size() > 25) {
                    UChat.chat("&8 - &aAnd " + (Hysentials.INSTANCE.getOnlineCache().onlinePlayers.size() - 25) + " more...");
                }
                break;
            }

            case "phone":
            case "menu": {
                Hysentials.INSTANCE.guiDisplayHandler.setDisplayNextTick(new HyPhoneGUI());
                break;
            }

            case "discord": {
                UTextComponent text = new UTextComponent(HysentialsConfig.chatPrefix + " §aJoin the discord here: ");
                text.appendSibling(
                    new UTextComponent("§b§nhttps://discord.gg/mtAXV24bqM")
                        .setClick(ClickEvent.Action.OPEN_URL, "https://discord.gg/mtAXV24bqM")
                        .setHover(HoverEvent.Action.SHOW_TEXT, "§7Click to open the discord invite link."
                ));
                UChat.chat(text);
                break;
            }

            case "editor": {
                new CodeEditor().openGui(args.length > 1 ? args[1] : "default");
                break;
            }

            case "import": {
                if (args.length > 1) {
                    handleImport(args[1]);
                } else {
                    UChat.chat(HysentialsConfig.chatPrefix + " §cYou must specify an id as an argument!");
                    UChat.chat(HysentialsConfig.chatPrefix + " §cUsage: /hysentials import <id>");
                }
                break;
            }

            case "quests": {
                new QuestMainGui().open();
                break;
            }

            case "test": {
                String command2 = args.length > 1 ? args[1] : "";
                String joinedArgsAfterCommand = String.join(" ", Arrays.stream(args).skip(2).toArray(String[]::new));
                handleTest(command2, joinedArgsAfterCommand);
                break;
            }

            default: {
                UChat.chat(HysentialsConfig.chatPrefix + " §cUnknown command! Use /hysentials help for a list of commands.");
                break;
            }
        }
    }

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
                        JsonObject club = ClubDashboard.getClub();
                        String otherCode = NetworkUtils.getString("http://127.0.0.1:8080/api/club/action?clubID=" + (club != null ? club.get("id").getAsString() : null) + "&id=" + id);
                        JSONObject otherJson = new JSONObject(otherCode);
                        if (otherJson.has("action")) {
                            codeToBeCompiled = otherJson.getJSONObject("action").getJSONObject("action").getString("code");
                        } else {
                            String code = NetworkUtils.getString("http://127.0.0.1:8080/api/action?id=" + id);
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

    public void helpPage(int page) {
        UTextComponent text = new UTextComponent("");
        int maxPage = 3;
        text.appendText("§9&m-----------------------------------------------------\n");
        switch (page) {
            case 1: {
                String pageS = ChatLib.getCenteredText("&6Hysentials (Page 1/" + maxPage + ")\n");
                text.appendText(pageS);
                text.appendText("&e/hysentials help <page> &b- &bShows this help page.\n");
                text.appendText("&e/hysentials config &b- &bOpens the Hysentials config.\n");
                text.appendText("&e/hysentials reload &7- &bReloads the configs.\n");
                text.appendText("&e/hysentials reconnect &7- &bReconnects to the websocket.\n");
                text.appendText("&e/hysentials link &7- &bAccept a link request from discord.\n");
                text.appendText("&e/hysentials level &7- &bShows your Hysentials level.\n");
                text.appendText("&e/hysentials online &7- &bShows the online players.\n");
                text.appendText("&e/hysentials menu &7- &bOpens the Hysentials menu.\n");
                text.appendText("&e/hysentials discord &7- &bShows the discord invite link.\n");
                text.appendText("&e/hysentials editor <file> &7- &bOpens the HTSL code editor.\n");
                text.appendText("&e/hysentials import <file> &7- &bImports a HTSL action.\n");
                text.appendText("§9&m-----------------------------------------------------");
                break;
            }
            case 2: {
                String pageS = ChatLib.getCenteredText("&6Hysentials (Page 2/" + maxPage + ")\n");
                text.appendText(pageS);
                text.appendText("&e/globalchat <message> &7- &bSends a message to global chat.\n");
                text.appendText("&e/hysentials quests &7- &bOpens the Quests menu.\n");
                text.appendText("&e/sbboxes editor &7- &bOpens the Scoreboard Boxes editor.\n");
                text.appendText("&e/actionlibrary &7- &bOpens the Action Library.\n");
                text.appendText("&e/club help &7- &bShows the club help page.\n");
                text.appendText("&e/club create <name> &7- &bCreate a club with the specified name\n");
                text.appendText("&e/club invite <player> &7- &bInvite a player to your club\n");
                text.appendText("&e/club join <name> &7- &bUsed to accept a club invite\n");
                text.appendText("&e/club leave &7- &bLeave your current club\n");
                text.appendText("&e/club dashboard &7- &bOpen the club dashboard\n");
                text.appendText("&e/club list &7- &bList all players in your club\n");
                text.appendText("§9&m-----------------------------------------------------");
                break;
            }
            case 3: {
                String pageS = ChatLib.getCenteredText("&6Hysentials (Page 3/" + maxPage + ")\n");
                text.appendText(pageS);
                text.appendText("&e/glow &7- &bToggles the enchant glint on held item.\n");
                text.appendText("&e/removeglow &7- &bRemoves the enchant glint on held item.\n");
                text.appendText("&e/ill <line> <value> &7- &bInsert lore line.\n");
                text.appendText("&e/rll <line> &7- &bRemove lore line.\n");
                text.appendText("&e/removename &7- &bRemoves the name on held item.\n");
                text.appendText("&e/sll <line> <value> &7- &bSet lore line.\n");
                text.appendText("&e/rename <name> &7- &bSet name on held item.\n");
                text.appendText("&e/openinv <player> &7- &bView the held item and armor of a player.\n");
                //3 more can be added here
                text.appendText("§9&m-----------------------------------------------------");
            }
        }
        text.chat();
    }


    private static void handleTest(String command, String args) {
        if (Minecraft.getMinecraft().thePlayer.getName().equals("EndKloon") || Minecraft.getMinecraft().thePlayer.getName().equals("Sin_ender")) {
            switch (command.toLowerCase()) {
                case "cosmetic": {
                    Hysentials.INSTANCE.guiDisplayHandler.setDisplayNextTick(new CosmeticGui());
                    break;
                }

                case "spawnnpc": {
                    System.out.println("Woo");
                    NPC npc = NPC.npcs.get(0);
                    LostAdventure a = (LostAdventure) npc;
                    BlockPos pos = QuestNPC.checkPosition(40);
                    a.lastX = pos.getX();
                    a.lastY = pos.getY();
                    a.lastZ = pos.getZ();
                    a.spawnNPC(a.lastX, a.lastY, a.lastZ);

                    Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
                    double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX);
                    double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY);
                    double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ);

                    double x = pos.getX() - viewerX + 0.5f;
                    double y = pos.getY() - viewerY - viewer.getEyeHeight();
                    double z = pos.getZ() - viewerZ + 0.5f;

                    double distSq = x * x + y * y + z * z;
                    double dist = Math.sqrt(distSq);

                    UTextComponent textComponent = new UTextComponent("&e[NPC] ??????&f: *Growns* How did I get here?");
                    textComponent.setHover(HoverEvent.Action.SHOW_TEXT, "§7Someone has been here before you...\n§7Maybe you should ask them what happened?\n§7Distance: §e" + ((int) dist) + " §7blocks away");
                    a.chatID = new Random().nextInt(Integer.MAX_VALUE - 1);
                    Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(textComponent, a.chatID);

                    break;
                }

                case "profile": {
                    Hysentials.INSTANCE.guiDisplayHandler.setDisplayNextTick(new DefaultProfileGui(Minecraft.getMinecraft().thePlayer));
                    break;
                }

                case "check": {
                    QuestHandler.checkQuest(Quest.getQuestById(args));
                }

                case "getnpcskin": {
                    MovingObjectPosition obj = QuestNPC.getMouseOverExtended(4);
                    if (obj.entityHit != null && obj.entityHit instanceof EntityPlayer) {
                        EntityPlayer player = (EntityPlayer) obj.entityHit;
                        GameProfile profile = player.getGameProfile();
                        JsonElement properties = new PropertyMap.Serializer().serialize(profile.getProperties(), null, null);
                        //put that in clipboard with java
                        String json = properties.toString();
                        StringSelection stringSelection = new StringSelection(json);
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(stringSelection, null);
                    }
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
            }
        }
    }
}
