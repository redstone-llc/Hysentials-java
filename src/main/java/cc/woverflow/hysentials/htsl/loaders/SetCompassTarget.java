package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;
public class SetCompassTarget extends Loader {
    public SetCompassTarget(String location, String coordinates) {
        super("Set Compass Target", "compassTarget", location);
        if (location == null) return;
        add(click(10));
        switch (location) {
            case "house_spawn":
                add(click(10));
                break;
            case "current_location":
                add(click(11));
                break;
            case "custom_coordinates":
                if (coordinates == null) return;
                add(click(12));
                add(anvil(coordinates));
                break;
        }
    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErrors) {
        super.load(index, args, compileErrors);
        if (!Arrays.asList("house_spawn", "current_location", "custom_coordinates").contains(args.get(0))) {
            compileErrors.add(String.format("&cUnknown location type &e%s&c on line %d", args.get(0), index + 1));
        }
        try {
            if (args.size() == 2) {
                String[] coordinates = args.get(1).split(" ");
                return new SetCompassTarget("custom_coordinates", String.format("%s %s %s", coordinates[0], coordinates[1], coordinates[2]));
            } else {
                return new SetCompassTarget(args.get(0), null);
            }
        } catch (Exception error) {
            compileErrors.add(String.format("&cLocation type &e\"custom_coordinates\"&c requires a second argument. Line &e%d", index + 1));
        }
        return null;
    }

    @Override
    public String export(List<String> args) {
        return "compassTarget " + args.get(0) + (args.size() == 2 ? " \"" + args.get(1) : "\"");
    }
}
