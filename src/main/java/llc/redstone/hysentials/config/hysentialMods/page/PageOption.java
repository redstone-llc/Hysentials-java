/*
 * This file is part of OneConfig.
 * OneConfig - Next Generation Config Library for Minecraft: Java Edition
 * Copyright (C) 2021~2023 Polyfrost.
 *   <https://polyfrost.cc> <https://github.com/Polyfrost/>
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 *   OneConfig is licensed under the terms of version 3 of the GNU Lesser
 * General Public License as published by the Free Software Foundation, AND
 * under the Additional Terms Applicable to OneConfig, as published by Polyfrost,
 * either version 1.0 of the Additional Terms, or (at your option) any later
 * version.
 *
 *   This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 * License.  If not, see <https://www.gnu.org/licenses/>. You should
 * have also received a copy of the Additional Terms Applicable
 * to OneConfig, as published by Polyfrost. If not, see
 * <https://polyfrost.cc/legal/oneconfig/additional-terms>
 */

package llc.redstone.hysentials.config.hysentialMods.page;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.elements.BasicOption;
import cc.polyfrost.oneconfig.gui.OneConfigGui;
import cc.polyfrost.oneconfig.gui.elements.BasicElement;
import cc.polyfrost.oneconfig.renderer.NanoVGHelper;
import cc.polyfrost.oneconfig.utils.InputHandler;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.config.HysentialsModCard;
import llc.redstone.hysentials.config.hysentialMods.rank.RankStuff;
import llc.redstone.hysentials.util.C;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PageOption extends BasicOption {
    private List<BasicElement> element = new ArrayList<>();
    private List<Config> config;


    public PageOption(Field field, Object parent, String name, String description, String category, String subcategory, boolean group) {
        super(field, parent, name, description, category, subcategory, 0);
        try {
            if (group) {
                config = (List<Config>) get();
            } else {
                config = Collections.singletonList((Config) get());
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        if (config != null) {
            for (Config config : config) {
                if (config != null) {
                    element.add(new HysentialsModCard(config.mod, true, !config.enabled, false));
                }
            }
        }
    }

    public static PageOption create(Field field, Object parent) {
        PageAnnotation page = field.getAnnotation(PageAnnotation.class);
        return new PageOption(field, parent, page.name(), page.description(), page.category(), page.subcategory(), page.group());
    }

    @Override
    public void draw(long vg, int x, int y, InputHandler inputHandler) {
        if (OneConfigGui.INSTANCE == null) return;
        final NanoVGHelper nanoVGHelper = NanoVGHelper.INSTANCE;
        for (BasicElement element : element) {
            element.draw(vg, x, y, inputHandler);
            x += 260;
        }
    }


    @Override
    public int getHeight() {
        return 135;
    }
}
