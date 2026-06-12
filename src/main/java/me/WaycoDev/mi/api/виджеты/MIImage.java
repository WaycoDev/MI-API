//MIImage пока что не работает
/*
package me.WaycoDev.mi.api;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class MIImage extends MIElement {
    private Identifier texture;
    private int imageWidth, imageHeight;

    public MIImage(int x, int y, int width, int height, Identifier texture, MITheme theme) {
        super(x, y, width, height, theme);
        this.texture = texture;
    }

    public MIImage(int x, int y, int width, int height, String filePath, MITheme theme) {
        super(x, y, width, height, theme);
        loadImageFromFile(filePath);
    }

    public void loadImageFromFile(String filePath) {
        CompletableFuture.runAsync(() -> {
            try {
                File file = new File(filePath);
                if (!file.exists()) return;

                BufferedImage image = ImageIO.read(file);
                if (image == null) return;

                this.imageWidth = image.getWidth();
                this.imageHeight = image.getHeight();

                // Используем RGBA формат
                NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, imageWidth, imageHeight, false);

                for (int y = 0; y < imageHeight; y++) {
                    for (int x = 0; x < imageWidth; x++) {
                        // setColorArgb работает напрямую с ARGB
                        nativeImage.setColorArgb(x, y, image.getRGB(x, y));
                    }
                }

                NativeImageBackedTexture nativeTexture = new NativeImageBackedTexture(nativeImage);

                // registerDynamicTexture принимает ТОЛЬКО один аргумент
                Identifier dynamicTexture = MinecraftClient.getInstance().getTextureManager()
                        .registerDynamicTexture(nativeTexture);

                MinecraftClient client = MinecraftClient.getInstance();
                client.execute(() -> this.texture = dynamicTexture);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        if (texture == null) return;

        // drawTexture в 1.21.4
        ctx.drawTexture(texture, x, y, 0, 0, width, height, width, height);
    }
}
*/