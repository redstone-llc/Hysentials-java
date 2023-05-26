package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;
public class SetCompassTarget extends Loader {
    public SetCompassTarget(String location, String coordinates) {
        super("Set Compass Target", "compassTarget", location);

        add(click(10));

        switch (location) {
            case "house_spawn":
                add(click(10));
                break;
            case "current_location":
                add(click(11));
                break;
            case "custom_coordinates":
                add(click(12));
                add(anvil(coordinates));
                break;
        }
    }
}
