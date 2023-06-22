package cc.woverflow.hysentials.handlers.chat.modules.misc;

import cc.woverflow.hysentials.Hysentials;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.network.play.client.C01PacketChatMessage;

import static cc.woverflow.hysentials.handlers.chat.modules.misc.Limit256.c01MessageField;

public class GuiChat256 extends GuiChat
{
    public GuiChat256()
    {
    	super();

    }

    public GuiChat256(String defaultText)
    {
    	super(defaultText);
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
	@Override
    public void initGui()
    {
		super.initGui();
        this.inputField.setMaxStringLength(256);
    }

	@Override
	public void sendChatMessage(String msg)
    {
        this.sendChatMessage(msg, true);
    }

	@Override
    public void sendChatMessage(String msg, boolean addToChat)
    {
        if (addToChat)
        {
            this.mc.ingameGUI.getChatGUI().addToSentMessages(msg);
        }
        if (net.minecraftforge.client.ClientCommandHandler.instance.executeCommand(mc.thePlayer, msg) != 0) return;

        if (msg.length() > 256)
        {
            msg = msg.substring(0, 256);
        }
        sendMessage(msg);
    }

    public static void sendMessage(String msg) {
        C01PacketChatMessage packet = new C01PacketChatMessage();

        try {
            c01MessageField.set(packet, msg);
        } catch (IllegalAccessException e) {
            Hysentials.INSTANCE.getLogger().error("Error setting message length, sticking with 100.", e);
            packet = new C01PacketChatMessage(msg);
        }

        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(packet);
    }
}
