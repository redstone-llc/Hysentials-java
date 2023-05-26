package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class SendAChatMessage extends Loader {
    public SendAChatMessage(String message) {
        super("Send a Chat Message", "chat", message);

        if (!message.equals("Hello!")) {
            add(click(10));
            add(chat(message));
        }
    }
}
