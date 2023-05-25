package main.java.cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

public class DisplayTitle extends Loader {
    public DisplayActionBar(JSONObject actionData) {
        super(actionData, "Display Title");

        if (actionData.has("title")) {
            add(LoaderObject.click(10));
            add(LoaderObject.option(actionData.getString("title")));
        }

        if (actionData.has("title")) {
            add(LoaderObject.click(10));
            add(LoaderObject.option(actionData.getString("title")));
        }

        if (actionData.has("subtitle")) {
            add(LoaderObject.click(11));
            add(LoaderObject.option(actionData.getString("subtitle")));
        }

        if (actionData.has("fadeIn") && !actionData.isNull("fadeIn") && !Double.isNaN(actionData.getDouble("fadeIn")) && actionData.getDouble("fadeIn") != 1) {
            add(LoaderObject.click(12));
            add(LoaderObject.anvil(actionData.getString("fadeIn")));
        }

        if (actionData.has("stay") && !actionData.isNull("stay") && !Double.isNaN(actionData.getDouble("stay")) && actionData.getDouble("stay") != 5) {
            add(LoaderObject.click(13));
            add(LoaderObject.anvil(actionData.getString("stay")));
        }

        if (actionData.has("fadeOut") && !actionData.isNull("fadeOut") && !Double.isNaN(actionData.getDouble("fadeOut")) && actionData.getDouble("fadeOut") != 1) {
            add(LoaderObject.click(14));
            add(LoaderObject.anvil(actionData.getString("fadeOut")));
        }
    }
}
