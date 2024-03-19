package llc.redstone.hysentials.command;


import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.Multithreading;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.crash.CrashReport;

public class QwestiiTestCommand extends CommandBase {
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "ignite";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/ignite";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Multithreading.runAsync(() -> {
            try {
                UChat.chat("&eKaboom in 3...");
                Thread.sleep(1000);
                UChat.chat("&62...");
                Thread.sleep(1000);
                UChat.chat("&c1...");
                Thread.sleep(1000);
                UChat.chat("&8*CRACKLING EXPLOSION NOISES*");
                Thread.sleep(1000);
                UChat.chat("\n&4K A B O O M\n");
                Thread.sleep(3000);
                Minecraft.getMinecraft().crashed(new CrashReport("Kaboom", new Throwable())
                );
            } catch (InterruptedException ignored) {
            }
        });
    }
}
