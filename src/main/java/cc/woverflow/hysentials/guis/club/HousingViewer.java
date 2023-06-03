package cc.woverflow.hysentials.guis.club;

import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.woverflow.hysentials.guis.container.Container;
import cc.woverflow.hysentials.guis.container.GuiItem;
import cc.woverflow.hysentials.util.Material;
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
    JSONObject clubData;
    Map<Integer, List<Integer>> slotsMap = new HashMap<>();

    public HousingViewer(JSONObject clubData) {
        super(clubData.getString("name") + " Houses", 3);
        this.clubData = clubData;
        slotsMap.put(1, Arrays.asList(13));
        slotsMap.put(2, Arrays.asList(12, 14));
        slotsMap.put(3, Arrays.asList(12, 13, 14));
        slotsMap.put(4, Arrays.asList(11, 12, 13, 14));
        slotsMap.put(5, Arrays.asList(11, 12, 13, 14, 15));
    }

    @Override
    public void setItems() {
        JSONArray houses = clubData.getJSONArray("houses");
        for (int i = 0; i < Math.min(houses.length(), 5); i++) {
            JSONObject house = houses.getJSONObject(i);
            ItemStack item = ClubDashboard.getItemfromNBT(house.getString("nbt"));
            if (clubData.getString("owner").equals(Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString())) {
                List<String> lore = getLore(item);
                lore.add("§bRight click to remove");
                setLore(item, lore);
            }
            setItem(slotsMap.get(houses.length()).get(i), GuiItem.fromStack(
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
            JSONArray houses = clubData.getJSONArray("houses");
            for (int i = 0; i < Math.min(houses.length(), 5); i++) {
                JSONObject house = houses.getJSONObject(i);
                int slot = slotsMap.get(houses.length()).get(i);
                if (event.getSlot() == slot) {
                    if (clubData.getString("owner").equals(Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString()) && event.getButton() == 1) {
                        JSONObject data = new JSONObject();
                        data.put("houses", house);
                        data.put("remove", true);
                        Multithreading.runAsync(() -> {
                            ClubDashboard.clubData = ClubDashboard.getClub();
                            ClubDashboard.update(data);
                            new HousingViewer(Objects.requireNonNull(ClubDashboard.getClub())).open();
                        });
                    } else {
                        Minecraft.getMinecraft().thePlayer.closeScreen();
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/visit " + house.getString("username") + " " + house.getString("name"));
                    }
                }
            }
        });
    }
}
