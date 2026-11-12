package de.sapatorl.cosmetics.mixin;

import de.sapatorl.cosmetics.Cosmetics;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Spieler werden in 26.3 über AvatarRenderer gezeichnet – hier ganz am Ende das Cosmetic setzen. */
@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin {

    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
            at = @At("TAIL"))
    private void sapatorl$applyCosmetic(Avatar entity, AvatarRenderState state, float partialTick, CallbackInfo ci) {
        Cosmetics.applyTo(entity, state);
    }
}
