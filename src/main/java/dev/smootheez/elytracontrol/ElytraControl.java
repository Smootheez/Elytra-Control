package dev.smootheez.elytracontrol;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import dev.smootheez.elytracontrol.config.ElytraControlConfig;
import dev.smootheez.elytracontrol.gui.ElytraControlHud;
import dev.smootheez.elytracontrol.events.EndTickEvent;
import dev.smootheez.scl.registry.ConfigRegister;


@Environment(EnvType.CLIENT)
public class ElytraControl implements ClientModInitializer {
    public static final KeyBinding elytraToggleKey = new KeyBinding(
            "key." + Constants.MOD_ID + ".elytraToggle",
            InputUtil.GLFW_KEY_V,
            "key.category.elytracontrol"
    );

    @Override
    public void onInitializeClient() {
        Constants.LOGGER.info("Elytra Control Initialized");
        ConfigRegister.getInstance().register(ElytraControlConfig.getInstance());

        KeyBindingHelper.registerKeyBinding(elytraToggleKey);

        HudRenderCallback.EVENT.register(new ElytraControlHud());
        ClientTickEvents.END_CLIENT_TICK.register(new EndTickEvent());
    }
}
