package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import cc.woverflow.hysentials.htsl.Enchantment;
import org.json.JSONObject;

public class EnchantHeldItem extends Loader {
    public EnchantHeldItem(String enchant, String level) {
        super("Enchant Held Item", enchant, level);

        if (enchant != null) {
            add(LoaderObject.click(10));
            Enchantment enchantment = Enchantment.fromString(enchant);

            if (enchantment.isOnSecondPage()) {
                add(LoaderObject.click(53));
            }

            add(LoaderObject.click(enchantment.getSlot()));
        }

        if (level != null) {
            add(LoaderObject.click(11));
            add(LoaderObject.anvil(level));
        }
    }
}
