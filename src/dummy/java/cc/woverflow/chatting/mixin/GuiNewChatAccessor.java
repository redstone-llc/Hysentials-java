package cc.woverflow.chatting.mixin;

import java.util.List;

public interface GuiNewChatAccessor {
    List getDrawnChatLines();
    List getChatLines();
    int getScrollPos();
}
