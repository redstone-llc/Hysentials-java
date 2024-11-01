package llc.redstone.hysentials.handlers.groupchats;

import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import llc.redstone.hysentials.schema.HysentialsSchema;
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import llc.redstone.hysentials.websocket.Socket;
import net.minecraft.client.gui.ChatLine;
import org.polyfrost.chatting.chat.ChatTab;
import org.polyfrost.chatting.chat.ChatTabs;
import llc.redstone.hysentials.Hysentials;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GroupChat {
    private int tick;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        final LocrawInfo locraw = LocrawUtil.INSTANCE.getLocrawInfo();
        if (event.phase != TickEvent.Phase.START || UMinecraft.getPlayer() == null || !HypixelUtils.INSTANCE.isHypixel() || locraw == null) {
            return;
        }

        if (++this.tick == 80) {
            if (Hysentials.INSTANCE.isChatting) {
                Multithreading.runAsync(() -> {
                    try {
                        if (!Hysentials.INSTANCE.isCVGT1_5_3) return;
                        List<HysentialsSchema.Group> groups = Socket.cachedGroups;
                        if (groups.isEmpty()) return;

                        for (HysentialsSchema.Group group : groups) {
                            if (group == null) continue;
                            List<String> messages = group.getMessages() == null ? new ArrayList<>() : group.getMessages();
                            List<Object> lines = new ArrayList<>();
                            Collections.reverse(messages);
                            for (String message : messages) {
                                lines.add(new ChatLine(0, new UTextComponent(message), 0));
                            }
                            if (ChatTabs.INSTANCE.getTabs().stream().anyMatch((tab) -> tab.getName().equals(group.getName().toUpperCase()))) {
                                ChatTab tab = ChatTabs.INSTANCE.getTabs().stream().filter((t) -> t.getName().equals(group.getName().toUpperCase())).findFirst().orElse(null);
                                if (tab != null) {
                                    tab.setMessages(lines);
                                }
                                continue;
                            }
                            ChatTab tab = new ChatTab(
                                true,
                                group.getName().toUpperCase(),
                                true,
                                false,
                                Collections.singletonList("[" + group.getName() + "] "),
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                14737632,
                                16777120,
                                10526880,
                                ""
                            );
                            tab.setMessages(lines);
                            tab.initialize();

                            Multithreading.schedule(() -> ChatTabs.INSTANCE.getTabs().add(tab), 500, TimeUnit.MILLISECONDS);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            this.tick = 0;
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        this.tick = 70;
    }

    public static String getDividerAqua() {
        return "§b§m-----------------------------------------------------";
    }
}
