package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import cc.woverflow.hysentials.htsl.Enchantment;
import org.json.JSONObject;

public class EnchantHeldItem extends Loader {
    public EnchantHeldItem(JSONObject actionData) {
        super(actionData, "Enchant Held Item");

        if (actionData.has("enchantment")) {
            add(LoaderObject.click(10));
            Enchantment enchantment = Enchantment.fromString(actionData.getString("enchantment"));

            if (enchantment.isOnSecondPage()) {
                add(LoaderObject.click(53));
            }

            add(LoaderObject.click(slot));
        }

        if (actionData.has("level")) {
            add(LoaderObject.click(11));
            add(LoaderObject.anvil(actionData.getString("level")));
        }
    }
}
