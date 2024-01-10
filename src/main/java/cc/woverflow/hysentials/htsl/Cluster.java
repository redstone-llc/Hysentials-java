package cc.woverflow.hysentials.htsl;

import akka.actor.Kill;
import cc.woverflow.hysentials.htsl.cluster.*;
import cc.woverflow.hysentials.htsl.compiler.Compiler;
import cc.woverflow.hysentials.htsl.loaders.*;
import org.json.JSONObject;
import scala.Int;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;
import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.returnToEditActions;

public class Cluster {
    public static List<Cluster> loaders = new ArrayList<>();
    public List<Loader.LoaderObject> sequence = new ArrayList<>();
    public String name;
    public String keyword;
    public JSONObject actionData;
    public Object[] args = new Object[0];

    public static void registerClusters() {
        new Function(null, null);
        new Npc(null, null);
        new Button(null);
        new ActionPad(null);
        new PlayerJoin(null);
        new PlayerQuit(null);
        new PlayerKill(null);
        new PlayerDeath(null);
        new PlayerEnterPortal(null);
        new PlayerBlockBreak(null);
        new PlayerDamage(null);
        new GroupChange(null);
        new PlayerRespawn(null);
        new PvPStateChange(null);
        new StartParkour(null);
        new CompleteParkour(null);
        new FishCaught(null);
        new PlayerToggleFlight(null);
        new PlayerToggleSneak(null);
        new PlayerChangeHeldItem(null);
        new PlayerPickUpItem(null);
        new PlayerDropItem(null);
    }

    public static boolean isNAN(String s) {
        return s == null || s.equals("NaN");
    }

    public Cluster(JSONObject actionData, String name) {
        this.name = name;
        this.actionData = actionData;
        loaders.add(this);
    }

    public Cluster(String name, String keyword, Object... args) {
        this.name = name;
        this.keyword = keyword;
        this.args = args;
        loaders.add(this);
    }

    public List<String> stringArgs = null;

    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        return new Loader(name, keyword, this.args);
    }

    public Cluster create(int index, List<String> compileErrors, Object... args) {
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

    public String export (List<String> args) {
        return keyword;
    }

    public JSONObject getActionData() {
        return actionData;
    }

    public Cluster add(Loader.LoaderObject object) {
        sequence.add(object);
        return this;
    }

    public void loadActions(List<Object[]> loaders) {
        for (Object[] action : loaders) {
            List<Loader.LoaderObject> sequence = new ArrayList<>();
            Loader loader = (Loader) action[1];

            sequence.add(setActionName(loader.name));
            sequence.add(click(50));
            sequence.add(option(loader.name));

            sequence.addAll(loader.sequence);

            sequence.add(returnToEditActions());

            this.sequence.addAll(sequence);
        }
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
                operator = "set";
                break;
            case "multiply":
                operator = "mult";
                break;
            case "divide":
                operator = "div";
                break;
        }
        if (!Arrays.asList("inc", "dec", "=", "set", "multi", "div").contains(operator))
            return null;
        return operator;
    }
}
