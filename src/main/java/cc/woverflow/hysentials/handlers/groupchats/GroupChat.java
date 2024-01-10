package cc.woverflow.hysentials.handlers.groupchats;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.woverflow.hysentials.util.MUtils;
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.woverflow.chatting.chat.ChatTab;
import cc.woverflow.chatting.chat.ChatTabs;
import cc.woverflow.chatting.mixin.GuiNewChatAccessor;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.config.HysentialsConfig;

import cc.woverflow.hysentials.util.BlockWAPIUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.json.JSONObject;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GroupChat {
    private int tick;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        final LocrawInfo locraw = LocrawUtil.INSTANCE.getLocrawInfo();
        if (event.phase != TickEvent.Phase.START || Minecraft.getMinecraft().thePlayer == null || !HypixelUtils.INSTANCE.isHypixel() || locraw == null) {
            return;
        }

        if (++this.tick == 80) {
            if (Hysentials.INSTANCE.isChatting) {
                Multithreading.runAsync(() -> {
                    if (HysentialsConfig.globalChatEnabled) {
                        if (!ChatTabs.INSTANCE.getTabs().stream().anyMatch((tab) -> tab.getName().equals("GLOBAL"))) {
                            ChatTab tab = new ChatTab(
                                true,
                                "GLOBAL",
                                true,
                                false,
                                Collections.singletonList("Global" + " > "),
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                16755200,
                                16777120,
                                10526880,
                                ""
                            );
                            tab.initialize();

                            Multithreading.schedule(() -> ChatTabs.INSTANCE.getTabs().add(tab), 500, TimeUnit.MILLISECONDS);
                        } else {
                            ChatTab tab = ChatTabs.INSTANCE.getTabs().stream().filter((t) -> t.getName().equals("GLOBAL")).findFirst().orElse(null);
                            if (tab != null) {
                                tab.setEnabled(true);
                            }
                        }
                    } else {
                        ChatTab tab = ChatTabs.INSTANCE.getTabs().stream().filter((t) -> t.getName().equals("GLOBAL")).findFirst().orElse(null);
                        if (tab != null) {
                            tab.setEnabled(false);
                        }
                    }
//                    List<BlockWAPIUtils.Group> groups = BlockWAPIUtils.getGroups();
//                    List<BlockWAPIUtils.Group> foundGroups = new ArrayList<>();
//                    for (BlockWAPIUtils.Group group : groups) {
//                        for (String member : group.getMembers()) {
//                            if (UUID.fromString(member).equals(Minecraft.getMinecraft().thePlayer.getUniqueID())) {
//                                foundGroups.add(group);
//                            }
//                        }
//                    }
//                    if (foundGroups.size() == 0) return;
//
//                    for (BlockWAPIUtils.Group group : foundGroups) {
//                        if (group == null) continue;
//                        List<String> messages = group.getMessages() == null ? new ArrayList<>() : group.getMessages();
//                        Collections.reverse(messages);
//                        if (ChatTabs.INSTANCE.getTabs().stream().anyMatch((tab) -> tab.getName().equals(group.getName().toUpperCase()))) {
//                            continue;
//                        }
//                        ChatTab tab = new ChatTab(
//                            true,
//                            group.getName().toUpperCase(),
//                            true,
//                            false,
//                            Collections.singletonList(group.getName() + " > "),
//                            null,
//                            null,
//                            null,
//                            null,
//                            null,
//                            null,
//                            null,
//                            null,
//                            null,
//                            14737632,
//                            16777120,
//                            10526880,
//                            ""
//                        );
//                        tab.setMessages(messages);
//                        tab.initialize();
//
//                        Multithreading.schedule(() -> ChatTabs.INSTANCE.getTabs().add(tab), 500, TimeUnit.MILLISECONDS);
//                    }
                });
            }
            this.tick = 0;
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        this.tick = 70;
    }

    public static void chat(JSONObject json) {
        if (HysentialsConfig.globalChatEnabled) {
            MUtils.chat(json.getString("message"));
        }
    }

    public static void invite(JSONObject json) {
        if (json.has("reason")) {
            UTextComponent component = new UTextComponent("§bClick here §ato accept or type §b/group join " + json.getString("name"));
            component.setClick(ClickEvent.Action.RUN_COMMAND, "/group join " + json.getString("name"));
            component.setHover(HoverEvent.Action.SHOW_TEXT, "§e/group join " + json.getString("name"));
            MUtils.chat(getDividerAqua());
            MUtils.chat(json.getString("message"));
            UChat.chat(component);
            MUtils.chat(getDividerAqua());
        }
    }

    public static void hideTab(String name) {
        ChatTabs.INSTANCE.getTabs().remove(ChatTabs.INSTANCE.getTabs().stream().filter(tab -> tab.getName().equals(name)).findFirst().orElse(null));
    }



    public static String getDividerAqua() {
        return "§b§m-----------------------------------------------------";
    }
}
