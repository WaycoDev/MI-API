package me.WaycoDev.mi.api;

import me.WaycoDev.mi.api.utils.MIRenderer;
import net.minecraft.client.gui.DrawContext;
import java.util.ArrayList;
import java.util.List;

public class MITextBackground extends MIElement {
    private int color;
    private List<MIElement> children = new ArrayList<>();
    private boolean relativeCoordinates = false;

    public MITextBackground(int x, int y, int width, int height, MITheme theme) {
        super(x, y, width, height, theme);
        this.color = theme.getSurfaceBg();
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setRelativeCoordinates(boolean relative) {
        this.relativeCoordinates = relative;
    }

    public void addChild(MIElement child) {
        if (!relativeCoordinates) return;
        children.add(child);
        child.setPosition(this.x + child.x, this.y + child.y);
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        MIRenderer.drawRoundedRect(ctx, x, y, width, height, color, theme.getRadius());
        for (MIElement child : children) {
            child.render(ctx, mx, my, delta);
        }
        drawBorder(ctx);
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
}