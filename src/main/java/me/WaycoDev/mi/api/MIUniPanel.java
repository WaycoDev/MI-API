package me.WaycoDev.mi.api;

import net.minecraft.client.gui.DrawContext;
import java.util.ArrayList;
import java.util.List;

public class MIUniPanel extends MIElement {
    private List<MIElement> children = new ArrayList<>();
    private boolean relativeCoordinates = true; // всегда true для UniPanel

    public MIUniPanel(int x, int y, int width, int height, MITheme theme) {
        super(x, y, width, height, theme);
    }

    public void addChild(MIElement child) {
        children.add(child);
        child.setPosition(this.x + child.x, this.y + child.y);
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        for (MIElement child : children) {
            child.render(ctx, mx, my, delta);
        }
    }

    @Override
    public void setPosition(int newX, int newY) {
        int deltaX = newX - this.x;
        int deltaY = newY - this.y;
        super.setPosition(newX, newY);
        for (MIElement child : children) {
            child.setPosition(child.x + deltaX, child.y + deltaY);
        }
    }

    // Проброс событий не нужен — дети сами получат их через MIScreen
}