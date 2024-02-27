package org.polyfrost.chatting.chat;

import java.util.ArrayList;
import java.util.List;

public final class ChatTabs {
    public static final ChatTabs INSTANCE = new ChatTabs();

    public final ArrayList<ChatTab> getTabs() {
        throw new RuntimeException();
    }

    public final ArrayList<ChatTab> getCurrentTabs() {
        return null;
    }
}
