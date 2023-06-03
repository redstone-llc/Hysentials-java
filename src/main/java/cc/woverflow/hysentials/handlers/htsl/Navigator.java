package cc.woverflow.hysentials.handlers.htsl;

import cc.polyfrost.oneconfig.libs.checker.units.qual.N;
import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.event.events.GuiCloseEvent;
import cc.woverflow.hysentials.event.events.RenderItemInGuiEvent;
import cc.woverflow.hysentials.util.Renderer;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.registry.GameData;
import org.json.JSONObject;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static cc.woverflow.hysentials.handlers.guis.GameMenuOpen.field_lowerChestInventory;
import static cc.woverflow.hysentials.htsl.ModifyAnvilOutput.modifyOutput;
import static cc.woverflow.hysentials.util.Renderer.getImageFromUrl;

public class Navigator {
    private static DynamicTexture ARROW_TEXTURE_LOCATION;
    public static String optionBeingSelected;
    private static final int ARROW_SIZE = 50;
    private static final int ARROW_OFFSET_X = 10;
    private static final int ARROW_OFFSET_Y = -45;

    private static int SLOT_TO_MANUALLY_CLICK = -1;
    private static final int BACK_BUTTON_SLOT_OFFSET = 5;

    private static boolean drawArrow = false;
    private static int drawArrowX = 0;
    private static int drawArrowY = 0;
    public static List<String> itemsLoaded = new ArrayList<>();

    public static boolean isReady = false;
    public static boolean isSelecting = false;
    public static boolean isGoingPageOne = false;
    public static boolean isReturning = false;
    public static boolean isLoadingItem = false;
    public static boolean guiIsLoading = true;
    public static long lastItemAddedTimestamp = 0;

    private static final int ANVIL_SLOT = 2;
    private static final String ANVIL_GUI = "minecraft:anvil";

    private static Field guiLeft;
    private static Field guiTop;
    private static Field chatGuiInputField;

