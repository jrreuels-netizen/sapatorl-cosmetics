package de.sapatorl.cosmetics;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

/** Cosmetics-Menü: 3x3 Buttons, Absetzen, Fertig. */
public class CosmeticsScreen extends Screen {
    private static final int BTN_W = 120;
    private static final int BTN_H = 20;
    private static final int GAP = 6;

    public CosmeticsScreen() {
        super(Component.literal("sapatorl_ Cosmetics"));
    }

    @Override
    protected void init() {
        int cols = 3;
        int gridW = cols * BTN_W + (cols - 1) * GAP;
        int left = (this.width - gridW) / 2;
        int top = this.height / 2 - 60;

        StringWidget title = new StringWidget(
                Component.literal("✦ sapatorl_ Cosmetics ✦")
                        .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFF66FF)).withBold(true)),
                this.font);
        title.setX((this.width - title.getWidth()) / 2);
        title.setY(top - 30);
        this.addRenderableWidget(title);

        String current = Cosmetics.selected();
        for (int i = 0; i < Cosmetics.ALL.size(); i++) {
            Cosmetics.Entry e = Cosmetics.ALL.get(i);
            boolean active = e.id().equals(current);
            Component label = Component.literal((active ? "✔ " : "") + e.name())
                    .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(active ? 0x55FF55 : e.color())));
            int x = left + (i % cols) * (BTN_W + GAP);
            int y = top + (i / cols) * (BTN_H + GAP);
            this.addRenderableWidget(Button.builder(label, b -> {
                Cosmetics.select(active ? "" : e.id());
                this.rebuildWidgets();
            }).bounds(x, y, BTN_W, BTN_H).build());
        }

        int bottom = top + 3 * (BTN_H + GAP) + 14;
        int halfW = (gridW - GAP) / 2;
        this.addRenderableWidget(Button.builder(
                Component.literal("Absetzen").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFF5555))),
                b -> {
                    Cosmetics.select("");
                    this.rebuildWidgets();
                }).bounds(left, bottom, halfW, BTN_H).build());
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, b -> this.onClose())
                .bounds(left + halfW + GAP, bottom, halfW, BTN_H).build());

        StringWidget hint = new StringWidget(
                Component.literal("Tipp: Mit F5 siehst du dein Cosmetic. Nur du siehst es.")
                        .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xAAAAAA))),
                this.font);
        hint.setX((this.width - hint.getWidth()) / 2);
        hint.setY(bottom + BTN_H + 10);
        this.addRenderableWidget(hint);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
