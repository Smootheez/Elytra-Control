package net.smootheez.elytracontrol.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.smootheez.elytracontrol.ElytraControl;
import net.smootheez.elytracontrol.config.ElytraControlConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "checkFallFlying", at = @At("HEAD"), cancellable = true)
    private void injectCheckFallFlying(CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.isSneaking() || player.isFallFlying()){
            return;
        }
        double offset = 4.0 / 6.0;
        if (player.isSprinting()) {
            offset = 12.0 / 16.0;
        }

        if (doesCollideY(offset) && doesCollideY(-offset) || player.getUuidAsString().equals(ElytraControl.playerUUID) && !ElytraControl.elytraToggle) {
            cir.setReturnValue(false);
        }
    }

    @Unique
    private boolean doesCollideY(double offsetY) {
        return !((PlayerEntity) (Object) this).doesNotCollide(0, offsetY, 0);
    }
}
