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
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.config.migration.VigilanceMigrator;
import cc.woverflow.hysentials.Hysentials;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class HysentialsConfig extends Config {

    @Text(
        name = "Chat Prefix",
        category = "General",
        description = "The prefix of most Hysentials related messages, so you know the message is a result of Hysentials and not other mods."
    )
    public static String chatPrefix = "&b[HYSENTIALS]";

    @Switch(
        name = "Global Chat Enabled",
        category = "General",
        description = "Enable global chat. This will allow you to chat with other players who are using Hysentials."
    )
    public static boolean globalChatEnabled = true;


    public static int configNumber = 0;

    @Exclude
    public static final ArrayList<String> wbMessages = new ArrayList<>();


    public HysentialsConfig() {
        super(new Mod("Hysentials", ModType.HYPIXEL, new VigilanceMigrator(new File(Hysentials.INSTANCE.modDir, "hysentials.toml").getAbsolutePath())), "hysentials.json");
        initialize();
        try {
            File modDir = Hysentials.INSTANCE.modDir;
            File oldModDir = new File(modDir.getParentFile(), "Hysentials");
            File oldConfig = new File(oldModDir, "hysentials.toml");
            if (oldConfig.exists()) {
                FileUtils.writeStringToFile(new File(modDir, "hysentials.toml"), FileUtils.readFileToString(oldConfig, StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                if (!oldConfig.renameTo(new File(modDir, "hysentials_backup.toml"))) {
                    Files.move(oldConfig.toPath(), modDir.toPath().resolve("hysentials_backup.toml"), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (configNumber != 2) { // Config version has not been set or is outdated
            if (configNumber == 1) {

            }
            configNumber = 2; // set this to the current config version
            save();
        }
    }
}
