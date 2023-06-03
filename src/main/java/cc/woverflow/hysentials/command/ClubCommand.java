package cc.woverflow.hysentials.command;

import cc.polyfrost.oneconfig.libs.universal.UChat;
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

import static cc.woverflow.hysentials.handlers.redworks.BwRanks.randomString;

@Command(value = "club", description = "Club Commands")
public class ClubCommand {
    @SubCommand(aliases = {"create"}, description = "Create a club")
    public void create(String name) {
        Multithreading.runAsync(() -> {
            String id = randomString(8);
            JSONObject json = new JSONObject();
            json.put("name", name);
            json.put("owner", Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString());
            try (InputStreamReader input = new InputStreamReader(Hysentials.post("https://hysentials.redstone.llc/api/club/create"
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
        Socket.CLIENT.send(new Request(
            "method", "clubAccept",
            "club", name,
            "uuid", Minecraft.getMinecraft().getSession().getProfile().getId(),
            "serverId", Socket.serverId
            ).toString());
    }

    @SubCommand(aliases = {"list"}, description = "Leave a club")
    public void list() {
        try {
            String s = NetworkUtils.getString("https://hysentials.redstone.llc/api/club?uuid="
                + Minecraft.getMinecraft().getSession().getProfile().getId().toString()
                + "&key=" + Socket.serverId);
            JSONObject clubData = new JSONObject(s);
            if (!clubData.getBoolean("success")) {
                UChat.chat(HysentialsConfig.chatPrefix + " &c" + clubData.getString("message"));
                return;
            }
            JSONArray members = clubData.getJSONArray("members");
            Multithreading.runAsync(() -> {
                HashMap<String, String> userMap = new HashMap<>();
                for (int i = 0; i < members.length(); i++) {
                    String uuid = members.getString(i);
                    String name = HypixelAPIUtils.getUsername(uuid);
                    userMap.put(uuid, name);
                }

                List<String> displayNames = new ArrayList<>();
                for (int i = 0; i < members.length(); i++) {
                    String uuid = members.getString(i);
                    String name = userMap.get(uuid);
                    displayNames.add(HypixelAPIUtils.getDisplayName(name, uuid));
                }

                UChat.chat(HysentialsConfig.chatPrefix + " &aClub Members:");
                for (String displayName : displayNames) {
                    UChat.chat("   - &a" + displayName);
                }
            });
        } catch (Exception e) {

        }
    }

    @Main()
    public void main() {
        try {
            String s = NetworkUtils.getString("https://hysentials.redstone.llc/api/club?uuid="
                + Minecraft.getMinecraft().getSession().getProfile().getId().toString()
                + "&key=" + Socket.serverId);
            JSONObject clubData = new JSONObject(s);
            if (!clubData.getBoolean("success")) {
                UChat.chat(HysentialsConfig.chatPrefix + " &c" + clubData.getString("message"));
                return;
            }
        } catch (Exception e) {

        }
        new ClubDashboard().open(Minecraft.getMinecraft().thePlayer);
    }
}
