package betternarratorerror.mixin;

import betternarratorerror.util.RuntimeProperty;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.text2speech.Narrator;
import com.mojang.text2speech.NarratorLinux;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Narrator.class)
public interface NarratorMixin {
	@Inject(at = @At("HEAD"), method = "getNarrator", cancellable = true, remap = false)
	private static void getNarrator(CallbackInfoReturnable<Narrator> cir) {

		if (!RuntimeProperty.getBoolean("minecraft.narrator", true)) {
			cir.setReturnValue(Narrator.EMPTY);
		}
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Throwable;)V"), method = "getNarrator", remap = false)
	private static void getNarrator(Logger logger, String message, Throwable throwable, Operation<Void> original) {
		if (RuntimeProperty.getBoolean("minecraft.narrator.error.hide_stacktrace", true)){
			throwable.setStackTrace(new StackTraceElement[]{});
		}
		logger.error(message, throwable);
	}
}