/*
 * This file is part of OneConfig.
 * OneConfig - Next Generation Config Library for Minecraft: Java Edition
 * Copyright (C) 2021~2023 Polyfrost and Kendell R.
 *   <https://polyfrost.cc> <https://github.com/Polyfrost/>
 * Co-author: Kendell R (KTibow) <https://github.com/KTibow>
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

package cc.woverflow.hysentials.config;

import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.gui.OneConfigGui;
import cc.polyfrost.oneconfig.gui.animations.ColorAnimation;
import cc.polyfrost.oneconfig.gui.elements.BasicButton;
import cc.polyfrost.oneconfig.gui.elements.BasicElement;
import cc.polyfrost.oneconfig.gui.pages.ModConfigPage;
import cc.polyfrost.oneconfig.gui.pages.ModsPage;
import cc.polyfrost.oneconfig.platform.Platform;
import cc.polyfrost.oneconfig.renderer.NanoVGHelper;
import cc.polyfrost.oneconfig.renderer.font.Fonts;
import cc.polyfrost.oneconfig.renderer.scissor.Scissor;
import cc.polyfrost.oneconfig.renderer.scissor.ScissorHelper;
import cc.polyfrost.oneconfig.utils.InputHandler;
import cc.polyfrost.oneconfig.utils.color.ColorPalette;
import cc.polyfrost.oneconfig.utils.color.ColorUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class HysentialsModCard extends BasicElement {
    private final Mod modData;
    private final ColorAnimation colorFrame = new ColorAnimation(ColorPalette.SECONDARY);
    private final ColorAnimation colorToggle;
    private boolean active, disabled, favorite;
    private boolean isHoveredMain = false;
    private final HysentialsMods page;

    public HysentialsModCard(@NotNull Mod mod, boolean active, boolean disabled, boolean favorite, HysentialsMods page) {
        super(244, 244, false);
        this.modData = mod;
        this.active = active;
        toggled = active;
        colorToggle = new ColorAnimation(active ? ColorPalette.PRIMARY : ColorPalette.SECONDARY);
        this.disabled = disabled;
        this.favorite = favorite;
        toggled = active;
        this.page = page;
    }

    @Override
    public void draw(long vg, float x, float y, InputHandler inputHandler) {
        super.update(x, y, inputHandler);
        ScissorHelper scissorHelper = ScissorHelper.INSTANCE;
        NanoVGHelper nanoVGHelper = NanoVGHelper.INSTANCE;

        String cleanName = modData.name.replaceAll("§.", "");
        Scissor scissor = scissorHelper.scissor(vg, x, y, width, height);

        isHoveredMain = inputHandler.isAreaHovered(x, y, width, 87);
        boolean isHoveredSecondary = inputHandler.isAreaHovered(x, y + 87, width - 32, 32) && !disabled;
        if (disabled) nanoVGHelper.setAlpha(vg, 0.5f);
        nanoVGHelper.drawRoundedRectVaried(vg, x, y, width, 87, colorFrame.getColor(isHoveredMain, isHoveredMain && Platform.getMousePlatform().isButtonDown(0)), 12f, 12f, 0f, 0f);
        nanoVGHelper.drawRoundedRectVaried(vg, x, y + 87, width, 32, colorToggle.getColor(isHoveredSecondary, isHoveredSecondary && Platform.getMousePlatform().isButtonDown(0)), 0f, 0f, 12f, 12f);
        nanoVGHelper.drawLine(vg, x, y + 86, x + width, y + 86, 2, new Color(73, 79, 92, 255).getRGB());

        drawIcon(nanoVGHelper, vg, x, y, cleanName);

        Scissor scissor2 = scissorHelper.scissor(vg, x, y + 87, width - 32, 32);
        nanoVGHelper.drawText(vg, cleanName, x + 12, y + 103, ColorUtils.setAlpha(new Color(255, 255, 255, 255).getRGB(), (int) (colorToggle.getAlpha() * 255)), 14f, Fonts.MEDIUM);
        scissorHelper.resetScissor(vg, scissor2);

        if (clicked && isHoveredMain) {
            if (!active) toggled = false;
        }
        if (clicked && !isHoveredSecondary && active) toggled = true;
        if (!active & disabled) toggled = false;

        if (active != toggled) {
            active = toggled;
            if (modData.config.canToggle) {
                colorToggle.setPalette(active ? ColorPalette.PRIMARY : ColorPalette.SECONDARY);
                modData.config.enabled = active;
                modData.config.save();
            } else {
                toggled = active = true;
            }
        }
        nanoVGHelper.setAlpha(vg, 1f);
        scissorHelper.resetScissor(vg, scissor);
    }

    protected void drawIcon(NanoVGHelper nanoVGHelper, long vg, float x, float y, String cleanName) {
        int width = modData.iconWidth == -1 ? 56 : modData.iconWidth;
        int height = modData.iconHeight == -1 ? width : modData.iconHeight;

        if (modData.modIcon != null) {
            if (modData.modIcon.toLowerCase().endsWith(".svg")) {
                nanoVGHelper.drawSvg(vg, modData.modIcon, x + 122 - (width / 2f), y + 43 - (height / 2f), width, height);
            } else {
                nanoVGHelper.drawRoundImage(vg, modData.modIcon, x + 122 - (width / 2f), y + 43 - (height / 2f), width, height, 12f);
            }
        } else {
            nanoVGHelper.drawText(vg, cleanName, x + Math.max(0, (244 - nanoVGHelper.getTextWidth(vg, cleanName, 16, Fonts.MINECRAFT_BOLD))) / 2f, y + 44, ColorUtils.setAlpha(new Color(255, 255, 255, 255).getRGB(), (int) (colorFrame.getAlpha() * 255)), 16, Fonts.MINECRAFT_BOLD);
        }
    }

    public void onClick() {
        if (isHoveredMain) {
            OneConfigGui.INSTANCE.openPage(new ModConfigPage(modData.defaultPage));
        }
    }

    public Mod getModData() {
        return modData;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isFavorite() {
        return favorite;
    }
}
