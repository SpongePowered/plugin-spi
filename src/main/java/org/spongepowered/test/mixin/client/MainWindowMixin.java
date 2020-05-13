package org.spongepowered.test.mixin.client;

import net.minecraft.client.MainWindow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MainWindow.class)
public abstract class MainWindowMixin {

    @Inject(method = "throwExceptionForGlError", at = @At("HEAD"), cancellable = true)
    private static void vanilla$throwExceptionForGlError(int error, long description, CallbackInfo callbackInfo) {
        if (error == 65542) {
            callbackInfo.cancel();
        }
    }
}