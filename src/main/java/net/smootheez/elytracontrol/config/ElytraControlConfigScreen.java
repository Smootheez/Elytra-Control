package net.smootheez.elytracontrol.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.smootheez.elytracontrol.Constants;

import java.util.*;

public class ElytraControlConfigScreen extends Screen {
    private final Screen parent;
    private final ElytraControlConfig config;

    private List<Value<?>> values;

    public ElytraControlConfigScreen(Screen parent, ElytraControlConfig config) {
        super(Text.translatable(getTranslationKey("title")));
        this.parent = parent;
        this.config = config;
    }

    @Override
    protected void init() {
        Value<Boolean> elytraLock = Value.of(config.elytraLock, Value.Flag.RELOAD_WORLD_RENDERER);
        Value<Boolean> elytraCancel = Value.of(config.elytraCancel, Value.Flag.RELOAD_WORLD_RENDERER);
        Value<String> multiValue = Value.of(config.multiValue, Value.Flag.RELOAD_WORLD_RENDERER);

        values = List.of(elytraLock, elytraCancel);

        addDrawableChild(createBooleanValueButton(elytraLock, width / 2 - 100 - 110, height / 2 - 10 - 12, 200, 20));
        addDrawableChild(createBooleanValueButton(elytraCancel,width / 2 - 100 + 110, height / 2 - 10 - 12, 200, 20));
        addDrawableChild(createMultiOptionValueButton(multiValue, width / 2 - 100, height / 2 + 10, 200, 20));

        addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> {
            saveValues();
            close();
        }).dimensions(this.width / 2 - 75 + 79, this.height - 40, 150, 20).build());
        addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> {
            close();
        }).dimensions(this.width / 2 - 75 - 79, this.height - 40, 150, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

        context.drawCenteredTextWithShadow(this.textRenderer, Text.translatable(getTranslationKey("title")), this.width / 2, this.height / 2 - 80, 0xFFFFFF);
    }

    @Override
    public void close() {
        if (client == null){
            return;
        }
        client.setScreen(parent);
    }

    public void saveValues() {
        EnumSet<Value.Flag> flags = EnumSet.noneOf(Value.Flag.class);

        for (Value<?> value : values) {
            if (value.isChanged()){
                value.saveToOption();
                flags.addAll(value.getFlags());
            }
        }
        config.saveConfig();

        for (Value.Flag flag : flags) {
            flag.apply();
        }
    }

    private static String getTranslationKey(String optionKey){
        return "options." + Constants.MOD_ID + "." + optionKey;
    }

    private ButtonWidget createBooleanValueButton(Value<Boolean> value, int x, int y, int width, int height) {
        String translationKey = getTranslationKey(value.getOption().getKey());
        Text text = Text.translatable(translationKey);
        return ButtonWidget.builder(ScreenTexts.composeGenericOptionText(text, ScreenTexts.onOrOff(value.get())), (button) -> {
            boolean newValue = !value.get();
            value.set(newValue);
            Text valueText = ScreenTexts.onOrOff(newValue);
            if (value.isChanged()){
                valueText = valueText.copy().styled(style -> style.withBold(true));
            }
            button.setMessage(ScreenTexts.composeGenericOptionText(text, valueText));
        }).dimensions(x, y, width, height).build();
    }

    private ButtonWidget createMultiOptionValueButton(Value<String> value, int x, int y, int width, int height) {
        String translationKey = getTranslationKey(value.getOption().getKey());
        Text text = Text.translatable(translationKey);
        return ButtonWidget.builder(ScreenTexts.composeGenericOptionText(text, Text.of(value.get())), (button) -> {
            ((Option.MultiOption) value.getOption()).nextOption();
            String newValue = value.get();
            Text valueText = Text.of(newValue);
            if (value.isChanged()) {
                valueText = valueText.copy().styled(style -> style.withBold(true));
            }
            button.setMessage(ScreenTexts.composeGenericOptionText(text, valueText));
        }).dimensions(x, y, width, height).build();
    }

    private static class Value<T> {
        private final Option<T> option;
        private final Set<Flag> flags;
        private final T originalValue;
        private T value;

        public Value(Option<T> option, Set<Flag> flags) {
            this.option = option;
            this.flags = flags;
            originalValue = this.option.get();
            value = originalValue;
        }

        public static <T> Value<T> of(Option<T> option, Flag... flags) {
            EnumSet<Flag> flagSet = EnumSet.noneOf(Flag.class);
            Collections.addAll(flagSet, flags);
            return new Value<>(option, flagSet);
        }

        public Option<T> getOption() {
            return option;
        }

        public Set<Flag> getFlags() {
            return flags;
        }

        public T get() {
            return value;
        }

        public void set(T value) {
            this.value = value;
        }

        public boolean isChanged() {
            return !value.equals(originalValue);
        }

        public void saveToOption() {
            option.set(value);
        }

        public enum Flag {
            RELOAD_WORLD_RENDERER {
                @Override
                public void apply() {
                    MinecraftClient.getInstance().worldRenderer.reload();
                }
            };

            public abstract void apply();
        }
    }
}
