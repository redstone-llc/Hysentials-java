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

package cc.woverflow.hysentials.mixin;

import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.IntBuffer;

@Mixin(TextureUtil.class)
public class TextureUtilMixin {

    @Shadow
    static void bindTexture(int p_94277_0_) {
    }

    @Shadow
    public static void deleteTexture(int textureId) {
    }

    @Inject(method = "allocateTextureImpl", at = @At("HEAD"), cancellable = true)
    private static void injectTextureImpl(int glTextureId, int mipmapLevels, int width, int height, CallbackInfo ci) {
        deleteTexture(glTextureId);
        bindTexture(glTextureId);
        if (mipmapLevels >= 0) {
            GL11.glTexParameteri(3553, 33085, mipmapLevels);
            GL11.glTexParameteri(3553, 33082, 0);
            GL11.glTexParameteri(3553, 33083, mipmapLevels);
            GL11.glTexParameterf(3553, 34049, 0.0F);
        }

        for (int i = 0; i <= mipmapLevels; ++i) {
            GL11.glTexImage2D(3553, i, 6408, width >> i, height >> i, 0, 32993, 33639, (IntBuffer) null);
        }

        ci.cancel();
    }
}
