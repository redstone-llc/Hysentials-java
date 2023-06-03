package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;
public class Template extends Loader {
    public Template() {//Pass any arguments you need to the super constructor
        super("Template", "template"); //Pass the name and the word used in the compiler.
    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        //Create a new instance of this loader with the arguments passed to it.

        return null;
    }

    @Override
    public String export(List<String> args) {
        //Return the code that will be exported to the file.

        return null;
    }
}
