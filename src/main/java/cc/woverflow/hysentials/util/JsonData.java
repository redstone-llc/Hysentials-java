package cc.woverflow.hysentials.util;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.*;
import java.util.Objects;

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

    public JsonData(String resourcePath, String path, boolean resource) {
        this.path = path;
        try {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                InputStream io = JsonData.class.getResourceAsStream(resourcePath);
                String pathname = "./config/hysentials/" + resourcePath.substring(resourcePath.lastIndexOf("/") + 1);
                saveFileFromStream(io, pathname);
                file = new File(pathname);
            }
            String data = FileUtils.readFileToString(file);
            jsonObject = new JSONObject(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveFileFromStream(InputStream inputStream, String filePath) throws IOException {
        OutputStream outputStream = new FileOutputStream(filePath);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.close();
        inputStream.close();
    }

    public void save() {
        try {
            FileUtils.writeStringToFile(new File(path), jsonObject.toString(4));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
