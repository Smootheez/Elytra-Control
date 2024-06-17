package net.smootheez.elytracontrol.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameModeSelectionScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class ElytraControlConfigScreen extends Screen {
    private final Screen parent;
    private final ElytraControlConfig config;
    private List<Value<?>> values;

    protected ElytraControlConfigScreen(Screen parent, ElytraControlConfig config) {
        super(Text.translatable("gui.elytracontrol.config.title"));
        this.parent = parent;
        this.config = config;
    }

//    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
//        renderBackground(matrices);
//        drawCenteredText(matrices, textRenderer, title, width / 2, 10, 0xFFFFFF);
//        super.render(matrices, mouseX, mouseY, delta);
//    }

    public void close(){
        client.setScreen(parent);
    }

    private void setValues(){
        EnumSet<Value.Flag> flags = EnumSet.noneOf(Value.Flag.class);

        for (Value<?> value : values){
            if (value.isChanged()){
                value.saveToOption();
                flags.addAll(value.getFlags());
            }
        }
        config.save();
        for (Value.Flag flag : flags){
            flag.onSave();
        }
    }

    private static String getTranslationKey(String optionKey){
        return "option.elytracontrol." + optionKey;
    }

//    private ButtonWidget createBooleanValueButton(Value<Boolean> value, int x, int y, int width, int height){
//        String translationKey = getTranslationKey(value.getOption().getKey());
//        Text text = Text.translatable(translationKey);
//        return new ButtonWidget(x, y, width, height, ScreenTexts.composeGenericOptionText(text, ScreenTexts.onOrOff(value.get())),
//                button -> {
//
//        });
//    }

    public static class Value<T>{
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
        public enum Flag{
            RELOAD_WORLD_RENDERER{
                @Override
                public void onSave(){
                    MinecraftClient.getInstance().worldRenderer.reload();
                }
            };
            public abstract void onSave();
        }
    }
}
