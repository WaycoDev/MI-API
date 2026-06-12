package me.WaycoDev.mi.api;

import me.WaycoDev.mi.api.MIElement;
import me.WaycoDev.mi.api.MITheme;
import net.minecraft.client.gui.DrawContext;

public class MIScrollPanel extends MIElement {
    
    private MIUniPanel content;
    private int scrollY = 0;
    private int contentHeight = 0;
    private boolean scrollBarVisible = true;
    private int scrollBarWidth = 4;
    private int scrollBarColor;
    
    public MIScrollPanel(int x, int y, int w, int h, MITheme theme) {
        super(x, y, w, h, theme);
        this.scrollBarColor = theme.getAccentColor();
    }
    
    // Устанавливаем контент (вселенную, которую нужно скроллить)
    public void setContent(MIUniPanel content) {
        this.content = content;
        this.contentHeight = content.height;
    }
    
    // Обновить высоту контента (вызывать, когда контент изменился)
    public void updateContentHeight() {
        if (content != null) {
            this.contentHeight = content.height;
            // Корректируем scrollY, если он стал больше допустимого
            setScrollY(scrollY);
        }
    }
    
    private void drawScrollBar(DrawContext ctx) {
        if (!scrollBarVisible || contentHeight <= height) return;
        
        int barHeight = Math.max(30, (int)((float)height / contentHeight * height));
        int barY = y + (int)((float)scrollY / contentHeight * height);
        barY = Math.max(y, Math.min(barY, y + height - barHeight));
        
        ctx.fill(x + width - scrollBarWidth, barY, x + width, barY + barHeight, scrollBarColor);
        
        int bgColor = MITheme.withAlpha(scrollBarColor, 0.3f);
        ctx.fill(x + width - scrollBarWidth, y, x + width, y + height, bgColor);
    }
    
    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        if (content == null) return;
        
        ctx.enableScissor(x, y, width, height);
        
        // Рендерим контент со сдвигом
        int originalY = content.y;
        content.setPosition(content.x, content.y - scrollY);
        content.render(ctx, mx, my + scrollY, delta);
        content.setPosition(content.x, originalY);
        
        ctx.disableScissor();
        drawScrollBar(ctx);
    }
    
    @Override
    public boolean mouseScrolled(double mx, double my, double horizontal, double vertical) {
        if (!visible || !isMouseOver(mx, my) || content == null) return false;
        
        scrollY += vertical * 15;
        scrollY = Math.max(0, Math.min(scrollY, contentHeight - height));
        return true;
    }
    
    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (!visible || !isMouseOver(mx, my) || content == null) return false;
        return content.mouseClicked(mx, my + scrollY, button);
    }
    
    @Override
    public boolean mouseReleased(double mx, double my, int button) {
        if (!visible || content == null) return false;
        return content.mouseReleased(mx, my + scrollY, button);
    }
    
    @Override
    public void mouseMoved(double mx, double my) {
        if (!visible || content == null) return;
        content.mouseMoved(mx, my + scrollY);
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (content == null) return false;
        return content.keyPressed(keyCode, scanCode, modifiers);
    }
    
    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (content == null) return false;
        return content.charTyped(chr, modifiers);
    }
    
    @Override
    public void setPosition(int x, int y) {
        int deltaX = x - this.x;
        int deltaY = y - this.y;
        super.setPosition(x, y);
        if (content != null) {
            content.setPosition(content.x + deltaX, content.y + deltaY);
        }
    }
    
    public void setScrollBarVisible(boolean visible) { this.scrollBarVisible = visible; }
    public void setScrollBarWidth(int width) { this.scrollBarWidth = width; }
    public void setScrollBarColor(int color) { this.scrollBarColor = color; }
    
    public void setScrollY(int scroll) { 
        this.scrollY = Math.max(0, Math.min(scroll, Math.max(0, contentHeight - height)));
    }
    
    public int getScrollY() { return scrollY; }
    public int getContentHeight() { return contentHeight; }
}