package llc.redstone.hysentials.event.events;


import net.minecraftforge.fml.common.eventhandler.Event;

public class HousingJoinEvent extends Event {
    private String housingName;
    private String playerName;

    public HousingJoinEvent(String housingName, String playerName) {
        this.housingName = housingName;
        this.playerName = playerName;
    }

    public String getHousingName() {
        return housingName;
    }

    public String getPlayerName() {
        return playerName;
    }
}
