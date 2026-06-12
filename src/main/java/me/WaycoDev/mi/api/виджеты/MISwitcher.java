package me.WaycoDev.mi.api;

import me.WaycoDev.mi.api.MIElement;
import me.WaycoDev.mi.api.MITheme;
import me.WaycoDev.mi.api.utils.MIRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import java.util.function.Consumer;

public class MISwitcher extends MIElement {
    private boolean toggled = false;
    private Runnable onToggle;
    private Consumer<Boolean> onStateChanged;
    
    private String offText = "OFF";
    private String onText = "ON";
    private boolean showText = true;
    
    private int customOffBg = -1;
    private int customOnBg = -1;
    private int customKnob = -1;
    private int customOffTextColor = -1;
    private int customOnTextColor = -1;

    public MISwitcher(int x, int y, int width, int height, MITheme theme) {
        super(x, y, width, height, theme);
    }

    // ==================== СОСТОЯНИЕ ====================
    public void setToggled(boolean toggled) {
        if (this.toggled == toggled) return;
        this.toggled = toggled;
        if (onStateChanged != null) onStateChanged.accept(toggled);
        if (onToggle != null) onToggle.run();
    }
    
    public boolean isToggled() { return toggled; }
    
    // ==================== КОЛБЭКИ ====================
    public void setOnToggle(Runnable action) { this.onToggle = action; }
    public void setOnStateChanged(Consumer<Boolean> callback) { this.onStateChanged = callback; }
    
    // ==================== ВНЕШНИЙ ВИД ====================
    public void setOffText(String text) { this.offText = text; }
    public void setOnText(String text) { this.onText = text; }
    public void setShowText(boolean show) { this.showText = show; }
    
    public void setCustomOffBg(int color) { this.customOffBg = color; }
    public void setCustomOnBg(int color) { this.customOnBg = color; }
    public void setCustomKnob(int color) { this.customKnob = color; }
    public void setCustomOffTextColor(int color) { this.customOffTextColor = color; }
    public void setCustomOnTextColor(int color) { this.customOnTextColor = color; }
    
    // ==================== РЕНДЕР ====================
    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        // Определяем цвета
        int bgColor = toggled 
            ? (customOnBg != -1 ? customOnBg : theme.getSwitcherOnBg())
            : (customOffBg != -1 ? customOffBg : theme.getSwitcherOffBg());
        
        int knobColor = customKnob != -1 ? customKnob : theme.getSwitcherKnob();
        
        int textColor = toggled
            ? (customOnTextColor != -1 ? customOnTextColor : theme.getSwitcherOnText())
            : (customOffTextColor != -1 ? customOffTextColor : theme.getSwitcherOffText());
        
        // Рисуем фон (таблетку)
        MIRenderer.drawRoundedRect(ctx, x, y, width, height, bgColor, height / 2);
        
        // Рисуем кружок (ползунок)
        int knobSize = height - 4;
        int knobX = toggled ? x + width - knobSize - 2 : x + 2;
        int knobY = y + 2;
        MIRenderer.drawRoundedRect(ctx, knobX, knobY, knobSize, knobSize, knobColor, knobSize / 2);
        
        // Текст (если нужно)
        if (showText) {
            var tr = MinecraftClient.getInstance().textRenderer;
            String text = toggled ? onText : offText;
            int textX = x + (width - tr.getWidth(text)) / 2;
            int textY = y + (height - tr.fontHeight) / 2;
            ctx.drawText(tr, Text.literal(text), textX, textY, textColor, false);
        }
        
        drawBorder(ctx);
    }
    
    // ==================== СОБЫТИЯ ====================
    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (isMouseOver(mx, my) && button == 0) {
            setToggled(!toggled);
            return true;
        }
        return false;
    }
}