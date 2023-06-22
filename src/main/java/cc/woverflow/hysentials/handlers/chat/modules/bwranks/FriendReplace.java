package cc.woverflow.hysentials.handlers.chat.modules.bwranks;

import cc.polyfrost.oneconfig.events.event.ChatReceiveEvent;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.woverflow.hysentials.util.MUtils;
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hysentials.handlers.chat.ChatReceiveModule;
import cc.woverflow.hysentials.handlers.redworks.BwRanksUtils;
import cc.woverflow.hysentials.util.BlockWAPIUtils;
import cc.woverflow.hysentials.util.C;
import cc.woverflow.hysentials.util.HypixelRanks;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.Sys;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FriendReplace implements ChatReceiveModule {
    public static boolean collecting = false;
    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        if (!HypixelUtils.INSTANCE.isHypixel()) return;
        if (event.type != 0 && event.type != 1) return;
        String message = event.message.getFormattedText().replaceAll("§r", "");
        String[] split = message.split("\n");
        if (split.length < 4) return;
        if (C.removeColor(split[1]).replace(" ", "").startsWith("Friends(Page") || C.removeColor(split[1]).replace(" ", "").startsWith("<<Friends(Page")) {
            collecting = true;
        }


        if (!collecting) return;
        IChatComponent component = event.message;
        List<IChatComponent> siblings = component.getSiblings();
        Pattern pattern = Pattern.compile("§9(§[0-9a-fk-or])(.+)§r");
        for (IChatComponent comp : siblings) {
            String s = comp.getFormattedText();
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                String name = matcher.group(2);
                String color = matcher.group(1);
                for (HypixelRanks rank : HypixelRanks.values()) {
                    if (rank.getColor().equals(color)) {
                        s = s.replaceAll(color + name, rank.getNametag() + name);
                    }
                }
                siblings.set(siblings.indexOf(comp), new UTextComponent(s));
            }
        }
        Minecraft.getMinecraft().thePlayer.addChatMessage(component);
        event.setCanceled(true);
        collecting = false;
    }
}
