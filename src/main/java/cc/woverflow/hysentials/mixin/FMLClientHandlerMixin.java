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

import cc.woverflow.hysentials.util.SplashProgress;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.Semaphore;

//@Mixin(value=FMLClientHandler.class)
//public class FMLClientHandlerMixin {
//    @Redirect(method = "beginMinecraftLoading", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/client/SplashProgress;start()V"))
//    private void rdStart() {
//        SplashProgress.setProgress(1, "Starting Game...");
//    }
//
//
//
//    @Redirect(method = "haltGame", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/client/SplashProgress;finish()V"))
//    private void rdFinish() {
//    }
//
//    @Redirect(method = "finishMinecraftLoading", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/client/SplashProgress;finish()V"))
//    private void rdFinish2() {
//    }
//
//    @Redirect(method = "processWindowMessages", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/Semaphore;tryAcquire()Z"))
//    private boolean rdTryAcquire(Semaphore instance) {
//        return true;
//    }
//
//    @Redirect(method = "processWindowMessages", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/Semaphore;release()V"))
//    private void rdRelease(Semaphore instance) {
//    }
//}
