package llc.redstone.hysentials.handlers.misc;

import cc.polyfrost.oneconfig.utils.Multithreading;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.HysentialsUtilsKt;
import llc.redstone.hysentials.config.hysentialMods.ReplaceConfig;
import llc.redstone.hysentials.config.hysentialMods.replace.ReplaceOption;
import llc.redstone.hysentials.config.hysentialMods.replace.ReplaceStuff;
import llc.redstone.hysentials.event.events.HousingJoinEvent;
import llc.redstone.hysentials.event.events.HousingLeaveEvent;
import llc.redstone.hysentials.handlers.imageicons.ImageIcon;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static llc.redstone.hysentials.util.NetworkUtils.setupConnection;

public class HousingJoinHandler {
    boolean isHousing = false;

    @SubscribeEvent
    public void onWorldJoin(WorldEvent.Load event) {
        if (isHousing) {
            MinecraftForge.EVENT_BUS.post(new HousingLeaveEvent());
        }
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
                    JsonObject object = data.getAsJsonObject();
                    if (object.has("club")) {
                        JsonObject club = object.getAsJsonObject("club");
                        BlockWAPIUtils.currentHousingsClub = HysentialsSchema.Club.Companion.deserialize(club);
                        System.out.println("Club: " + BlockWAPIUtils.currentHousingsClub.getName());
                        List<ReplaceStuff> clubReplacements = Hysentials.INSTANCE.getConfig().replaceConfig.clubReplacements;
                        ReplaceStuff replaceStuff = clubReplacements.stream().filter(replaceStuff1 -> replaceStuff1.clubName.equals(BlockWAPIUtils.currentHousingsClub.getName())).findFirst().orElse(null);
                        if (replaceStuff != null) {
                            for (String key : BlockWAPIUtils.currentHousingsClub.getReplaceText().keySet()) {
                                if (!replaceStuff.replacements.containsKey(key) && !replaceStuff.deleted.contains(key)) {
                                    replaceStuff.replacements.put(key, BlockWAPIUtils.currentHousingsClub.getReplaceText().get(key));
                                    replaceStuff.clubReplacements.add(key);
                                }
                            }

                            //remove any keys that are no longer in the club
                            for (String key : replaceStuff.replacements.keySet()) {
                                if (!BlockWAPIUtils.currentHousingsClub.getReplaceText().containsKey(key) && replaceStuff.clubReplacements.contains(key)) {
                                    replaceStuff.replacements.remove(key);
                                    replaceStuff.clubReplacements.remove(key);
                                }
                            }
                        } else {
                            replaceStuff = new ReplaceStuff(BlockWAPIUtils.currentHousingsClub.getName());
                            for (String key : BlockWAPIUtils.currentHousingsClub.getReplaceText().keySet()) {
                                replaceStuff.replacements.put(key, BlockWAPIUtils.currentHousingsClub.getReplaceText().get(key));
                                replaceStuff.clubReplacements.add(key);
                            }
                            clubReplacements.add(replaceStuff);
                        }

                        cacheIcons(BlockWAPIUtils.currentHousingsClub);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //clubID -> iconID
    public static HashMap<String, List<String>> clubIcons = new HashMap<>();

    private void cacheIcons(HysentialsSchema.Club club) {
        if (club.getIcons() != null) {
            Multithreading.runAsync(() -> {
                try {
                    if (!new File("./config/hysentials/imageicons/clubs").exists()) {
                        new File("./config/hysentials/imageicons/clubs").mkdirs();
                    }
                    if (!new File("./config/hysentials/imageicons/clubs/" + club.getId()).exists()) {
                        new File("./config/hysentials/imageicons/clubs/" + club.getId()).mkdirs();
                    }
                    for (String key : club.getIcons().keySet()) {
                        String url = club.getIcons().get(key);
                        if (url != null) {
                            clubIcons.computeIfAbsent(club.getId(), k -> new ArrayList<>());
                            if (clubIcons.get(club.getId()).contains(key)) {
                                continue;
                            }
                            try (BufferedInputStream in = new BufferedInputStream(setupConnection(url, "OneConfig/1.0.0", 5000, false));
                                 FileOutputStream fileOutputStream = new FileOutputStream("./config/hysentials/imageicons/clubs/" + club.getId() + "/" + key + ".png")) {
                                byte[] data = new byte[1024];
                                int count;
                                while ((count = in.read(data, 0, 1024)) != -1) {
                                    fileOutputStream.write(data, 0, count);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            File file = new File("./config/hysentials/imageicons/clubs/" + club.getId() + "/" + key + ".png");
                            if (file.exists()) {
                                clubIcons.compute(club.getId(), (k, v) -> {
                                    if (v == null) {
                                        v = new ArrayList<>();
                                    }
                                    v.add(key);
                                    return v;
                                });
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                Minecraft.getMinecraft().addScheduledTask(() -> {
                    for (String icon : clubIcons.get(club.getId())) {
                        File file = new File("./config/hysentials/imageicons/clubs/" + club.getId() + "/" + icon + ".png");
                        if (!file.exists()) continue;
                        try {
                            System.out.println("Loading icon " + icon);
                            new ImageIcon(icon, "./config/hysentials/imageicons/clubs/" + club.getId() + "/" + icon + ".png", false);
                            System.out.println("Loaded icon " + icon);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            });
        }
    }

    @SubscribeEvent
    public void onHousingLeave(HousingLeaveEvent event) {
        BlockWAPIUtils.currentHousingsClub = null;

        for (String key : clubIcons.keySet()) {
            for (String icon : clubIcons.get(key)) {
                ImageIcon.imageIcons.remove(icon);
            }
        }
    }
}
