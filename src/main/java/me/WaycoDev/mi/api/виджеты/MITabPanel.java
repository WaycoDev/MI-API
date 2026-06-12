package me.WaycoDev.mi.api;

import java.util.ArrayList;
import java.util.List;

public class MITabPanel extends MIUniPanel {
    private List<MITabButton> buttons = new ArrayList<>();
    private int activeIndex = -1;

    public MITabPanel(int x, int y, int width, int height, MITheme theme) {
        super(x, y, width, height, theme);
    }

    public void addTab(MITabButton button, MIUniPanel content) {
        button.addLinkedUniverse(content);
        buttons.add(button);
        this.addChild(button);
        if (activeIndex == -1) {
            setActiveTab(0);
        }
    }

    public void setActiveTab(int index) {
        if (activeIndex == index) return;
        if (activeIndex != -1) {
            buttons.get(activeIndex).setActive(false);
        }
        activeIndex = index;
        buttons.get(activeIndex).setActive(true);
        for (MIUniPanel universe : buttons.get(activeIndex).linkedUniverses) {
            universe.setVisible(true);
        }
    }

    public void closeTab(MITabButton button) {
        int index = buttons.indexOf(button);
        if (index != -1) {
            // Скрываем и удаляем кнопку и связанные вселенные
            button.setVisible(false);
            for (MIUniPanel universe : button.linkedUniverses) {
                universe.setVisible(false);
            }
            buttons.remove(index);
            // Если была активна — переключаем на другую
            if (activeIndex == index) {
                setActiveTab(0);
            }
        }
    }
}