package llc.redstone.hysentials.guis.container;

import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.event.events.GuiMouseClickEvent;
import llc.redstone.hysentials.util.C;
import llc.redstone.hysentials.util.Material;
import llc.redstone.hysentials.util.PaginationList;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class PaginationContainer extends Container {
    public int page = 1;
    public int maxPages = 1;
    public String search = "";
    public PaginationList<ItemStack> paginationList;

    public PaginationContainer(String title, int rows, String search) {
        super(title, rows);
        this.search = search;
    }

    public abstract List<ItemStack> getItems();

    public abstract BiConsumer<GuiAction.GuiClickEvent, ItemStack> getAction();

    @Override
    public void setItems() {
        //set the outline
        border(GuiItem.fromStack(BLACK_STAINED_GLASS_PANE));
        //set the pagination items
        List<ItemStack> newItems = getItems();
        if (search != null) {
            newItems = newItems.stream().filter(item -> C.removeColor(item.getDisplayName().toLowerCase()).contains(search.toLowerCase())).collect(Collectors.toList());
        }

        newItems.sort(Comparator.comparing(ItemStack::getDisplayName));

        paginationList = new PaginationList<>(newItems, ((rows - 2) * 7) - 1);
        maxPages = paginationList.size();
        if (page > maxPages) {
            page = maxPages;
        }
        if (page < 1) {
            page = 1;
        }

        List<ItemStack> pageItems = paginationList.getPage(page);
        try {
            for (ItemStack pageItem : pageItems) {
                addItem(GuiItem.fromStack(pageItem));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (page > 1) {
            setItem(45, GuiItem.makeColorfulItem(Material.ARROW, "&aPrevious Page", 1, 0));
        }
        if (page < maxPages) {
            setItem(53, GuiItem.makeColorfulItem(Material.ARROW, "&aNext Page", 1, 0));
        }
        //close
        setItem(49, GuiItem.makeColorfulItem(Material.BARRIER, "&cClose", 1, 0));
        //search
        if (search != null) {
            setItem(48, GuiItem.makeColorfulItem(Material.HOPPER, "&aSearch", 1, 0, "&7Current Search: &a" + search, "", "&eClick to search"));
        } else {
            setItem(48, GuiItem.makeColorfulItem(Material.HOPPER, "&aSearch", 1, 0, "&7Current Search: &aNone", "", "&eClick to search"));
        }

        //back
        if (this instanceof Backable) {
            Backable back = ((Backable) this);
            setItem(back.backItemSlot(), GuiItem.makeColorfulItem(Material.ARROW, "&a" + back.backTitle(), 1, 0));
        }
    }

    @Override
    public void handleMenu(MouseEvent event) {

    }

    @Override
    public void setClickActions() {
        setDefaultAction(event -> {
            event.getEvent().cancel();
        });
        setAction(45, event -> {
            page--;
            update();
        });
        setAction(53, event -> {
            page++;
            update();
        });
        setAction(49, event -> {
            event.getEvent().cancel();
            Minecraft.getMinecraft().thePlayer.closeScreen();
        });
        setAction(48, event -> {
            event.getEvent().cancel();
            Minecraft.getMinecraft().thePlayer.closeScreen();
            Hysentials.INSTANCE.sendMessage("&aPlease enter your search query in chat.");
            chatRequest(message -> {
                search = message;
                this.open();
                return null;
            }, 30000);
        });
        int[] slots = new int[((rows - 2) * 7)];
        for (int i = 0; i < rows - 2; i++) {
            for (int j = 0; j < 7; j++) {
                slots[((i * 7) + j)] = ((i + 1) * 9) + j;
            }
        }

        for (int i = 0; i < slots.length; i++) {
            int slot = slots[i];
            int finalI = i;
            setAction(slot, event -> {
                event.getEvent().cancel();
                ItemStack item = paginationList.getPage(page).get(finalI - 1);
                getAction().accept(event, item);
            });
        }

        if (this instanceof Backable) {
            Backable back = ((Backable) this);
            setAction(back.backItemSlot(), event -> {
                event.getEvent().cancel();
                back.openBack();
            });
        }
    }

}
