package net.smootheez.elytracontrol;

import net.fabricmc.api.ClientModInitializer;

public class ElytraControl implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Constant.LOGGER.info("Elytra Control Initialized");
    }
}
