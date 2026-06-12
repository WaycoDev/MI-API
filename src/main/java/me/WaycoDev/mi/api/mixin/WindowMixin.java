package me.WaycoDev.mi.api.mixin;

import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Window.class)
public class WindowMixin {

    @Inject(method = "getScaledWidth", at = @At("RETURN"), cancellable = true)
    private void forceScaledWidth(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(((Window)(Object)this).getFramebufferWidth());
    }

    @Inject(method = "getScaledHeight", at = @At("RETURN"), cancellable = true)
    private void forceScaledHeight(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(((Window)(Object)this).getFramebufferHeight());
    }
}