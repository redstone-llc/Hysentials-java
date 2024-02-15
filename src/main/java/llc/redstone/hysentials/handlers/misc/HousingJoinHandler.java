package llc.redstone.hysentials.handlers.misc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.HysentialsUtilsKt;
import llc.redstone.hysentials.event.events.HousingJoinEvent;
import llc.redstone.hysentials.handlers.redworks.HousingScoreboard;
import llc.redstone.hysentials.handlers.sbb.SbbRenderer;
import llc.redstone.hysentials.schema.HysentialsSchema;
import llc.redstone.hysentials.util.BlockWAPIUtils;
import llc.redstone.hysentials.util.C;
import llc.redstone.hysentials.util.NetworkUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HousingJoinHandler {
    boolean isHousing = false;

    @SubscribeEvent
    public void onWorldJoin(WorldEvent.Load event) {
        isHousing = false;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (SbbRenderer.housingScoreboard != null && SbbRenderer.housingScoreboard.getHousingName() != null && !isHousing) {
            isHousing = true;
            MinecraftForge.EVENT_BUS.post(new HousingJoinEvent(SbbRenderer.housingScoreboard.getHousingName(), SbbRenderer.housingScoreboard.getHousingCreator()));
        }
    }

    @SubscribeEvent
    public void onHousingJoin(HousingJoinEvent event) {
        System.out.println("Just joined housing " + event.getHousingName() + " with owner " + event.getPlayerName());
        try {
            JSONObject request = new JSONObject();
            request.put("houseName", C.removeColor(event.getHousingName()));
            request.put("playerName", event.getPlayerName());
            try (InputStreamReader input = new InputStreamReader(Hysentials.post(HysentialsUtilsKt.getHYSENTIALS_API() + "/club", request), StandardCharsets.UTF_8)) {
                String s = IOUtils.toString(input);
                JsonElement data = new JsonParser().parse(s);
                if (data != null && data.isJsonObject()) {
                    System.out.println(data.toString());
                    JsonObject object = data.getAsJsonObject();
                    if (object.has("club")) {
                        JsonObject club = object.getAsJsonObject("club");
                        BlockWAPIUtils.currentHousingsClub = HysentialsSchema.Club.Companion.deserialize(club);
                        System.out.println("Just joined housing " + event.getHousingName() + " with club " + BlockWAPIUtils.currentHousingsClub.getName() + " and owner " + event.getPlayerName());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
