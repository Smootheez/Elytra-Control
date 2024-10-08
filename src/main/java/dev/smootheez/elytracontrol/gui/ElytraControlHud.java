package dev.smootheez.elytracontrol.gui;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import dev.smootheez.elytracontrol.Constants;
import dev.smootheez.elytracontrol.config.ElytraControlConfig;
import dev.smootheez.elytracontrol.handler.EndClientTickHandler;

public class ElytraControlHud implements HudRenderCallback {
    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        Identifier elytraLockTexture = new Identifier(Constants.MOD_ID, "textures/gui/elytra_lock.png");
        Text elytraLockNotifier = Text.translatable("notifier." + Constants.MOD_ID + ".toggle");

        if (!EndClientTickHandler.elytraToggle && ElytraControlConfig.getInstance().getShowLockIcon().getValue()) {
            drawContext.drawTexture(elytraLockTexture,
                    client.getWindow().getScaledWidth() / 2 + 98, client.getWindow()
                            .getScaledHeight() - 19, 0, 0, 16, 16,
                    16, 16);
            drawContext.drawText(client.textRenderer, elytraLockNotifier,
                    client.getWindow().getScaledWidth() / 2 + 116, client.getWindow()
                            .getScaledHeight() - 14, 0xFF1313, false);
        }
    }
}
