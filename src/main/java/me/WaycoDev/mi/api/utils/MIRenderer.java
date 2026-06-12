package me.WaycoDev.mi.api.utils;

import net.minecraft.client.gui.DrawContext;

public class MIRenderer {
    
    public static void drawRoundedRect(DrawContext ctx, int x, int y, int w, int h, int color, int radius) {
        drawRoundedRect(ctx, x, y, w, h, color, radius, true, true, true, true);
    }
    
    public static void drawRoundedRect(DrawContext ctx, int x, int y, int w, int h, int color, int radius,
                                        boolean topLeft, boolean topRight, boolean bottomLeft, boolean bottomRight) {
        ctx.fill(x + radius, y, x + w - radius, y + h, color);
        ctx.fill(x, y + radius, x + w, y + h - radius, color);
        
        if (topLeft) ctx.fill(x, y, x + radius, y + radius, color);
        if (topRight) ctx.fill(x + w - radius, y, x + w, y + radius, color);
        if (bottomLeft) ctx.fill(x, y + h - radius, x + radius, y + h, color);
        if (bottomRight) ctx.fill(x + w - radius, y + h - radius, x + w, y + h, color);
    }
    
    public static void drawHollowRect(DrawContext ctx, int x, int y, int w, int h, int color, int radius) {
        ctx.fill(x + radius, y, x + w - radius, y + 1, color);
        ctx.fill(x + radius, y + h - 1, x + w - radius, y + h, color);
        ctx.fill(x, y + radius, x + 1, y + h - radius, color);
        ctx.fill(x + w - 1, y + radius, x + w, y + h - radius, color);
        ctx.fill(x, y, x + 1, y + 1, color);
        ctx.fill(x + w - 1, y, x + w, y + 1, color);
        ctx.fill(x, y + h - 1, x + 1, y + h, color);
        ctx.fill(x + w - 1, y + h - 1, x + w, y + h, color);
    }
}