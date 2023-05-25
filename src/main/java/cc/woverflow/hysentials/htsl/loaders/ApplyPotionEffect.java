package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import cc.woverflow.hysentials.htsl.PotionEffect;
import org.json.JSONObject;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.anvil;
import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.click;

public class ApplyPotionEffect extends Loader {
    public ApplyPotionEffect(String effect, double duration, double amplifier, boolean overrideExistingEffects) {
        super("Apply Potion Effect", effect, duration, amplifier, overrideExistingEffects);

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
}
