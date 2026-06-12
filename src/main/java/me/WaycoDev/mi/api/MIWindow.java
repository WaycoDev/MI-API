package me.WaycoDev.mi.api;

import me.WaycoDev.mi.api.utils.MIRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public abstract class MIWindow extends MIScreen {
    protected int windowX = 0, windowY = 0;
    protected int windowWidth, windowHeight;
    protected String windowTitle = "";
    protected boolean showHeader = true;
    protected boolean showCloseButtons = true;
    protected boolean draggable = true;
    
    protected boolean dragging = false;
    protected int dragOffsetX, dragOffsetY;
    
    // ==================== КОНСТРУКТОРЫ ====================
    protected MIWindow(Text title, MITheme theme, int width, int height, 
                       boolean showHeader, boolean showCloseButtons, boolean draggable) {
        super(title, theme);
        this.windowTitle = title.getString();
        this.windowWidth = width;
        this.windowHeight = height;
        this.showHeader = showHeader;
        this.showCloseButtons = showCloseButtons;
        this.draggable = draggable;
    }
    
    protected MIWindow(Text title, MITheme theme, int width, int height) {
        this(title, theme, width, height, true, true, true);
    }
    
    // ==================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ====================
    protected int getHeaderHeight() {
        return showHeader ? theme.getHeaderHeight() : 0;
    }
    
    public int getContentY() {
        return windowY + getHeaderHeight();
    }
    
    public int getContentHeight() {
        return windowHeight - getHeaderHeight();
    }
    
    protected boolean isMouseOverHeader(double mx, double my) {
        return showHeader && mx >= windowX && mx <= windowX + windowWidth
                && my >= windowY && my <= windowY + getHeaderHeight();
    }
    
    protected boolean isCloseButtonClicked(double mx, double my) {
        if (!showCloseButtons) return false;
        int btnSize = theme.getCloseButtonSize();
        int btnX = windowX + 8;
        int btnY = windowY + (getHeaderHeight() - btnSize) / 2;
        return mx >= btnX && mx <= btnX + btnSize && my >= btnY && my <= btnY + btnSize;
    }
    
    public void close() {
        MinecraftClient.getInstance().setScreen(null);
    }
    
    // ==================== INIT ====================
    @Override
    protected void init() {
        super.init();
        windowX = (screenWidth - windowWidth) / 2;
        windowY = (screenHeight - windowHeight) / 2;
        
        buildWindowUI();
    }
    
    // ==================== UI ====================
    protected abstract void buildWindowUI();
    
    protected void addWindowElement(MIElement e) {
        e.setPosition(windowX + e.x, getContentY() + e.y);
        addElement(e);
    }
    
    protected void addFixedElement(MIElement e) {
        e.setPosition(windowX + e.x, windowY + e.y);
        addElement(e);
    }
    
    // ==================== НОВЫЕ МЕТОДЫ УДАЛЕНИЯ ====================
    public void removeWindowElement(MIElement e) {
        removeElement(e);
    }
    
    public void clearWindowElements() {
        clearElements();
    }
    // ==============================================================
    
    // ==================== RENDER ====================
    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        renderBackground(ctx, mx, my, delta);
        
        int radius = theme.getRadius();
        boolean hasHeader = showHeader && getHeaderHeight() > 0;
        
        if (hasHeader) {
            MIRenderer.drawRoundedRect(ctx, windowX, windowY, windowWidth, getHeaderHeight(), 
                    theme.getHeaderBg(), radius, true, true, false, false);
            ctx.fill(windowX, windowY + getHeaderHeight(), 
                    windowX + windowWidth, windowY + windowHeight, theme.getPageBg());
            MIRenderer.drawRoundedRect(ctx, windowX, windowY + windowHeight - radius, 
                    windowWidth, radius, theme.getPageBg(), radius, false, false, true, true);
            
            int titleWidth = MinecraftClient.getInstance().textRenderer.getWidth(windowTitle);
            int titleX = windowX + (windowWidth - titleWidth) / 2;
            int titleY = windowY + (getHeaderHeight() - 8) / 2;
            ctx.drawText(MinecraftClient.getInstance().textRenderer, Text.literal(windowTitle),
                    titleX, titleY, theme.getTextPrimary(), false);
            
            if (showCloseButtons) {
                int btnSize = theme.getCloseButtonSize();
                int btnY = windowY + (getHeaderHeight() - btnSize) / 2;
                int spacing = btnSize + 4;
                ctx.fill(windowX + 8, btnY, windowX + 8 + btnSize, btnY + btnSize, theme.getRedButtonColor());
                ctx.fill(windowX + 8 + spacing, btnY, windowX + 8 + spacing + btnSize, btnY + btnSize, theme.getYellowButtonColor());
                ctx.fill(windowX + 8 + spacing * 2, btnY, windowX + 8 + spacing * 2 + btnSize, btnY + btnSize, theme.getGreenButtonColor());
            }
        } else {
            MIRenderer.drawRoundedRect(ctx, windowX, windowY, windowWidth, windowHeight, 
                    theme.getPageBg(), radius);
        }
        
        // Отрисовка всех элементов (включая MIUniPanel)
        for (MIElement e : elements) {
            if (e.isVisible()) e.render(ctx, mx, my, delta);
        }
        
        // Обводка окна (если есть)
        if (hasBorder()) {
            if (hasHeader) {
                MIRenderer.drawRoundedHollowRect(ctx, windowX, windowY, windowWidth, getHeaderHeight(), 
                        borderColor, borderWidth, radius, true, true, false, false);
            } else {
                MIRenderer.drawRoundedHollowRect(ctx, windowX, windowY, windowWidth, windowHeight, 
                        borderColor, borderWidth, radius);
            }
        }
    }
    
    // ==================== ПЕРЕТАСКИВАНИЕ ====================
    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (button == 0) {
            if (isCloseButtonClicked(mx, my)) {
                close();
                return true;
            }
            if (draggable && showHeader && isMouseOverHeader(mx, my)) {
                dragging = true;
                dragOffsetX = (int)mx - windowX;
                dragOffsetY = (int)my - windowY;
                return true;
            }
        }
        
        // Передаём события элементам окна
        for (MIElement e : elements) {
            if (e.isVisible() && e.mouseClicked(mx, my, button)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void mouseMoved(double mx, double my) {
        if (dragging) {
            int newX = (int)mx - dragOffsetX;
            int newY = (int)my - dragOffsetY;
            int deltaX = newX - windowX;
            int deltaY = newY - windowY;
            windowX = Math.max(0, Math.min(newX, screenWidth - windowWidth));
            windowY = Math.max(0, Math.min(newY, screenHeight - windowHeight));
            
            for (MIElement e : elements) {
                e.setPosition(e.x + deltaX, e.y + deltaY);
            }
        }
        
        for (MIElement e : elements) {
            if (e.isVisible()) e.mouseMoved(mx, my);
        }
    }
    
    @Override
    public boolean mouseReleased(double mx, double my, int button) {
        dragging = false;
        for (MIElement e : elements) {
            if (e.isVisible() && e.mouseReleased(mx, my, button)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontal, double vertical) {
        for (MIElement e : elements) {
            if (e.isVisible() && e.mouseScrolled(mouseX, mouseY, horizontal, vertical)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (MIElement e : elements) {
            if (e.isVisible() && e.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean charTyped(char chr, int modifiers) {
        for (MIElement e : elements) {
            if (e.isVisible() && e.charTyped(chr, modifiers)) {
                return true;
            }
        }
        return false;
    }
    
    // ==================== НАСТРОЙКИ ====================
    public void setShowHeader(boolean show) { this.showHeader = show; }
    public void setDraggable(boolean drag) { this.draggable = drag; }
    public void setWindowTitle(String title) { this.windowTitle = title; }
}