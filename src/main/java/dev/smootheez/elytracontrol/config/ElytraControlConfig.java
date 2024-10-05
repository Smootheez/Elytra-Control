package dev.smootheez.elytracontrol.config;

import dev.smootheez.scl.annotation.Config;
import dev.smootheez.scl.api.ConfigProvider;
import dev.smootheez.scl.option.ConfigOption;

@Config("elytracontrol")
public class ElytraControlConfig implements ConfigProvider {
    private static final ElytraControlConfig INSTANCE = new ElytraControlConfig();
    private final String FUN_CATEGORY = "funOptionsCategory";

    private final ConfigOption<Boolean> elytraLock = ConfigOption.create("elytraLock", true);
    private final ConfigOption<Boolean> elytraCancel = ConfigOption.create("elytraCancel", true);
    private final ConfigOption<Boolean> funOptions = ConfigOption.create("funOptions", false);
    private final ConfigOption<Boolean> showLockIcon = ConfigOption.create("showLockIcon", true);
    private final ConfigOption<Boolean> easyFlight = ConfigOption.create("easyFlight", false);

    @Config.Category(FUN_CATEGORY)
    private final ConfigOption<Double> wingFlapSpeed = ConfigOption.create("wingFlapSpeed", 0.3, Double.MIN_VALUE, Double.MAX_VALUE);
    @Config.Category(FUN_CATEGORY)
    private final ConfigOption<Double> minVelocity = ConfigOption.create("minVelocity", 0.6, Double.MIN_VALUE, Double.MAX_VALUE);
    @Config.Category(FUN_CATEGORY)
    private final ConfigOption<Double> maxVelocity = ConfigOption.create("maxVelocity", 1.8, Double.MIN_VALUE, Double.MAX_VALUE);
    @Config.Category(FUN_CATEGORY)
    private final ConfigOption<Double> flapAmplitude = ConfigOption.create("flapAmplitude", 0.5, Double.MIN_VALUE, Double.MAX_VALUE);
    @Config.Category(FUN_CATEGORY)
    private final ConfigOption<Double> subtleFlapFactor = ConfigOption.create("subtleFlapFactor", 0.2, Double.MIN_VALUE, Double.MAX_VALUE);

    public ConfigOption<Boolean> getShowLockIcon() {
        return showLockIcon;
    }

    public ConfigOption<Boolean> getFunOptions() {
        return funOptions;
    }

    public ConfigOption<Double> getMinVelocity() {
        return minVelocity;
    }

    public ConfigOption<Double> getMaxVelocity() {
        return maxVelocity;
    }

    public ConfigOption<Double> getFlapAmplitude() {
        return flapAmplitude;
    }

    public ConfigOption<Double> getSubtleFlapFactor() {
        return subtleFlapFactor;
    }

    public ConfigOption<Double> getWingFlapSpeed() {
        return wingFlapSpeed;
    }

    public ConfigOption<Boolean> getElytraLock() {
        return elytraLock;
    }

    public ConfigOption<Boolean> getElytraCancel() {
        return elytraCancel;
    }

    public static ElytraControlConfig getInstance() {
        return INSTANCE;
    }

    public ConfigOption<Boolean> getEasyFlight() {
        return easyFlight;
    }
}
