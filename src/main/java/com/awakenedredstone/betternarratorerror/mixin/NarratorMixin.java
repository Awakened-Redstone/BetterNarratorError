package com.awakenedredstone.betternarratorerror.mixin;

import com.awakenedredstone.betternarratorerror.ExceptionFixer;
import com.awakenedredstone.betternarratorerror.config.NarratorConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.text2speech.Narrator;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Narrator.class)
public interface NarratorMixin {

    @Inject(at = @At("HEAD"), method = "getNarrator", cancellable = true, remap = false)
	private static void getNarrator(CallbackInfoReturnable<Narrator> cir) {
		if (NarratorConfig.INSTANCE.disableNarrator) {
            // Log a warning so players know why it isn't working
            Narrator.LOGGER.warn("Narrator disabled via Better Narrator Error");
            cir.setReturnValue(Narrator.EMPTY);
        }
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Throwable;)V"), method = "getNarrator", remap = false)
	private static void getNarrator(Logger logger, String message, Throwable throwable, Operation<Void> original) {
        ExceptionFixer.hideStacktraces(throwable);
        original.call(logger, message, throwable);
    }
}
