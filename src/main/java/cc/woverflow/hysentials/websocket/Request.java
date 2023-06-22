package cc.woverflow.hysentials.websocket;

import cc.woverflow.hysentials.util.DuoVariable;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Request {
    List<DuoVariable<String, Object>> data = new ArrayList<>();
    public Request(Object... data) {
        for (int i = 0; i < data.length; i+=2) {
            if (data[i] instanceof String && data[i+1] != null) {
                this.data.add(new DuoVariable<>((String) data[i], data[i+1]));
            } else {
                throw new IllegalArgumentException("Invalid data provided");
            }
        }
    }
    @Override
    public String toString() {
        JSONObject json = new JSONObject();
        for (DuoVariable<String, Object> duoVariable : data) {
            json.put(duoVariable.getFirst(), duoVariable.getSecond());
        }
        return json.toString();
    }
}
