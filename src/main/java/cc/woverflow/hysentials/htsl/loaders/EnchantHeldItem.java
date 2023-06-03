package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import cc.woverflow.hysentials.htsl.Enchantment;
import org.json.JSONObject;

import java.util.List;

public class EnchantHeldItem extends Loader {
    public EnchantHeldItem(String enchant, String level) {
        super("Enchant Held Item", "enchant", enchant, level);

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

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        return new EnchantHeldItem(args.get(0), args.get(1));
    }

    @Override
    public String export(List<String> args) {
        return "enchant \"" + args.get(0) + "\" " + args.get(1) + "";
    }
}
