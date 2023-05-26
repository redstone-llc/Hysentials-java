package cc.woverflow.hysentials.htsl.compiler;

import cc.woverflow.hysentials.htsl.Loader;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Compiler {
    public Compiler(String code) {
        String[] lines = code.split("\n");
        List<String> compileErrors = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            line = removeWhitespace(line);

            List<String> args = getArgs(line);
            String action = new String(args.get(0));
            args.remove(0);

            switch (action) {
                case "//":
                    break;
                default: {
                    try {
                        if (Loader.loaders.stream().anyMatch(loader -> loader.keyword.equals(action))) {
                            Loader loader = Loader.loaders.stream().filter(l -> l.keyword.equals(action)).findFirst().orElse(null);
                            if (loader != null) {
                                loader.load(i, args, compileErrors);
                            }
                        } else {
                            compileErrors.add("&cUnknown action: " + action + " on line " + (i + 1));
                        }
                    } catch (Exception e) {
                        compileErrors.add("&cError on line " + (i + 1) + ": " + e.getMessage());
                    }
                }
            }
        }

    }

    private String removeWhitespace(String line) {
        return line.trim();
    }

    public List<String> getArgs(String input) {
        List<String> result = new ArrayList<>();
        Matcher matcher = Pattern.compile("\"((?:\\\\\"|[^\"])*)\"|'((?:\\\\'|[^'])*)'|(\\S+)").matcher(input);
        while (matcher.find()) {
            result.add(matcher.group(1) != null ? matcher.group(1) : matcher.group(2) != null ? matcher.group(2) : matcher.group(3));
        }
        return result;
    }
}
