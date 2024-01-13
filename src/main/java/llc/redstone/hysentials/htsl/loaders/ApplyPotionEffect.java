package llc.redstone.hysentials.htsl.loaders;

import llc.redstone.hysentials.htsl.Loader;
import llc.redstone.hysentials.htsl.PotionEffect;
import llc.redstone.hysentials.htsl.Loader;
import llc.redstone.hysentials.htsl.PotionEffect;
import org.json.JSONObject;
import scala.Int;

import java.util.List;

import static llc.redstone.hysentials.htsl.Loader.LoaderObject.anvil;
import static llc.redstone.hysentials.htsl.Loader.LoaderObject.click;

public class ApplyPotionEffect extends Loader {
    public ApplyPotionEffect(String effect, String duration, String amplifier, boolean overrideExistingEffects) {
        super("Apply Potion Effect", "applyPotion", effect, duration, amplifier, overrideExistingEffects);

        if (effect != null) {
            add(LoaderObject.click(10));
            PotionEffect potion = PotionEffect.fromString(effect);
            if (potion.isOnSecondPage()) {
                add(LoaderObject.click(53));
            }
            add(LoaderObject.click(potion.getSlot()));
        }

        if (!isNAN(duration) && Integer.parseInt(duration) > 60) {
            add(LoaderObject.click(11));
            add(LoaderObject.anvil(duration));
        }

        if (!isNAN(amplifier) && Integer.parseInt(amplifier) != 1) {
            add(LoaderObject.click(12));
            add(LoaderObject.anvil(amplifier));
        }

        if (overrideExistingEffects) {
            add(LoaderObject.click(13));
        }
    }

    @Override
    public Loader load(int index, List<String> args, List<String> errors) {
        boolean override = false;
        if (args.size() != 4) {
            errors.add("&cIncomplete arguments on line &e" + (index + 1) + "&c!");
            return null;
        }
        if (args.get(3).equalsIgnoreCase("true")) {
            override = true;
        } else {
            errors.add("&cInvalid argument on line &e" + (index + 1) + "&c!");
        }
        return new ApplyPotionEffect(args.get(0), args.get(1), args.get(2), override);
    }

    @Override
    public String export(List<String> args) {
        return "applyPotion \"" + args.get(0) + "\" " + args.get(1) + " " + args.get(2) + " " + args.get(3) + "";
    }
}
