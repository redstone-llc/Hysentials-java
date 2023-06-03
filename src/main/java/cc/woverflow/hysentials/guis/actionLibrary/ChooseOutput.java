package cc.woverflow.hysentials.guis.actionLibrary;

import cc.woverflow.hysentials.guis.container.Container;
import cc.woverflow.hysentials.guis.container.GuiItem;
import cc.woverflow.hysentials.handlers.htsl.Exporter;
import cc.woverflow.hysentials.handlers.htsl.Navigator;
import cc.woverflow.hysentials.util.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.MouseEvent;

public class ChooseOutput extends Container {
   GuiScreen back;
    public ChooseOutput(GuiScreen back) {
        super("Choose Output", 3);
        this.back = back;
    }

    @Override
    public void setItems() {
        setItem(14, GuiItem.fromStack(
            GuiItem.makeColorfulItem(Material.DARK_OAK_DOOR_ITEM, "&aClub", 1, 0,
                "&7Click to export action",
                "&7to your club!",
                "",
                "&eClick to export!"
            )));
        setItem(12, GuiItem.fromStack(
            GuiItem.makeColorfulItem(Material.BOOKSHELF, "&aFile", 1, 0,
                "&7Click to export action",
                "&7to a file!",
                "",
                "&eClick to cancel!"
            )
        ));
        setItem(11, GuiItem.fromStack(
            GuiItem.makeColorfulItem(Material.PAPER, "&aClipboard", 1, 0,
                "&7Click to export action",
                "&7to your clipboard!",
                "",
                "&eClick to export!"
            )));
        setItem(15, GuiItem.fromStack(
            GuiItem.makeColorfulItem(Material.BOOK, "&aAction Library", 1, 0,
                "&7Click to export action",
                "&7to the action library!",
                "",
                "&eClick to export!"
            )));
    }

    @Override
    public void handleMenu(MouseEvent event) {

    }

    @Override
    public void setClickActions() {
        setDefaultAction((event) -> {
            event.getEvent().cancel();
        });

        setAction(14, (event) -> {
            event.getEvent().cancel();
            Exporter.export = "club";
            Minecraft.getMinecraft().thePlayer.closeScreen();

            Minecraft.getMinecraft().thePlayer.sendChatMessage("/functions");
            Navigator.setSelecting(Exporter.name);
        });
        setAction(12, (event) -> {
            event.getEvent().cancel();
            Exporter.export = "file";
            Minecraft.getMinecraft().thePlayer.closeScreen();

            Minecraft.getMinecraft().thePlayer.sendChatMessage("/functions");
            Navigator.setSelecting(Exporter.name);
        });

        setAction(11, (event) -> {
            event.getEvent().cancel();
            Exporter.export = "clipboard";
            Minecraft.getMinecraft().thePlayer.closeScreen();

            Minecraft.getMinecraft().thePlayer.sendChatMessage("/functions");
            Navigator.setSelecting(Exporter.name);
        });

        setAction(15, (event) -> {
            event.getEvent().cancel();
            Exporter.export = "library";
            Minecraft.getMinecraft().thePlayer.closeScreen();

            Minecraft.getMinecraft().thePlayer.sendChatMessage("/functions");
            Navigator.setSelecting(Exporter.name);
        });
    }
}
