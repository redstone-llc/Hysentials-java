/*
 * This file is part of OneConfig.
 * OneConfig - Next Generation Config Library for Minecraft: Java Edition
 * Copyright (C) 2021~2024 Polyfrost.
 *   <https://polyfrost.org> <https://github.com/Polyfrost/>
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
 * <https://polyfrost.org/legal/oneconfig/additional-terms>
 */

package llc.redstone.hysentials.guis.polyui;

import cc.polyfrost.oneconfig.gui.elements.config.ConfigKeyBind;
import cc.polyfrost.oneconfig.libs.universal.*;
import llc.redstone.hysentials.polyui.DrawableDSL;
import llc.redstone.hysentials.polyui.RendererImpl;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.*;
import org.polyfrost.polyui.PolyUI;
import org.polyfrost.polyui.color.Colors;
import org.polyfrost.polyui.color.DarkTheme;
import org.polyfrost.polyui.color.PolyColor;
import org.polyfrost.polyui.component.Drawable;
import org.polyfrost.polyui.event.InputManager;
import org.polyfrost.polyui.input.Translator;
import org.polyfrost.polyui.property.Settings;
import org.polyfrost.polyui.renderer.data.Cursor;
import org.polyfrost.polyui.unit.Align;
import org.polyfrost.polyui.unit.Vec2;

import java.util.function.Consumer;

import static org.lwjgl.opengl.GL11.glViewport;


@SuppressWarnings("unused")
public class PolyUIScreen extends UScreen implements UIPause, BlurScreen {
    @Nullable
    public final PolyUI polyUI;

    @NotNull
    public final InputManager inputManager;

    @Nullable
    public final Vec2 desiredResolution;

    public boolean pauses, blurs;

    private final MCWindow window;

    private Runnable close;

    //#if MC<=11300
    private float mx, my;
    //#endif

     @Contract("_, null, _, _, _, _, _, _, null -> fail")
    public PolyUIScreen(@Nullable Settings settings,
                        @Nullable InputManager inputManager,
                        @Nullable Translator translator,
                        @Nullable Align alignment,
                        @Nullable Colors colors,
                        @Nullable PolyColor backgroundColor,
                        @Nullable Vec2 desiredResolution,
                        @Nullable Vec2 size,
                        Drawable... drawables) {
        super(true);

        Settings s = settings == null ? new Settings() : settings;
        s.enableInitCleanup(false);
        s.enableForceSettingInitialSize(true);
        if (drawables == null || drawables.length == 0) {
            if (inputManager == null) throw new IllegalArgumentException("Must be created with an inputManager or drawables");
            this.inputManager = inputManager;
            this.polyUI = null;
            this.desiredResolution = null;
            this.window = null;
        } else {
            Colors c = colors == null ? new DarkTheme() : colors;
            Align a = alignment == null ? new Align(Align.Main.Start, Align.Cross.Start, Align.Mode.Horizontal, Vec2.ZERO, 50) : alignment;
            this.polyUI = new PolyUI(drawables, LwjglManagerImpl.INSTANCE.getRenderer(), s, inputManager, translator, backgroundColor, a, c, size);
            this.window = new MCWindow(UMinecraft.getMinecraft());
            this.window.setPixelRatio(scale());
            this.polyUI.setWindow(window);
            this.inputManager = this.polyUI.getInputManager();
            this.desiredResolution = desiredResolution;
            adjustResolution(width(), height(), true);
        }
    }

    public PolyUIScreen(Drawable... drawables) {
        this(null, null, null, null, null, null, null, null, drawables);
    }

    public PolyUIScreen(@Nullable Align alignment, Vec2 size, Drawable... drawables) {
        this(null, null, null, alignment, null, null, null, size, drawables);
    }

    public PolyUIScreen(@NotNull InputManager inputManager) {
        this(null, inputManager, null, null, null, null, null, null);
    }

    @ApiStatus.Internal
    public PolyUIScreen(@NotNull PolyUI polyUI) {
        super(true);
        this.polyUI = polyUI;
        this.inputManager = polyUI.getInputManager();
        desiredResolution = null;
        window = new MCWindow(UMinecraft.getMinecraft());
        window.setPixelRatio(scale());
        polyUI.setWindow(window);
    }