    public Navigator() {
        try {
            ARROW_TEXTURE_LOCATION = new DynamicTexture(getImageFromUrl("https://raw.githubusercontent.com/BusterBrown1218/HTSL/main/assets/red-arrow.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            guiTop = GuiContainer.class.getDeclaredField("field_147009_r");
            guiTop.setAccessible(true);
            guiLeft = GuiContainer.class.getDeclaredField("field_147003_i");
            guiLeft.setAccessible(true);
            chatGuiInputField = GuiChat.class.getDeclaredField("field_146415_a");
            chatGuiInputField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setArrowToSlot(int slotId) throws IllegalAccessException {
        Slot slot = Minecraft.getMinecraft().thePlayer.openContainer.getSlot(slotId);
        int slotX = slot.xDisplayPosition;
        int slotY = slot.yDisplayPosition;
        int guiTop = Navigator.guiTop.getInt(Minecraft.getMinecraft().currentScreen);
        int guiLeft = Navigator.guiLeft.getInt(Minecraft.getMinecraft().currentScreen);
        drawArrowX = slotX + guiLeft + ARROW_OFFSET_X;
        drawArrowY = slotY + guiTop + ARROW_OFFSET_Y;
        setNotReady();
        drawArrow = true;
    }

    public static void click(int slotId) {
        if (HysentialsConfig.htslSafeMode) {
            SLOT_TO_MANUALLY_CLICK = slotId;
            try {
                setArrowToSlot(slotId);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else {
            Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().thePlayer.openContainer.windowId, slotId, 2, 3, Minecraft.getMinecraft().thePlayer);
            setNotReady();
        }
    }

    public static void returnToEditActions() {
        isReturning = true;
        String containerName = getContainerName();
        if (containerName != null && (containerName.equals("Edit Actions") || containerName.startsWith("Actions: "))) {
            isReturning = false;
            return;
        }
        goBack();
    }

    public static String getContainerName() {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiChest) {
            GuiChest chest = (GuiChest) Minecraft.getMinecraft().currentScreen;
            try {
                if (field_lowerChestInventory.get(chest) instanceof IInventory) {
                    IInventory inventory = (IInventory) field_lowerChestInventory.get(chest);
                    return inventory.getName();
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public static void setSelecting(String option) {
        isSelecting = true;
        optionBeingSelected = option;
    }

    public static void setSelecting(int slot, int page) {
        isSelecting = true;
        optionBeingSelected = slot + ":" + page;
    }

    public static void selectItem(JSONObject item) {
        switch (item.getString("type")) {
            case "customItem":
                ItemStack itemStack = getItemFromNBT(item.getJSONObject("itemData").getString("item").replace("\\", ""));
                isLoadingItem = true;
                utilLoadItem(itemStack, 26);
                setNotReady();
                break;
            case "clickSlot":
                click(item.getInt("slot") + 35);
                break;
        }
    }

    public static ItemStack getItemFromNBT(String nbt) {
        try {
            return ItemStack.loadItemStackFromNBT(JsonToNBT.getTagFromJson(nbt));
        } catch (NBTException e) {
            throw new RuntimeException(e);
        }
    }

    public static void utilLoadItem(ItemStack item, int slot) {
        //send packet C10PacketCreativeInventoryAction
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(slot, item));
    }

    public static boolean selectOption(String optionName) {
        Container playerContainer = Minecraft.getMinecraft().thePlayer.openContainer;
        for (int index = 0; index < playerContainer.inventorySlots.size(); index++) {
            ItemStack item = playerContainer.getSlot(index).getStack();
            if (item == null) continue; // Skip empty slots
            if (ChatColor.Companion.stripControlCodes(item.getDisplayName()).equals(optionName)) {
                click(index);
                isSelecting = false;
                return true;
            }
        }
        ItemStack item = playerContainer.getSlot(53).getStack();
        if (item != null && GameData.getItemRegistry().getId(item.getItem()) == 262) {
            click(53);
            return true;
        }
        goBack();
        isSelecting = false;
        return false;
    }

    public static boolean pageOne() {
        Container playerContainer = Minecraft.getMinecraft().thePlayer.openContainer;
        int currentPage = 1;
        if (Navigator.getContainerName().matches("\\(\\d/\\d\\)")) {
            currentPage = Integer.parseInt(Navigator.getContainerName().split("/")[0].replace("(", ""));
        }

        ItemStack item = playerContainer.getSlot(45).getStack();
        if (item != null && GameData.getItemRegistry().getId(item.getItem()) == 262) {
            click(45);
            return true;
        } else {
            return false;
        }
    }

    public static boolean selectOption(int slot, int page) {
        Container playerContainer = Minecraft.getMinecraft().thePlayer.openContainer;
        int currentPage = 1;
        if (Navigator.getContainerName().split("/").length > 1) {
            currentPage = Integer.parseInt(Navigator.getContainerName().split("/")[0].replace("(", ""));
        }
        for (int index = 0; index < playerContainer.inventorySlots.size(); index++) {
            ItemStack item = playerContainer.getSlot(index).getStack();
            if (item == null) continue; // Skip empty slots
            if (index == (slot) && currentPage == page) {
                click(index);
                isSelecting = false;
                return true;
            }
        }
        ItemStack item = playerContainer.getSlot(53).getStack();
        if (item != null && GameData.getItemRegistry().getId(item.getItem()) == 262) {
            click(53);
            return true;
        }
        goBack();
        isSelecting = false;
        return false;
    }



    public static void goBack() {
        click(Minecraft.getMinecraft().thePlayer.openContainer.inventorySlots.size() - BACK_BUTTON_SLOT_OFFSET - 36);
    }

    public static void inputAnvil(String text) {
        if (HysentialsConfig.htslSafeMode) {
            SLOT_TO_MANUALLY_CLICK = ANVIL_SLOT;
        }
        modifyOutput(text);

        setNotReady();
    }

    public static void inputChat(String text) {
        if (HysentialsConfig.htslSafeMode) {
            String finalText = text;
            Multithreading.schedule(() -> {
                Minecraft.getMinecraft().displayGuiScreen(new GuiChat(finalText));
            }, 100, TimeUnit.MILLISECONDS);
        } else {
            if (text.startsWith("/")) {
                text = "&r" + text;
            }
            Minecraft.getMinecraft().thePlayer.sendChatMessage(text);
        }
        setNotReady();
    }

    public static void setNotReady() {
        itemsLoaded.clear();
        lastItemAddedTimestamp = 0;
        isReady = false;
        drawArrow = false;
    }

    @SubscribeEvent(
        priority = EventPriority.HIGHEST
    )
    public void renderArrow(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (drawArrow) {
            GL11.glTranslated(0, 0, 400);
            Renderer.drawImage(ARROW_TEXTURE_LOCATION, drawArrowX, drawArrowY, ARROW_SIZE, ARROW_SIZE);
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        setNotReady();
        guiIsLoading = true;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChat(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        if (message.split("\n").length < 3) return;
        String line = message.split("\n")[2];
        if (line.equals(" [PREVIOUS] [CANCEL]") || line.startsWith(" [CANCEL]")) {
            isReady = true;
            Multithreading.schedule(() -> {
                if (!isReady) {
                    isReady = true;
                }
            }, 100, TimeUnit.MILLISECONDS);
        }
    }

    @SubscribeEvent
    public void onGuiRender(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (isReady) return;
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().thePlayer.openContainer == null) {
            return;
        }
        if (event.gui instanceof GuiRepair) {
            if (lastItemAddedTimestamp == 0 || (System.currentTimeMillis() - lastItemAddedTimestamp) < HysentialsConfig.guiCooldown) {
                return;
            }
            isReady = true;
            guiIsLoading = false;
        }
        String containerName = getContainerName();
        if (containerName == null) {
            return;
        }
        if (containerName.equals("Housing Menu")) {
            return;
        }
        if (lastItemAddedTimestamp == 0 || (System.currentTimeMillis() - lastItemAddedTimestamp) < HysentialsConfig.guiCooldown) {
            return;
        }
        isReady = true;
        guiIsLoading = false;
    }

    @SubscribeEvent
    public void renderItemIntoGui(RenderItemInGuiEvent event) {
        if (guiIsLoading) {
            try {
                if (event.getStack() != null && event.getStack().getDisplayName() != null) {
                    if (!itemsLoaded.contains(event.getStack().getDisplayName())) {
                        itemsLoaded.add(event.getStack().getDisplayName());
                        lastItemAddedTimestamp = System.currentTimeMillis();
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }

    @SubscribeEvent
    public void packetReceived(FMLNetworkEvent.ClientConnectedToServerEvent event) {
//        event.manager.channel().pipeline()
//            .addAfter("fml:packet_handler", "hysentials:packet_handler", new ChannelDuplexHandler() {
//                @Override
//                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                    if (!isLoadingItem) {
//                        return;
//                    }
//                    if (msg instanceof S2FPacketSetSlot) {
//                        S2FPacketSetSlot packet = (S2FPacketSetSlot) msg;
//                        String containerName = getContainerName();
//                        if (containerName != null && !containerName.equals("Select an Item")) {
//                            isLoadingItem = false;
//                            click(53); // Slot used to load items
//                        }
//                    }
//                    super.channelRead(ctx, msg);
//                }
//
//                @Override
//                public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//                    if (msg instanceof C0EPacketClickWindow) {
//                        String containerName = getContainerName();
//                        if (containerName.equals("Housing Menu") || Navigator.isReady) return;
//                        C0EPacketClickWindow packet = (C0EPacketClickWindow) msg;
//                        if (packet.getSlotId() != Navigator.SLOT_TO_MANUALLY_CLICK) {
//                            promise.cancel(true);
//                        }
//                    }
//                }
//            });
    }
}
