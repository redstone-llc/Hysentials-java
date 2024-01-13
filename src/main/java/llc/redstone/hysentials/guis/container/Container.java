package llc.redstone.hysentials.guis.container;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.Multithreading;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.event.EventBus;
import llc.redstone.hysentials.event.events.GuiMouseClickEvent;
import llc.redstone.hysentials.util.MUtils;
import llc.redstone.hysentials.util.Material;
import llc.redstone.hysentials.websocket.Socket;
import com.google.common.collect.Lists;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.websocket.Socket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInvBasic;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Container extends InventoryBasic {
    protected String title;
    public static Container INSTANCE;
    public static HashMap<UUID, Function<String, String>> chatRequests = new HashMap<>();
    protected int rows = 1;
    public Map<Integer, GuiItem> guiItems;
    public Map<Integer, GuiAction> slotActions;
    public GuiChestCustom guiChest;
    public boolean isOpen;
    public GuiAction defaultAction;
    public ItemStack[] inventoryContents;
    public ItemStack BLACK_STAINED_GLASS_PANE = GuiItem.makeColorfulItem(Material.STAINED_GLASS_PANE, "&0", 1, 15);

    public Container(String title, int rows) {
        super(title, true, rows*9);
        this.title = title;
        this.rows = rows;
        this.inventoryContents = new ItemStack[rows*9];
        guiItems = new HashMap<>();
        slotActions = new HashMap<>();
        INSTANCE = this;
    }

    public void setItem(int slot, GuiItem item) {
        guiItems.put(slot, item);
    }

    public void setItem(int slot, ItemStack item) {
        guiItems.put(slot, GuiItem.fromStack(item));
    }

    public void addItem(GuiItem item) {
        for (int i = 0; i < rows*9; i++) {
            if (!guiItems.containsKey(i)) {
                setItem(i, item);
                return;
            }
        }
    }


    public void fill(GuiItem item) {
        for (int i = 0; i < rows*9; i++) {
            if (!guiItems.containsKey(i) || guiItems.get(i).getItemStack().getItem() == item.getItemStack().getItem()) {
                setItem(i, item);
            }
        }
    }

    public void border(GuiItem item) {
        for (int i = 0; i < rows*9; i++) {
            if (i < 9 || i > (rows*9)-9 || i % 9 == 0 || i % 9 == 8) {
                setItem(i, item);
            }
        }
    }

    public void setAction(int slot, GuiAction action) {
        slotActions.put(slot, action);
    }

    public void setDefaultAction(GuiAction action) {
        defaultAction = action;
    }

    /**
     * @param function The function to be called when the entered text is received
     * @param expire  The time in milliseconds before the request expires
     */
    protected void chatRequest(Function<String, String> function, long expire) {
        UUID id = UUID.randomUUID();
        chatRequests.put(id, function);
        Multithreading.schedule(() -> {
            chatRequests.remove(id);
        }, expire, TimeUnit.MILLISECONDS);
    }

    public abstract void setItems();

    public abstract void handleMenu(MouseEvent event);

    public abstract void setClickActions();

    protected void drawGuiContainerBackgroundLayer(int mouseX, int mouseY) {

    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

    }

    protected boolean keyTyped(char typedChar, int keyCode) throws IOException {
        return true;
    }

    public void update() {
        if (isOpen) {
            guiItems.clear();
            for (int i = 0; i < rows*9; i++) {
                setInventorySlotContents(i, null);
            }
            setItems();
            for (Map.Entry<Integer, GuiItem> entry : guiItems.entrySet()) {
                Integer slot = entry.getKey();
                GuiItem item = entry.getValue();
                setInventorySlotContents(slot, item.getItemStack());
            }
            slotActions.clear();
            setClickActions();
        }
    }

    public void open() {
        open(Minecraft.getMinecraft().thePlayer);
    }

    public void open(@NotNull EntityPlayer owner) {
        if (!Socket.linked) {
            UChat.chat("&cYou must be linked to a discord account to use this feature.");
            return;
        }
        setItems();
        for (Map.Entry<Integer, GuiItem> entry : guiItems.entrySet()) {
            Integer slot = entry.getKey();
            GuiItem item = entry.getValue();
            setInventorySlotContents(slot, item.getItemStack());
        }
        setClickActions();
        isOpen = true;
        guiChest = new GuiChestCustom(owner.inventory, this) {
            @Override
            protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
                super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
                Container.this.drawGuiContainerBackgroundLayer(mouseX, mouseY);
            }

            @Override
            protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
                super.drawGuiContainerForegroundLayer(mouseX, mouseY);
                Container.this.drawGuiContainerForegroundLayer(mouseX, mouseY);
            }

            @Override
            public void onGuiClosed() {
                super.onGuiClosed();
                isOpen = false;
                guiChest = null;
            }

            @Override
            protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
                super.mouseClicked(mouseX, mouseY, mouseButton);
                Container.this.mouseClicked(mouseX, mouseY, mouseButton);
            }

            @Override
            protected void keyTyped(char typedChar, int keyCode) throws IOException {
                if (Container.this.keyTyped(typedChar, keyCode)) {
                    super.keyTyped(typedChar, keyCode);
                }
            }
        };
        Hysentials.INSTANCE.guiDisplayHandler.setDisplayNextTick(guiChest);
    }
}
