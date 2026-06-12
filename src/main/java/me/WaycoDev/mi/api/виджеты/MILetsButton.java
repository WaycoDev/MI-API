package me.WaycoDev.mi.api;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class MILetsButton extends MIElement {
    private Runnable action;
    private Identifier icon;
    private int iconSize = 0;

    public MILetsButton(int x, int y, int size, MITheme theme) {
        super(x, y, size, size, theme);
    }

    public void setIcon(Identifier icon, int size) {
        this.icon = icon;
        this.iconSize = size;
    }

    public void setAction(Runnable action) {
        this.action = action;
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        float scale = pressed ? theme.getClickScale() : 1.0f;
        int drawSize = (int)(width * scale);
        int offset = (width - drawSize) / 2;

        ctx.fill(x + offset, y + offset, x + offset + drawSize, y + offset + drawSize, theme.getAccentColor());

        if (icon != null && iconSize > 0) {
            int iconDrawSize = (int)(iconSize * scale);
            int iconX = x + offset + (drawSize - iconDrawSize) / 2;
            int iconY = y + offset + (drawSize - iconDrawSize) / 2;
            ctx.drawTexture(RenderLayer::getGuiTextured, icon, 
                iconX, iconY, 0, 0, iconDrawSize, iconDrawSize, iconDrawSize, iconDrawSize);
        }
        
        drawBorder(ctx);
    }

    @Override
    protected void onClick() {
        if (action != null) action.run();
    }
}