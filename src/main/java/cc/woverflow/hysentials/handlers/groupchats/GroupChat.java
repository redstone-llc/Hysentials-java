/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2022  W-OVERFLOW
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cc.woverflow.hysentials.handlers.groupchats;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.woverflow.chatting.chat.ChatTab;
import cc.woverflow.chatting.chat.ChatTabs;
import cc.woverflow.chatting.mixin.GuiNewChatAccessor;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.user.Player;
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
        final LocrawInfo locraw = HypixelUtils.INSTANCE.getLocrawInfo();
        if (event.phase != TickEvent.Phase.START || Minecraft.getMinecraft().thePlayer == null || !HypixelUtils.INSTANCE.isHypixel() || locraw == null) {
            return;
        }

        if (++this.tick == 80) {
            if (Hysentials.INSTANCE.isChatting) {
                Multithreading.runAsync(() -> {
                    List<BlockWAPIUtils.Group> groups = BlockWAPIUtils.getGroups();
                    List<BlockWAPIUtils.Group> foundGroups = new ArrayList<>();
                    for (BlockWAPIUtils.Group group : groups) {
                        for (String member : group.getMembers()) {
                            if (UUID.fromString(member).equals(Minecraft.getMinecraft().thePlayer.getUniqueID())) {
                                foundGroups.add(group);
                            }
                        }
                    }
                    if (foundGroups.size() == 0) return;

                    for (BlockWAPIUtils.Group group : foundGroups) {
                        if (group == null) continue;
                        List<String> messages = group.getMessages() == null ? new ArrayList<>() : group.getMessages();
                        Collections.reverse(messages);
                        if (ChatTabs.INSTANCE.getTabs().stream().anyMatch((tab) -> tab.getName().equals(group.getName().toUpperCase()))) {
                            continue;
                        }
                        ChatTab tab = new ChatTab(
                            true,
                            group.getName().toUpperCase(),
                            true,
                            false,
                            Collections.singletonList(group.getName() + " > "),
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
                        System.out.println("Adding group chat messages");
                        tab.setMessages(messages);
                        tab.initialize();

                        System.out.println("Added group chat tab for " + tab.getName());

                        Multithreading.schedule(() -> ChatTabs.INSTANCE.getTabs().add(tab), 500, TimeUnit.MILLISECONDS);
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

    public static void chat(JSONObject json) {
        BlockWAPIUtils.Group group = Hysentials.INSTANCE.getOnlineCache().getCachedGroups().stream().filter(g -> g.getName().equalsIgnoreCase(json.getString("name"))).findFirst().get();
        group.getMessages().add(json.getString("message"));
        if (Hysentials.INSTANCE.isChatting) {
            ChatTabs.INSTANCE.getTabs().stream().filter((tab) -> tab.getName().equalsIgnoreCase(json.getString("name"))).findFirst().ifPresent((tab) -> {
                tab.getMessages().add(0, json.getString("message"));
            });
        }
        UChat.chat(json.getString("message"));
    }

    public static void invite(JSONObject json) {
        if (json.has("reason")) {
            UTextComponent component = new UTextComponent("§bClick here §ato accept or type §b/group join " + json.getString("name"));
            component.setClick(ClickEvent.Action.RUN_COMMAND, "/group join " + json.getString("name"));
            component.setHover(HoverEvent.Action.SHOW_TEXT, "§e/group join " + json.getString("name"));
            UChat.chat(getDividerAqua());
            UChat.chat(json.getString("message"));
            UChat.chat(component);
            UChat.chat(getDividerAqua());
        }
    }

    public static String getDividerAqua() {
        return "§b§m-----------------------------------------------------";
    }
}
