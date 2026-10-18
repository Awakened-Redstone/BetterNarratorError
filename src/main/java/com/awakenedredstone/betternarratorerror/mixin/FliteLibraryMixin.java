package com.awakenedredstone.betternarratorerror.mixin;

import com.awakenedredstone.betternarratorerror.ExceptionFixer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(targets = {"com.mojang.text2speech.NarratorLinux$FliteLibrary", "com.mojang.text2speech.NarratorLinux$FliteLibrary$CmuUsKal16"})
public class FliteLibraryMixin {
    @ModifyArg(at = @At(value = "INVOKE", target = "Lcom/mojang/text2speech/Narrator$InitializeException;<init>(Ljava/lang/String;Ljava/lang/Throwable;)V"), index = 1, method = "loadNative", remap = false)
    private static Throwable pleaseDontGenerateAHugeStackTraceThanks(Throwable cause) {
        return ExceptionFixer.fixException(cause);
    }
}
