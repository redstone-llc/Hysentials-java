package llc.redstone.hysentials.guis.club;

import llc.redstone.hysentials.guis.container.Backable;
import llc.redstone.hysentials.guis.container.GuiAction;
import llc.redstone.hysentials.guis.container.GuiItem;
import llc.redstone.hysentials.guis.container.PaginationContainer;
import llc.redstone.hysentials.schema.HysentialsSchema;
import llc.redstone.hysentials.util.Input;
import llc.redstone.hysentials.util.Material;
import llc.redstone.hysentials.util.Renderer;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class ClubReplace extends PaginationContainer implements Backable {
    HysentialsSchema.Club clubData;
    Input input2 = new Input(0, 0, 0, 18);
    Input.Button button2 = new Input.Button(0, 0, 0, 18, "Cancel");
    String editKey;
    public ClubReplace(String search) {
        super("Club Replace", 6, search);
        clubData = ClubDashboard.clubData;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(int mouseX, int mouseY) {
        if (editKey != null) {
            int guiTop = this.guiChest.guiTop;
            int guiWidth = this.guiChest.xSize;
            int guiHeight = this.guiChest.ySize;
            int guiLeft = this.guiChest.guiLeft;
            int guiRight = guiLeft + guiWidth;

            int margin = 5;
            int sizeDifference = 10;

            button.width = 50;
            button2.width = 50;
            input.width = guiWidth - sizeDifference - margin - button.width + 100;
            input.xPosition = guiLeft + margin - 50;
            input.yPosition = guiTop - input.height - margin - 23;
            Renderer.drawString("Replace", input.xPosition - 45, input.yPosition + (input.height / 2));
            input2.width = guiWidth - sizeDifference - margin - button.width + 100;
            input2.xPosition = guiLeft + margin - 50;
            input2.yPosition = guiTop - input.height - margin;
            Renderer.drawString("With", input2.xPosition - 45, input2.yPosition + (input2.height / 2));


            button.xPosition = guiLeft + guiWidth - button.width - margin + 50;
            button.yPosition = guiTop - button.height - margin;
            button2.xPosition = guiLeft + guiWidth - button.width - margin + 50;
            button2.yPosition = guiTop - button.height - margin - 23;

            input.drawTextBox();
            input2.drawTextBox();
            button.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
            button2.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
        }
    }

    @Override
    protected boolean keyTyped(char typedChar, int keyCode) throws IOException {
        if (editKey != null) {
            input.textboxKeyTyped(typedChar, keyCode);
            input2.textboxKeyTyped(typedChar, keyCode);
            return false;
        }
        return true;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        input.mouseClicked(mouseX, mouseY, mouseButton);
        input2.mouseClicked(mouseX, mouseY, mouseButton);
        if (button.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY) && editKey != null) {
            if (input.getText().isEmpty()) {
                return;
            }
            JSONObject replacing = new JSONObject();
            replacing.put("replace", input.getText());
            replacing.put("with", input2.getText());
            if (editKey.equals(input.getText())) {
                clubData.getReplaceText().put(input.getText(), input2.getText());
            } else {
                clubData.getReplaceText().put(input.getText(), input2.getText());
                clubData.getReplaceText().remove(editKey);
                replacing.put("edit", editKey);
            }
            JSONObject data = new JSONObject();
            data.put("replacing", replacing);
            ClubDashboard.update(data);
            update();
            editKey = null;
            input.setText("");
            input2.setText("");
        }
        if (button2.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY) && editKey != null) {
            editKey = null;
            input.setText("");
            input2.setText("");
        }
    }

    @Override
    public List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();
        List<String> keys = new ArrayList<>(clubData.getReplaceText().keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            ItemStack item = GuiItem.makeItem(
                    Material.PAPER,
                    "§aReplacement #" + (i + 1),
                    1,
                    0,
                    "§7Replace: §f" + key,
                    "§7With: §f" + clubData.getReplaceText().get(key),
                    "",
                    "§eClick to edit",
                    "§bRight click to remove"
            );
            items.add(item);
        }
        return items;
    }

    @Override
    public void setItems() {
        super.setItems();
        setItem(53, GuiItem.makeColorfulItem(Material.PAPER, "&aAdd Replacement", 1, 0,
                "&eClick to add a new replacement"));
        setItem(52, GuiItem.makeColorfulItem(Material.WOOL, "&" + (clubData.getRegex() ? "aDisable Regex Replacement" : "cEnable Regex Replacement"), 1, clubData.getRegex() ? 5 : 14,
                "&eClick to " + (clubData.getRegex() ? "§cdisable" : "§aenable") + " &eregex replacement"));
    }

    @Override
    public void setClickActions() {
        super.setClickActions();
        setAction(53, event -> {
            event.getEvent().cancel();
            editKey = "";
            input.setText("");
            input2.setText("");
        });
        setAction(52, event -> {
            event.getEvent().cancel();
            clubData.setRegex(!clubData.getRegex());
            JSONObject data = new JSONObject();
            data.put("regex", clubData.getRegex());
            ClubDashboard.update(data);
            update();
        });
    }

    @Override
    public BiConsumer<GuiAction.GuiClickEvent, ItemStack> getAction(int index) {
        List<String> keys = new ArrayList<>(clubData.getReplaceText().keySet());
        Collections.sort(keys);
        return (event, item) -> {
            if (event.getButton() == 1) {
                clubData.getReplaceText().remove(keys.get(index));
                JSONObject data = new JSONObject();
                JSONObject replacing = new JSONObject();
                replacing.put("edit", keys.get(index));
                data.put("replacing", replacing);
                ClubDashboard.update(data);
                update();
                return;
            }
            String key = keys.get(index);
            editKey = key;
            input.writeText(key);
            input2.writeText(clubData.getReplaceText().get(key));
        };
    }

    @Override
    public void openBack() {
        new ClubDashboard(clubData).open();
    }

    @Override
    public String backTitle() {
        return "Club Dashboard";
    }

    @Override
    public int backItemSlot() {
        return 47;
    }
}
