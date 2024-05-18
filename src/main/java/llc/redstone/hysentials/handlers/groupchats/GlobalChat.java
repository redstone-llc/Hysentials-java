package llc.redstone.hysentials.handlers.groupchats;

import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.config.hysentialmods.ChatConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.polyfrost.chatting.chat.ChatTab;
import org.polyfrost.chatting.chat.ChatTabs;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class GlobalChat {
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
                    try {
                        if (ChatConfig.globalChat) {
                            if (ChatTabs.INSTANCE.getTabs().stream().noneMatch((tab) -> tab.getName().equals("GLOBAL"))) {
                                ChatTab tab = new ChatTab(
                                    true,
                                    "GLOBAL",
                                    true,
                                    false,
                                    Arrays.asList("GLOBAL > ", ":globalchat:", ChatConfig.globalPrefix),
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
                                ChatTabs.INSTANCE.getTabs().stream().filter((t) -> t.getName().equals("GLOBAL")).findFirst().ifPresent(tab -> tab.setEnabled(true));
                            }
                        } else {
                            ChatTabs.INSTANCE.getTabs().stream().filter((t) -> t.getName().equals("GLOBAL")).findFirst().ifPresent(tab -> tab.setEnabled(false));
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
        this.tick = 79;
    }
}
