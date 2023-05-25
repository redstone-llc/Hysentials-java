/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2022  W-OVERFLOW
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

package cc.woverflow.hysentials.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.woverflow.hytils.HytilsReborn;

public class HysentialsConfig extends Config {
    // GENERAL
    @Text(
        name = "Chat Prefix",
        category = "General",
        subcategory = "General",
        description = "The prefix of most Hysentials related messages, so you know the message is a result of Hysentials and not other mods."
    )
    public static String chatPrefix = "&b[HYSENTIALS]";

    @Switch(
        name = "Global Chat Enabled",
        category = "General",
        subcategory = "Chat",
        description = "Enable global chat. This will allow you to chat with other players who are using Hysentials."
    )
    public static boolean globalChatEnabled = true;

    @Switch(
        name = "Futuristic Ranks",
        category = "General",
        subcategory = "Ranks",
        description = "Enable futuristic ranks. This will allow you to see an image as a users rank."
    )
    public static boolean futuristicRanks = true;

    @Switch(
        name = "Futuristic Channels",
        category = "General",
        subcategory = "Chat",
        description = "Enable futuristic channels. This will allow you to see an image as a chat channel"
    )
    public static boolean futuristicChannels = true;

    @Button(
            name = "Additional Configs",
            category = "General",
            subcategory = "Hytils",
            description = "Opens the Hytils config page.",
            text = "OPEN")
    public void openHytilsConfig() {
        HytilsReborn.INSTANCE.getConfig().openGui();
    }

//    // PETS
//    @Button(
//        name = "Hamster Pet",
//        category = "Pets",
//        subcategory = "Hamster",
//        description = "Will enable the hamster pet, if you have it unlocked.",
//        text = "DISABLED"
//    )
//    public void hamsterEnabled() {
//
//    }
//
//    @Button(
//        name = "Cubit Pet",
//        category = "Pets",
//        subcategory = "Cubit",
//        description = "Will enable the cubit pet, if you have it unlocked. Only available for \"Special\" Plus Players.",
//        text = "DISABLED"
//    )
//    public void cubitEnabled() {
//    }

    // LOBBY

    @Switch(
        name = "Housing Lag Reducer",
        category= "Lobby",
        subcategory = "General",
        description = "Will reduce the lag in the housing lobby, by hiding armorstands further than 20 blocks away from the player."
    )
    public static boolean housingLagReducer = true;

    // Scoreboard Boxes
    @Color(
        name = "Color Picker",
        description = "Color for the boxes",
        category = "SBBoxes",
        subcategory = "General",
        allowAlpha = true
    )
    public static OneColor boxColor = new OneColor(0, 0, 0, 125);

    @Checkbox(
        name = "Box Shadows",
        description = "Enables box shadows",
        category = "SBBoxes",
        subcategory = "General"
    )
    public static boolean boxShadows = true;

    @Checkbox(
        name = "Scoreboard Boxes",
        description = "Enables scoreboard boxes",
        category = "SBBoxes",
        subcategory = "Boxes"
    )
    public static boolean scoreboardBoxes = true;

    @Checkbox(
        name = "Show Scoreboard",
        description = "Enables scoreboard",
        category = "SBBoxes",
        subcategory = "Boxes"
    )
    public static boolean showScoreboard = true;

    @Dropdown(
        name = "Border Radius",
        description = "Border radius of the boxes",
        category = "SBBoxes",
        subcategory = "Boxes",
        options = {"0", "2", "4"}
    )
    public static int scoreboardBoxesBorderRadius = 1;

    @Checkbox(
        name = "Action Bar",
        description = "Enables better action bar",
        category = "SBBoxes",
        subcategory = "Action Bar"
    )
    public static boolean actionBar = true;

    @Dropdown(
        name = "Border Radius",
        description = "Border radius of the action bar",
        category = "SBBoxes",
        subcategory = "Action Bar",
        options = {"0", "2", "4"}
    )
    public static int actionBarBorderRadius = 1;

    @Checkbox(
        name = "Scoreboard",
        description = "Enables better scoreboard",
        category = "SBBoxes",
        subcategory = "Scoreboard"
    )
    public static boolean scoreboard = true;

    @Checkbox(
        name = "Red Numbers",
        description = "Enables Scoreboard numbers",
        category = "SBBoxes",
        subcategory = "Scoreboard"
    )
    public static boolean redNumbers = true;

    @Dropdown(
        name = "Border Radius",
        description = "Border radius of the scoreboard",
        category = "SBBoxes",
        subcategory = "Scoreboard",
        options = {"0", "2", "4"}
    )
    public static int scoreboardBorderRadius = 1;


    public HysentialsConfig() {
        super(new Mod("Hysentials", ModType.HYPIXEL), "hysentials.json");
        this.initialize();
    }
}
