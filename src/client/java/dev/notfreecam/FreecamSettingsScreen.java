package dev.notfreecam;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class FreecamSettingsScreen extends Screen {
    private final Screen parent;

    public FreecamSettingsScreen() {
        this(null);
    }

    public FreecamSettingsScreen(Screen parent) {
        super(Component.literal("NotFreecam Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int center = this.width / 2;
        int y = this.height / 2 - 30;
        this.addRenderableWidget(Button.builder(speedLabel(), button -> {
            float next = NotFreecamClient.getSpeed() + 0.25F;
            if (next > 5.0F) next = 0.1F;
            NotFreecamClient.setSpeed(next);
            button.setMessage(speedLabel());
        }).bounds(center - 100, y, 200, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("Done"),
                button -> this.onClose()).bounds(center - 100, y + 30, 200, 20).build());
    }

    private static Component speedLabel() {
        return Component.literal(String.format(java.util.Locale.ROOT,
                "Camera speed: %.2f", NotFreecamClient.getSpeed()));
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) this.minecraft.gui.setScreen(this.parent);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        this.extractBackground(graphics, mouseX, mouseY, delta);
        graphics.centeredText(this.font, this.title, this.width / 2, this.height / 2 - 65, 0xFFFFFF);
        graphics.centeredText(this.font,
                Component.literal("WASD move  |  Space up  |  Shift down  |  Sprint = boost"),
                this.width / 2, this.height / 2 + 35, 0xAAAAAA);
        super.render(graphics, mouseX, mouseY, delta);
    }
}
