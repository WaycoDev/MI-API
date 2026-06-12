package me.WaycoDev.mi.api;

import net.minecraft.util.Identifier;
import java.util.ArrayList;
import java.util.List;

public class MITabButton extends MIDefaultButton {
    private List<MIUniPanel> linkedUniverses = new ArrayList<>();
    private Runnable globalSwitchAction = null;
    private boolean destroyOnGlobalSwitch = false;
    
    // Для выбранного состояния
    private int selectedBgColor = -1;
    private int selectedTextColor = -1;
    private Identifier selectedIcon = null;
    private Identifier defaultIcon = null;
    private boolean isSelected = false;

    public MITabButton(int x, int y, int width, int height, String text, MITheme theme) {
        super(x, y, width, height, text, theme);
    }

    // ========== УПРАВЛЕНИЕ ВСЕЛЕННЫМИ ==========
    public void addLinkedUniverse(MIUniPanel universe) {
        linkedUniverses.add(universe);
        universe.setVisible(false);
    }

    public void setGlobalSwitch(Runnable action, boolean destroySelf) {
        this.globalSwitchAction = action;
        this.destroyOnGlobalSwitch = destroySelf;
    }

    // ========== НАСТРОЙКА ВЫБРАННОГО СОСТОЯНИЯ ==========
    public void setSelectedBgColor(int color) { this.selectedBgColor = color; }
    public void setSelectedTextColor(int color) { this.selectedTextColor = color; }
    public void setSelectedIcon(Identifier icon) { this.selectedIcon = icon; }
    public void setDefaultIcon(Identifier icon) { this.defaultIcon = icon; }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
        if (selected) {
            if (selectedBgColor != -1) setBackgroundColor(selectedBgColor);
            else setBackgroundColor(theme.getAccentColor());
            if (selectedTextColor != -1) setTextColor(selectedTextColor);
            if (selectedIcon != null) setIcon(selectedIcon, iconSize);
        } else {
            setBackgroundColor(-1);
            setTextColor(theme.getTextPrimary());
            if (defaultIcon != null) setIcon(defaultIcon, iconSize);
        }
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        // Сначала рендерим фон (обычный или выбранный)
        if (isSelected && selectedBgColor != -1) {
            MIRenderer.drawRoundedRect(ctx, x, y, width, height, selectedBgColor, theme.getRadius());
        } else if (backgroundColor != -1) {
            MIRenderer.drawRoundedRect(ctx, x, y, width, height, backgroundColor, theme.getRadius());
        }
        
        // Потом hover (только если не selected)
        if (hovered && !isSelected) {
            int hover = MITheme.withAlpha(theme.getHoverColor(), theme.getHoverAlpha());
            MIRenderer.drawRoundedRect(ctx, x, y, width, height, hover, theme.getRadius());
        }
        
        // Отрисовка иконки (с учётом selected)
        Identifier currentIcon = (isSelected && selectedIcon != null) ? selectedIcon : icon;
        if (currentIcon != null) {
            int iconY = y + (height - iconSize) / 2;
            ctx.drawTexture(RenderLayer::getGuiTextured, currentIcon, x + 8, iconY, 0, 0, iconSize, iconSize, iconSize, iconSize);
        }
        
        // Текст
        int currentTextColor = (isSelected && selectedTextColor != -1) ? selectedTextColor : textColor;
        int textX = x + (icon != null ? 8 + iconSize + iconPadding : 8);
        int textY = y + (height - tr.fontHeight) / 2;
        ctx.drawText(tr, Text.literal(text), textX, textY, currentTextColor, false);
        
        drawBorder(ctx);
    }

    @Override
    protected void onClick() {
        if (globalSwitchAction != null) {
            globalSwitchAction.run();
            if (destroyOnGlobalSwitch && this.parentTabPanel != null) {
                this.parentTabPanel.closeTab(this);
            }
        } else {
            for (MIUniPanel universe : linkedUniverses) {
                universe.setVisible(true);
            }
        }
    }
}