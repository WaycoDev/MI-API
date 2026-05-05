# MI-API
MI API – interfaces that don't look like they did in 2011
[![GitHub Release](https://img.shields.io/github/v/release/WaycoDev/MI-API)](https://github.com/WaycoDev/MI-API/releases)
[![Fabric](https://img.shields.io/badge/mod%20loader-Fabric-orange)](https://fabricmc.net/)
# 🧩 Modern Interface API (MI API) — Create modern GUIs in Minecraft

**MI API** is an independent client-side library for Fabric that allows you to create windowed interfaces with theming, scrolling, and full input control**, without relying on vanilla Minecraft's hacky `guiScale`.

* **Minimalism:** Runs on the barebones Fabric Loader, **Fabric API is not required**.
* **Power:** Ready-made widgets (`MIWindow`, `MIInputField`, buttons), input events, scrolling.
* **Sharpness:** Uses absolute pixels (thanks to the `Window` mixin), your UI is always perfectly sharp.

* * *

## 📥 Installation

### For users
1. Install **Fabric Loader** (>=0.16.0) for Minecraft 1.21.4.
2. Download `mi-api-1.0.0.jar` from the [Releases](#) section.
3. Place the file in the `mods/` folder and run the game.

### For developers (integrating into your mod)

#### Gradle (Fabric)
```gradle
repositories {
maven {
name = "GitHubPackages"
url = "https://maven.pkg.github.com/WaycoDev/MI-API"
}
}

dependencies {
modImplementation "com.vivid.mi:mi-api:1.0.0"
}
```

#### Maven
```xml
<repository>
<id>github-mi-api</id>
<url>https://maven.pkg.github.com/WaycoDev/MI-API</url>
</repository>

<dependency>
<groupId>com.vivid.mi</groupId>
<artifactId>mi-api</artifactId>
<version>1.0.0</version>
</dependency>
```

> **Note:** Replace `WaycoDev/MI-API` with the actual path to your repository if it differs.

* * *

## 🚀 Quick Start: Your First Window

```java
public class DemoWindow extends MIWindow {
private MILabel label;
private MIDefaultButton button;

public DemoWindow(MITheme theme) {
super(Text.literal("Hello, world!"), theme, 300, 150);
}

@Override
protected void buildWindowUI() {
label = new MILabel(150, 50, "Welcome to the MI API!", theme);
button = new MIDefaultButton(100, 90, 100, 20, "Close", theme);
button.setAction(() -> close());

addWindowElement(label);
addWindowElement(button);
}
}

// Somewhere in your client code:
MITheme dark = new MITheme();
MinecraftClient.getInstance().setScreen(new DemoWindow(dark));
```

* * *

## 🧬 Architecture and Components

| Class | Role |
|-------|------|
| `MITheme` | Central repository of all colors, radii, and animations. |
| `MIScreen` | Basic screen. Manages the `MIElement` list and event routing. |
| `MIWindow` | A full-fledged window with a title bar, buttons (`red` / `yellow` / `green`), and **auto-scrolling**. |
| `MIElement` | Abstract widget. All other elements inherit from it. |
| `MIRenderer` | Drawing utilities: rounded rectangles, gradients, borders. |

### Available Widgets

* **`MIDefaultButton`** — A standard button with a hover and text.
* **`MILetsButton`** — Green accent button.
* **`MIInputField`** — Input field with cursor, arrows, and `Backspace`.
* **`MILabel`** — Centered and scalable text.
* **`MITextBackground`** — Panel with background (for grouping widgets).
* **`MIScrollPanel`** — Vertical scrolling container (built into `MIWindow`).

* * *

## 🎨 Theming: change the appearance in a second

```java
MITheme light = new MITheme();
light.setPageBg(0xFFFFFFFF); // white background
light.setTextPrimary(0xFF000000); // black text
light.setAccentColor(0xFF0078D7); // blue accent
light.setRadius(12); // increased roundness
```

Available parameters (all with getters and setters):

| Parameter | Type | Default | Description |
|----------|-------------|-----------|
| `pageBg` | `int` | `0xFF17181C` | Page Background |
| `surfaceBg` | `int` | `0xFF262930` | Surface Background |
| `headerBg` | `int` | `0xFF101114` | Header Background |
| `textPrimary` | `int` | `0xFFE0E0E0` | Primary Text |
| `textMuted` | `int` | `0xFF8A8D93` | Dimmed text |
| `accentColor` | `int` | `0xFF04FA00` | Accent color (buttons, borders) |
| `radius` | `int` | `8` | Corner rounding (pixels) |
| `headerHeight` | `int` | `28` | Window title bar height |

* * *

## ⌨️ Advanced Window (`MIWindow`) and its features

* ✅ **Draggable** by title bar (with screen boundaries limitation).
* ✅ **Automatic scrolling** — a scrollbar appears when the content doesn't fit.
* ✅ **Modal** — background dimming (`setModal(true)` method).
* ✅ **Callbacks for title buttons**: `setOnClose(...)`, `setOnMinimize(...)`, `setOnMaximize(...)`.
* ✅ **Built-in maximize** (green button).

### Complex window example

```java
MIWindow settings = new MIWindow(Text.literal("Settings"), theme, 500, 400) {
@Override
protected void buildWindowUI() {
// Add elements...
}
};
settings.setModal(true);
settings.setOnClose(() -> saveConfig());
settings.setShowCloseButtons(true);
settings.setAutoScroll(true);
```

* * *

## ⚙️ Mixin: Why Your UI Is Always Crisp

The library uses exactly **one** mixin, which forces 1:1 scaling for the entire client.

```java
@Mixin(W)
