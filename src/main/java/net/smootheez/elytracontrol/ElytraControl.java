package net.smootheez.elytracontrol;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffects;

@Environment(EnvType.CLIENT)
public class ElytraControl implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Constant.LOGGER.info("Elytra Control Initialized");
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) {
                return;
            }

            while (checkStopFlying(client)) {
                client.player.stopFallFlying();
            }
        });
    }

    protected boolean checkStopFlying(MinecraftClient client) {
        if (client.player == null) {
            return false;
        }
        return client.options.jumpKey.wasPressed() && !client.player.isOnGround() && !client.player.isTouchingWater() && !client.player.hasStatusEffect(StatusEffects.LEVITATION);
    }
}
