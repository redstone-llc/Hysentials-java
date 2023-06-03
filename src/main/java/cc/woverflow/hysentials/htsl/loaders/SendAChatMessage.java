package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class SendAChatMessage extends Loader {
    public SendAChatMessage(String message) {
        super("Send a Chat Message", "chat", message);

        if (message != null && !message.equals("Hello!")) {
            add(click(10));
            add(chat(message));
        }
    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        return new SendAChatMessage(args.get(0));
    }

    @Override
    public String export(List<String> args) {
        return "chat \"" + args.get(0) + "\"";
    }
}
