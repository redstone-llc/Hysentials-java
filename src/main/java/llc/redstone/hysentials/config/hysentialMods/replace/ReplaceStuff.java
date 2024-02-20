package llc.redstone.hysentials.config.hysentialMods.replace;

import cc.polyfrost.oneconfig.gui.elements.BasicButton;
import cc.polyfrost.oneconfig.gui.elements.text.TextInputField;
import llc.redstone.hysentials.config.hysentialMods.utils.DeleteElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReplaceStuff {
    public String clubName;
    public HashMap<String, String> replacements;
    public List<String> clubReplacements;
    public List<String> deleted;

    transient public List<TextInputField> replaceField = new ArrayList<>();
    transient public List<TextInputField> withField = new ArrayList<>();
    transient public List<DeleteElement> removeButton = new ArrayList<>();

    transient public BasicButton addIcon;

    public ReplaceStuff(String name) {
        this.clubName = name;
        this.replacements = new HashMap<>();
        this.deleted = new ArrayList<>();
        this.clubReplacements = new ArrayList<>();
    }

    public Boolean isWithField(TextInputField field) {
        if (field == null) return null;
        for (TextInputField with : withField) {
            if (with == field) return true;
        }
        for (TextInputField replace : replaceField) {
            if (replace == field) return false;
        }
        return null;
    }
}
