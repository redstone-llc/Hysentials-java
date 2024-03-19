package org.polyfrost.chatting.chat;

import java.util.List;

public final class ChatTab {
    public ChatTab(boolean enabled,
                   String name,
                   boolean unformatted,
                   Boolean lowercase,
                   java.util.List<String> startsWith,
                   java.util.List<String> contains,
                   java.util.List<String> endsWith,
                   java.util.List<String> equals,
                   java.util.List<String> uncompiledRegex,
                   java.util.List<String> ignoreStartsWith,
                   java.util.List<String> ignoreContains,
                   java.util.List<String> ignoreEndsWith,
                   java.util.List<String> ignoreEquals,
                   java.util.List<String> uncompiledIgnoreRegex,
                   Integer color,
                   Integer hoveredColor,
                   Integer selectedColor,
                   String prefix) {

    }

    public void initialize() {
        throw new RuntimeException();
    }

    public void setMessages(List<Object> message) {
        throw new RuntimeException();
    }

    public List<Object> getMessages() {
        throw new RuntimeException();
    }

    public final String getPrefix() {
        return "this.prefix";
    }
    public final String getName() {
        return "this.name";
    }
    public final boolean isEnabled() {
        return false;
    }
    public final void setEnabled(boolean enabled) {

    }
}
