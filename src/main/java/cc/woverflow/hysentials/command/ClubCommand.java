package cc.woverflow.hysentials.command;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.woverflow.hysentials.util.MUtils;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import cc.polyfrost.oneconfig.utils.commands.annotations.SubCommand;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.guis.club.ClubDashboard;
import cc.woverflow.hysentials.util.HypixelAPIUtils;
import cc.woverflow.hysentials.websocket.Request;
import cc.woverflow.hysentials.websocket.Socket;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.org.apache.xpath.internal.operations.Mult;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cc.woverflow.hysentials.guis.club.ClubDashboard.update;
import static cc.woverflow.hysentials.handlers.redworks.BwRanks.randomString;

@Command(value = "club", description = "Club Commands",
customHelpMessage = {
    "§9&m-----------------------------------------------------",
    "&aClub Commands: &c[BETA]",
    "&e/club create <name> &7- &bCreate a club with the specified name",
    "&e/club invite <player> &7- &bInvite a player to your club",
    "&e/club join <name> &7- &bUsed to accept a club invite",
    "&e/club leave &7- &bLeave your current club",
    "&e/club dashboard &7- &bOpen the club dashboard",
    "&e/club list &7- &bList all players in your club",
    "§9&m-----------------------------------------------------"
})
public class ClubCommand {
    @SubCommand(aliases = {"create"}, description = "Create a club")
    public void create(String name) {
        if (!Socket.linked) {
            UChat.chat("&cYou must be linked to a discord account to use this feature.");
            return;
        }
        Multithreading.runAsync(() -> {
            String id = randomString(8);
            JSONObject json = new JSONObject();
            json.put("name", name);
            json.put("owner", Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString());
            try (InputStreamReader input = new InputStreamReader(Hysentials.post("http://127.0.0.1:8080/api/club/create"
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
            MUtils.chat("&cYou must be linked to a discord account to use this feature.");
            return;
        }
        Socket.CLIENT.sendText(new Request(
            "method", "clubAccept",
            "club", name,
            "uuid", Minecraft.getMinecraft().getSession().getProfile().getId(),
            "serverId", Socket.serverId
        ).toString());
    }

    @SubCommand(aliases = {"list"})
    public void list() {
        if (!Socket.linked) {
            MUtils.chat("&cYou must be linked to a discord account to use this feature.");
            return;
        }
        try {
            String s = NetworkUtils.getString("http://127.0.0.1:8080/api/club?uuid="
                + Minecraft.getMinecraft().getSession().getProfile().getId().toString()
                + "&key=" + Socket.serverId);
            JSONObject clubData = new JSONObject(s);
            if (!clubData.getBoolean("success")) {
                MUtils.chat(HysentialsConfig.chatPrefix + " &c" + clubData.getString("message"));
                return;
            }
            JSONObject club = clubData.getJSONObject("club");
            JSONArray members = club.getJSONArray("members");
            Multithreading.runAsync(() -> {
                HashMap<String, String> userMap = new HashMap<>();
                for (int i = 0; i < members.length(); i++) {
                    String uuid = members.getString(i);
                    String name = HypixelAPIUtils.getUsername(uuid);
                    userMap.put(uuid, name);
                }

                MUtils.chat(HysentialsConfig.chatPrefix + " &aClub Members:");
                for (Map.Entry<String, String> displayName : userMap.entrySet()) {
                    String uuid = displayName.getKey();
                    String name = displayName.getValue();
                    MUtils.chat("   - &a" + name + (club.getString("owner").equals(uuid) ? " &8(Owner)" : ""));
                }
            });
        } catch (Exception e) {

        }
    }
    @SubCommand(aliases = {"invite"}, description = "Invite a player to your club")
    public void invite(String name) {
        if (!Socket.linked) {
            MUtils.chat("&cYou must be linked to a discord account to use this feature.");
            return;
        }
        Multithreading.runAsync(() -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("invitee", name);
            ClubDashboard.clubData = ClubDashboard.getClub();
            update(jsonObject);
        });
    }

    @SubCommand(aliases = {"leave"}, description = "Leave a club")
    public void leave() {
        if (!Socket.linked) {
            MUtils.chat("&cYou must be linked to a discord account to use this feature.");
            return;
        }
        Multithreading.runAsync(() -> {
            JSONObject json = new JSONObject();
            json.put("leave", true);
            ClubDashboard.clubData = ClubDashboard.getClub();
            update(json);
        });
    }

    @SubCommand(aliases = {"dashboard", "db"}, description = "Open the club dashboard")
    public void dashboard() {
        if (!Socket.linked) {
            MUtils.chat("&cYou must be linked to a discord account to use this feature.");
            return;
        }
        try {
            String s = NetworkUtils.getString("http://127.0.0.1:8080/api/club?uuid="
                + Minecraft.getMinecraft().getSession().getProfile().getId().toString()
                + "&key=" + Socket.serverId);
            JsonObject clubData = new JsonParser().parse(s).getAsJsonObject();
            if (!clubData.get("success").getAsBoolean()) {
                MUtils.chat(HysentialsConfig.chatPrefix + " &c" + clubData.get("message").getAsString());
                return;
            }
            new ClubDashboard(clubData).open(Minecraft.getMinecraft().thePlayer);
        } catch (Exception e) {
        }
    }
}
