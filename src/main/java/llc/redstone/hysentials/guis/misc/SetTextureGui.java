package llc.redstone.hysentials.guis.misc;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import llc.redstone.hysentials.command.RemoveNameCommand;
import llc.redstone.hysentials.command.RenameCommand;
import llc.redstone.hysentials.guis.container.Container;
import llc.redstone.hysentials.guis.container.GuiItem;
import llc.redstone.hysentials.util.*;
import llc.redstone.hysentials.utils.StringUtilsKt;
import com.google.common.collect.Lists;
import llc.redstone.hysentials.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static llc.redstone.hysentials.util.Material.BARRIER;
import static llc.redstone.hysentials.util.Material.PAPER;

public class SetTextureGui extends Container {
    HashMap<String, List<ItemStack>> cataItems = new HashMap<>();
    public int page = 1;
    public String category = null;
    public String search = null;
    public PaginationList<ItemStack> paginationList;

    Field guiTopField;
    Field xSizeField;

    public SetTextureGui() {
        super("Select Texture", 6);
        input = new Input(0, 0, 0, 18);
        input.setEnabled(true);
        input.setText("Search Textures");
        input.setMaxStringLength(24);

        cataItems.put("Miscellaneous", new ArrayList<>());
        cataItems.put("Materials", new ArrayList<>());
        cataItems.put("Weapons", new ArrayList<>());
        cataItems.put("Tools", new ArrayList<>());
        cataItems.put("Armor", new ArrayList<>());
        cataItems.put("Seasonal", new ArrayList<>());
        try {
            guiTopField = net.minecraft.client.gui.inventory.GuiContainer.class.getDeclaredField("field_147009_r");
            xSizeField = net.minecraft.client.gui.inventory.GuiContainer.class.getDeclaredField("field_146999_f");
            guiTopField.setAccessible(true);
            xSizeField.setAccessible(true);


            URL url = new java.net.URL("https://raw.githubusercontent.com/sinender/Neighbor/main/textures.txt");

            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                String i = line.split(":")[2];
                ItemStack item = GuiItem.makeColorfulItem(PAPER, "&" + i.charAt(0) + "&" + i.charAt(1) + "&" + i.charAt(2) + "&r&a" + line.split(":")[1],
                    1, 0,
                    "&7Texture ID: &a" + i,
                    "",
                    "&eClick to select this texture.");
                ItemNBT.setString(item, "texture", i);
                if (!cataItems.containsKey(line.split(":")[0]))
                    cataItems.put(line.split(":")[0], Lists.newArrayList(item));
                else if (!cataItems.get(line.split(":")[0]).contains(item))
                    cataItems.get(line.split(":")[0]).add(item);
            }

            reader.close();
        } catch (IOException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

    }

