package me.WaycoDev.mi.api;

import me.WaycoDev.mi.api.utils.MIRenderer;
import net.minecraft.client.gui.DrawContext;

public abstract class MIElement {
    protected int x, y, width, height;
    protected boolean hovered, pressed, visible = true;
    protected MITheme theme;
    protected float textScale = 1.0f;

    // Новое: параметры рамки
    protected int borderColor = -1;      // -1 означает, что рамка не рисуется
    protected int borderWidth = 1;
    protected int borderRadius = -1;     // -1 = использовать радиус из темы

    public MIElement(int x, int y, int w, int h, MITheme theme) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.theme = theme;
    }

    // ========== НОВЫЕ МЕТОДЫ ДЛЯ РАМКИ ==========
    public void setBorder(int color, int width) {
        this.borderColor = color;
        this.borderWidth = Math.max(1, width);
    }

    public void setBorder(int color, int width, int radius) {
        this.borderColor = color;
        this.borderWidth = Math.max(1, width);
        this.borderRadius = radius;
    }

    public void disableBorder() {
        this.borderColor = -1;
    }

    public boolean hasBorder() {
        return borderColor != -1;
    }

    public int getBorderRadius() {
        return borderRadius != -1 ? borderRadius : theme.getRadius();
    }

    protected void drawBorder(DrawContext ctx) {
        if (!hasBorder()) return;
        MIRenderer.drawRoundedHollowRect(ctx, x, y, width, height, borderColor, borderWidth, getBorderRadius());
    }
    // ===========================================

    public boolean isMouseOver(double mx, double my) {
        return visible && mx >= x && mx <= x + width && my >= y && my <= y + height;
    }

    public void mouseMoved(double mx, double my) {
        hovered = isMouseOver(mx, my);
    }

    public boolean mouseClicked(double mx, double my, int button) {
        if (visible && button == 0 && isMouseOver(mx, my)) {
            pressed = true;
            return true;
        }
        return false;
    }

    public boolean mouseReleased(double mx, double my, int button) {
        if (button == 0 && pressed) {
            pressed = false;
            if (isMouseOver(mx, my)) onClick();
            return true;
        }
        return false;
    }

    public boolean mouseScrolled(double mx, double my, double horizontalAmount, double verticalAmount) {
        return false;
    }

    protected void onClick() {}

    public abstract void render(DrawContext ctx, int mx, int my, float delta);

    public void setPosition(int x, int y) { this.x = x; this.y = y; }
    public void setSize(int w, int h) { this.width = w; this.height = h; }
    public void setVisible(boolean v) { this.visible = v; }
    public boolean isVisible() { return visible; }
    public boolean isHovered() { return hovered; }
    public boolean isPressed() { return pressed; }

    public void setTextScale(float scale) {
        this.textScale = Math.max(0.5f, Math.min(scale, 3.0f));
    }

    public boolean charTyped(char chr, int modifiers) {
        return false;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }
}