package dev.smootheez.elytracontrol.handler;

import dev.smootheez.elytracontrol.config.ElytraControlConfig;
import dev.smootheez.elytracontrol.events.EndTickEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;

public class EasyFlightHandler {

    private static final int FIRST_JUMP_DURATION = 1;
    private static final int SECOND_JUMP_DELAY = 1;
    private static final int SECOND_JUMP_DURATION = 1;
    private static final int USE_KEY_DELAY = 1;
    private static final int USE_KEY_DURATION = 1;

    private static boolean isDoubleJumping = false;
    private static int tickCounter = 0;
    private static boolean wasUseKeyPressed = false;

    public static void easyFlight(MinecraftClient client) {
        if (client.player == null) return;

        boolean isUseKeyPressed = client.options.useKey.isPressed();

        if (!isDoubleJumping) {
            if (isUseKeyPressed && !wasUseKeyPressed && shouldInitiateEasyFlight(client)) {
                startDoubleJump(client);
            }
        } else {
            executeDoubleJumpSequence(client);
        }

        wasUseKeyPressed = isUseKeyPressed;
    }

    private static boolean shouldInitiateEasyFlight(MinecraftClient client) {
        if (client.player == null || client.interactionManager == null) return false;

        boolean hasElytra = client.player.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof ElytraItem;
        return EndTickEvent.elytraToggle && ElytraControlConfig.getInstance().getEasyFlight().getValue()
                && !client.player.isFallFlying() && client.player.isOnGround()
                && isValidFireworkHolding(client.player) && hasElytra
                && crosshairCheck(client) && !client.interactionManager.getCurrentGameMode().isCreative();
    }

    private static void startDoubleJump(MinecraftClient client) {
        isDoubleJumping = true;
        tickCounter = 0;
        client.options.useKey.setPressed(false);
    }

    private static void executeDoubleJumpSequence(MinecraftClient client) {
        tickCounter++;
        int totalDuration = FIRST_JUMP_DURATION + SECOND_JUMP_DELAY + SECOND_JUMP_DURATION + USE_KEY_DELAY + USE_KEY_DURATION;

        if (tickCounter <= FIRST_JUMP_DURATION ||
                (tickCounter == FIRST_JUMP_DURATION + SECOND_JUMP_DELAY + SECOND_JUMP_DURATION)) {
            client.options.jumpKey.setPressed(true);
        } else if (tickCounter == totalDuration - USE_KEY_DURATION) {
            client.options.useKey.setPressed(true);
        } else if (tickCounter > totalDuration) {
            resetDoubleJump(client);
        } else {
            client.options.jumpKey.setPressed(false);
            client.options.useKey.setPressed(false);
        }
    }

    private static void resetDoubleJump(MinecraftClient client) {
        isDoubleJumping = false;
        client.options.jumpKey.setPressed(false);
        client.options.useKey.setPressed(false);
    }

    private static boolean isValidFireworkHolding(ClientPlayerEntity player) {
        ItemStack mainHand = player.getMainHandStack();
        ItemStack offHand = player.getOffHandStack();

        return mainHand.getItem() instanceof FireworkRocketItem || offHand.getItem() instanceof FireworkRocketItem;
    }

    private static boolean crosshairCheck(MinecraftClient client) {
        HitResult hitResult = client.crosshairTarget;

        if (client.player == null) return false;

        ItemStack itemStack = client.player.getStackInHand(client.player.getActiveHand());

        TypedActionResult<ItemStack> typedActionResult = itemStack.use(client.world, client.player, client.player.getActiveHand());

        boolean isUsingItem = typedActionResult.getResult().isAccepted();

        boolean isSwingHand = client.player.handSwinging;

        return hitResult == null || hitResult.getType() == HitResult.Type.MISS && !isUsingItem && !isSwingHand;
    }
}