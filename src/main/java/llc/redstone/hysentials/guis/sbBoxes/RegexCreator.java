package llc.redstone.hysentials.guis.sbBoxes;

import llc.redstone.hysentials.util.C;
import llc.redstone.hysentials.util.ImageIconRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexCreator {
    private final List<Pattern> regexes;

    public RegexCreator() {
        regexes = new ArrayList<>();

        // <Name>: <!color><Value>
        regexes.add(Pattern.compile("(.+:&[0-9a-fk-or] &[0-9a-fk-or]|.+: |.+: &[0-9a-fk-or])(.+)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.UNICODE_CASE | Pattern.UNICODE_CHARACTER_CLASS));
        // <Name> <!color><Value> THIS WAS ANNOYING TO MAKE
        regexes.add(Pattern.compile("(.+ &[0-9a-fk-or]|.+ )((?:[0-9]+,{1}?)+[0-9]+|[0-9]+)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.UNICODE_CASE | Pattern.UNICODE_CHARACTER_CLASS));
        // <color><Month>/<Day>/<Year> <Server>
        regexes.add(Pattern.compile("(\\d.)\\/(\\d.)\\/(\\d.) (.+)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.UNICODE_CASE | Pattern.UNICODE_CHARACTER_CLASS));
        // [◆⏣] <!color><Value>
        regexes.add(Pattern.compile("([◆⏣] &[0-9a-fk-or]|[◆⏣] )(.+)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.UNICODE_CASE | Pattern.UNICODE_CHARACTER_CLASS));
        regexes.add(Pattern.compile("&[0-9a-fk-or]([0-9]{0,2}):([0-9]{0,2})(.+) ", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.UNICODE_CASE | Pattern.UNICODE_CHARACTER_CLASS));

        // Slayer requirements
        regexes.add(Pattern.compile("((?!\\()&e(\\d+)&7/&c(\\d+)(?>&7\\) | &7))(.+)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.UNICODE_CASE | Pattern.UNICODE_CHARACTER_CLASS));
    }

    public String[] createRegex(String text) {
        text = C.removeRepeatColor(text);
        text = text.replace("§", "&");
        String display = text;
        String r = text;
        boolean done = false;
        for (Pattern regex : regexes) {
            if (done) break;
            Matcher m = regex.matcher(text);
            while (m.find()) {
                if (m.groupCount() == 2 && regex != regexes.get(3)) {
                    display = m.group(1) + "{$1}";
                    r = m.group(1) + "(.+)";
                }
                if (m.groupCount() == 3 && regex == regexes.get(4)) {
                    display = "&7{$1}:{$2}{$3}";
                    r = "&[0-9a-fk-or]([0-9]{0,2}):([0-9]{0,2})(.+) ";
                }
                if (m.groupCount() == 2 && regex == regexes.get(3)) {
                    display = m.group(1) + "{$1}{$2}";
                    r = C.removeColor(m.group(1)) + "(&[0-9a-fk-or])(.+)";
                }
                if (m.groupCount() == 4 && regex == regexes.get(2)) {
                    display = "&7{$1}/{$2}/{$3} {$4}";
                    r = "(\\d.)\\/(\\d.)\\/(\\d.) (.+)";
                }
                if (m.groupCount() == 4 && regex == regexes.get(5)) {
                    display = "&7(&e{$2}&7/&c{$3}&7) {$4}";
                    r = "((?!\\()&e(\\d+)&7/&c(\\d+)(?>&7\\) | &7))(.+)";
                }
                done = true;
            }
        }
        return new String[]{r, display};
    }
}
