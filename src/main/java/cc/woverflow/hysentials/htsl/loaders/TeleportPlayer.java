package main.java.cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

public class TeleportPlayer extends Loader {
    public TeleportPlayer(JSONObject actionData) {
        super(actionData, "Teleport Player");

        add(LoaderObject.click(10));

        String location = actionData.optString("location");
        switch (location) {
            case "house_spawn":
                add(LoaderObject.click(10));
                break;
            case "current_location":
                add(LoaderObject.click(11));
                break;
            case "custom_coordinates":
                add(LoaderObject.click(12));
                JSONArray coordinates = actionData.getJSONArray("coordinates");
                String coordinatesText = String.join(" ", coordinates.toList());
                add(LoaderObject.anvil(coordinatesText));
                break;
        }
    }
}
