package dev.smootheez.elytracontrol.handler;

import dev.smootheez.elytracontrol.config.ElytraControlConfig;
import dev.smootheez.elytracontrol.registry.KeyBinds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class AutoFlightHandler {
    private static final float UP_PITCH = -40.0F;
    private static final float DOWN_PITCH = 38.0F;
    private static final double MIN_VELOCITY = 0.3589;  // Adjusted from 0.3588
    private static final double MAX_VELOCITY = 1.8941;  // Adjusted from 1.8941
    private static final double PITCH_ADJUSTMENT_SPEED = ElytraControlConfig.getInstance().getAdjustPitch().getValue(); // Default value 2.0F

    private static boolean isAdjustingPitch = false;
    private static float targetPitch = 0.0F;

    public static void autoFlight(MinecraftClient client) {
        PlayerEntity player = client.player;
        if (player == null) return;

        if (KeyBinds.autoFlightKey.wasPressed()) isAdjustingPitch = !isAdjustingPitch;

        if (isAdjustingPitch && player.isGliding()) {
            Vec3d velocity = player.getVelocity();
            double speed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
            float currentPitch = player.getPitch();

            if (speed <= MIN_VELOCITY) {
                targetPitch = DOWN_PITCH;
            } else if (speed >= MAX_VELOCITY) {
                targetPitch = UP_PITCH;
            }

            float pitchDiff = targetPitch - currentPitch;

            if (Math.abs(pitchDiff) > 0.01F) {
                float adjustment = Math.signum(pitchDiff) * (float) PITCH_ADJUSTMENT_SPEED;
                float newPitch = currentPitch + adjustment;

                if (targetPitch > currentPitch) {
                    newPitch = Math.min(newPitch, targetPitch);
                } else {
                    newPitch = Math.max(newPitch, targetPitch);
                }

                player.setPitch(newPitch);
            }

            player.sendMessage(Text.of(String.format("Speed: %.2f | Pitch: %.2f | Target: %.2f | Height: %.2f",
                    speed, player.getPitch(), targetPitch, player.getY())), false);
        }
    }
}