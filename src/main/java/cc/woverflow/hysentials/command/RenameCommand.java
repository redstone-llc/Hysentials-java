package cc.woverflow.hysentials.command;


import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.woverflow.hysentials.util.C;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;

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
            UChat.chat("§cYou must be holding an item!");
            return;
        }
        StringBuilder builder = new StringBuilder("");
        String completeArgs = "";

        for (String name : args) {
            builder.append(name).append(" ");
        }

        completeArgs = builder.toString().trim();

        item.setStackDisplayName(C.translate(completeArgs));
        setCreativeAction(item, Minecraft.getMinecraft().thePlayer.inventory.currentItem);
        UChat.chat("§aRenamed item to: §r" + C.translate(completeArgs));
    }

    public static void setCreativeAction(ItemStack item, int slot){
        Minecraft.getMinecraft().playerController.sendSlotPacket(item, slot);
    }
}
