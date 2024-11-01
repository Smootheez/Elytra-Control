package dev.smootheez.elytracontrol;

import dev.smootheez.elytracontrol.registry.KeyBinds;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import dev.smootheez.elytracontrol.config.ElytraControlConfig;
import dev.smootheez.elytracontrol.gui.ElytraControlHud;
import dev.smootheez.elytracontrol.event.EndTickEvent;
import dev.smootheez.scl.registry.ConfigRegister;


@Environment(EnvType.CLIENT)
public class ElytraControl implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Constants.LOGGER.info("Initializing " + Constants.MOD_ID + "...");
        ConfigRegister.getInstance().register(ElytraControlConfig.getInstance());

        KeyBinds.registerKeyBinds();

        HudRenderCallback.EVENT.register(new ElytraControlHud());
        ClientTickEvents.END_CLIENT_TICK.register(new EndTickEvent());
    }
}
