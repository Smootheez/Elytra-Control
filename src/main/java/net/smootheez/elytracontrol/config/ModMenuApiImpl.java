package net.smootheez.elytracontrol.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuApiImpl implements ModMenuApi {
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new ElytraControlConfigScreen(parent, ElytraControlConfig.INSTANCE);
    }
}