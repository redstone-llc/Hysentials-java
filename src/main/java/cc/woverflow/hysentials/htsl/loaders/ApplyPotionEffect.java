package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import cc.woverflow.hysentials.htsl.PotionEffect;
import org.json.JSONObject;

import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.anvil;
import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.click;

public class ApplyPotionEffect extends Loader {
    public ApplyPotionEffect(String effect, double duration, double amplifier, boolean overrideExistingEffects) {
        super("Apply Potion Effect", "applyPotion", effect, duration, amplifier, overrideExistingEffects);

        if (effect != null) {
            add(click(10));
            PotionEffect potion = PotionEffect.fromString(effect);
            if (potion.isOnSecondPage()) {
                add(click(53));
            }
            add(click(potion.getSlot()));
        }

        if (!Double.isNaN(duration) && duration > 60) {
            add(click(11));
            add(anvil(String.valueOf(duration)));
        }

        if (!Double.isNaN(amplifier) && amplifier != 1) {
            add(click(12));
            add(anvil(String.valueOf(amplifier)));
        }

        if (overrideExistingEffects) {
            add(click(13));
        }
    }

    @Override
    public void load(int index, List<String> args, List<String> errors) {
        boolean override = false;
        if (args.size() != 4) {
            errors.add("&cIncomplete arguments on line &e" + (index + 1) + "&c!");
            return;
        }
        if (args.get(3).equalsIgnoreCase("true")) {
            override = true;
        } else {
            errors.add("&cInvalid argument on line &e" + (index + 1) + "&c!");
        }
        new ApplyPotionEffect(args.get(0), Double.parseDouble(args.get(1)), Double.parseDouble(args.get(2)), override);
    }
}
