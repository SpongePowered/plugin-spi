package org.spongepowered.test.mixin;

import org.lwjgl.opengl.GL;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GL.class)
public abstract class GLMixin {

    @Redirect(method = "createCapabilities(Z)Lorg/lwjgl/opengl/GLCapabilities;", at = @At(value = "INVOKE", target = "Lorg/lwjgl/system/MemoryUtil;memUTF8Safe(J)Ljava/lang/String;"))
    private static String lwjgl$setVersion(long call) {
        return "4.1";
    }

    @Redirect(method = "createCapabilities(Z)Lorg/lwjgl/opengl/GLCapabilities;", at = @At(value = "INVOKE", target = "Lorg/lwjgl/system/JNI;callI(J)I"))
    private static int lwjgl$nullError(long __functionAddress) {
        return 0;
    }
}
