package llc.redstone.hysentials.command;

import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.commands.annotations.Greedy;
import llc.redstone.hysentials.HysentialsUtilsKt;
import llc.redstone.hysentials.schema.HysentialsSchema;
import llc.redstone.hysentials.util.BlockWAPIUtils;
import llc.redstone.hysentials.util.DuoVariable;
import llc.redstone.hysentials.util.MUtils;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import cc.polyfrost.oneconfig.utils.commands.annotations.SubCommand;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.config.HysentialsConfig;
import llc.redstone.hysentials.guis.club.ClubDashboard;
import llc.redstone.hysentials.websocket.Request;
import llc.redstone.hysentials.websocket.Socket;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.org.apache.xpath.internal.operations.Mult;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.config.HysentialsConfig;
import llc.redstone.hysentials.guis.club.ClubDashboard;
import llc.redstone.hysentials.handlers.redworks.BwRanks;
import llc.redstone.hysentials.schema.HysentialsSchema;
import llc.redstone.hysentials.util.BlockWAPIUtils;
import llc.redstone.hysentials.websocket.Socket;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static llc.redstone.hysentials.HysentialsUtilsKt.*;
import static llc.redstone.hysentials.guis.club.ClubDashboard.clubData;
import static llc.redstone.hysentials.guis.club.ClubDashboard.update;
import static llc.redstone.hysentials.handlers.redworks.BwRanks.randomString;

@Command(value = "club", description = "Club Commands",
    customHelpMessage = {
        "§9&m                                                        ",
        "&aClub Commands &c[BETA]",
        "&e/club create <name> &7- &bCreate a club with the specified name.",
        "&e/club invite <player> &7- &bInvite a player to your club.",
        "&e/club join <name> &7- &bUsed to accept a club invite.",
        "&e/club leave &7- &bLeave your current club.",
        "&e/club dashboard &7- &bOpen the club dashboard.",
        "&e/club list &7- &bList all players in your club.",
        "&e/club house <index> help &7- &bManage your club houses.",
        "§9&m                                                        "
    }, aliases = {"clubs"})
