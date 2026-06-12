package me.WaycoDev.mi.api;

import me.WaycoDev.mi.api.utils.MIRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class MIInputField extends MIElement {
    private String text = "";
    private String placeholder;
    private boolean focused;
    private int cursorPos = 0;
    private int customRadius = -1;
    private int outlineColor = -1;
    private int outlineWidth = 1;

    public MIInputField(int x, int y, int w, int h, MITheme theme) {
        super(x, y, w, h, theme);
    }

    public void setRadius(int r) { this.customRadius = r; }
    public void setOutlineColor(int c) { this.outlineColor = c; }
    public void setOutlineWidth(int w) { this.outlineWidth = w; }
    public void setPlaceholder(String p) { this.placeholder = p; }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        int radius = (customRadius > 0) ? customRadius : theme.getRadius();
        MIRenderer.drawRoundedRect(ctx, x, y, width, height, theme.getHeaderBg(), radius);

        if (focused) {
            int color = (outlineColor != -1) ? outlineColor : theme.getAccentColor();
            for (int i = 0; i < outlineWidth; i++) {
                MIRenderer.drawHollowRect(ctx, x + i, y + i, width - i*2, height - i*2, color, radius);
            }
        }

        var tr = MinecraftClient.getInstance().textRenderer;
        String display = text.isEmpty() && !focused ? placeholder : text;
        int col = text.isEmpty() && !focused ? theme.getTextMuted() : theme.getTextPrimary();
        
        String beforeCursor = text.substring(0, Math.min(cursorPos, text.length()));
        int cursorX = x + 8 + tr.getWidth(beforeCursor);
        cursorX = Math.min(cursorX, x + width - 8);
        
        int offset = 0;
        if (cursorX > x + width - 8) {
            offset = cursorX - (x + width - 8);
        }
        
        ctx.enableScissor(x + 8, y + 4, x + width - 8, y + height - 4);
        ctx.drawText(tr, Text.literal(display), x + 8 - offset, y + (height - 8) / 2, col, false);
        ctx.disableScissor();

        if (focused && (System.currentTimeMillis() / 500) % 2 == 0) {
            ctx.fill(cursorX - offset, y + 4, cursorX - offset + 1, y + height - 4, theme.getTextPrimary());
        }
        
        drawBorder(ctx);
    }

    private void updateCursorPosition() {
        cursorPos = Math.max(0, Math.min(cursorPos, text.length()));
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (super.mouseClicked(mx, my, button)) {
            focused = true;
            return true;
        }
        focused = false;
        return false;
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (!focused) return false;
        if (chr >= 32) {
            text = text.substring(0, cursorPos) + chr + text.substring(cursorPos);
            cursorPos++;
            updateCursorPosition();
        }
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!focused) return false;
        switch (keyCode) {
            case 259:
                if (cursorPos > 0) {
                    text = text.substring(0, cursorPos - 1) + text.substring(cursorPos);
                    cursorPos--;
                    updateCursorPosition();
                }
                return true;
            case 263:
                cursorPos = Math.max(0, cursorPos - 1);
                updateCursorPosition();
                return true;
            case 262:
                cursorPos = Math.min(text.length(), cursorPos + 1);
                updateCursorPosition();
                return true;
            case 256:
                focused = false;
                return true;
        }
        return false;
    }

    public String getText() { return text; }
    public void setText(String t) { text = t; cursorPos = text.length(); updateCursorPosition(); }
    @Override protected void onClick() {}
}