    protected final void adjustResolution(float w, float h, boolean force) {
        // asm: normally, a polyui instance is as big as its window and that is it.
        // however, inside minecraft, the actual content is smaller than the window size, so resizing it directly would just fuck it up.
        // so instead, the developer specifies a resolution that their UI was designed for, and we resize accordingly.
        if (polyUI == null || desiredResolution == null) return;
        float sx = w / desiredResolution.getX();
        float sy = h / desiredResolution.getY();
        if (sx == 1f && sy == 1f) return;
        Vec2 size = polyUI.getMaster().getSize();
        polyUI.resize(size.getX() * sx, size.getY() * sy, force);
    }


    public boolean useMinecraftUIScaling() {
        return false;
    }

    public final PolyUIScreen closeCallback(Runnable r) {
         close = r;
         return this;
    }


    @Override
    public void onDrawScreen(@NotNull UMatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (polyUI == null) return;
        UGraphics.GL.pushMatrix();
        Vec2 size = polyUI.getMaster().getSize();
        float scale = scale();
        float ox = (width() / 2f - size.getX() / 2f) * scale;
        float oy = (height() / 2f - size.getY() / 2f) * scale;
        glViewport((int) ox, (int) oy, (int) (size.getX() * scale), (int) (size.getY() * scale));

        //#if MC<11300
        if (mouseX != mx || mouseY != my) {
            mx = mouseX;
            my = mouseY;
            this.mouseMoved(mx, my);
        }
        //#endif

        matrices.runReplacingGlobalState(polyUI::render);

        glViewport(0, 0, UResolution.getViewportWidth(), UResolution.getViewportHeight());
        UGraphics.GL.popMatrix();
    }

    @Override
    @MustBeInvokedByOverriders
    public final void onResize(Minecraft client, int width, int height) {
        if (polyUI == null) return;
        float w = (float) UResolution.getViewportWidth();
        float h = (float) UResolution.getViewportHeight();
        adjustResolution(w, h, false);
    }


    @Override
    public void onKeyPressed(int keyCode, char scanCode, @Nullable UKeyboard.Modifiers modifiers) {
        if (keyCode == UKeyboard.KEY_ESCAPE && shouldCloseOnEsc()) {
            UScreen.displayScreen(null);
        }
        inputManager.keyDown(keyCode);
    }

    @Override
    public void onKeyReleased(int keyCode, char typedChar, @Nullable UKeyboard.Modifiers modifiers) {
        inputManager.keyUp(keyCode);
    }

    @Override
    @MustBeInvokedByOverriders
    public void onMouseClicked(double mouseX, double mouseY, int mouseButton) {
        inputManager.mousePressed(mouseButton);
    }

    @Override
    @MustBeInvokedByOverriders
    public void onMouseReleased(double mouseX, double mouseY, int mouseButton) {
        inputManager.mouseReleased(mouseButton);
    }

    @Override
    @MustBeInvokedByOverriders
    public void onMouseScrolled(double delta) {
        inputManager.mouseScrolled(0f, (float) delta);
    }

    //#if MC>=11300
    //$$ @Override
    //#endif
    public boolean shouldCloseOnEsc() {
        return true;
    }

    //#if MC<=11300
    @Override
    //#endif
    public boolean doesGuiPauseGame() {
        return doesUIPauseGame();
    }

    @Override
    public boolean doesUIPauseGame() {
        return pauses;
    }

    @Override
    public boolean hasBackgroundBlur() {
        return blurs;
    }


    //#if MC>=11300
    //$$ @Override
    //#endif
    @MustBeInvokedByOverriders
    public void mouseMoved(double mouseX, double mouseY) {
        if (polyUI == null) return;
        Vec2 size = polyUI.getMaster().getSize();
        float ox = (float) UResolution.getWindowWidth() / 2f - size.getX() / 2f;
        float oy = (float) UResolution.getWindowHeight() / 2f - size.getY() / 2f;
        inputManager.mouseMoved((float) UMouse.Raw.getX() - ox, (float) UMouse.Raw.getY() - oy);
    }

    @Override
    @MustBeInvokedByOverriders
    public void onScreenClose() {
        if (close != null) close.run();
        if (polyUI == null) return;
        // noinspection DataFlowIssue
        this.polyUI.getWindow().setCursor(Cursor.Pointer);
    }

    public final Drawable getMaster() {
        if (polyUI == null) throw new IllegalArgumentException("no drawables attached this way");
        return polyUI.getMaster();
    }

    public final float width() {
        return useMinecraftUIScaling() ? UResolution.getScaledWidth() : UResolution.getWindowWidth();
    }

    public final float height() {
        return useMinecraftUIScaling() ? UResolution.getScaledHeight() : UResolution.getWindowHeight();
    }

    public final float scale() {
        return (float) UResolution.getViewportWidth() / UResolution.getWindowWidth();
    }
}
