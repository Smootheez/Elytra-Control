package dev.smootheez.elytracontrol.mixin;

import net.minecraft.entity.player.PlayerEntity;
import dev.smootheez.elytracontrol.handler.EndClientTickHandler;
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

        if (doesCollideY(offset) && doesCollideY(-offset) || player.getUuidAsString().equals(EndClientTickHandler.playerUUID) && !EndClientTickHandler.elytraToggle) {
            cir.setReturnValue(false);
        }
    }

    @Unique
    private boolean doesCollideY(double offsetY) {
        return !((PlayerEntity) (Object) this).doesNotCollide(0, offsetY, 0);
    }
}
