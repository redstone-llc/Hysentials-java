package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import cc.woverflow.hysentials.htsl.PotionEffect;
import org.json.JSONObject;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.anvil;
import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.click;

public class ApplyPotionEffect extends Loader {
    public ApplyPotionEffect(JSONObject actionData) {
        super(actionData, "Apply Potion Effect");

        if (actionData.has("effect")) {
            add(click(10));
            PotionEffect effect = PotionEffect.fromString(actionData.getString("effect"));
            if (effect.isOnSecondPage()) {
                add(click(53));
            }
            add(click(effect.getSlot()));
        }

        if (!Double.isNaN(actionData.getDouble("duration")) && actionData.getDouble("duration") > 60) {
            add(click(11));
            add(anvil(String.valueOf(actionData.getDouble("duration"))));
        }

        if (!Double.isNaN(actionData.getDouble("amplifier")) && actionData.getDouble("amplifier") != 1) {
            add(click(12));
            add(anvil(String.valueOf(actionData.getDouble("amplifier"))));
        }

        if (actionData.getBoolean("overrideExistingEffects")) {
            add(click(13));
        }
    }
}
