package cc.woverflow.hysentials.htsl;

import cc.woverflow.hysentials.htsl.loaders.Conditional;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;
import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.returnToEditActions;

public class Loader {
    public static List<Loader> loaders = new ArrayList<>();
    public List<LoaderObject> sequence = new ArrayList<>();
    public String name;
    public String keyword;
    public JSONObject actionData;
    public Object[] args = new Object[0];

    public Loader(JSONObject actionData, String name) {
        this.name = name;
        this.actionData = actionData;
        loaders.add(this);
    }

    public Loader(String name, String keyword, Object... args) {
        this.name = name;
        this.keyword = keyword;
        this.args = args;
        loaders.add(this);
    }

    public void load(int index, List<String> args, List<String> compileErorrs) {

    }

    public JSONObject getActionData () {
        return actionData;
    }

    public Loader add(LoaderObject object) {
        sequence.add(object);
        return this;
    }

    public List<LoaderObject> loadAction(Object[] action) {
        ArrayList<Loader> loaders = new ArrayList<>(Loader.loaders);
        loaders.removeIf(loader -> loader instanceof Conditional);
        Loader loader = loaders.stream().filter(l -> l.name.equals(action[0])).findFirst().orElse(null);
        if (loader == null) {
            return new ArrayList<>();
        }

        List<LoaderObject> sequence = new ArrayList<>();

        sequence.add(setActionName(loader.name));
        sequence.add(click(50));
        sequence.add(option(loader.name));

        sequence.addAll(loader.sequence);

        sequence.add(returnToEditActions());

        return sequence;
    }

    public String validOperator(String operator) {
        switch (operator) {
            case "inc":
            case "+=":
                operator = "increment";
                break;
            case "dec":
            case "-=":
                operator = "decrement";
                break;
            case "=":
                operator = "set";
                break;
            case "mult":
            case "*=":
                operator = "multiply";
                break;
            case "div":
            case "/=":
            case "//=":
                operator = "divide";
                break;
        }
        if (!Arrays.asList("increment", "decrement", "set", "multiply", "divide").contains(operator))
            return null;
        return operator;
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

        public static LoaderObject returnToEditActions() {
            return new LoaderObject("returnToEditActions");
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

        public static LoaderObject setActionName(String value) {
            return new LoaderObject("setActionName", "actionName", value);
        }

        public static LoaderObject item(String value) {
            return new LoaderObject("item", "item", value);
        }
    }
}
