package cc.woverflow.hysentials.event.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class HypixelAchievementEvent extends Event {

    private final String achievement;

    public HypixelAchievementEvent(String achievement) {
        this.achievement = achievement;
    }

    public String getAchievement() {
        return achievement;
    }
}
