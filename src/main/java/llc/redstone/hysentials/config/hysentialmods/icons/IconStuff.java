package llc.redstone.hysentials.config.hysentialmods.icons;

public class IconStuff {
    public String name;
    public String localPath;
    public boolean custom = false;

    public transient int width = 64;
    public transient int height = 32;

    public IconStuff(String name, String localPath) {
        this.name = name;
        this.localPath = localPath;
    }

}
