package net.smootheez.elytracontrol;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.text.Text;


@Environment(EnvType.CLIENT)
public class ElytraControl implements ClientModInitializer {
    public static boolean elytraToggle = true;
    protected static final KeyBinding elytraToggleKey = new KeyBinding(
            "key." + Constants.MOD_ID + ".elytra_toggle",
            InputUtil.GLFW_KEY_V,
            "key.category.elytracontrol"
    );

    public static String playerUUID;

    private int elytraTime = 0;

    private static final Text ELYTRA_ENABLED_TEXT = Text.translatable("message." + Constants.MOD_ID + ".elytra_enabled");
    private static final Text ELYTRA_DISABLED_TEXT = Text.translatable("message." + Constants.MOD_ID + ".elytra_disabled");
    @Override
    public void onInitializeClient() {
        Constants.LOGGER.info("Elytra Control Initialized");

        KeyBindingHelper.registerKeyBinding(elytraToggleKey);

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

            if (client.options.jumpKey.isPressed() && client.player.isFallFlying() && elytraTime > 10) {
                client.player.stopFallFlying();
                client.player.networkHandler.sendPacket(new ClientCommandC2SPacket(client.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
            }
            if (client.player.isFallFlying() && !client.options.jumpKey.isPressed()) {
                elytraTime++;
                if (elytraTime == 20){
                    elytraTime = 10;
                }
            } else {
                elytraTime = 0;
            }

        });
    }
}
