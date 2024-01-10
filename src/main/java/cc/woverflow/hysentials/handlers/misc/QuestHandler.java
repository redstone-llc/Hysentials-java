package cc.woverflow.hysentials.handlers.misc;

import cc.woverflow.hysentials.quest.Quest;
import cc.woverflow.hysentials.schema.HysentialsSchema;
import cc.woverflow.hysentials.websocket.Request;
import cc.woverflow.hysentials.websocket.Socket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

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
        if (Socket.cachedUser != null && Socket.cachedUser.getQuests() != null) {
            for (Quest quest : Quest.questInstances) {
                if (Socket.cachedUser.getQuests().stream().noneMatch(q -> q.getId().equals(quest.getId()))) {
                    quest.inRotation = false;
                    quest.isActive = false;
                    quest.isCompleted = false;
                    quest.progress = 0;
                    quest.rewards.clear();
                    continue;
                }
                HysentialsSchema.Quest questData = Socket.cachedUser.getQuests().stream().filter(q -> q.getId().equals(quest.getId())).findFirst().get();
                quest.isActive = questData.getActivated();
                quest.inRotation = true;
                quest.isCompleted = questData.getCompleted();
                quest.progress = questData.getProgress();
                if (questData.getRewards() != null) {
                    quest.rewards.clear();
                    HysentialsSchema.LevelRewards rewards = questData.getRewards();
                    DecimalFormat largeFormat = new DecimalFormat("#,###");
                    if (rewards.getCosmetic() != null) {
                        quest.rewards.add("   &8+&cCOMING SOON");
                    }
                    if (rewards.getEmeralds() != null) {
                        quest.rewards.add("   &8+&a" + largeFormat.format(rewards.getEmeralds()) + " Emeralds");
                    }
                    if (rewards.getExp() != null) {
                        quest.rewards.add("   &8+&3" + largeFormat.format(rewards.getExp()) + " Hysentials XP");
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
