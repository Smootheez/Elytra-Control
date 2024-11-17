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
    private static final ElytraControlConfig config = ElytraControlConfig.getInstance();

    private static final float UP_PITCH = -40.0F;
    private static final float DOWN_PITCH = 38.0F;
    private static final double MIN_VELOCITY = 0.3;
    private static final double MAX_VELOCITY = 1.8;

    private static boolean isAdjustingPitch = false;
    private static float targetPitch = 0.0F;

    public static void autoFlight(MinecraftClient client) {
        PlayerEntity player = client.player;
        if (player == null) return;

        handleAutoFlightToggle();

        if (isAdjustingPitch && player.isGliding()) processAutoFlight(player);
    }

    private static void handleAutoFlightToggle() {
        if (KeyBinds.autoFlightKey.wasPressed()) {
            isAdjustingPitch = !isAdjustingPitch;
            if (isAdjustingPitch) targetPitch = DOWN_PITCH;
        }
    }

    private static void processAutoFlight(PlayerEntity player) {
        double speed = calculateSpeed(player);
        float currentPitch = player.getPitch();

        updateTargetPitch(speed);
        adjustPlayerPitch(player, currentPitch);
        sendDebugMessage(player, speed);
    }

    private static double calculateSpeed(PlayerEntity player) {
        Vec3d velocity = player.getVelocity();
        return Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
    }

    private static void updateTargetPitch(double speed) {
        if (speed <= MIN_VELOCITY) targetPitch = DOWN_PITCH;
        else if (speed >= MAX_VELOCITY) targetPitch = UP_PITCH;
    }

    private static void adjustPlayerPitch(PlayerEntity player, float currentPitch) {
        double pitchAdjustmentSpeedUp = config.getPitchUpSpeed().getValue();
        double pitchAdjustmentSpeedDown = config.getPitchDownSpeed().getValue();
        float pitchDiff = targetPitch - currentPitch;

        if (Math.abs(pitchDiff) > 0.01F) {
            float adjustment;
            if (pitchDiff > 0) adjustment = (float) pitchAdjustmentSpeedDown;
            else adjustment = (float) pitchAdjustmentSpeedUp;

            float newPitch = currentPitch + Math.signum(pitchDiff) * adjustment;

            if (targetPitch > currentPitch) newPitch = Math.min(newPitch, targetPitch);
            else newPitch = Math.max(newPitch, targetPitch);

            player.setPitch(newPitch);
        }
    }

    private static void sendDebugMessage(PlayerEntity player, double speed) {
        player.sendMessage(Text.of(String.format(
                "Speed: %.2f | Pitch: %.2f | Target: %.2f | Height: %.2f",
                speed, player.getPitch(), targetPitch, player.getY()
        )), false);
    }
}