public class ClubCommand {
    @SubCommand(aliases = {"create"}, description = "Create a club")
    public void create(@Greedy String name) {
        if (!Socket.linked) {
            UChat.chat("&cYou must be linked to a discord account to use this feature.");
            return;
        }
        Multithreading.runAsync(() -> {
            String id = BwRanks.randomString(8);
            JSONObject json = new JSONObject();
            json.put("name", name);
            json.put("owner", Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString());
            try (InputStreamReader input = new InputStreamReader(Hysentials.post(getHYSENTIALS_API() + "/club/create"
                + "?id=" + id
                + "&uuid=" + Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString()
                + "&key=" + Socket.serverId, json)
                , StandardCharsets.UTF_8)) {
                String s = IOUtils.toString(input);
                JSONObject object = new JSONObject(s);
                if (object.getBoolean("success")) {
                    UChat.chat(HysentialsConfig.chatPrefix + " &aClub successfully created!");
                } else {
                    UChat.chat(HysentialsConfig.chatPrefix + " &cClub failed to create!");
                    UChat.chat("   - &c" + object.getString("message"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @SubCommand(aliases = {"join"}, description = "Join a club")
    public void join(String name) {
        if (!Socket.linked) {
            UChat.chat("&cYou must be linked to a Discord account to use this feature.");
            return;
        }
        Socket.CLIENT.sendText(new Request(
            "method", "clubAccept",
            "club", name,
            "uuid", Minecraft.getMinecraft().getSession().getProfile().getId(),
            "serverId", Socket.serverId
        ).toString());
        Socket.awaiting.add(
            new DuoVariable<>("clubAccept", (json) -> {
                if (json.get("success").getAsBoolean()) {
                    UChat.chat(HysentialsConfig.chatPrefix + " &aSuccessfully joined " + name + "!");
                } else {
                    UChat.chat(HysentialsConfig.chatPrefix + " &cFailed to join " + name + "!");
                    UChat.chat("   - &c" + json.get("message").getAsString());
                }
            })
        );
    }

    @SubCommand(aliases = {"house"})
    public void house(int index, @Greedy String args) {
        if (!Socket.linked) {
            UChat.chat("&cYou must be linked to a Discord account to use this feature.");
            return;
        }
        if (args.isEmpty()) {
            UChat.chat("&cInvalid arguments, use /club house <index> <item|name|username|remove> [args]");
            return;
        }
        String command = args.split(" ")[0];
        Multithreading.runAsync(() -> {
            ClubDashboard.getClub();
            HysentialsSchema.Club club = ClubDashboard.clubData;
            HysentialsSchema.House house = club.getHouses().get(index);
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonObject1 = new JSONObject();
            switch (command.toLowerCase()) {
                case "help": {
                    UChat.chat("&e/club house <index> <item|name|username|remove> [args]");
                    break;
                }
                case "item": {
                    ItemStack item = Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem();
                    if (item == null) {
                        UChat.chat("&cYou must be holding an item to use this command.");
                        return;
                    }
                    String nbt = item.serializeNBT().toString();


                    jsonObject1.put("name", house.getName());
                    jsonObject1.put("username", house.getUsername());
                    jsonObject1.put("nbt", nbt);

                    jsonObject.put("houses", jsonObject1);
                    jsonObject.put("update", true);
                    break;
                }
                case "name": {
                    if (args.split(" ").length < 2) {
                        UChat.chat("&cInvalid arguments, use /club house <index> name <name>");
                        return;
                    }

                    jsonObject1.put("name", args.split(" ")[1]);
                    jsonObject1.put("username", house.getUsername());
                    jsonObject1.put("nbt", house.getNbt());

                    jsonObject.put("houses", jsonObject1);
                    jsonObject.put("update", true);
                    break;
                }
                case "username": {
                    if (args.split(" ").length < 2) {
                        UChat.chat("&cInvalid arguments, use /club house <index> username <username>");
                        return;
                    }

                    jsonObject1.put("name", house.getName());
                    jsonObject1.put("username", args.split(" ")[1]);
                    jsonObject1.put("nbt", house.getNbt());

                    jsonObject.put("houses", jsonObject1);
                    jsonObject.put("update", true);
                    break;
                }
                case "remove": {

                    jsonObject1.put("name", house.getName());
                    jsonObject1.put("username", house.getUsername());
                    jsonObject1.put("nbt", house.getNbt());

                    jsonObject.put("houses", jsonObject1);
                    jsonObject.put("remove", true);
                    break;
                }
            }
            ClubDashboard.update(jsonObject);
        });
    }

    @SubCommand(aliases = {"list"})
    public void list() {
        if (!Socket.linked) {
            UChat.chat("&cYou must be linked to a Discord account to use this feature.");
            return;
        }
        try {
            String s = NetworkUtils.getString(getHYSENTIALS_API() + "/club?uuid="
                + Minecraft.getMinecraft().getSession().getProfile().getId().toString()
                + "&key=" + Socket.serverId);
            JSONObject clubData = new JSONObject(s);
            if (!clubData.getBoolean("success")) {
                UChat.chat(HysentialsConfig.chatPrefix + " &c" + clubData.getString("message"));
                return;
            }
            JSONObject club = clubData.getJSONObject("club");
            JSONArray members = club.getJSONArray("members");
            Multithreading.runAsync(() -> {
                HashMap<String, String> userMap = new HashMap<>();
                for (int i = 0; i < members.length(); i++) {
                    String uuid = members.getString(i);
                    String name = BlockWAPIUtils.getUsername(UUID.fromString(uuid));
                    userMap.put(uuid, name);
                }

                UChat.chat(HysentialsConfig.chatPrefix + " &aClub Members:");
                for (Map.Entry<String, String> displayName : userMap.entrySet()) {
                    String uuid = displayName.getKey();
                    String name = displayName.getValue();
                    UChat.chat("   - &a" + name + (club.getString("owner").equals(uuid) ? " &8(Owner)" : ""));
                }
            });
        } catch (Exception e) {

        }
    }

    @SubCommand(aliases = {"invite"}, description = "Invite a player to your club.")
    public void invite(String name) {
        if (!Socket.linked) {
            UChat.chat("&cYou must be linked to a Discord account to use this feature.");
            return;
        }
        Multithreading.runAsync(() -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("invitee", name);
            ClubDashboard.getClub();
            ClubDashboard.update(jsonObject);
        });
    }

    @SubCommand(aliases = {"leave"}, description = "Leave a club.")
    public void leave() {
        if (!Socket.linked) {
            UChat.chat("&cYou must be linked to a Discord account to use this feature.");
            return;
        }
        Multithreading.runAsync(() -> {
            JSONObject json = new JSONObject();
            json.put("leave", true);
            ClubDashboard.getClub();
            ClubDashboard.update(json);
        });
    }

    @SubCommand(aliases = {"dashboard", "db"}, description = "Open the club dashboard.")
    public void dashboard() {
        if (!Socket.linked) {
            UChat.chat("&cYou must be linked to a Discord account to use this feature.");
            return;
        }
        try {
            String s = NetworkUtils.getString(getHYSENTIALS_API() + "/club?uuid="
                + Minecraft.getMinecraft().getSession().getProfile().getId().toString()
                + "&key=" + Socket.serverId);
            JsonObject clubData = new JsonParser().parse(s).getAsJsonObject();
            if (!clubData.get("success").getAsBoolean()) {
                UChat.chat(HysentialsConfig.chatPrefix + " &c" + clubData.get("message").getAsString());
                return;
            }
            new ClubDashboard(clubData).open(Minecraft.getMinecraft().thePlayer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
