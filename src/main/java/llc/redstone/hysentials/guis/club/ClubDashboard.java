package llc.redstone.hysentials.guis.club;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import llc.redstone.hysentials.HysentialsUtilsKt;
import llc.redstone.hysentials.config.HysentialsConfig;
import llc.redstone.hysentials.schema.HysentialsSchema.Club;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.guis.container.Container;
import llc.redstone.hysentials.guis.container.GuiItem;
import llc.redstone.hysentials.util.MUtils;
import llc.redstone.hysentials.util.Material;
import llc.redstone.hysentials.websocket.Socket;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import llc.redstone.hysentials.schema.HysentialsSchema;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.MouseEvent;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ClubDashboard extends Container {
    public static HysentialsSchema.Club clubData;
    public static ClubDashboard instance;

    public ClubDashboard(JsonObject clubData) {
        super("Club Dashboard", 3);
        instance = this;
        if (!clubData.get("success").getAsBoolean()) {
            return;
        }
        ClubDashboard.clubData = HysentialsSchema.Club.Companion.deserialize(clubData.getAsJsonObject("club"));
    }

    public ClubDashboard(HysentialsSchema.Club clubData) {
        super("Club Dashboard", 3);
        instance = this;
        ClubDashboard.clubData = clubData;
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
            setItem(12, GuiItem.fromStack(
                GuiItem.makeColorfulItem(Material.PAPER, "&aInvite Players", 1, 0,
                    "&8Option",
                    "",
                    "&7Invite players to your club!",
                    "&8Maximum of 15 players per club.",
                    "",
                    "&eClick to invite!"
                )));


            setItem(14, GuiItem.fromStack(
                GuiItem.makeColorfulItem(Material.DOUBLE_PLANT, "&aClub Icons", 1, 0,
                    "&8Option",
                    "",
                    "&7View and delete the icons you",
                    "&7want shown in any of your houses",
                    "&7Current Icons: &a" + clubData.getIcons().size() + "&7/&e100",
                    "",
                    "&eClick to edit!"
                )
            ));
            setItem(15, GuiItem.fromStack(
                GuiItem.makeColorfulItem(Material.EMPTY_MAP, "&aText Replace", 1, 0,
                    "&8Option",
                    "",
                    "&7Replace any text rendered in game,",
                    "&7this includes items, chat, and more!",
                    "&7This effects all of the houses in the club!",
                    "&7Current Replacements: &a" + clubData.getReplaceText().size() + "&7/&e100",
                    "",
                    "&eClick to edit!"
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
                GuiItem.makeColorfulItem(Material.BARRIER, "&cCurrently nothing yet", 1, 0,
                    "&8Option",
                    "",
                    "&7This feature is not yet available!",
                    "&7We are still trying to figure out",
                    "&7what to put here!",
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

        setAction(12, (event) -> {
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
            }
        });

        setAction(14, (event) -> {
            event.getEvent().cancel();
            Minecraft.getMinecraft().thePlayer.closeScreen();
            new ClubIcons(null).open();
        });

        setAction(15, (event) -> {
            event.getEvent().cancel();
            Minecraft.getMinecraft().thePlayer.closeScreen();
            new ClubReplace(null).open();
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
            if (json.getBoolean("success")) {
                UChat.chat(HysentialsConfig.chatPrefix + " " + (json.has("message") ? json.getString("message") : "Successfully updated club!"));
            } else {
                UChat.chat(HysentialsConfig.chatPrefix + " &cFailed to update club!");
                UChat.chat("   - &c" + json.getString("message"));
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
        clubData = HysentialsSchema.Club.Companion.deserialize(obj.get("club").getAsJsonObject());
        return obj;
    }

    public static ItemStack getItemfromNBT(JsonObject nbt) {
        ItemStack itemStack = null;
        try {
            if (nbt == null) {
                return null;
            }
            if (nbt.has("id") && nbt.has("Count") && nbt.has("Damage")) {
                itemStack = new ItemStack(Item.getByNameOrId(nbt.get("id").getAsString()), nbt.get("Count").getAsInt(), (short) nbt.get("Damage").getAsInt());

                if (nbt.has("tag")) {
                    NBTTagCompound nbtTagCompound = JsonToNBT.getTagFromJson(nbt.get("tag").toString());
                    itemStack.setTagCompound(nbtTagCompound);

                    JsonObject tag = nbt.get("tag").getAsJsonObject();
                    if (tag.has("display")) {
                        JsonObject display = tag.get("display").getAsJsonObject();
                        if (display.has("Name")) {
                            itemStack.setStackDisplayName(display.get("Name").getAsString());
                        }
                        if (display.has("Lore")) {
                            List<String> lore = new ArrayList<>();
                            for (int i = 0; i < display.get("Lore").getAsJsonArray().size(); i++) {
                                lore.add(display.get("Lore").getAsJsonArray().get(i).getAsString());
                            }
                            GuiItem.setLore(itemStack, lore);
                        }
                    }
                }
            }
        } catch (NBTException e) {
            throw new RuntimeException(e);
        }
        return itemStack;
    }
}
