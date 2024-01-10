package cc.woverflow.hysentials.guis.club;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.woverflow.hysentials.HysentialsUtilsKt;
import cc.woverflow.hysentials.schema.HysentialsSchema.Club;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.guis.actionLibrary.SharedCode;
import cc.woverflow.hysentials.guis.container.Container;
import cc.woverflow.hysentials.guis.container.GuiItem;
import cc.woverflow.hysentials.util.Material;
import cc.woverflow.hysentials.websocket.Socket;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraftforge.client.event.MouseEvent;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class ClubDashboard extends Container {
    public static Club clubData;
    public static ClubDashboard instance;

    public ClubDashboard(JsonObject clubData) {
        super("Club Dashboard", 3);
        instance = this;
        if (!clubData.get("success").getAsBoolean()) {
            return;
        }
        ClubDashboard.clubData = Club.Companion.deserialize(clubData.getAsJsonObject("club"));
    }

    @Override
    public void setItems() {
        if (clubData.isOwner()) {
            setItem(10, GuiItem.fromStack(
                GuiItem.makeColorfulItem(Material.SIGN, "&aClub Name", 1, 0,
                    "&8Option",
                    "",
                    "&7Change your club name!",
                    "&7Current Name: &b" + clubData.getName(),
                    "&8You can only change your club name",
                    "&8once every 30d!",
                    "",
                    "&eClick to change!"
                )));
            setItem(11, GuiItem.fromStack(
                GuiItem.makeColorfulItem(Material.BOOK, "&a/Visit Alias", 1, 0,
                    "&8Option",
                    "",
                    "&7Add your own unique &d/visit &7ending!",
                    "&7Only allocated clubhouses will be shown",
                    "&7Current Alias: &6" + clubData.getAlias(),
                    "",
                    "&eClick to set!"
                )));
            setItem(13, GuiItem.fromStack(
                GuiItem.makeColorfulItem(Material.PAPER, "&aInvite Players", 1, 0,
                    "&8Option",
                    "",
                    "&7Invite players to your club!",
                    "&8Maximum of 15 players per club.",
                    "",
                    "&eClick to invite!"
                )));

            setItem(15, GuiItem.fromStack(
                GuiItem.makeColorfulItem(Material.STORAGE_MINECART, "&aView Shared Code", 1, 0,
                    "&8Option",
                    "",
                    "&7View all of your conditions and functions",
                    "&7that are shared within your club!",
                    "",
                    "&eClick to view!"
                )));
            setItem(16, GuiItem.fromStack(
                GuiItem.makeColorfulItem(Material.DARK_OAK_DOOR_ITEM, "&aClubhouses", 1, 0,
                    "&8Option",
                    "",
                    "&7Select which one of your houses you",
                    "&7want shown in the &b/visit " + clubData.getAlias(),
                    "&7menu!",
                    "",
                    "&7Clubhouse Slots: &a" + clubData.getHouses().size() + "&7/&e5",
                    "",
                    "&eClick to edit!"
                )));
        } else {
            setItem(13, GuiItem.fromStack(
                GuiItem.makeColorfulItem(Material.STORAGE_MINECART, "&aView Shared Code", 1, 0,
                    "&8Option",
                    "",
                    "&7View all of your conditions and functions",
                    "&7that are shared within your club!",
                    "",
                    "&eClick to view!"
                )));
        }
    }

    @Override
    public void handleMenu(MouseEvent event) {
    }

    public static boolean selectingName = false;
    public static boolean selectingAlias = false;
    public static boolean invitePlayers = false;
    public static boolean clubhouseSelect = false;

    @Override
    public void setClickActions() {
        setDefaultAction((event) -> {
            event.getEvent().cancel();
        });

        setAction(10, (event) -> {
            event.getEvent().cancel();
            Minecraft.getMinecraft().thePlayer.closeScreen();
            selectingName = true;
            UChat.chat("&7Please type your new club name in chat!");
            UChat.chat("&7You can only change your club name once every 30d!");
            Multithreading.schedule(() -> {
                if (!selectingName) {
                    return;
                }
                selectingName = false;
                UChat.chat("&7Club name change request has expired!");
            }, 5, TimeUnit.MINUTES);
        });

        setAction(11, (event) -> {
            event.getEvent().cancel();
            Minecraft.getMinecraft().thePlayer.closeScreen();
            selectingAlias = true;
            UChat.chat("&7Please type your new club alias in chat!");

            Multithreading.schedule(() -> {
                if (!selectingAlias) {
                    return;
                }
                selectingAlias = false;
                UChat.chat("&7Club alias change request has expired!");
            }, 5, TimeUnit.MINUTES);
        });

        setAction(13, (event) -> {
            event.getEvent().cancel();
            if (clubData.isOwner()) {
                Minecraft.getMinecraft().thePlayer.closeScreen();
                invitePlayers = true;
                UChat.chat("\n&ePlease send the username of the user you want to invite in chat!");

                Multithreading.schedule(() -> {
                    if (!invitePlayers) {
                        return;
                    }
                    invitePlayers = false;
                    UChat.chat("&cInvite request has expired!");
                }, 5, TimeUnit.MINUTES);
            } else {
                Minecraft.getMinecraft().thePlayer.closeScreen();
                new SharedCode(clubData).open();
            }
        });

        setAction(15, (event) -> {
            event.getEvent().cancel();
            Minecraft.getMinecraft().thePlayer.closeScreen();
            new SharedCode(clubData).open();
        });

        setAction(16, (event) -> {
            event.getEvent().cancel();
            Minecraft.getMinecraft().thePlayer.closeScreen();
            clubhouseSelect = true;
            UChat.chat("&ePlease do &b/visit <player name> &eand select the house you want to use!");
            Multithreading.schedule(() -> {
                if (!clubhouseSelect) {
                    return;
                }
                clubhouseSelect = false;
                UChat.chat("&cClubhouse selection request has expired!");
                Minecraft.getMinecraft().thePlayer.closeScreen();
            }, 5, TimeUnit.MINUTES);
        });
    }

    public static String handleSentMessage(String message) {
        if (message.startsWith("/")) {
            return message;
        }
        if (selectingName) {
            selectingName = false;
            Multithreading.runAsync(() -> {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", message);
                update(jsonObject);
            });
            JsonObject newClubData = getClub();
            if (newClubData == null) {
                return null;
            }
            new ClubDashboard(newClubData).open();
            return null;
        }
        if (selectingAlias) {
            selectingAlias = false;
            Multithreading.runAsync(() -> {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("alias", message);
                update(jsonObject);
            });
            JsonObject newClubData = getClub();
            if (newClubData == null) {
                return null;
            }
            new ClubDashboard(newClubData).open();
            return null;
        }

        if (invitePlayers) {
            invitePlayers = false;
            Multithreading.runAsync(() -> {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("invitee", message);
                update(jsonObject);
            });
            JsonObject newClubData = getClub();
            if (newClubData == null) {
                return null;
            }
            new ClubDashboard(newClubData).open();
            return null;
        }
        return message;
    }

    public static void update(JSONObject json) {
        try (InputStreamReader input = new InputStreamReader(
            Hysentials.post((!json.has("invitee") ? HysentialsUtilsKt.getHYSENTIALS_API() + "/club/update" : HysentialsUtilsKt.getHYSENTIALS_API() + "/club/invite")
                + "?id=" + clubData.getId()
                + "&uuid=" + Minecraft.getMinecraft().getSession().getProfile().getId().toString()
                + "&key=" + Socket.serverId, json), StandardCharsets.UTF_8)) {
            String s = IOUtils.toString(input);
            JSONObject object = new JSONObject(s);
            System.out.println(object.toString());
            if (!object.getBoolean("success")) {
                UChat.chat("&c" + object.getString("message"));
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JsonObject getClub() {
        String s = NetworkUtils.getString(HysentialsUtilsKt.getHYSENTIALS_API() + "/club?uuid="
            + Minecraft.getMinecraft().getSession().getProfile().getId().toString()
            + "&key=" + Socket.serverId);
        JsonObject obj = new JsonParser().parse(s).getAsJsonObject();
        if (!obj.get("success").getAsBoolean()) {
            return null;
        }
        clubData = Club.Companion.deserialize(obj.get("club").getAsJsonObject());
        return obj;
    }

    public static ItemStack getItemfromNBT(String nbt) {
        ItemStack itemStack = null;
        try {
            itemStack = ItemStack.loadItemStackFromNBT(JsonToNBT.getTagFromJson(nbt));
        } catch (NBTException e) {
            throw new RuntimeException(e);
        }
        return itemStack;
    }
}
