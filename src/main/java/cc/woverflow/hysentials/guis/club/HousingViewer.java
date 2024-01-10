package cc.woverflow.hysentials.guis.club;

import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.woverflow.hysentials.guis.container.Container;
import cc.woverflow.hysentials.guis.container.GuiItem;
import cc.woverflow.hysentials.schema.HysentialsSchema;
import cc.woverflow.hysentials.util.Material;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.MouseEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import scala.util.parsing.json.JSON;

import java.util.*;

import static cc.woverflow.hysentials.guis.container.GuiItem.getLore;
import static cc.woverflow.hysentials.guis.container.GuiItem.setLore;

public class HousingViewer extends Container {
    HysentialsSchema.Club clubData;
    Map<Integer, List<Integer>> slotsMap = new HashMap<>();

    public HousingViewer(HysentialsSchema.Club clubData) {
        super(clubData.getName() + " Houses", 3);
        this.clubData = clubData;
        slotsMap.put(1, Arrays.asList(13));
        slotsMap.put(2, Arrays.asList(12, 14));
        slotsMap.put(3, Arrays.asList(12, 13, 14));
        slotsMap.put(4, Arrays.asList(11, 12, 13, 14));
        slotsMap.put(5, Arrays.asList(11, 12, 13, 14, 15));
    }

    @Override
    public void setItems() {
        List<JsonObject> houses = clubData.getHouses();
        for (int i = 0; i < Math.min(houses.size(), 5); i++) {
            JsonObject house = houses.get(i).getAsJsonObject();
            ItemStack item = ClubDashboard.getItemfromNBT(house.get("nbt").getAsString());
            if (clubData.getOwner().equals(Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString())) {
                List<String> lore = getLore(item);
                lore.add("§bRight click to remove");
                setLore(item, lore);
            }
            setItem(slotsMap.get(houses.size()).get(i), GuiItem.fromStack(
                item
            ));
        }
    }

    @Override
    public void handleMenu(MouseEvent event) {

    }

    @Override
    public void setClickActions() {
        setDefaultAction((event) -> {
            event.getEvent().cancel();
            List<JsonObject> houses = clubData.getHouses();
            for (int i = 0; i < Math.min(houses.size(), 5); i++) {
                JsonObject house = houses.get(i).getAsJsonObject();
                int slot = slotsMap.get(houses.size()).get(i);
                if (event.getSlot() == slot) {
                    if (clubData.getOwner().equals(Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString()) && event.getButton() == 1) {
                        JSONObject data = new JSONObject();
                        data.put("houses", house);
                        data.put("remove", true);
                        Multithreading.runAsync(() -> {
                            ClubDashboard.getClub();
                            ClubDashboard.update(data);
                            new HousingViewer(ClubDashboard.clubData).open();
                        });
                    } else {
                        Minecraft.getMinecraft().thePlayer.closeScreen();
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/visit " + house.get("username").getAsString() + " " + house.get("name").getAsString());
                    }
                }
            }
        });
    }
}
