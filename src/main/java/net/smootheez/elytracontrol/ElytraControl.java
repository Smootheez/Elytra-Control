package net.smootheez.elytracontrol;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.text.Text;


@Environment(EnvType.CLIENT)
public class ElytraControl implements ClientModInitializer {
    public static boolean elytraToggle = true;
    protected static final KeyBinding elytraToggleKey = new KeyBinding(
            "key." + Constants.MOD_ID + ".elytra_toggle",
            InputUtil.GLFW_KEY_V,
            KeyBinding.GAMEPLAY_CATEGORY
    );
    public static String playerUUID;
    private static final Text ELYTRA_ENABLED_TEXT = Text.translatable("message." + Constants.MOD_ID + ".elytra_enabled");
    private static final Text ELYTRA_DISABLED_TEXT = Text.translatable("message." + Constants.MOD_ID + ".elytra_disabled");
    @Override
    public void onInitializeClient() {
        Constants.LOGGER.info("Elytra Control Initialized");
        endClientTickEvent();
    }

    private void endClientTickEvent() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) {
                return;
            }

            if (playerUUID == null) {
                playerUUID = client.player.getUuidAsString();
            }

            while (elytraToggleKey.wasPressed()) {
                elytraToggle = !elytraToggle;
                client.inGameHud.setOverlayMessage(elytraToggle ? ELYTRA_ENABLED_TEXT : ELYTRA_DISABLED_TEXT, false);
            }

            while (shouldStopFlying(client)) {
                client.player.stopFallFlying();
            }

        });
    }
    protected boolean shouldStopFlying(MinecraftClient client) {
        if (client.player == null) {
            return false;
        }

        return client.options.jumpKey.wasPressed() && !client.player.isOnGround() && !client.player.isTouchingWater() && !client.player.hasStatusEffect(StatusEffects.LEVITATION) && client.player.isFallFlying();
    }
}
