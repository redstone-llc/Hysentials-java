package cc.woverflow.hysentials.command;


import cc.woverflow.hysentials.util.MUtils;
import cc.woverflow.hysentials.util.C;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;

public class RenameCommand extends CommandBase {
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "rename";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/rename <value>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        ItemStack item = Minecraft.getMinecraft().thePlayer.getHeldItem();
        if (item == null || item.getItem() == null) {
            MUtils.chat("§cYou must be holding an item!");
            return;
        }
        item = item.copy();
        StringBuilder builder = new StringBuilder("");
        String completeArgs = "";

        for (String name : args) {
            builder.append(name).append(" ");
        }

        completeArgs = builder.toString().trim();

        item.setStackDisplayName(C.translate(completeArgs));
        setCreativeAction(item, Minecraft.getMinecraft().thePlayer.inventory.currentItem);
        MUtils.chat("§aRenamed item to: §r" + C.translate(completeArgs));
    }

    public static void setCreativeAction(ItemStack item, int slot){
        Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(
            new C10PacketCreativeInventoryAction(slot + 36, item)
        );
    }
}
