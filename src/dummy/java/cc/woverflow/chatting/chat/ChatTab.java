/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020, 2021, 2022  Polyfrost, Sk1er LLC and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cc.woverflow.chatting.chat;

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

    public void setMessages(List<String> message) {
        throw new RuntimeException();
    }

    public List<String> getMessages() {
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
