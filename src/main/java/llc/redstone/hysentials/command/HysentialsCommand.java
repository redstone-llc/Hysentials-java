package llc.redstone.hysentials.command;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.HysentialsUtilsKt;
import llc.redstone.hysentials.config.HysentialsConfig;
import llc.redstone.hysentials.guis.misc.HysentialsLevel;
import llc.redstone.hysentials.guis.quest.QuestMainGui;
import llc.redstone.hysentials.handlers.imageicons.ImageIcon;
import llc.redstone.hysentials.polyui.ui.VisitHouseScreen;
import llc.redstone.hysentials.util.LocrawUtil;
import llc.redstone.hysentials.handlers.npc.NPC;
import llc.redstone.hysentials.htsl.compiler.CompileKt;
import llc.redstone.hysentials.macrowheel.MacroWheelSelector;
import llc.redstone.hysentials.profileViewer.DefaultProfileGui;
import llc.redstone.hysentials.renderer.plusStand.PlusStandEntity;
import llc.redstone.hysentials.schema.HysentialsSchema;
import llc.redstone.hysentials.util.*;
import llc.redstone.hysentials.utils.ChatLib;
import llc.redstone.hysentials.websocket.Request;
import llc.redstone.hysentials.websocket.Socket;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import net.hypixel.data.type.GameType;
import net.hypixel.data.type.ServerType;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.handler.ClientboundPacketHandler;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundLocationPacket;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.client.FMLClientHandler;
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
    public static boolean collecting = false;
    public static double time = 0;
    public static int count = 0;

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
                    UChat.chat("&8 - &aAnd " + (Socket.cachedUsers.size() - 25) + " more...");
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
                try {
                    String code = NetworkUtils.getString(HysentialsUtilsKt.getHYSENTIALS_API() + "/action?id=" + id);
                    JSONObject json = new JSONObject(code);
                    if (json.has("action")) {
                        codeToBeCompiled = json.getJSONObject("action").getJSONObject("action").getString("code");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                codeToBeCompiled = FileUtils.readFileToString(file);
            }

            if (codeToBeCompiled != null) {
                CompileKt.compileFile(codeToBeCompiled, true);
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

                case "locraw": {
                    LocrawInfo info = LocrawUtil.INSTANCE.getLocrawInfo();
                    if (info != null) {
                        UChat.chat("§aLocraw Info:");
                        UChat.chat("&7ServerName: &a" + info.getServerName());
                        UChat.chat("&7GameType: &a" + info.getGameType());
                        UChat.chat("&7LobbyName: &a" + info.getLobbyName());
                        UChat.chat("&7ModeName: &a" + info.getGameMode());
                        UChat.chat("&7MapName: &a" + info.getMapName());
                    } else {
                        UChat.chat("§cLocraw Info is null!");
                    }
                    break;
                }


                case "render": {


                    //average fps
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
                        Hysentials.INSTANCE.sendMessage("&aAverage time per render: " + time / count);

                    });


                    break;
                }

                case "plus": {
                    UChat.chat("Sending Location Request...");
                    HypixelModAPI.getInstance().registerHandler(new ClientboundPacketHandler() {
                        @Override
                        public void onLocationEvent(ClientboundLocationPacket packet) {
                            double x = 0;
                            double y = 0;
                            double z = 0;
                            float yaw  = 0;
                            if (!packet.getServerType().isPresent()) return;
                            ServerType serverType = packet.getServerType().get();
                            if (!(serverType instanceof GameType)) return;
                            GameType gameType = (GameType) serverType;
                            switch (gameType) {
                                case HOUSING: {
                                    x = -16;
                                    y = 65;
                                    z = 30;
                                    yaw = 320f;
                                    break;
                                }
                            }

                            PlusStandEntity entity = new PlusStandEntity(Minecraft.getMinecraft().theWorld);
                            entity.setPositionAndRotation(x, y, z, yaw, 0);
                            Minecraft.getMinecraft().theWorld.spawnEntityInWorld(entity);
                        }
                    });
                    break;
                }

                case "house": {
                    VisitHouseScreen.INSTANCE.open();
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

                case "copyholo": {
                    List<MovingObjectPosition> objs = getMouseOverExtended(4);
                    List<String> holo = new ArrayList<>();
                    for (MovingObjectPosition obj : objs) {
                        if (obj.entityHit != null && obj.entityHit instanceof EntityArmorStand) {
                            EntityArmorStand stand = (EntityArmorStand) obj.entityHit;
                            String name = stand.getCustomNameTag();
                            if (name != null && !name.isEmpty()) {
                                holo.add(name);
                            }
                        }
                    }
                    String holoString = String.join("\n", holo);
                    StringSelection stringSelection = new StringSelection(holoString);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(stringSelection, null);
                    Hysentials.INSTANCE.sendMessage("&aCopied hologram text to clipboard!");
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

    public static List<MovingObjectPosition> getMouseOverExtended(float dist)
    {
        List<MovingObjectPosition> mopReturn = new ArrayList<>();
        Minecraft mc = FMLClientHandler.instance().getClient();
        Entity theRenderViewEntity = mc.getRenderViewEntity();
        AxisAlignedBB theViewBoundingBox = new AxisAlignedBB(
            theRenderViewEntity.posX-0.5D,
            theRenderViewEntity.posY-0.0D,
            theRenderViewEntity.posZ-0.5D,
            theRenderViewEntity.posX+0.5D,
            theRenderViewEntity.posY+1.5D,
            theRenderViewEntity.posZ+0.5D
        );
        MovingObjectPosition returnMOP = null;
        if (mc.theWorld != null)
        {
            double var2 = dist;
            returnMOP = theRenderViewEntity.rayTrace(var2, 0);
            double calcdist = var2;
            Vec3 pos = theRenderViewEntity.getPositionEyes(0);
            var2 = calcdist;
            if (returnMOP != null)
            {
                calcdist = returnMOP.hitVec.distanceTo(pos);
            }

            Vec3 lookvec = theRenderViewEntity.getLook(0);
            Vec3 var8 = pos.addVector(lookvec.xCoord * var2,
                lookvec.yCoord * var2,
                lookvec.zCoord * var2);
            Entity pointedEntity = null;
            float var9 = 1.0F;
            @SuppressWarnings("unchecked")
            List<Entity> list = mc.theWorld.getEntitiesWithinAABBExcludingEntity(
                theRenderViewEntity,
                theViewBoundingBox.addCoord(
                    lookvec.xCoord * var2,
                    lookvec.yCoord * var2,
                    lookvec.zCoord * var2).expand(var9, var9, var9));
            double d = calcdist;

            for (Entity entity : list)
            {
                if (entity.canBeCollidedWith())
                {
                    float bordersize = entity.getCollisionBorderSize();
                    AxisAlignedBB aabb = new AxisAlignedBB(
                        entity.posX-entity.width/2,
                        entity.posY,
                        entity.posZ-entity.width/2,
                        entity.posX+entity.width/2,
                        entity.posY+entity.height,
                        entity.posZ+entity.width/2);
                    aabb.expand(bordersize, bordersize, bordersize);
                    MovingObjectPosition mop0 = aabb.calculateIntercept(pos, var8);

                    if (aabb.isVecInside(pos))
                    {
                        if (0.0D < d || d == 0.0D)
                        {
                            mopReturn.add(new MovingObjectPosition(entity));
                            d = 0.0D;
                        }
                    } else if (mop0 != null)
                    {
                        double d1 = pos.distanceTo(mop0.hitVec);

                        if (d1 < d || d == 0.0D)
                        {
                            mopReturn.add(new MovingObjectPosition(entity));
                            d = d1;
                        }
                    }
                }
            }
        }
        return mopReturn;
    }
}
