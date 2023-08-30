package cc.woverflow.hysentials.handlers.misc;

import cc.woverflow.hysentials.quest.Quest;
import cc.woverflow.hysentials.websocket.Request;
import cc.woverflow.hysentials.websocket.Socket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class QuestHandler {
    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent.Pre evt) {
        for (Quest quest : Quest.questInstances) {
            quest.renderOverlay(evt);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMessageReceive(ClientChatReceivedEvent evt) {
        if (evt.type == 2) {
            return;
        }
        for (Quest quest : Quest.questInstances) {
            quest.onMessageReceive(evt.message.getFormattedText());
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent evt) {
        if (evt.phase != TickEvent.Phase.END) {
            return;
        }
        if (Socket.cachedData.has("quests")) {
            for (Quest quest : Quest.questInstances) {
                if (!Socket.cachedData.getJSONObject("quests").has(quest.getId())) {
                    quest.inRotation = false;
                    quest.isActive = false;
                    quest.isCompleted = false;
                    quest.progress = 0;
                    quest.rewards.clear();
                    continue;
                }
                JSONObject questData = Socket.cachedData.getJSONObject("quests").getJSONObject(quest.getId());
                quest.isActive = questData.getBoolean("activated");
                quest.inRotation = true;
                quest.isCompleted = questData.getBoolean("completed");
                quest.progress = questData.getInt("progress");
                if (questData.has("rewards")) {
                    quest.rewards.clear();
                    JSONObject rewards = questData.getJSONObject("rewards");
                    DecimalFormat largeFormat = new DecimalFormat("#,###");
                    for (String key : rewards.keySet()) {
                        switch (key) {
                            case "emeralds": {
                                quest.rewards.add("   &8+&a" + largeFormat.format(rewards.getInt(key)) + " Emeralds");
                                break;
                            }
                            case "exp": {
                                quest.rewards.add("   &8+&3" + largeFormat.format(rewards.getInt(key)) + " Hysentials XP");
                                break;
                            }
                            case "cosmetic": {
                                //todo add cosmetic rewards
                                quest.rewards.add("   &8+&cCOMING SOON");
                                break;
                            }
                        }
                    }
                }
                quest.data = questData;
            }
        }
        for (Quest quest : Quest.questInstances) {
            quest.onTick();
        }
    }

    public static void enableQuest(Quest quest) {
        Socket.CLIENT.sendText(new Request(
            "method", "activeQuest",
            "questId", quest.getId(),
            "active", quest.isActive,
            "key", Socket.serverId,
            "uuid", Minecraft.getMinecraft().thePlayer.getUniqueID().toString()
        ).toString());
    }

    public static void rerollQuest() {
        Socket.CLIENT.sendText(new Request(
            "method", "rerollQuest",
            "key", Socket.serverId,
            "uuid", Minecraft.getMinecraft().thePlayer.getUniqueID().toString()
        ).toString());
    }

    public static void checkQuest(Quest quest) {
        Socket.CLIENT.sendText(new Request(
            "method", "checkQuest",
            "questId", quest.getId(),
            "key", Socket.serverId,
            "uuid", Minecraft.getMinecraft().thePlayer.getUniqueID().toString()
        ).toString());
    }
}
