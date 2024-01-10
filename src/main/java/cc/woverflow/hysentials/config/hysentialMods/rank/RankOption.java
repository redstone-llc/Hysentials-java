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

package cc.woverflow.hysentials.config.hysentialMods.rank;

import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.elements.BasicOption;
import cc.polyfrost.oneconfig.gui.OneConfigGui;
import cc.polyfrost.oneconfig.gui.elements.BasicElement;
import cc.polyfrost.oneconfig.gui.elements.ColorSelector;
import cc.polyfrost.oneconfig.gui.elements.config.ConfigColorElement;
import cc.polyfrost.oneconfig.renderer.NanoVGHelper;
import cc.polyfrost.oneconfig.renderer.asset.Image;
import cc.polyfrost.oneconfig.renderer.asset.SVG;
import cc.polyfrost.oneconfig.renderer.font.Fonts;
import cc.polyfrost.oneconfig.utils.InputHandler;

import java.lang.reflect.Field;

public class RankOption extends BasicOption {
    private final BasicElement element = new BasicElement(64, 32, false);
    private final BasicElement element2 = new BasicElement(64, 32, false);
    private final BasicElement element3 = new BasicElement(32, 32, false);
    private boolean open = false;
    private boolean open2 = false;
    private RankStuff defaultRank;


    public RankOption(Field field, Object parent, String name, String description, String category, String subcategory, String defaultNametagColor, String defaultChatMessageColor) {
        super(field, parent, name, description, category, subcategory, 0);
        defaultRank = new RankStuff(new OneColor(defaultNametagColor), new OneColor(defaultChatMessageColor));
    }

    public static RankOption create(Field field, Object parent) {
        RankAnnotation color = field.getAnnotation(RankAnnotation.class);
        return new RankOption(field, parent, color.name(), color.description(), color.category(), color.subcategory(), color.defaultNametagColor(), color.defaultChatMessageColor());
    }

    @Override
    public void draw(long vg, int x, int y, InputHandler inputHandler) {
        if (OneConfigGui.INSTANCE == null) return;
        final NanoVGHelper nanoVGHelper = NanoVGHelper.INSTANCE;
        y = y + 20;

        if (!isEnabled()) nanoVGHelper.setAlpha(vg, 0.5f);
        element.disable(!isEnabled());
        element2.disable(!isEnabled());

        RankStuff rank;
        try {
            rank = (RankStuff) get();
        } catch (IllegalAccessException e) {
            return;
        }
        nanoVGHelper.drawText(vg, name, x, y + 3, nameColor, 14f, Fonts.MEDIUM);

        element.update(x + 384, y, inputHandler);
        element2.update(x + 416 + 128, y, inputHandler);
        element3.update(x + 415 + 128 + 64 + 32, y, inputHandler);

        float lenth = nanoVGHelper.getTextWidth(vg, "Nametag Color", 14f, Fonts.MEDIUM);
        nanoVGHelper.drawText(vg, "Nametag Color", x + 385 + 32 - (lenth / 2), y - 16, nameColor, 14f, Fonts.MEDIUM);

        nanoVGHelper.drawHollowRoundRect(vg, x + 384, y - 1, 64, 32, new java.awt.Color(73, 79, 92, 255).getRGB(), 12f, 2f);
        nanoVGHelper.drawRoundImage(vg, new Image("/assets/oneconfig/options/AlphaGrid.png"), x + 389, y + 4, 56, 24, 8f);
        nanoVGHelper.drawRoundedRect(vg, x + 389, y + 4, 56, 24, rank.nametagColor.getRGBNoAlpha(), 8f);

        lenth = nanoVGHelper.getTextWidth(vg, "Chat Message Color", 14f, Fonts.MEDIUM);
        nanoVGHelper.drawText(vg, "Chat Message Color", x + 416 + 128 + 32 - (lenth / 2), y - 16, nameColor, 14f, Fonts.MEDIUM);

        nanoVGHelper.drawHollowRoundRect(vg, x + 415 + 128, y - 1, 64, 32, new java.awt.Color(73, 79, 92, 255).getRGB(), 12f, 2f);
        nanoVGHelper.drawRoundImage(vg, new Image("/assets/oneconfig/options/AlphaGrid.png"), x + 420 + 128, y + 4, 56, 24, 8f);
        nanoVGHelper.drawRoundedRect(vg, x + 420 + 128, y + 4, 56, 24, rank.chatMessageColor.getRGBNoAlpha(), 8f);

        //reset button
        if (element3.isHovered()) nanoVGHelper.setAlpha(vg, 0.5f);
        if (!defaultRank.nametagColor.getHex().equals(rank.nametagColor.getHex()) || !defaultRank.chatMessageColor.getHex().equals(rank.chatMessageColor.getHex())) {
            nanoVGHelper.drawRoundedRect(vg, x + 415 + 128 + 64 + 32, y - 1, 32, 32, new java.awt.Color(73, 79, 92, 255).getRGB(), 12f);
            nanoVGHelper.drawSvg(vg, new SVG("/assets/hysentials/gui/reset.svg"), x + 415 + 128 + 64 + 32, y - 1, 32, 32);
        }

        if (element.isClicked() && !open) {
            open = true;
            OneConfigGui.INSTANCE.initColorSelector(new ColorSelector(rank.nametagColor, inputHandler.mouseX(), inputHandler.mouseY(), true, inputHandler));
        }
        if (element2.isClicked() && !open2 && !open) {
            open2 = true;
            OneConfigGui.INSTANCE.initColorSelector(new ColorSelector(rank.chatMessageColor, inputHandler.mouseX(), inputHandler.mouseY(), true, inputHandler));
        }
        if (element3.isClicked() && !open && !open2 && (!defaultRank.nametagColor.getHex().equals(rank.nametagColor.getHex()) || !defaultRank.chatMessageColor.getHex().equals(rank.chatMessageColor.getHex()))) {
            rank.nametagColor = defaultRank.nametagColor;
            rank.chatMessageColor = defaultRank.chatMessageColor;
            setColor(rank);
        }

        if (OneConfigGui.INSTANCE.currentColorSelector == null) {
            open = false;
            open2 = false;
        } else if (open) rank.nametagColor = (OneConfigGui.INSTANCE.getColor());
        else if (open2) rank.chatMessageColor = (OneConfigGui.INSTANCE.getColor());
        setColor(rank);
        nanoVGHelper.setAlpha(vg, 1f);
    }

    protected void setColor(RankStuff rank) {
        try {
            set(rank);
        } catch (IllegalAccessException ignored) {
        }
    }

    @Override
    public int getHeight() {
        return 48;
    }
}
