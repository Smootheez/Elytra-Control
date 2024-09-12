package net.smootheez.elytracontrol.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.smootheez.elytracontrol.config.ElytraControlConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ElytraEntityModel.class)
public class ElytraEntityModelMixin<T extends LivingEntity> {

    @Shadow @Final private ModelPart rightWing;
    @Shadow @Final private ModelPart leftWing;

    @Unique private static final float MIN_VELOCITY = ElytraControlConfig.getInstance().getMinVelocity().getValue().floatValue();
    @Unique private static final float MAX_VELOCITY = ElytraControlConfig.getInstance().getMaxVelocity().getValue().floatValue();
    @Unique private static final float FLAP_SPEED = ElytraControlConfig.getInstance().getWingFlapSpeed().getValue().floatValue();
    @Unique private static final float FLAP_AMPLITUDE = ElytraControlConfig.getInstance().getFlapAmplitude().getValue().floatValue();
    @Unique private static final float SUBTLE_FLAP_FACTOR = ElytraControlConfig.getInstance().getSubtleFlapFactor().getValue().floatValue();

    @Unique
    private float calculateFlapAngle(float tickDelta, float velocity) {
        if (velocity < MIN_VELOCITY) {
            return MathHelper.sin(tickDelta * FLAP_SPEED) * FLAP_AMPLITUDE;
        } else if (velocity > MAX_VELOCITY) {
            return 0.0f;
        } else {
            float t = (velocity - MIN_VELOCITY) / (MAX_VELOCITY - MIN_VELOCITY);
            float smoothT = smoothstep(t);
            float flapAmplitude = FLAP_AMPLITUDE * (1.0f - smoothT);
            float subtleFlapAmplitude = FLAP_AMPLITUDE * SUBTLE_FLAP_FACTOR * (1.0f - t);

            return MathHelper.sin(tickDelta * FLAP_SPEED) * flapAmplitude +
                    MathHelper.sin(tickDelta * FLAP_SPEED * 2) * subtleFlapAmplitude;
        }
    }

    @Unique
    private float smoothstep(float x) {
        x = MathHelper.clamp(x, 0.0f, 1.0f);
        return x * x * (3 - 2 * x);
    }

    @Inject(method = "setAngles*", at = @At("HEAD"), cancellable = true)
    public void injectSetAngles(T livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        float pitch = 0.2617994f;
        float roll = -0.2617994f;
        float yaw = 0.0f;
        float pivotY = 0.0f;

        float flapAngle;
        if (livingEntity.isFallFlying() && ElytraControlConfig.getInstance().getFunOptions().getValue()) {
            flapAngle = calculateFlapAngle(ageInTicks, (float) livingEntity.getVelocity().length());
        } else {
            flapAngle = 0.0f;
        }

        if (livingEntity.isFallFlying()) {
            float fallFactor = calculateFallFactor(livingEntity.getVelocity());
            pitch = fallFactor * 0.34906584f + (1.0f - fallFactor) * pitch;
            roll = fallFactor * -1.5707964f + (1.0f - fallFactor) * roll;
        } else if (livingEntity.isInSneakingPose()) {
            pitch = 0.6981317f;
            roll = -0.7853982f;
            pivotY = 3.0f;
            yaw = 0.08726646f;
        }

        setWingAngles(livingEntity, pitch, roll, yaw, pivotY, flapAngle);

        ci.cancel();
    }

    @Unique
    private float calculateFallFactor(Vec3d velocity) {
        if (velocity.y >= 0.0) return 1.0f;
        Vec3d normalizedVelocity = velocity.normalize();
        return 1.0f - (float) Math.pow(-normalizedVelocity.y, 1.5);
    }

    @Unique
    private void setWingAngles(T livingEntity, float pitch, float roll, float yaw, float pivotY, float flapAngle) {
        leftWing.pivotY = pivotY;

        if (livingEntity instanceof AbstractClientPlayerEntity player) {
            updatePlayerElytraAngles(player, pitch, roll, yaw, flapAngle);
        } else {
            setWingAnglesDirect(pitch, roll, yaw, flapAngle);
        }

        rightWing.yaw = -leftWing.yaw;
        rightWing.pivotY = leftWing.pivotY;
        rightWing.pitch = leftWing.pitch;
        rightWing.roll = -leftWing.roll;
    }

    @Unique
    private void updatePlayerElytraAngles(AbstractClientPlayerEntity player, float pitch, float roll, float yaw, float flapAngle) {
        player.elytraPitch += (pitch - player.elytraPitch) * 0.1f;
        player.elytraYaw += (yaw - player.elytraYaw) * 0.1f;
        player.elytraRoll += (roll - player.elytraRoll) * 0.1f;

        leftWing.pitch = player.elytraPitch + flapAngle;
        leftWing.yaw = player.elytraYaw;
        leftWing.roll = player.elytraRoll;
    }

    @Unique
    private void setWingAnglesDirect(float pitch, float roll, float yaw, float flapAngle) {
        leftWing.pitch = pitch + flapAngle;
        leftWing.roll = roll;
        leftWing.yaw = yaw;
    }
}