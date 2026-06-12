package me.WaycoDev.mi.api;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import java.util.*;

public abstract class MIScreen extends Screen {
    protected final List<MIElement> elements = new ArrayList<>();
    protected MITheme theme;
    protected int screenWidth, screenHeight;

    protected MIScreen(Text title, MITheme theme) {
        super(title);
        this.theme = theme;
    }

    @Override
    protected void init() {
        super.init();
        this.screenWidth = width;
        this.screenHeight = height;
        elements.clear();
        buildUI();
    }

    protected abstract void buildUI();

    protected void addElement(MIElement e) {
        elements.add(e);
    }

    // ==================== НОВЫЕ МЕТОДЫ УДАЛЕНИЯ ====================
    public void removeElement(MIElement element) {
        elements.remove(element);
    }

    public void clearElements() {
        elements.clear();
    }

    public void removeElementById(int index) {
        if (index >= 0 && index < elements.size()) {
            elements.remove(index);
        }
    }

    public List<MIElement> getElements() {
        return Collections.unmodifiableList(elements);
    }
    // ==============================================================

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        renderBackground(ctx, mx, my, delta);
        for (MIElement e : elements) {
            if (e.isVisible()) {
                e.render(ctx, mx, my, delta);
            }
        }
        super.render(ctx, mx, my, delta);
    }

    // ==================== ПРОБРОС СОБЫТИЙ ====================
    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
        for (MIElement e : elements) {
            e.mouseMoved(mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        for (MIElement e : elements) {
            if (e.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (super.mouseReleased(mouseX, mouseY, button)) {
            return true;
        }
        for (MIElement e : elements) {
            if (e.mouseReleased(mouseX, mouseY, button)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
            return true;
        }
        for (MIElement e : elements) {
            if (e.isMouseOver(mouseX, mouseY)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)) {
            return true;
        }
        for (MIElement e : elements) {
            if (e.isMouseOver(mouseX, mouseY)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (super.charTyped(chr, modifiers)) {
            return true;
        }
        for (MIElement e : elements) {
            if (e.charTyped(chr, modifiers)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        for (MIElement e : elements) {
            if (e.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}