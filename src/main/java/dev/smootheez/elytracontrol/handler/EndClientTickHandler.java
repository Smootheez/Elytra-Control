package dev.smootheez.elytracontrol.handler;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import dev.smootheez.elytracontrol.Constants;
import dev.smootheez.elytracontrol.ElytraControl;
import dev.smootheez.elytracontrol.config.ElytraControlConfig;

import java.util.Random;

public class EndClientTickHandler implements ClientTickEvents.EndTick {
    public static boolean elytraToggle = true;
    public static String playerUUID;
    private int elytraTime = 0;

    @Override
    public void onEndTick(MinecraftClient client) {
        Random random = new Random();

        if (client.player == null) return;

        int randomNumber = random.nextInt(3) + 1;
        KeyBinding keyJump = client.options.jumpKey;
        boolean fallFlyingEntity = client.player.isFallFlying();

        if (playerUUID == null) {
            playerUUID = client.player.getUuidAsString();
        }

        toggleElytraIfKeyPressed(client);

        if (keyJump.isPressed() && fallFlyingEntity && elytraTime > randomNumber && ElytraControlConfig.getInstance().getElytraCancel().getValue()) {
            stopFlying(client);
        }

        updateElytraFlyingTime(client);

    }

    private void toggleElytraIfKeyPressed(MinecraftClient client) {
        while (ElytraControl.elytraToggleKey.wasPressed() && ElytraControlConfig.getInstance().getElytraLock().getValue()) {
            elytraToggle = !elytraToggle;
            client.player.sendMessage(ScreenTexts.composeToggleText(Text.translatable("message." + Constants.MOD_ID + ".toggle"), elytraToggle), true);
        }
    }

    private void stopFlying(MinecraftClient client) {
        client.player.stopFallFlying();
        client.player.networkHandler.sendPacket(new ClientCommandC2SPacket(client.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
    }

    private void updateElytraFlyingTime(MinecraftClient client) {
        if (client.player.isFallFlying() && !client.options.jumpKey.isPressed()) {
            elytraTime = (elytraTime + 1) % 1000;
        } else {
            elytraTime = 0;
        }
    }
}
