package me.WaycoDev.mi.api;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class MILabel extends MIElement {
    private String text;
    private int color;
    private boolean centered = true;

    public MILabel(int x, int y, String text, MITheme theme) {
        super(x, y, 0, 0, theme);
        this.text = text;
        this.color = theme.getTextPrimary();
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        var tr = MinecraftClient.getInstance().textRenderer;
        this.width = tr.getWidth(text);
        this.height = tr.fontHeight;

        var matrices = ctx.getMatrices();
        matrices.push();
        matrices.scale(textScale, textScale, 1.0f);

        int drawX = centered ? (int)(x / textScale) - width / 2 : (int)(x / textScale);
        int drawY = (int)(y / textScale);

        ctx.drawText(tr, Text.literal(text), drawX, drawY, color, false);
        matrices.pop();
        
        drawBorder(ctx);
    }

    public void setText(String t) { text = t; }
    public void setColor(int c) { color = c; }
    public void setCentered(boolean c) { centered = c; }
    @Override protected void onClick() {}
}