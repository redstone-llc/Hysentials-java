package llc.redstone.hysentials.websocket;

import llc.redstone.hysentials.util.DuoVariable;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Request {
    List<DuoVariable<String, Object>> data = new ArrayList<>();
    public Request(Object... data) {
        data = getData(data);
        for (int i = 0; i < data.length; i+=2) {
            if (data[i] instanceof String && data[i+1] != null) {
                this.data.add(new DuoVariable<>((String) data[i], data[i+1]));
            } else {
                throw new IllegalArgumentException("Invalid data provided");
            }
        }
    }
    private Object[] getData(Object... data) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < data.length; i+=2) {
            if (data[i] instanceof Object[]) {
                list.addAll(Arrays.asList((Object[]) data[i]));
            } else {
                list.add(data[i]);
                list.add(data[i + 1]);
            }
        }
        return list.toArray();
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