    int[] border = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 18, 19, 27, 28, 36, 37, 46, 17, 26, 35, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};

    @Override
    public void setItems() {
        List<ItemStack> newItems = new ArrayList<>();
        if (category != null) {
            newItems = cataItems.get(category);
        } else {
            for (List<ItemStack> items : cataItems.values()) {
                newItems.addAll(items);
            }
        }
        if (search != null) {
            newItems = newItems.stream().filter(item -> C.removeColor(item.getDisplayName().toLowerCase()).contains(search.toLowerCase())).collect(Collectors.toList());
        }
        newItems.sort(Comparator.comparing(ItemStack::getDisplayName));
        paginationList = new PaginationList<>(newItems, 27);

        for (int i : border) {
            setItem(i, GuiItem.fromStack(BLACK_STAINED_GLASS_PANE));
        }

        //close
        setItem(49, GuiItem.fromStack(GuiItem.makeColorfulItem(BARRIER, "&cClose", 1, 0, "&7Close this menu.")));
        //category misc
        setItem(0, GuiItem.fromStack(GuiItem.makeColorfulItem(PAPER, "&0&9&4&aMiscellaneous", 1, 0, "&7This category has items", "&7that don't fit any", "&7of the other categories.", "", "&eClick to open!")));
        setItem(9, GuiItem.fromStack(GuiItem.makeColorfulItem(PAPER, "&0&6&9&aMaterials", 1, 0, "&7This category has items", "&7that you would find", "&7as materials.", "", "&eClick to open!")));
        setItem(18, GuiItem.fromStack(GuiItem.makeColorfulItem(PAPER, "&0&5&4&aWeapons", 1, 0, "&7This category has items", "&7that you would find", "&7as weapons.", "", "&eClick to open!")));
        setItem(27, GuiItem.fromStack(GuiItem.makeColorfulItem(PAPER, "&0&1&4&aTools", 1, 0, "&7This category has items", "&7that you would find", "&7as tools.", "", "&eClick to open!")));
        setItem(36, GuiItem.fromStack(GuiItem.makeColorfulItem(PAPER, "&0&3&6&aArmor", 1, 0, "&7This category has items", "&7that you would find", "&7as armor.", "", "&eClick to open!")));
        setItem(45, GuiItem.fromStack(GuiItem.makeColorfulItem(PAPER, "&0&9&7&aSeasonal", 1, 0, "&7This category has items", "&7that you would find", "&7as seasonal.", "&cWill change frequently!", "", "&eClick to open!")));
        int[] items = new int[]{0, 9, 18, 27, 36, 45};
        String[] catas = new String[]{"Miscellaneous", "Materials", "Weapons", "Tools", "Armor", "Seasonal"};
        for (int i = 0; i < items.length; i++) {
            int j = items[i];
            if (Arrays.asList(catas).get(i).equals(category)) {
                ItemStack item = guiItems.get(j).getItemStack();
                item.addEnchantment(Enchantment.lure, 10);
                GuiItem.hideFlag(item, 1);
                setItem(j, GuiItem.fromStack(item));
            }
        }

        if ((search != null && !search.isEmpty()) || category != null) {
            setItem(48, GuiItem.fromStack(GuiItem.makeColorfulItem(PAPER, "&0&9&2&cReset", 1, 0, "&7This will clear search", "&7aswell as the category", "&7selected!", "", "&eClick to reset!")));
        }

        if (paginationList.getPageCount() > page + 1) {
            setItem(53, GuiItem.fromStack(GuiItem.makeColorfulItem(PAPER, "&aNext Page", 1, 0, "&7(" + (page + 1) + "/" + paginationList.getPageCount() + ")", "", "&eClick to turn page!")));
        }
        if (page != 1) {
            setItem(46, GuiItem.fromStack(GuiItem.makeColorfulItem(PAPER, "&aPrevious Page", 1, 0, "&7(" + (page - 1) + "/" + paginationList.getPageCount() + ")", "", "&eClick to turn page!")));
        }

        if (paginationList.getPage(page) == null) {
            return;
        }
        if (paginationList.getPage(page).isEmpty()) {
            return;
        }
        for (int i = 0; i < paginationList.getPage(page).size(); i++) {
            if (paginationList.getPage(page).get(i) != null) {
                addItem(GuiItem.fromStack(paginationList.getPage(page).get(i)));
            }
        }
    }

    Input input;

    @Override
    protected void drawGuiContainerBackgroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(mouseX, mouseY);
        GlStateManager.pushMatrix();
        int guiTop = this.guiChest.guiTop;
        int guiWidth = this.guiChest.xSize;

        int margin = 5;
        int sizeDifference = 10;

        input.width = guiWidth - sizeDifference - margin;
        input.xPosition = Renderer.screen.getWidth() / 2 - input.width / 2;
        input.yPosition = guiTop - input.height - margin;

        input.drawTextBox();
        GlStateManager.popMatrix();
    }

    @Override
    protected boolean keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (input.isFocused()) {
            input.textboxKeyTyped(typedChar, keyCode);
            if (keyCode == 1) { // keycode for escape key
                Minecraft.getMinecraft().thePlayer.closeScreen();
                return false;
            }
            if (keyCode == 15) { // keycode for tab key
                return false;
            }
            if (keyCode == 28) { // keycode for enter key
                return false;
            }
            //keycode for e key
            if (keyCode == 18) {
                return false;
            }
            search = input.getText();
            page = 1;
            update();
        }
        return true;
    }

    @Override
    protected void mouseClicked(int x, int y, int mouseButton) throws IOException {
        super.mouseClicked(x, y, mouseButton);

        input.mouseClicked(x, y, mouseButton);
        if (x > input.xPosition && x < input.xPosition + input.getWidth() && y > input.yPosition && y < input.yPosition + input.height) {
            if (input.getText().equals("Search Textures")) {
                input.setText("");
                input.setCursorPosition(0);
            }
            input.setEnabled(true);
            input.setFocused(true);
        } else {
            input.setEnabled(false);
            input.setFocused(false);
        }
    }

    @Override
    public void handleMenu(MouseEvent event) {

    }

    @Override
    public void setClickActions() {
        setDefaultAction((e) -> {
            e.getEvent().cancel();
            if (!guiItems.containsKey(e.getSlot())) {
                return;
            }
            ItemStack i = guiItems.get(e.getSlot()).getItemStack();
            if (!i.hasDisplayName() || i.getDisplayName().equals("§0")) {
                return;
            }
            if (e.getSlot() == 0 || e.getSlot() == 9 || e.getSlot() == 18 || e.getSlot() == 27 || e.getSlot() == 36 || e.getSlot() == 45) {
                category = C.removeColor(i.getDisplayName());
                page = 1;
                update();
                return;
            }

            String texture = ItemNBT.getString(i, "texture");
            if (texture == null) {
                return;
            }
            if (texture.length() != 3) {
                return;
            }
            ItemStack item = Minecraft.getMinecraft().thePlayer.getHeldItem();
            if (item.getDisplayName().matches("§[0-9A-FK-OR]§[0-9A-FK-OR]§[0-9A-FK-OR][a-zA-Z0-9 ]*")) {
                item.setStackDisplayName(item.getDisplayName().replaceFirst("§[0-9A-FK-OR]§[0-9A-FK-OR]§[0-9A-FK-OR]", "§" + texture.charAt(0) + "§" + texture.charAt(1) + "§" + texture.charAt(2)));
            } else {
                item.setStackDisplayName("§" + texture.charAt(0) + "§" + texture.charAt(1) + "§" + texture.charAt(2) + item.getDisplayName());
            }
            RenameCommand.setCreativeAction(item, Minecraft.getMinecraft().thePlayer.inventory.currentItem);
            UChat.chat("&aItem texture set to: " + item.getDisplayName());
        });

        setAction(49, (e) -> {
            e.getEvent().cancel();
            Minecraft.getMinecraft().thePlayer.closeScreen();
        });

        setAction(48, (e) -> {
            e.getEvent().cancel();
            category = null;
            search = null;
            page = 1;
            input.setText("Search Textures");
            update();
        });

        setAction(53, (e) -> {
            if (paginationList.getPageCount() <= page + 1) {
                e.getEvent().cancel();
                return;
            }
            e.getEvent().cancel();
            page++;
            update();
        });

        setAction(46, (e) -> {
            if (page == 1) {
                e.getEvent().cancel();
                return;
            }
            e.getEvent().cancel();
            page--;
            update();
        });
    }
}
