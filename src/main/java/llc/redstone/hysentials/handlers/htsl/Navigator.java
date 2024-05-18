package llc.redstone.hysentials.handlers.htsl;

import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import llc.redstone.hysentials.HysentialsUtilsKt;
import llc.redstone.hysentials.config.hysentialmods.HousingConfig;
import llc.redstone.hysentials.event.events.GuiLoadedEvent;
import llc.redstone.hysentials.event.events.GuiMouseClickEvent;
import llc.redstone.hysentials.handlers.chat.modules.misc.GuiChat256;
import cc.polyfrost.oneconfig.utils.Multithreading;
import llc.redstone.hysentials.event.events.RenderItemInGuiEvent;
import llc.redstone.hysentials.htsl.ModifyAnvilOutput;
import llc.redstone.hysentials.util.Renderer;
import llc.redstone.hysentials.utils.ChatLib;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static llc.redstone.hysentials.handlers.guis.GameMenuOpen.field_lowerChestInventory;
import static llc.redstone.hysentials.util.Renderer.getImageFromUrl;

public class Navigator {
    private static DynamicTexture ARROW_TEXTURE_LOCATION;
    public static String optionBeingSelected;
    public static String optionBeingSelected2;
    public static int optionBeingSelectedSlot;
    private static final int ARROW_SIZE = 50;
    private static final int ARROW_OFFSET_X = 10;
    private static final int ARROW_OFFSET_Y = -45;

    private static int SLOT_TO_MANUALLY_CLICK = -1;
    private static final int BACK_BUTTON_SLOT_OFFSET = 5;

    private static boolean drawArrow = false;
    private static int drawArrowX = 0;
    private static int drawArrowY = 0;
    public static List<String> itemsLoaded = new ArrayList<>();

    public static boolean isReady = true;
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

    private static String func;
    public static boolean isWorking = false;

