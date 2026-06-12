package me.WaycoDev.mi.api;

public class MITheme {
    private int pageBg = 0xFF17181C;
    private int surfaceBg = 0xFF262930;
    private int headerBg = 0xFF101114;
    private int textPrimary = 0xFFE0E0E0;
    private int textMuted = 0xFF8A8D93;
    private int accentColor = 0xFF04FA00;
    private int hoverColor = 0xFF414858;
    
    // ← ДОБАВЛЕНО ДЛЯ MISwitcher
    private int switcherOffBg = 0xFF666666;
    private int switcherOnBg = 0xFF00AA00;
    private int switcherKnob = 0xFFFFFFFF;
    private int switcherOffText = 0xFFAAAAAA;
    private int switcherOnText = 0xFFFFFFFF;
    
    private int radius = 8;
    private int padding = 8;
    private int headerHeight = 28;
    
    private int animationSpeed = 200;
    private float hoverAlpha = 0.15f;
    private float clickScale = 0.8f;
    
    private int redButtonColor = 0xFFFF5F56;
    private int yellowButtonColor = 0xFFFFBD2E;
    private int greenButtonColor = 0xFF27C93F;
    private int closeButtonSize = 12;
    
    public MITheme() {}
    
    // ... существующие геттеры/сеттеры (pageBg, surfaceBg, headerBg и т.д.) ...
    // (они у тебя уже есть, я их не трогаю)
    
    // ↓ НОВЫЕ ГЕТТЕРЫ/СЕТТЕРЫ ДЛЯ MISwitcher
    public int getSwitcherOffBg() { return switcherOffBg; }
    public void setSwitcherOffBg(int c) { this.switcherOffBg = c; }
    
    public int getSwitcherOnBg() { return switcherOnBg; }
    public void setSwitcherOnBg(int c) { this.switcherOnBg = c; }
    
    public int getSwitcherKnob() { return switcherKnob; }
    public void setSwitcherKnob(int c) { this.switcherKnob = c; }
    
    public int getSwitcherOffText() { return switcherOffText; }
    public void setSwitcherOffText(int c) { this.switcherOffText = c; }
    
    public int getSwitcherOnText() { return switcherOnText; }
    public void setSwitcherOnText(int c) { this.switcherOnText = c; }
    
    // ... остальные твои методы (getRedButtonColor, withAlpha и т.д.) остаются без изменений
}