package llc.redstone.hysentials.quest;

import llc.redstone.hysentials.quest.quests.LobbyParkourist;
import llc.redstone.hysentials.quest.quests.LostMage;
import llc.redstone.hysentials.schema.HysentialsSchema;
import llc.redstone.hysentials.quest.dailyquests.BlitzDuelsRandom;
import llc.redstone.hysentials.quest.dailyquests.DuelsMaster;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class Quest {
    public static List<Quest> questInstances = new ArrayList<>();
    private String name;
    private String description;
    private String id;
    private int goal;
    private boolean daily;

    public boolean isActive;
    public boolean inRotation;
    public boolean isCompleted;
    public int progress;
    public List<String> rewards = new ArrayList<>();
    public HysentialsSchema.Quest data;

    public Quest(String name, String description, String id, boolean daily) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.goal = 1;
        this.daily = daily;
    }

    public Quest(String name, String description, String id, int goal, boolean daily) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.goal = goal;
        this.daily = daily;
    }

    public static Quest getQuestById(String args) {
        for (Quest quest : questInstances) {
            if (quest.getId().equals(args)) {
                return quest;
            }
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public boolean isDaily() {
        return this.daily;
    }

    public String getDescription() {
        return this.description;
    }

    public String getId() {
        return this.id;
    }

    public void renderOverlay(RenderGameOverlayEvent.Pre evt) {

    }

    public void onMessageReceive(String message) {

    }

    public abstract void onTick();

    public abstract void onQuestStart();

    public abstract void onQuestEnd();

    public static void registerQuests() {
        questInstances.add(new BlitzDuelsRandom());
        questInstances.add(new DuelsMaster());
        questInstances.add(new LobbyParkourist());
        questInstances.add(new LostMage());
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.id);
        jsonObject.put("progress", this.progress);
        jsonObject.put("completed", this.isCompleted);
        jsonObject.put("activated", this.isActive);
        return jsonObject.toString();
    }

    public int getGoal() {
        return this.goal;
    }
}