    public Navigator() {
        try {
            ARROW_TEXTURE_LOCATION = new DynamicTexture(getImageFromUrl(HysentialsUtilsKt.getHYSENTIALS_API() + "/resource?file=red-arrow.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            guiTop = ReflectionHelper.findField(GuiContainer.class, "guiTop", "field_147009_r");
            guiTop.setAccessible(true);
            guiLeft = ReflectionHelper.findField(GuiContainer.class, "guiLeft", "field_147003_i");
            guiLeft.setAccessible(true);
            chatGuiInputField = ReflectionHelper.findField(GuiChat.class, "inputField", "field_146415_a");
            chatGuiInputField.setAccessible(true);
        } catch (Exception e) {
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
        if (HousingConfig.htslSafeMode) {
            SLOT_TO_MANUALLY_CLICK = slotId;
            try {
                setArrowToSlot(slotId);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else {
            GuiMouseClickEvent event = new GuiMouseClickEvent(slotId, new CallbackInfo("test", true));
            MinecraftForge.EVENT_BUS.post(event);
            if (!event.getCi().isCancelled()) Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().thePlayer.openContainer.windowId, slotId, 2, 3, Minecraft.getMinecraft().thePlayer);
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

        if (containerName != null && (containerName.equals("Functions") || containerName.equals("Edit NPC")) ) {
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

    public static int getContainerSize() {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiChest) {
            GuiChest chest = (GuiChest) Minecraft.getMinecraft().currentScreen;
            try {
                if (field_lowerChestInventory.get(chest) instanceof IInventory) {
                    IInventory inventory = (IInventory) field_lowerChestInventory.get(chest);
                    return inventory.getSizeInventory();
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return -1;
    }

    public static void setSelecting(String option) {
        isSelecting = true;
        optionBeingSelected = option;
    }

    public static void setSelectingOrSlot(String option, int slot) {
        isSelecting = true;
        optionBeingSelected2 = option;
        optionBeingSelectedSlot = slot;
    }

    public static void setSelecting(int slot, int page) {
        isSelecting = true;
        optionBeingSelected = slot + ":" + page;
    }

    public static void selectItem(String item) {
        if (item != null) {
                ItemStack itemStack = getItemFromNBT(item);
                isLoadingItem = true;
                utilLoadItem(itemStack, 18);
                setNotReady();
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
        Minecraft.getMinecraft().playerController.sendSlotPacket(item, slot);
    }

    public static boolean selectOption(String optionName) {
        Container playerContainer = Minecraft.getMinecraft().thePlayer.openContainer;
        for (int index = 0; index < playerContainer.inventorySlots.size(); index++) {
            ItemStack item = playerContainer.getSlot(index).getStack();
            if (item == null) continue; // Skip empty slots
            if (ChatColor.Companion.stripControlCodes(item.getDisplayName()).equalsIgnoreCase(optionName)) {
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

    public static boolean selectOptionOrClick(String optionName, int slot) {
        Container playerContainer = Minecraft.getMinecraft().thePlayer.openContainer;
        for (int index = 0; index < playerContainer.inventorySlots.size(); index++) {
            ItemStack item = playerContainer.getSlot(index).getStack();
            if (item == null) continue; // Skip empty slots
            if (ChatColor.Companion.stripControlCodes(item.getDisplayName()).equals(optionName)) {
                click(index);
                isSelecting = false;
                optionBeingSelected2 = null;
                optionBeingSelectedSlot = -1;
                return true;
            }
        }
        ItemStack item = playerContainer.getSlot(53).getStack();
        if (item != null && GameData.getItemRegistry().getId(item.getItem()) == 262) {
            click(53);
            return true;
        }
//        goBack();
        isSelecting = false;
        optionBeingSelected2 = null;
        optionBeingSelectedSlot = -1;
        Navigator.click(slot);
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
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        if (HousingConfig.htslSafeMode) {
            if (!(screen instanceof GuiRepair)) return;
            SLOT_TO_MANUALLY_CLICK = ANVIL_SLOT;
        }
        ModifyAnvilOutput.modifyOutput(text);

        setNotReady();
    }

    public static void inputChat(String text, String func, boolean command) {
        if (text.startsWith("/") && !command) text = "&r" + text;
        if (func != null) {
            Navigator.func = func;
        }
        if (HousingConfig.htslSafeMode) {
            String finalText = text;
            Multithreading.schedule(() -> {
                try {
                    Minecraft.getMinecraft().displayGuiScreen(new GuiChat256(finalText));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 100, TimeUnit.MILLISECONDS);
        } else {
            ChatLib.say(
                (command ? "" : "/ac ") + text
            );
        }
        setNotReady();
    }

    public static void command(String command) {
        if (HousingConfig.htslSafeMode) {
            String finalCommand = "/" + command;
            Multithreading.schedule(() -> {
                Minecraft.getMinecraft().displayGuiScreen(new GuiChat(finalCommand));
            }, 100, TimeUnit.MILLISECONDS);
        } else {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/" + command);
        }
        setNotReady();
    }

    public static void setNotReady() {
        itemsLoaded.clear();
        lastItemAddedTimestamp = 0;
        isReady = false;
        drawArrow = false;
    }

    public static String guiToOpen = null;
    public static void manualOpen(String msg, String s) {
        guiToOpen = s;
        UChat.chat(msg);
        setNotReady();
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
//        if (event.gui instanceof GuiChat) return;
        setNotReady();
        guiIsLoading = true;
    }

    public static String chatInput = "";

    public static String getChatInput() {
        return chatInput;
    }


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChat(ClientChatReceivedEvent event) {
        if (!Navigator.isWorking) return;
        String message = event.message.getUnformattedText();
        if (message.split("\n").length < 3) return;
        String line = message.split("\n")[2];
        if (line.equals(" [PREVIOUS] [CANCEL]") || line.startsWith(" [CANCEL]")) {
            isReady = true;
            chatInput = event.message.getSiblings().get(0).getChatStyle().getChatClickEvent().getValue();
            Multithreading.schedule(() -> {
                if (!isReady) {
                    isReady = true;
                }
            }, 100, TimeUnit.MILLISECONDS);
        } else if (line.equals("Could not find a function with that name!")) {
            ChatLib.command("function create " + Navigator.func);
        }
    }


    @SubscribeEvent
    public void onGuiRender(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (isReady) return;
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().thePlayer.openContainer == null) {
            return;
        }
        if (event.gui instanceof GuiRepair) {
            if (lastItemAddedTimestamp == 0 || (System.currentTimeMillis() - lastItemAddedTimestamp) < HousingConfig.guiCooldown) {
                return;
            }
            isReady = true;
            guiIsLoading = false;
        }
        String containerName = getContainerName();
        if (containerName == null) {
            return;
        }
//        if (containerName.equals("Housing Menu")) {
//            return;
//        } I AM DUMB LOL
        if (lastItemAddedTimestamp == 0 || (System.currentTimeMillis() - lastItemAddedTimestamp) < HousingConfig.guiCooldown) {
            return;
        }
        isReady = true;
        guiIsLoading = false;
        GuiLoadedEvent guiLoadedEvent = new GuiLoadedEvent(event.gui, containerName);
        MinecraftForge.EVENT_BUS.post(guiLoadedEvent);
        if (guiToOpen != null) {
            GuiScreen screen = Minecraft.getMinecraft().currentScreen;
            if (screen == null) return;
            if (screen instanceof GuiChest) {
                GuiChest chest = (GuiChest) screen;
                if (ActionGUIHandler.getType(chest, true).equals(guiToOpen)) {
                    guiToOpen = null;
                    Queue.timeWithoutOperation = 0;
                }
            }
        }
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
