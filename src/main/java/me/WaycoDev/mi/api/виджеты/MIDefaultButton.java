package me.WaycoDev.mi.api;

import me.WaycoDev.mi.api.utils.MIRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MIDefaultButton extends MIElement {
    private String text;
    private int textColor;
    private int backgroundColor = -1;
    private Runnable action;
    
    private Identifier icon;
    private int iconSize = 0;
    private int iconPadding = 4;

    public MIDefaultButton(int x, int y, int w, int h, String text, MITheme theme) {
        super(x, y, w, h, theme);
        this.text = text;
        this.textColor = theme.getTextPrimary();
    }

    public void setIcon(Identifier icon, int size) {
        this.icon = icon;
        this.iconSize = size;
    }

    public void setIconPadding(int padding) {
        this.iconPadding = padding;
    }

    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
    }

    public void setTextColor(int color) {
        this.textColor = color;
    }

    public void setAction(Runnable action) {
        this.action = action;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        if (backgroundColor != -1) {
            MIRenderer.drawRoundedRect(ctx, x, y, width, height, backgroundColor, theme.getRadius());
        }

        if (hovered) {
            int hover = MITheme.withAlpha(theme.getHoverColor(), theme.getHoverAlpha());
            MIRenderer.drawRoundedRect(ctx, x, y, width, height, hover, theme.getRadius());
        }

        var tr = MinecraftClient.getInstance().textRenderer;
        int textX = x + 8;
        
        if (icon != null && iconSize > 0) {
            int iconY = y + (height - iconSize) / 2;
            ctx.drawTexture(RenderLayer::getGuiTextured, icon, 
                x + 8, iconY, 0, 0, iconSize, iconSize, iconSize, iconSize);
            textX += iconSize + iconPadding;
        }

        int textY = y + (height - tr.fontHeight) / 2;
        ctx.drawText(tr, Text.literal(text), textX, textY, textColor, false);
        
        drawBorder(ctx);
    }

    @Override
    protected void onClick() {
        if (action != null) action.run();
    }
}