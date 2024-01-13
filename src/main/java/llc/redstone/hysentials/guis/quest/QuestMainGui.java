package llc.redstone.hysentials.guis.quest;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import llc.redstone.hysentials.guis.container.Container;
import llc.redstone.hysentials.guis.container.GuiItem;
import llc.redstone.hysentials.util.Material;
import llc.redstone.hysentials.guis.container.Container;
import llc.redstone.hysentials.guis.container.GuiItem;
import llc.redstone.hysentials.util.Material;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.MouseEvent;

public class QuestMainGui extends Container {
    public QuestMainGui() {
        super("Quest Menu", 3);
    }

    @Override
    public void setItems() {
        fill(GuiItem.fromStack(this.BLACK_STAINED_GLASS_PANE));
        setItem(12, GuiItem.fromStack(GuiItem.makeColorfulItem(Material.BOOK_AND_QUILL, "&aDaily Quests", 1, 0,
            "&7View your current daily quests",
            "&7and claim rewards for completing them",
            "",
            "&eClick to view your daily quests")
        ));

        setItem(14, GuiItem.fromStack(GuiItem.makeColorfulItem(Material.EMERALD_BLOCK, "&aQuests", 1, 0,
            "&7View your current quests",
            "&7and claim rewards for completing them",
            "",
            "&eClick to view your quests")
        ));

        setItem(22, GuiItem.fromStack(GuiItem.makeColorfulItem(Material.BARRIER, "&cClose", 1, 0)));
    }

    @Override
    public void handleMenu(MouseEvent event) {

    }

    @Override
    public void setClickActions() {
        setDefaultAction((event) -> event.getEvent().cancel());
        setAction(12, (event) -> {
            event.getEvent().cancel();
            new DailyQuestGui().open();
        });
        setAction(14, (event) -> {
            event.getEvent().cancel();
            new QuestGui().open();
        });
        setAction(22, (event) -> {
            event.getEvent().cancel();
            Minecraft.getMinecraft().thePlayer.closeScreen();
        });
    }
}
