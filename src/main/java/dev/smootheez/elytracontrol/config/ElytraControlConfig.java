package dev.smootheez.elytracontrol.config;

import dev.smootheez.elytracontrol.config.option.LockIconMode;
import dev.smootheez.scl.annotation.Config;
import dev.smootheez.scl.api.ConfigProvider;
import dev.smootheez.scl.option.ConfigOption;

@Config("elytracontrol")
public class ElytraControlConfig implements ConfigProvider {
    private static final ElytraControlConfig INSTANCE = new ElytraControlConfig();

    private final ConfigOption<Boolean> elytraLock = ConfigOption.create("elytraLock", true);
    private final ConfigOption<Boolean> elytraCancel = ConfigOption.create("elytraCancel", true);
    private final ConfigOption<LockIconMode> lockIconMode = ConfigOption.create("iconLockMode", LockIconMode.ICON_TEXT);
    private final ConfigOption<Boolean> easyFlight = ConfigOption.create("easyFlight", false);
    private final ConfigOption<Boolean> easyFlightMessage = ConfigOption.create("easyFlightMessage", true);
    private final ConfigOption<Boolean> elytraLockMessage = ConfigOption.create("elytraLockMessage", true);

    private final ConfigOption<Double> pitchUpSpeed = ConfigOption.create("pitchUpSpeed", 2.3, 0.1, 4.0);
    private final ConfigOption<Double> pitchDownSpeed = ConfigOption.create("pitchDownSpeed", 2.0, 0.1, 4.0);

    public static ElytraControlConfig getInstance() {
        return INSTANCE;
    }

    public ConfigOption<Double> getPitchDownSpeed() {
        return pitchDownSpeed;
    }

    public ConfigOption<LockIconMode> getLockIconMode() {
        return lockIconMode;
    }

    public ConfigOption<Boolean> getElytraLock() {
        return elytraLock;
    }

    public ConfigOption<Boolean> getElytraCancel() {
        return elytraCancel;
    }

    public ConfigOption<Boolean> getEasyFlight() {
        return easyFlight;
    }

    public ConfigOption<Boolean> getEasyFlightMessage() {
        return easyFlightMessage;
    }

    public ConfigOption<Boolean> getElytraLockMessage() {
        return elytraLockMessage;
    }

    public ConfigOption<Double> getPitchUpSpeed() {
        return pitchUpSpeed;
    }
}
