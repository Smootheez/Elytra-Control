package net.smootheez.elytracontrol;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.smootheez.elytracontrol.gui.ElytraControlHud;
import net.smootheez.elytracontrol.handler.EndClientTickHandler;


@Environment(EnvType.CLIENT)
public class ElytraControl implements ClientModInitializer {
    public static final KeyBinding elytraToggleKey = new KeyBinding(
            "key." + Constants.MOD_ID + ".elytra_toggle",
            InputUtil.GLFW_KEY_V,
            "key.category.elytracontrol"
    );
    @Override
    public void onInitializeClient() {
        Constants.LOGGER.info("Elytra Control Initialized");

        KeyBindingHelper.registerKeyBinding(elytraToggleKey);
        HudRenderCallback.EVENT.register(new ElytraControlHud());
        ClientTickEvents.END_CLIENT_TICK.register(new EndClientTickHandler());
    }
}
