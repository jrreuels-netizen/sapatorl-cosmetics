package de.sapatorl.cosmetics.mixin;

import de.sapatorl.cosmetics.Cosmetics;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Fallback: direkt nach der Kopf-Item-Berechnung von Vanilla (an jedem RETURN). */
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {

    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V",
            at = @At("RETURN"),
            require = 0)
    private void sapatorl$applyCosmetic(LivingEntity entity, LivingEntityRenderState state, float partialTick, CallbackInfo ci) {
        Cosmetics.applyTo(entity, state);
    }
}
