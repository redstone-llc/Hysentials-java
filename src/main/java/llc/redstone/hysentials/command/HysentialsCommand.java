package llc.redstone.hysentials.command;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.HysentialsUtilsKt;
import llc.redstone.hysentials.config.HysentialsConfig;
import llc.redstone.hysentials.guis.club.ClubDashboard;
import llc.redstone.hysentials.guis.misc.HysentialsLevel;
import llc.redstone.hysentials.guis.quest.QuestMainGui;
import llc.redstone.hysentials.handlers.htsl.CodeEditor;
import llc.redstone.hysentials.handlers.imageicons.ImageIcon;
import llc.redstone.hysentials.handlers.npc.NPC;
import llc.redstone.hysentials.htsl.compiler.Compiler;
import llc.redstone.hysentials.macrowheel.MacroWheelSelector;
import llc.redstone.hysentials.profileViewer.DefaultProfileGui;
import llc.redstone.hysentials.schema.HysentialsSchema;
import llc.redstone.hysentials.util.*;
import llc.redstone.hysentials.utils.ChatLib;
import llc.redstone.hysentials.websocket.Request;
import llc.redstone.hysentials.websocket.Socket;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.MovingObjectPosition;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


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

            case "commandwheel": {
                new MacroWheelSelector().open();
                break;
            }

            case "config": {
                Hysentials.INSTANCE.getConfig().openGui();
                break;
            }

            case "reload": {
                ImageIcon.reloadIcons();
                Hysentials.INSTANCE.sbBoxes = new SBBJsonData("./config/hysentials/lines.json", new JSONObject().put("lines", new JSONArray()));
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
                        JsonObject response = Socket.linkingData;
                        response.addProperty("server", false);
                        Socket.CLIENT.sendText(response.toString());
                        Socket.linking = false;
                        Socket.linkingData = null;
                        Socket.linked = true;
                        UChat.chat(HysentialsConfig.chatPrefix + " §aSuccessfully linked your Discord account!");
                    });
                } else {
                    UChat.chat(HysentialsConfig.chatPrefix + " §cYou are not currently linked to your Discord account! You must run /link in the Discord to link it.");
                }
                break;
            }

            case "level": {
                new HysentialsLevel(0).open();
                break;
            }

            case "online": {
                Hysentials.INSTANCE.sendMessage("§aOnline Players (" + Socket.cachedUsers.size() + "):");
                for (Map.Entry<String, HysentialsSchema.User> player : Socket.cachedUsers.entrySet().stream().limit(25).collect(Collectors.toList())) {
                    UChat.chat("&8 - &a" + player.getValue().getUsername());
                }
                if (Socket.cachedUsers.size() > 25) {
                    UChat.chat("&8 - &aAnd " + ( Socket.cachedUsers.size() - 25) + " more...");
                }
                break;
            }

            case "phone":
            case "menu": {
                UChat.chat(HysentialsConfig.chatPrefix + " §cComing soon!");
                break;
            }

            case "discord": {
                UTextComponent text = new UTextComponent(HysentialsConfig.chatPrefix + " §aJoin the Discord here: ");
                text.appendSibling(
                    new UTextComponent("§b§nhttps://discord.gg/mtAXV24bqM")
                        .setClick(ClickEvent.Action.OPEN_URL, "https://discord.gg/mtAXV24bqM")
                        .setHover(HoverEvent.Action.SHOW_TEXT, "§7Click to open the Discord"
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
                    UChat.chat(HysentialsConfig.chatPrefix + " §cYou must specify an ID as an argument!");
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
                        String otherCode = NetworkUtils.getString(HysentialsUtilsKt.getHYSENTIALS_API() + "/club/action?clubID=" + (club != null ? club.get("id").getAsString() : null) + "&id=" + id);
                        JSONObject otherJson = new JSONObject(otherCode);
                        if (otherJson.has("action")) {
                            codeToBeCompiled = otherJson.getJSONObject("action").getJSONObject("action").getString("code");
                        } else {
                            String code = NetworkUtils.getString(HysentialsUtilsKt.getHYSENTIALS_API() + "/action?id=" + id);
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
        text.appendText("§9&m                                                           \n");
        switch (page) {
            case 1: {
                String pageS = ChatLib.getCenteredText("&6Hysentials (Page 1/" + maxPage + ")\n");
                text.appendText(pageS);
                text.appendText("&e/hysentials help <page> &b- &bShows this help page.\n");
                text.appendText("&e/hysentials config &b- &bOpens the Hysentials config.\n");
                text.appendText("&e/hysentials commandwheel &7- &bOpens the Command Wheel Editor.\n");
                text.appendText("&e/hysentials reload &7- &bReloads the configs.\n");
                text.appendText("&e/hysentials reconnect &7- &bReconnects to the websocket.\n");
                text.appendText("&e/hysentials link &7- &bAccept a link request from discord.\n");
                text.appendText("&e/hysentials level &7- &bShows your Hysentials level.\n");
                text.appendText("&e/hysentials online &7- &bShows the online players.\n");
                text.appendText("&e/hysentials menu &7- &bOpens the Hysentials menu.\n");
                text.appendText("&e/hysentials discord &7- &bShows the discord invite link.\n");
                text.appendText("&e/hysentials editor <file> &7- &bOpens the HTSL code editor.\n");
                text.appendText("§9&m                                                           ");
                break;
            }
            case 2: {
                String pageS = ChatLib.getCenteredText("&6Hysentials (Page 2/" + maxPage + ")\n");
                text.appendText(pageS);
                text.appendText("&e/hysentials import <file> &7- &bImports a HTSL action.\n");
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
                text.appendText("§9&m                                                           ");
                break;
            }
            case 3: {
                String pageS = ChatLib.getCenteredText("&6Hysentials (Page 3/" + maxPage + ")\n");
                text.appendText(pageS);
                text.appendText("&e/club list &7- &bList all players in your club\n");
                text.appendText("&e/glow &7- &bToggles the enchant glint on held item.\n");
                text.appendText("&e/removeglow &7- &bRemoves the enchant glint on held item.\n");
                text.appendText("&e/ill <line> <value> &7- &bInsert lore line.\n");
                text.appendText("&e/rll <line> &7- &bRemove lore line.\n");
                text.appendText("&e/removename &7- &bRemoves the name on held item.\n");
                text.appendText("&e/sll <line> <value> &7- &bSet lore line.\n");
                text.appendText("&e/rename <name> &7- &bSet name on held item.\n");
                text.appendText("&e/openinv <player> &7- &bView the held item and armor of a player.\n");
                //2 more can be added here
                text.appendText("§9&m                                                           ");
            }
        }
        text.chat();
    }


    private static void handleTest(String command, String args) {
        if (Minecraft.getMinecraft().thePlayer.getName().equals("EndKloon") || Minecraft.getMinecraft().thePlayer.getName().equals("Sin_ender")) {
            switch (command.toLowerCase()) {
                case "switch": {
                    boolean isCurrentlyLocal = HysentialsUtilsKt.getHYSENTIALS_API().equals("http://localhost:8080/api");
                    if (HysentialsUtilsKt.isLocalOn()) {
                        HysentialsUtilsKt.setHYSENTIALS_API(!isCurrentlyLocal ? "http://localhost:8080/api" : "http://backend.redstone.llc/api");
                        HysentialsUtilsKt.setWEBSOCKET(!isCurrentlyLocal ? "ws://localhost:8080/ws" : "ws://backend.redstone.llc/ws");
                        Socket.CLIENT.sendClose();
                        Socket.createSocket();
                    } else if (isCurrentlyLocal) {
                        HysentialsUtilsKt.setHYSENTIALS_API("http://backend.redstone.llc/api");
                        HysentialsUtilsKt.setWEBSOCKET("ws://backend.redstone.llc/ws");
                        Socket.CLIENT.sendClose();
                        Socket.createSocket();
                    } else {
                        Hysentials.INSTANCE.sendMessage("&cLocal is not on and you are already on the main server!");
                    }
                    break;
                }

                case "doorbell": {
                    Socket.CLIENT.sendText(
                        new Request(
                            "method", "doorbellAuthenticate",
                            "uuid", Minecraft.getMinecraft().thePlayer.getUniqueID().toString(),
                            "key", Socket.serverId
                        ).toString()
                    );
                    break;
                }

                case "commandwheel": {
                    new MacroWheelSelector().open();
                    break;
                }

                case "profile": {
                    Hysentials.INSTANCE.guiDisplayHandler.setDisplayNextTick(new DefaultProfileGui(Minecraft.getMinecraft().thePlayer));
                    break;
                }

                case "getnpcskin": {
                    MovingObjectPosition obj = NPC.getMouseOverExtended(4);
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
