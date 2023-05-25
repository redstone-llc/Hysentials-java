package cc.woverflow.hysentials.guis.actionLibrary;

import cc.woverflow.hysentials.guis.container.Container;
import cc.woverflow.hysentials.guis.container.GuiItem;
import cc.woverflow.hysentials.util.Material;
import net.minecraftforge.client.event.MouseEvent;

import static net.minecraft.client.Minecraft.getMinecraft;

public class ActionLibrary extends Container {
    public ActionLibrary() {
        super("Action Library", 6);
    }

    @Override
    public void setItems() {
        setItem(0, GuiItem.fromStack(
            GuiItem.makeColorfulItem(Material.ACTIVATOR_RAIL, "&aFunctions", 1, 0, "&7Edit and create functions that", "&7can be called from other action", "&7holders.", "", "&eClick to open.")
        ));
        setItem(9, GuiItem.fromStack(
            GuiItem.makeColorfulItem(Material.EMPTY_MAP, "&aGroup Presets", 1, 0, "&7Edit and create groups", "&7that can be saved and shared.", "", "&eClick to open.")
        ));
        setItem(18, GuiItem.fromStack(
            GuiItem.makeColorfulItem(Material.STONE_AXE, "&aLayout Presets", 1, 0, "&7Edit and create layouts", "&7that can be saved and shared.", "", "&eClick to open.")
        ));
        setItem(27, GuiItem.fromStack(
            GuiItem.makeColorfulItem(Material.REDSTONE, "&aConditional Presets", 1, 0, "&7Edit and create conditionals", "&7that can be saved and shared.", "", "&eClick to open.")
        ));
    }

    @Override
    public void handleMenu(MouseEvent event) {

    }

    @Override
    public void setClickActions() {
        setDefaultAction((event) -> {
            event.getEvent().cancel();
        });
        setAction(0, (event) -> {
            getMinecraft().thePlayer.closeScreen();
            getMinecraft().thePlayer.sendChatMessage("/function");
        });

    }
}
