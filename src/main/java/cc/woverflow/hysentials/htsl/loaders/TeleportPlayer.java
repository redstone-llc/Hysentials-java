package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class TeleportPlayer extends Loader {
    public TeleportPlayer(String location, List<String> coordinates) {
        super("Teleport Player", "tp", location, coordinates);
        if (location == null) return;
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
                if (coordinates == null) return;
                String coordinatesText = String.join(" ", coordinates);
                add(LoaderObject.anvil(coordinatesText));
                break;
        }
    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        try {
            if (args.size() == 2) {
                String[] coordinates = args.get(1).split(" ");
                return new TeleportPlayer("custom_coordinates", Arrays.asList(coordinates));
            } else {
                return new TeleportPlayer(args.get(0), null);
            }
        } catch (Exception error) {
            compileErorrs.add(String.format("&cLocation type &e\"custom_coordinates\"&c requires a second argument. Line &e%d", index + 1));
        }
        return null;
    }

    @Override
    public String export(List<String> args) {
        return "tp " + args.get(0) + (args.size() == 2 ? " \"" + args.get(1) : "\"");
    }
}
