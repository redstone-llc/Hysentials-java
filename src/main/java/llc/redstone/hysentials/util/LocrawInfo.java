package llc.redstone.hysentials.util;

import net.hypixel.data.region.Environment;
import net.hypixel.data.type.GameType;
import net.hypixel.data.type.LobbyType;
import net.hypixel.data.type.ServerType;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * The LocrawInfo class represents information about the server and game mode. It provides methods to get and set the server name, game mode, game type, map name, proxy name, environment
 *, and lobby name.
 */
public class LocrawInfo {
    private final String serverName;
    private @Nullable ServerType serverType;
    private final @Nullable String lobbyName;
    private final @Nullable String mode;
    private final @Nullable String map;

    public LocrawInfo(String serverName, @Nullable ServerType serverType, @Nullable String lobbyName, @Nullable String mode, @Nullable String map) {
        this.serverName = serverName;
        this.serverType = serverType;
        this.lobbyName = lobbyName;
        this.mode = mode;
        this.map = map;
    }

    public LocrawInfo(ClientboundLocationPacket packet) {
        try {
            this.serverName = packet.getServerName();
            this.serverType = packet.getServerType().orElse(null);
            this.lobbyName = packet.getLobbyName().orElse(null);
            this.mode = packet.getMode().orElse(null);
            this.map = packet.getMap().orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing LocrawInfo", e);
        }
    }

    /**
     * @return The serverID of the server you are currently on, ex: mini121
     */
    public String getServerName() {
        return serverName;
    }
    /**
     * @return The GameMode of the server, ex: solo_insane
     */
    public String getGameMode() {
        return mode;
    }

    /**
     * @return The GameType of the server as an Enum.
     */
    public @Nullable GameType getGameType() {
        if (serverType instanceof GameType) {
            return (GameType) serverType;
        }
        return null;
    }

    /**
     * @return The LobbyType of the server as an Enum.
     */
    public @Nullable LobbyType getLobbyType() {
        if (serverType instanceof LobbyType) {
            return (LobbyType) serverType;
        }
        return null;
    }

    /**
     * @return The ServerType of the server as an Enum.
     */
    public @Nullable ServerType getServerType() {
        return serverType;
    }

    /**
     * @param gameType The GameType to set it to.
     */
    public void setGameType(GameType gameType) {
        this.serverType = gameType;
    }

    /**
     * @return The map of the server, ex: Shire.
     */
    public String getMapName() {
        return map;
    }


    /**
     * Retrieves the lobby name associated with this LocrawInfo object.
     *
     * @return The lobby name, or null if it has not been set.
     */
    public String getLobbyName() {
        return lobbyName;
    }

    @Override
    public String toString() {
        return "LocrawInfo{" + "serverId='" + serverName + '\'' + ", gameMode='" + mode + '\'' + ", mapName='" + map + '\'' + ", rawGameType='" + serverType + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocrawInfo that = (LocrawInfo) o;
        return Objects.equals(serverName, that.serverName) && Objects.equals(mode, that.mode) && Objects.equals(map, that.map) && Objects.equals(serverType, that.serverType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverName, mode, map, serverType);
    }
}
