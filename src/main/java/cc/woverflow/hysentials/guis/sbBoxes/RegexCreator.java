package cc.woverflow.hysentials.guis.sbBoxes;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexCreator {
    private final List<Pattern> regexes;

    public RegexCreator() {
        regexes = new ArrayList<>();

        // <Name>: <!color><Value>
        regexes.add(Pattern.compile("(.+:§[0-9a-fk-or] §[0-9a-fk-or]|.+: |.+: §[0-9a-fk-or])(.+)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE));
        // <Name> <!color><Value> THIS WAS ANNOYING TO MAKE
        regexes.add(Pattern.compile("(.+ §[0-9a-fk-or]|.+ )((?:[0-9]+,{1}?)+[0-9]+|[0-9]+)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE));
        // <color><Month>/<Day>/<Year> <Server>
        regexes.add(Pattern.compile("(\\d.)\\/(\\d.)\\/(\\d.) (.+)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE));
        // [◆⏣] <!color><Value>
        regexes.add(Pattern.compile("([◆⏣] §[0-9a-fk-or]|[◆⏣] )(.+)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE));
        regexes.add(Pattern.compile("§[0-9a-fk-or]([0-9]{0,2}):([0-9]{0,2})(.+) ", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE));
    }

    public String[] createRegex(String text) {
        String display = text;
        String r = text;
        boolean done = false;
        for (Pattern regex : regexes) {
            if (done) break;
            Matcher m = regex.matcher(text);
            while (m.find()) {
                if (m.groupCount() == 2) {
                    display = m.group(1) + "{$1}";
                    r = m.group(1) + "(.+)";
                }
                if (m.groupCount() == 3 && regex == regexes.get(4)) {
                    display = "§7{$1}:{$2}{$3}";
                    r = "§[0-9a-fk-or]([0-9]{0,2}):([0-9]{0,2})(.+) ";
                }
                if (m.groupCount() == 4 && regex == regexes.get(2)) {
                    display = "§7{$1}/{$2}/{$3} {$4}";
                    r = "(\\d.)\\/(\\d.)\\/(\\d.) (.+)";
                }
            }
        }
        return new String[]{r, display};
    }
}
