package de.sapatorl.cosmetics.mixin;

import de.sapatorl.cosmetics.Cosmetics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Setzt für den eigenen Spieler das gewählte Cosmetic als "Kopf-Item" in den Render-State,
 * sodass Vanilla es genau wie einen Kürbis auf dem Kopf rendert. Nur clientseitig.
 */
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {

    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V",
            at = @At("TAIL"))
    private void sapatorl$applyCosmetic(LivingEntity entity, LivingEntityRenderState state, float partialTick, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (entity != mc.player) return;
        ItemStack cosmetic = Cosmetics.stack();
        if (cosmetic.isEmpty()) return;
        // Echten Helm/Kopf-Item nicht überdecken
        if (!entity.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) return;
        mc.getItemModelResolver().updateForLiving(state.headItem, cosmetic, ItemDisplayContext.HEAD, entity);
    }
}
