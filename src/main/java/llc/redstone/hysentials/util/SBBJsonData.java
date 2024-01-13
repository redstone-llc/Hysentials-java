package llc.redstone.hysentials.util;

import llc.redstone.hysentials.guis.utils.SBBoxes;
import org.json.JSONArray;
import org.json.JSONObject;

public class SBBJsonData extends JsonData{
    public SBBJsonData(String path, JSONObject defaultData) {
        super(path, defaultData);
        JSONArray jsonArray = jsonObject.getJSONArray("lines");
        SBBoxes.boxes.clear();
        for (Object line : jsonArray) {
            JSONObject obj = (JSONObject) line;
            SBBoxes.boxes.add(new SBBoxes(obj));
        }
    }
}
