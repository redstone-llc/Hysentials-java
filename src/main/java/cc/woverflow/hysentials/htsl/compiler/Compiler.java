package cc.woverflow.hysentials.htsl.compiler;

import cc.woverflow.hysentials.handlers.htsl.Queue;
import cc.woverflow.hysentials.htsl.Loader;
import cc.woverflow.hysentials.htsl.OccurrenceChecker;
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
    public Compiler(String code) {
        String[] lines = code.split("\n");
        String subaction = "";
        List<String> compileErrors = new ArrayList<>();
        Object[] subactions = null;
        List<Loader> actions = new ArrayList<>();
        compiedActions = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            line = removeWhitespace(line);

            List<String> args = getArgs(line);
            if (args.isEmpty()) {
                continue;
            }
            String action = new String(args.get(0));
            Loader actionData = null;
            args.remove(0);
            switch (action) {
                case "//":
                    break;
                default: {
                    if (action.equals("if") && !(subaction.equals("if") || subaction.equals("else"))) break;
                    if (line.startsWith("}") && subaction.equals("else")) break;
                    if (line.startsWith("}") && subaction.equals("if") && !(line.startsWith("} else") || line.startsWith("}else"))) break;
                    if ((line.startsWith("} else {") || line.startsWith("}else")) && subaction.equals("if")) break;
                    if (line.startsWith("random {") && subaction.equals("")) break;
                    if (line.startsWith("}") && subaction.equals("random")) break;
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
                compileErrors.forEach(System.out::println);
                return;
            }
            if (actionData != null) {
                if (subaction.equals("if")) {
                    ((List<Object[]>) subactions[2]).add(new Object[]{actionData.name, actionData});
                    compiedActions.add(actionData);
                }
                if (subaction.equals("")) {
                    actions.add(actionData);
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

        String limitExceeded = OccurrenceChecker.checkOccurrences(actions);
        if (limitExceeded != null) {
            System.out.println(limitExceeded);
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

        queue.add(new Loader.LoaderObject("done"));

        for (Loader.LoaderObject loaderObject : queue) {
            System.out.println(loaderObject.type + " " + loaderObject.key + ": " + loaderObject.value);
        }
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
