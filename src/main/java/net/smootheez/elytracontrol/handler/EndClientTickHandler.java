package net.smootheez.elytracontrol.handler;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.smootheez.elytracontrol.Constants;
import net.smootheez.elytracontrol.ElytraControl;
import net.smootheez.elytracontrol.config.ElytraControlConfig;

public class EndClientTickHandler implements ClientTickEvents.EndTick {
    public static boolean elytraToggle = true;
    public static String playerUUID;
    private int elytraTime = 0;
    @Override
    public void onEndTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }
        KeyBinding keyJump = client.options.jumpKey;
        boolean fallFlyingEntity = client.player.isFallFlying();

        if (playerUUID == null) {
            playerUUID = client.player.getUuidAsString();
        }

        toggleElytraIfKeyPressed(client);

        if (keyJump.isPressed() && fallFlyingEntity && elytraTime > 10 && ElytraControlConfig.INSTANCE.elytraCancel.get()){
            stopFlying(client);
        }

        updateElytraFlyingTime(client);

    }

    private void toggleElytraIfKeyPressed(MinecraftClient client){
        while (ElytraControl.elytraToggleKey.wasPressed() && ElytraControlConfig.INSTANCE.elytraLock.get()) {
            elytraToggle = !elytraToggle;
            client.player.sendMessage(ScreenTexts.composeToggleText(Text.translatable("message." + Constants.MOD_ID + ".toggle"), elytraToggle), true);
        }
    }

    private void stopFlying(MinecraftClient client) {
        client.player.stopFallFlying();
        client.player.networkHandler.sendPacket(new ClientCommandC2SPacket(client.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
    }

    private void updateElytraFlyingTime(MinecraftClient client){
        if (client.player.isFallFlying() && !client.options.jumpKey.isPressed()) {
            elytraTime++;
            if (elytraTime == 20){
                elytraTime = 10;
            }
        } else {
            elytraTime = 0;
        }
    }
}
