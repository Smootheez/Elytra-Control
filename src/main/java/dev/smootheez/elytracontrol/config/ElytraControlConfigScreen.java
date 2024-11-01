package dev.smootheez.elytracontrol.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import dev.smootheez.elytracontrol.Constants;
import dev.smootheez.scl.gui.widget.AutoConfigListWidget;
import dev.smootheez.scl.registry.ConfigRegister;

@Environment(EnvType.CLIENT)
public class ElytraControlConfigScreen extends Screen {
    private final Screen parent;

    protected ElytraControlConfigScreen(Screen screen) {
        super(Text.translatable("elytracontrol.title"));
        this.parent = screen;
    }

    @Override
    protected void init() {
        addDrawableChild(new AutoConfigListWidget(ElytraControlConfig.getInstance(), Constants.MOD_ID));
        addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> close()).dimensions(this.width / 2 + 5, this.height - 27, 150, 20).build());

        addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> {
            ConfigRegister.getInstance().save(ElytraControlConfig.class);
            close();
        }).dimensions(this.width / 2 - 155, this.height - 27, 150, 20).build());
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 12, 0xFFFFFF);
    }
}
