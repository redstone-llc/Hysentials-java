package cc.woverflow.hysentials.htsl.compiler;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.woverflow.hysentials.handlers.htsl.Navigator;
import cc.woverflow.hysentials.handlers.htsl.Queue;
import cc.woverflow.hysentials.htsl.Cluster;
import cc.woverflow.hysentials.htsl.Loader;
import cc.woverflow.hysentials.htsl.OccurrenceChecker;
import cc.woverflow.hysentials.htsl.cluster.Function;
import cc.woverflow.hysentials.htsl.loaders.Conditional;
import cc.woverflow.hysentials.htsl.loaders.RandomAction;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static cc.woverflow.hysentials.handlers.htsl.Queue.*;

public class Compiler {
    public static List<Loader> compiedActions = new ArrayList<>();
    public static List<String> clusterTypes = Arrays.asList("function", "npc", "button", "pad", "playerJoin", "playerQuit", "playerDeath", "playerKill", "playerRespawn", "groupChange", "pvpStateChange", "fishCaught", "playerEnterPortal", "playerDamage", "playerBlockBreak", "startParkour", "completeParkour");

    public Compiler(String code) {
        String[] lines = code.split("\n");
        String subaction = "";
        String cluster = "";
        List<String> compileErrors = new ArrayList<>();
        Object[] subactions = null;
        Object[] clustersActions = null;
        List<Loader> actions = new ArrayList<>();
        List<Cluster> clusters = new ArrayList<>();
        compiedActions = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            line = removeWhitespace(line);
            if (line.startsWith("//")) continue;
            List<String> args = getArgs(line);
            if (args.isEmpty()) {
                continue;
            }
            String action = new String(args.get(0));
            Loader actionData = null;
            args.remove(0);
            switch (action) {
                case "// ":
                case "//":
                    break;
                default: {
                    if (action.equals("if") && !(subaction.equals("if") || subaction.equals("else"))) break;
                    if (line.startsWith("}") && subaction.equals("else")) break;
                    if (line.startsWith("}") && subaction.equals("if") && !(line.startsWith("} else") || line.startsWith("}else")))
                        break;
                    if ((line.startsWith("} else {") || line.startsWith("}else")) && subaction.equals("if")) break;
                    if (line.startsWith("random {") && subaction.equals("")) break;
                    if (line.startsWith("}") && subaction.equals("random")) break;
                    if (line.startsWith("goto")) break;
//                    boolean stop = false;
//                    for (String clusterType : clusterTypes) {
//                        if ((line.startsWith(clusterType + " (") || line.startsWith(clusterType + "{") || line.startsWith(clusterType + " {")) && cluster.equals("")) {
//                            stop = true;
//                            break;
//                        }
//                        if (line.startsWith("}") && cluster.equals(clusterType)) {
//                            stop = true;
//                            break;
//                        }
//                    }
//                    if (stop) break;
                    try {
                        if (Loader.loaders.stream().anyMatch(loader -> loader.keyword.equals(action))) {
                            Loader loader = Loader.loaders.stream().filter(l -> l.keyword.equals(action)).findFirst().orElse(null);
                            if (loader != null) {
                                loader = loader.load(i, args, compileErrors);
                                if (loader != null) {
                                    actionData = (loader);
                                }
                            }
                        } else {
                            compileErrors.add("&cUnknown action: " + action + " on line " + (i + 1));
                        }
                    } catch (Exception e) {
                    }
                }
            }

            if (!subaction.isEmpty() && (action.equals("if") || action.equals("random"))) {
                compileErrors.add(String.format("&cInvalid nested actions &e%s", action));
            }

            if (action.equals("goto") && subaction.isEmpty()) {
                String type = args.get(0);
                String name = null;
                if (args.size() > 1) {
                    name = args.get(1);
                }
                if (clustersActions != null) {

                    String finalCluster1 = cluster;
                    Cluster c = Cluster.loaders.stream().filter(loader -> loader.keyword.equals(finalCluster1)).findFirst().orElse(null);
                    if (c == null) {
                        compileErrors.add("&cUnknown cluster type: " + finalCluster1);
                        break;
                    }
                    c = c.create(i, compileErrors, (String) clustersActions[0], (List<Object[]>) clustersActions[1]);
                    clusters.add(c);
                    subaction = "";
                    clustersActions = new Object[]{name, new ArrayList<>()};
                    subactions = null;
                    cluster = type;
                } else {
                    cluster = type;
                    clustersActions = new Object[]{name, new ArrayList<>()};
                }
            }

//            for (String type : clusterTypes) {
//                if (action.equals(type) && subaction.equals("")) {
//                    String name = null;
//                    if (type.equals("function") || type.equals("npc")) {
//                        String arg = line.substring(9, line.length() - 2);
//                        if (arg.startsWith("(")) {
//                            arg = arg.substring(1, arg.length() - 1);
//                        }
//                        if (arg.endsWith(")")) {
//                            arg = arg.substring(0, arg.length() - 2);
//                        }
//                        name = getArgs(arg).get(0);
//                    }
//                    cluster = type;
//                    clustersActions = new Object[]{name, new ArrayList<>()};
//                }
//
//                if (line.startsWith("}") && cluster.equals(type) && !subaction.equals("if") && !subaction.equals("else") && !subaction.equals("random")) {
//                    Cluster c = Cluster.loaders.stream().filter(loader -> loader.keyword.equals(type)).findFirst().orElse(null);
//                    if (c == null) {
//                        compileErrors.add("&cUnknown cluster type: " + type);
//                        break;
//                    }
//                    c = c.create(i, compileErrors, (String) clustersActions[0], (List<Object[]>) clustersActions[1]);
//                    clusters.add(c);
//                    subaction = "";
//                    clustersActions = null;
//                    subactions = null;
//                    cluster = "";
//                }
//            }

            if (action.equals("if") && !(subaction.equals("if") || subaction.equals("else"))) {
                subaction = "if";
                ConditionCompiler.ConditionResult result = ConditionCompiler.compile(line.substring(3, line.length() - 3), compileErrors);
                if (result.error != null) {
                    compileErrors.add(result.error);
                }
                boolean match = args.get(0).equals("or");
                subactions = new Object[]{result.condition, match, new ArrayList<>(), new ArrayList<>()};
            }
            if (line.startsWith("}") && subaction.equals("else")) {
                actionData = (new Conditional((List<Object[]>) subactions[0], (Boolean) subactions[1], (List<Object[]>) subactions[2], (List<Object[]>) subactions[3]));
                subaction = "";
                subactions = null;
            }
            if (line.startsWith("}") && subaction.equals("if") && !(line.startsWith("} else") || line.startsWith("}else"))) {
                actionData = (new Conditional((List<Object[]>) subactions[0], (Boolean) subactions[1], (List<Object[]>) subactions[2], (List<Object[]>) subactions[3]));
                subaction = "";
                subactions = null;
            }
            if ((line.startsWith("} else {") || line.startsWith("}else")) && subaction.equals("if")) {
                subaction = "else";
            }
            if (line.startsWith("random {") && subaction.equals("")) {
                subaction = "random";
                subactions = new Object[]{new ArrayList<>(), false, new ArrayList<>(), new ArrayList<>()};
            }
            if (line.startsWith("}") && subaction.equals("random")) {
                actionData = (new RandomAction((List<Object[]>) subactions[2]));
                subaction = "";
                subactions = null;
            }

            if (compileErrors.size() > 0) {
                compileErrors.forEach(UChat::chat);
                return;
            }
            if (actionData != null) {
                if (subaction.equals("if")) {
                    ((List<Object[]>) subactions[2]).add(new Object[]{actionData.name, actionData});
                    compiedActions.add(actionData);
                }
                if (subaction.equals("") && cluster.equals("")) {
                    actions.add(actionData);
                }
                if (subaction.equals("") && !cluster.equals("")) {
                    ((List<Object[]>) clustersActions[1]).add(new Object[]{actionData.name, actionData});
                }
                if (subaction.equals("else")) {
                    ((List<Object[]>) subactions[3]).add(new Object[]{actionData.name, actionData});
                    compiedActions.add(actionData);
                }
                if (subaction.equals("random")) {
                    ((List<Object[]>) subactions[2]).add(new Object[]{actionData.name, actionData});
                    compiedActions.add(actionData);
                }
            }
        }
        if (!cluster.equals("")) {
            String finalCluster = cluster;
            Cluster c = Cluster.loaders.stream().filter(loader -> loader.keyword.equals(finalCluster)).findFirst().orElse(null);
            if (c == null) {
                compileErrors.add("&cUnknown cluster type: " + cluster);
            }
            c = c.create(0, compileErrors, (String) clustersActions[0], (List<Object[]>) clustersActions[1]);
            clusters.add(c);
        }
        String limitExceeded = OccurrenceChecker.checkOccurrences(actions);
        if (limitExceeded != null) {
            UChat.chat("&cYou have exceeded the limit of: " + limitExceeded);
            return;
        }

