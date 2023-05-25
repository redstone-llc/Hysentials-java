package cc.woverflow.hysentials.util;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;

public class JsonData {
    String path;
    public JSONObject jsonObject;

    public JsonData(String path, JSONObject defaultData) {
        this.path = path;
        try {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                FileUtils.writeStringToFile(file, defaultData.toString());
            }
            String data = FileUtils.readFileToString(file);
            jsonObject = new JSONObject(data);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        try {
            FileUtils.writeStringToFile(new File(path), jsonObject.toString(4));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
