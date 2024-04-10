package llc.redstone.hysentials.guis.club;

import llc.redstone.hysentials.guis.container.Backable;
import llc.redstone.hysentials.guis.container.GuiAction;
import llc.redstone.hysentials.guis.container.GuiItem;
import llc.redstone.hysentials.guis.container.PaginationContainer;
import llc.redstone.hysentials.schema.HysentialsSchema;
import llc.redstone.hysentials.util.Material;
import net.minecraft.item.ItemStack;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class ClubIcons extends PaginationContainer implements Backable {
    HysentialsSchema.Club clubData;
    public ClubIcons(String search) {
        super("Club Icons", 6, search);
        clubData = ClubDashboard.clubData;
    }

    @Override
    public void openBack() {
        new ClubDashboard(clubData);
    }

    @Override
    public String backTitle() {
        return "Club Dashboard";
    }

    @Override
    public int backItemSlot() {
        return 45;
    }

    @Override
    public List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();
        List<String> keys = new ArrayList<>(clubData.getIcons().keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            ItemStack item = GuiItem.makeItem(
                Material.DOUBLE_PLANT,
                "§aIcon #" + (i + 1),
                1,
                0,
                "",
                "§7Icon: :" + key + ":",
                "§bRight click to remove"
            );
            items.add(item);
        }
        return items;
    }

    @Override
    public void setItem(int slot, GuiItem item) {
        super.setItem(slot, item);
        setItem(53, GuiItem.makeColorfulItem(Material.PAPER, "&aAdd Replacement", 1, 0,
            "&7You can add Image Icons in our discord",
                  "&b&nhttps://discord.gg/mtAXV24bqM",
                  "&7And then do &b/clubicon <iconName>",
                  "&7and attach the image"));
    }

    @Override
    public BiConsumer<GuiAction.GuiClickEvent, ItemStack> getAction(int index) {
        List<String> keys = new ArrayList<>(clubData.getIcons().keySet());
        Collections.sort(keys);
        return (event, item) -> {
            if (event.getButton() == 1) {
                clubData.getIcons().remove(keys.get(index));
                JSONObject data = new JSONObject();
                data.put("removeIcon", keys.get(index));
                ClubDashboard.update(data);
                update();
            }
        };
    }
}