        //Load actions
        for (int i = 0; i < actions.size(); i++) {
            List<Loader.LoaderObject> sequence = new ArrayList<>();
            Loader loader = actions.get(i);
            sequence.add(Loader.LoaderObject.setGuiContext(loader.name));
            sequence.add(Loader.LoaderObject.click(50));
            sequence.add(Loader.LoaderObject.option(loader.name));
            sequence.addAll(loader.sequence);
            sequence.add(new Loader.LoaderObject("returnToEditActions"));
            queue.addAll(sequence);
        }
        if (clusters.size() != 0) {
            for (int i = 0; i < clusters.size(); i++) {
                Cluster c = clusters.get(i);
                List<Loader.LoaderObject> sequence = new ArrayList<>(c.sequence);
                queue.addAll(sequence);
            }
        }

        queue.add(new Loader.LoaderObject("done"));
    }

    private String removeWhitespace(String line) {
        return line.trim();
    }

    public static List<String> getArgs(String input) {
        List<String> result = new ArrayList<>();
        Matcher matcher = Pattern.compile("\"((?:\\\\\"|[^\"])*)\"|'((?:\\\\'|[^'])*)'|(\\S+)").matcher(input);
        while (matcher.find()) {
            result.add(matcher.group(1) != null ? matcher.group(1) : matcher.group(2) != null ? matcher.group(2) : matcher.group(3));
        }
        return result;
    }
}
