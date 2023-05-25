package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

public class DisplayTitle extends Loader {
    public DisplayTitle(String title, String subtitle, double fadeIn, double stay, double fadeOut) {
        super("Display Title", title, subtitle, fadeIn, stay, fadeOut);

        if (title != null) {
            add(LoaderObject.click(10));
            add(LoaderObject.option(title));
        }

        if (subtitle != null) {
            add(LoaderObject.click(11));
            add(LoaderObject.option(subtitle));
        }

        if (!Double.isNaN(fadeIn) && fadeIn != 1) {
            add(LoaderObject.click(12));
            add(LoaderObject.anvil(String.valueOf(fadeIn)));
        }

        if (!Double.isNaN(stay) && stay != 5) {
            add(LoaderObject.click(13));
            add(LoaderObject.anvil(String.valueOf(stay)));
        }

        if (!Double.isNaN(fadeOut) && fadeOut != 1) {
            add(LoaderObject.click(14));
            add(LoaderObject.anvil(String.valueOf(fadeOut)));
        }
    }
}
