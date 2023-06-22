package cc.woverflow.hysentials.htsl;

import akka.actor.Kill;
import cc.woverflow.hysentials.htsl.compiler.Compiler;
import cc.woverflow.hysentials.htsl.loaders.*;
import org.json.JSONObject;
import scala.Int;

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

    public static void registerLoaders() {
        new ApplyInventoryLayout(null);
        new ApplyPotionEffect(null, null, null, false);
        new Cancel();
        new ChangeGlobalStat(null, null, null);
        new ChangeHealth(null, null);
        new ChangeHungerLevel(null, null);
        new ChangeMaxHealth(null, null, false);
        new ChangePlayerGroup(null, false);
        new ChangePlayerStat(null, null, null);
        new ClearAllPotionEffects();
        new Conditional(null, false, null, null);
        new DisplayActionBar(null);
        new DisplayTitle(null, null, null, null, null);
        new EnchantHeldItem(null, null);
        new Exit();
        new FailParkour(null);
        new FullHeal();
        new GiveExperienceLevels(null);
        new GiveItem(null, false);
        new GoToHouseSpawn();
        new KillPlayer();
        new ParkourCheckpoint();
        new PlaySound(null, null);
        new RandomAction(null);
        new RemoveItem(null);
        new ResetInventory();
        new SendAChatMessage(null);
        new SendToLobby(null);
        new SetCompassTarget(null, null);
        new SetGamemode(null);
        new TeleportPlayer(null, null);
        new TriggerFunction(null, false);
        new UseRemoveHeldItem();
    }

    public static boolean isNAN(String s) {
        return s == null || s.equals("NaN");
    }

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

    public List<String> stringArgs = null;

    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        return null;
    }

    public static boolean canExport(Loader loader) {
        try {
            loader.export(new ArrayList<>());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String export(List<String> args) {
        return keyword;
    }

    public JSONObject getActionData() {
        return actionData;
    }

    public Loader add(LoaderObject object) {
        sequence.add(object);
        return this;
    }

    public List<LoaderObject> loadAction(Object[] action) {
        ArrayList<Loader> loaders = new ArrayList<>(Compiler.compiedActions);
        loaders.removeIf(loader -> loader instanceof Conditional);
        Loader loader = loaders.stream().filter(l -> l.equals(action[1])).findFirst().orElse(null);
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

    public String undoValidOperator(String operator) {
        switch (operator.toLowerCase()) {
            case "increment":
                operator = "inc";
                break;
            case "decrement":
                operator = "dec";
                break;
            case "set":
                operator = "=";
                break;
            case "multiply":
                operator = "multi";
                break;
            case "divide":
                operator = "div";
                break;
        }
        if (!Arrays.asList("inc", "dec", "=", "multi", "div").contains(operator))
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

        public static LoaderObject command(String value) {
            return new LoaderObject("command", "command", value);
        }

        public static LoaderObject selectOrClick(String value, Integer slot) {
            return new LoaderObject("selectOrClick", value, String.valueOf(slot));
        }

        public static LoaderObject manualOpen(String value, String msg) {
            return new LoaderObject("manualOpen", value, msg);
        }

        public static LoaderObject close() {
            return new LoaderObject("close");
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
