package llc.redstone.hysentials.handlers.redworks;

import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.polyfrost.oneconfig.utils.Notifications;
import llc.redstone.hysentials.event.events.HysentialsLoadedEvent;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NeighborInstall {

//    @SubscribeEvent
//    public void onGameFinished(HysentialsLoadedEvent event) {
//        System.out.println("Hysentials loaded, checking for neighborhood pack...");
//        Multithreading.runAsync(() -> {
//            String currentVersion = "";
//            List<ResourcePackRepository.Entry> oldPacks = new ArrayList<>();
//            for (ResourcePackRepository.Entry entry : Minecraft.getMinecraft().getResourcePackRepository().getRepositoryEntriesAll()) {
//                String[] split = entry.getTexturePackDescription().split("\n");
//                if (split.length > 1) {
//                    if (split[2].startsWith("Neighbor")) {
//                        currentVersion = split[2].split(" ")[1].replace("§r", "");
//                        oldPacks.add(entry);
//                    }
//                }
//            }
//            JsonElement element = NetworkUtils.getJsonElement("https://api.github.com/repos/blockworks-studio/Neighborhood/releases");
//            String latestVersion = element.getAsJsonArray().get(0).getAsJsonObject().get("tag_name").getAsString();
//            if (currentVersion.equals(latestVersion)) {
//                return;
//            }
//            Notifications.INSTANCE.send("Neighborhood pack installation", "Starting to install the neighborhood pack. This may take a few minutes. It will automatically be added when its done.");
//
//            for (JsonElement asset : element.getAsJsonArray().get(0).getAsJsonObject().get("assets").getAsJsonArray()) {
//                JsonObject assetObject = asset.getAsJsonObject();
//                String name = assetObject.get("name").getAsString();
//                if (name.endsWith(".zip")) {
//                    String[] split = name.split("\\.");
//                    NetworkUtils.downloadFile(assetObject.get("browser_download_url").getAsString(), new File("./resourcepacks/" + "§0!    §fNeighborhood §7v1.0.0§" + split[split.length - 2] + ".zip"));
//                }
//            }
//
//            for (ResourcePackRepository.Entry entry : oldPacks) {
//                Minecraft.getMinecraft().getResourcePackRepository().getRepositoryEntriesAll().remove(entry);
//                try { //Just in case its a directory
//                    FileUtils.deleteDirectory(new File("./resourcepacks/" + entry.getResourcePackName()));
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//                new File("./resourcepacks/" + entry.getResourcePackName()).delete();
//
//
//            }
//
//            Minecraft.getMinecraft().getResourcePackRepository().updateRepositoryEntriesAll();
//
//            Notifications.INSTANCE.send("Neighborhood pack installation", "The neighborhood pack has been installed. You can now use it in the resource pack menu.");
//        });
//    }
}
