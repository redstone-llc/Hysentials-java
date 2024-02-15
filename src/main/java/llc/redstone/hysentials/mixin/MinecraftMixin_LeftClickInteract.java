/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020, 2021, 2022  Polyfrost, Sk1er LLC and contributors
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
 */package llc.redstone.hysentials.mixin;

import cc.polyfrost.oneconfig.libs.checker.units.qual.A;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import llc.redstone.hysentials.GuiIngameHysentials;
import llc.redstone.hysentials.config.HysentialsConfig;
import llc.redstone.hysentials.macrowheel.overlay.MacroWheelOverlay;
import llc.redstone.hysentials.macrowheel.overlay.MacroWheelOverlayKt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin_LeftClickInteract {
    @Shadow
    public WorldClient theWorld;

    @Shadow
    public MovingObjectPosition objectMouseOver;

    @Shadow
    public EntityPlayerSP thePlayer;

    @Redirect(method = "clickMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;clickBlock(Lnet/minecraft/util/BlockPos;Lnet/minecraft/util/EnumFacing;)Z"))
    private boolean captureClickBlock(PlayerControllerMP instance, BlockPos itemstack, EnumFacing block1) {
        ForgeEventFactory.onPlayerInteract(thePlayer, PlayerInteractEvent.Action.LEFT_CLICK_BLOCK, theWorld, itemstack, objectMouseOver.sideHit, objectMouseOver.hitVec);
        return instance.clickBlock(itemstack, block1);
    }

    @Inject(method = "clickMouse", at = @At("HEAD"), cancellable = true)
    private void clickMouse(CallbackInfo ci) {
        if (HysentialsConfig.macroWheelKeyBind.isActive() && !MacroWheelOverlayKt.getStopped() && GuiIngameHysentials.cooldown < System.currentTimeMillis()) {
            ci.cancel();
        }
    }

    @Inject(method = "rightClickMouse", at = @At("HEAD"), cancellable = true)
    private void rightClickMouse(CallbackInfo ci) {
        if (HysentialsConfig.macroWheelKeyBind.isActive() && !MacroWheelOverlayKt.getStopped() && GuiIngameHysentials.cooldown < System.currentTimeMillis()) {
            ci.cancel();
        }
    }



//    @Inject(method = "loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;)V", at = @At("HEAD"))
//    private void loadWorld(WorldClient worldClient, CallbackInfo ci) {
//        EventBus.INSTANCE.post(new WorldChangeEvent());
//    }
}
