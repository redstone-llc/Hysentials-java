package cc.woverflow.hysentials.htsl;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Loader {
    public static List<Loader> loaders = new ArrayList<>();
    public List<LoaderObject> sequence = new ArrayList<>();
    public String name;
    public JSONObject actionData;

    public Loader(JSONObject actionData, String name) {
        this.name = name;
        this.actionData = actionData;
        loaders.add(this);
    }

    public JSONObject getActionData () {
        return actionData;
    }

    public Loader add(LoaderObject object) {
        sequence.add(object);
        return this;
    }

    public static class LoaderObject {
        public String type;
        public String key;
        public Object value;

        public LoaderObject(String type, String key, Object value) {
            this.type = type;
            this.key = key;
            this.value = value;
        }

        public LoaderObject(String type) {
            this.type = type;
        }

        public static LoaderObject anvil(String value) {
            return new LoaderObject("anvil", "text", value);
        }

        public static LoaderObject back() {
            return new LoaderObject("back");
        }

        public static LoaderObject click(Integer value) {
            return new LoaderObject("click", "slot", value);
        }

        public static LoaderObject option(String value) {
            return new LoaderObject("option", "option", value);
        }

        public static LoaderObject chat(String value) {
            return new LoaderObject("chat", "text", value);
        }

        public static LoaderObject setGuiContext(String value) {
            return new LoaderObject("setGuiContext", "context", value);
        }

        public static LoaderObject item(String value) {
            return new LoaderObject("item", "item", value);
        }
    }
}
