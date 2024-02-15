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

package llc.redstone.hysentials.config.hysentialMods.icons;

import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.annotations.Text;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.elements.BasicOption;
import cc.polyfrost.oneconfig.gui.OneConfigGui;
import cc.polyfrost.oneconfig.gui.elements.BasicButton;
import cc.polyfrost.oneconfig.gui.elements.BasicElement;
import cc.polyfrost.oneconfig.gui.elements.ColorSelector;
import cc.polyfrost.oneconfig.gui.elements.IFocusable;
import cc.polyfrost.oneconfig.gui.elements.config.ConfigColorElement;
import cc.polyfrost.oneconfig.gui.elements.text.TextInputField;
import cc.polyfrost.oneconfig.platform.Platform;
import cc.polyfrost.oneconfig.renderer.NanoVGHelper;
import cc.polyfrost.oneconfig.renderer.asset.Icon;
import cc.polyfrost.oneconfig.renderer.asset.Image;
import cc.polyfrost.oneconfig.renderer.asset.SVG;
import cc.polyfrost.oneconfig.renderer.font.Fonts;
import cc.polyfrost.oneconfig.utils.InputHandler;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.color.ColorPalette;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.config.hysentialMods.rank.RankAnnotation;
import llc.redstone.hysentials.config.hysentialMods.rank.RankOption;
import llc.redstone.hysentials.config.hysentialMods.rank.RankStuff;
import org.lwjgl.Sys;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IconsOption extends BasicOption implements IFocusable {
    List<TextInputField> textInputFields = new ArrayList<>();
    BasicButton button = new BasicButton(64, 32, "File Path", BasicButton.ALIGNMENT_CENTER, ColorPalette.PRIMARY);
    List<Image> images = new ArrayList<>();
    List<IconStuff> icons;

    TextInputField currentTextInputField;
    BasicButton addIcon;

    public IconsOption(Field field, Object parent, String name, String description, String category, String subcategory) {
        super(field, parent, name, description, category, subcategory, 0);
        try {
            this.icons = (List<IconStuff>) get();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        if (icons != null) {
            for (IconStuff icon : icons) {
                TextInputField textInputField = new TextInputField(260, 32, "Icon Name", false, false);
                textInputField.setInput(icon.name);
                if (!icon.custom) {
                    textInputField.disable(true);
                }
                textInputFields.add(textInputField);
                if (icon.localPath == null) {
                    images.add(null);
                } else {
                    images.add(new Image(icon.localPath));

                    File file = new File(icon.localPath);
                    if (file.exists()) {
                        try {
                            java.awt.Image image = ImageIO.read(file);
                            int scale = (32 / image.getHeight(null));
                            icon.width = image.getWidth(null) * scale;
                            icon.height = image.getHeight(null) * scale;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        addIcon = new BasicButton(64, 32, "Add Icon", BasicButton.ALIGNMENT_CENTER, ColorPalette.PRIMARY);
    }

    public static IconsOption create(Field field, Object parent) {
        IconsAnnotation color = field.getAnnotation(IconsAnnotation.class);
        return new IconsOption(field, parent, color.name(), color.description(), color.category(), color.subcategory());
    }

    @Override
    public void draw(long vg, int x, int y, InputHandler inputHandler) {
        final NanoVGHelper nanoVGHelper = NanoVGHelper.INSTANCE;

        if (!isEnabled()) nanoVGHelper.setAlpha(vg, 0.5f);
        int tempX = x;
        for (IconStuff icon : icons) {
            int index = icons.indexOf(icon);
            if (index > textInputFields.size() - 1) {
                continue;
            }
            textInputFields.get(index).draw(vg, x, y, inputHandler);
            if (images.get(index) == null) {
                button.draw(vg, x + 286, y, inputHandler);
            } else {
                nanoVGHelper.drawImage(vg, images.get(index), x + 286, y, icon.width, icon.height);
                if (images.get(index) != null && inputHandler.isAreaHovered(x + 286, y, icon.width, icon.height) && inputHandler.isClicked()) {
                    openFileAndSave(index);
                }
            }
            if (icon.custom) {
                nanoVGHelper.drawRoundedRect(vg, x + 286 + (images.get(index) == null ? 64 : icon.width) + 10, y, 32, 32, new java.awt.Color(255, 48, 48, 255).getRGB(), 12f);
                nanoVGHelper.drawSvg(vg, new SVG("/assets/hysentials/gui/delete.svg"), x + 286 + (images.get(index) == null ? 64 : icon.width) + 10 + 2, y + 2, 28, 28);
                if (inputHandler.isAreaHovered(x + 286 + (images.get(index) == null ? 64 : icon.width) + 10, y, 32, 32) && inputHandler.isClicked()) {
                    icons.remove(icon);
                    textInputFields.remove(index);
                    images.remove(index);
                    try {
                        set(icons);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if (textInputFields.get(index).isToggled()) {
                currentTextInputField = textInputFields.get(index);
            }
            if (images.get(index) == null && button.isClicked()) {
                openFileAndSave(index);
            }
            //every 2 icons, move down
            if (index % 2 == 0 && index != 0) {
                y += 40;
                x = tempX;
            } else {
                x += 512;
            }
        }
        y += 40;

        x = tempX;


        addIcon.draw(vg, x, y, inputHandler);
        if (addIcon.isClicked()) {
            IconStuff icon = new IconStuff("", null);
            icon.custom = true;
            icons.add(icon);
            textInputFields.add(new TextInputField(260, 32, "", false, false));
            images.add(null);
            try {
                set(icons);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        nanoVGHelper.setAlpha(vg, 1f);
    }

    public void openFileAndSave(int index) {
        Multithreading.runAsync(() -> {
            File base = new File("./config/hysentials/imageicons/");
            FileDialog fileDialog = new FileDialog((Frame) null, "Select an image", FileDialog.LOAD);
            fileDialog.setDirectory(base.getAbsolutePath());
            fileDialog.setFile("*.png");
            fileDialog.setVisible(true);
            String file = fileDialog.getFile();
            if (file != null) {
                String path = "./config/hysentials/imageicons/" + file;
                images.set(index, new Image(path));
                icons.get(index).localPath = path;
                File file1 = new File(path);
                if (file1.exists()) {
                    try {
                        java.awt.Image image = ImageIO.read(file1);
                        int scale = (32 / image.getHeight(null));
                        icons.get(index).width = image.getWidth(null) * scale;
                        icons.get(index).height = image.getHeight(null) * scale;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    set(icons);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    protected void set(Object object) throws IllegalAccessException {
        super.set(object);
        Hysentials.updateAndAdd();
    }

    @Override
    public void keyTyped(char key, int keyCode) {
        if (!isEnabled()) return;
        currentTextInputField.keyTyped(key, keyCode);
        try {
            IconStuff icon = icons.get(textInputFields.indexOf(currentTextInputField));
            icon.name = currentTextInputField.getInput();
            set(icons);
        } catch (IllegalAccessException ignored) {
        }
    }

    @Override
    public int getHeight() {
        return (40 * icons.size()) / 2 + 64;
    }

    @Override
    protected boolean shouldDrawDescription() {
        return super.shouldDrawDescription();
    }

    @Override
    public boolean hasFocus() {
        return currentTextInputField != null && currentTextInputField.isToggled();
    }
}
