package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class TeleportPlayer extends Loader {
    public TeleportPlayer(String location, List<String> coordinates) {
        super("Teleport Player", "tp", location, coordinates);

        add(LoaderObject.click(10));

        switch (location) {
            case "house_spawn":
                add(LoaderObject.click(10));
                break;
            case "current_location":
                add(LoaderObject.click(11));
                break;
            case "custom_coordinates":
                add(LoaderObject.click(12));
                String coordinatesText = String.join(" ", coordinates);
                add(LoaderObject.anvil(coordinatesText));
                break;
        }
    }
}
