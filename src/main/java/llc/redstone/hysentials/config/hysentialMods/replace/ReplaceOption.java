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

package llc.redstone.hysentials.config.hysentialMods.replace;

import cc.polyfrost.oneconfig.config.elements.BasicOption;
import cc.polyfrost.oneconfig.gui.elements.BasicButton;
import cc.polyfrost.oneconfig.gui.elements.IFocusable;
import cc.polyfrost.oneconfig.gui.elements.text.TextInputField;
import cc.polyfrost.oneconfig.renderer.NanoVGHelper;
import cc.polyfrost.oneconfig.renderer.asset.Image;
import cc.polyfrost.oneconfig.renderer.asset.SVG;
import cc.polyfrost.oneconfig.renderer.font.Fonts;
import cc.polyfrost.oneconfig.utils.InputHandler;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.color.ColorPalette;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.config.hysentialMods.icons.IconStuff;
import llc.redstone.hysentials.config.hysentialMods.icons.IconsAnnotation;
import llc.redstone.hysentials.config.hysentialMods.utils.DeleteElement;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReplaceOption extends BasicOption implements IFocusable {
    List<ReplaceStuff> replacements;

    TextInputField currentTextInputField;
    DeleteElement deleteElement = new DeleteElement(32, 32);
    boolean clubsPage = false;

    //Trying this new system was not worth it lol
    public ReplaceOption(Field field, Object parent, String name, String description, String category, String subcategory) {
        super(field, parent, name, description, category, subcategory, 0);
        if (subcategory.isEmpty()) {
            clubsPage = true;
        }

        reload();
    }

    private void reload () {
        try {
            this.replacements = (List<ReplaceStuff>) get();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        if (replacements != null) {
            for (ReplaceStuff replace : replacements) {
                replace.replaceField = new ArrayList<>();
                replace.withField = new ArrayList<>();
                replace.removeButton = new ArrayList<>();

                List<String> keys = new ArrayList<>(replace.replacements.keySet());
                keys.sort(String::compareTo);
                for (String key : keys) {
                    addElements(replace, key, replace.replacements.get(key), clubsPage);
                }

                replace.addIcon = new BasicButton(96, 32, "Add Replace", BasicButton.ALIGNMENT_CENTER, ColorPalette.PRIMARY);
            }
        }
    }

    public void addElements(ReplaceStuff replace, String key, String value, boolean clubsPage) {
        TextInputField replaceField = new TextInputField(460, 32, false, "String to replace");
        replaceField.setInput(key);
        if (clubsPage && replace.clubReplacements.contains(key)) {
            replaceField.disable(true);
        }
        replace.replaceField.add(replaceField);

        TextInputField withField = new TextInputField(460, 32, false, "String to replace with");
        withField.setInput(value);
        replace.withField.add(withField);

        replace.removeButton.add(new DeleteElement(32, 32));
    }

    public void updateElements(ReplaceStuff replace, String key, String value, boolean clubsPage) {
        List<String> keys = new ArrayList<>(replace.replacements.keySet());
        keys.sort(String::compareTo);
        int index = keys.indexOf(key);
        replace.replaceField.get(index).setInput(key);
        replace.withField.get(index).setInput(value);
    }

    public static ReplaceOption create(Field field, Object parent) {
        ReplaceAnnotation color = field.getAnnotation(ReplaceAnnotation.class);
        return new ReplaceOption(field, parent, color.name(), color.description(), color.category(), color.subcategory());
    }

    public  void removeElements(ReplaceStuff replaceStuff, String key) {
        List<String> keys = new ArrayList<>(replaceStuff.replacements.keySet());
        keys.sort(String::compareTo);
        int index = keys.indexOf(key);
        replaceStuff.replaceField.remove(index);
        replaceStuff.withField.remove(index);
        replaceStuff.removeButton.remove(index);
    }

    @Override
    public void draw(long vg, int x, int y, InputHandler inputHandler) {
        final NanoVGHelper nanoVGHelper = NanoVGHelper.INSTANCE;

        if (!isEnabled()) nanoVGHelper.setAlpha(vg, 0.5f);
        try {

            for (ReplaceStuff replace : replacements) {
                if (clubsPage) {
                    nanoVGHelper.drawText(vg, replace.clubName, x, y + 12, new Color(255, 255, 255, 229).getRGB(), 24, Fonts.MEDIUM);
                    y+= 36;
                }
                List<String> keys = new ArrayList<>(replace.replacements.keySet());
                keys.sort(String::compareTo);
                for (int i = 0; i < keys.size(); i++) {
                    if (replace.replaceField.size() != replace.replacements.size()) {
                        reload();
                    }
                    TextInputField replaceField = replace.replaceField.get(i);
                    TextInputField withField = replace.withField.get(i);
                    DeleteElement deleteElement = replace.removeButton.get(i);

                    replaceField.draw(vg, x, y, inputHandler);
                    withField.draw(vg, x + 475, y, inputHandler);


                    deleteElement.draw(vg, x + 950, y, inputHandler);
                    if (deleteElement.isClicked()) {
                        replace.replacements.remove(replaceField.getInput());
                        replace.replaceField.remove(i);

                        if (clubsPage && replace.clubReplacements.contains(replaceField.getInput())) {
                            replace.deleted.add(replaceField.getInput());
                        }

                        set(replacements);
                    }

                    if (replaceField.isClicked()) {
                        currentTextInputField = replaceField;
                    }
                    if (withField.isClicked()) {
                        currentTextInputField = withField;
                    }

                    y += 40;
                }

                replace.addIcon.draw(vg, x, y, inputHandler);
                if (replace.addIcon.isClicked()) {
                    replace.replacements.put("", "");
                    addElements(replace, "", "", clubsPage);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //I know there is an error but I cant be bothered implementing a fix for it
        }

        nanoVGHelper.setAlpha(vg, 1f);
    }

    @Override
    public void keyTyped(char key, int keyCode) {
        if (!isEnabled()) return;
        if (currentTextInputField != null) {
            currentTextInputField.keyTyped(key, keyCode);
        }
        try {
            ReplaceStuff icon = replacements.stream().filter(i -> i.isWithField(currentTextInputField) != null).findFirst().orElse(null);
            if (icon != null) {
                System.out.println(icon.replacements + " " + icon.isWithField(currentTextInputField) + " " + currentTextInputField.getInput());
                List<String> keys = new ArrayList<>(icon.replacements.keySet());
                keys.sort(String::compareTo);
                if (icon.isWithField(currentTextInputField) == null) return;
                int index = icon.isWithField(currentTextInputField) ? icon.withField.indexOf(currentTextInputField) : icon.replaceField.indexOf(currentTextInputField);
                if (index == -1) return;
                System.out.println(index + " " + keys.get(index) + " " + currentTextInputField.getInput() + " " + icon.replacements.get(keys.get(index)));
                if (icon.isWithField(currentTextInputField)) {
                    icon.replacements.put(keys.get(index), currentTextInputField.getInput());
                } else {
                    if (icon.replacements.containsKey(currentTextInputField.getInput()) || currentTextInputField.getInput().isEmpty()) {
                        return;
                    }
                    icon.replacements.put(currentTextInputField.getInput(), icon.replacements.get(keys.get(index)));
                    icon.replacements.remove(keys.get(index));
                }
                System.out.println(icon.replacements);
            }
            set(replacements);
        } catch (IllegalAccessException ignored) {
        }
    }

    @Override
    public int getHeight() {
        int height = 0;
        for (ReplaceStuff replace : replacements) {
            if (clubsPage) {
                height += 36;
            }
            for (int i = 0; i < replace.replacements.size(); i++) {
                height += 40;
            }
            height += 40;
        }
        return height;
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
