package me.WaycoDev.mi.api;

import com.mojang.authlib.GameProfile;
import me.WaycoDev.mi.api.cache.MIAvatarCache;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MIAvatar extends MIElement {
    private final UUID playerId;
    private volatile Identifier currentSkin;
    private final float u1, v1, u2, v2;
    private int radius;

    public MIAvatar(int x, int y, int size, UUID playerId,
                    float u1, float v1, float u2, float v2,
                    MITheme theme) {
        super(x, y, size, size, theme);
        this.playerId = playerId;
        this.u1 = u1;
        this.v1 = v1;
        this.u2 = u2;
        this.v2 = v2;
        this.radius = theme.getRadius();
        loadSkin();
    }

    private void loadSkin() {
        Identifier cached = MIAvatarCache.get(playerId);
        if (cached != null) {
            this.currentSkin = cached;
            return;
        }

        CompletableFuture.supplyAsync(() -> {
            try {
                MinecraftClient client = MinecraftClient.getInstance();
                GameProfile profile = new GameProfile(playerId, null);
                CompletableFuture<Optional<SkinTextures>> future = client.getSkinProvider().fetchSkinTextures(profile);
                Optional<SkinTextures> optionalSkin = future.join();

                if (optionalSkin.isPresent() && optionalSkin.get().texture() != null) {
                    return optionalSkin.get().texture();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Identifier.ofVanilla("textures/entity/steve.png");
        }).thenAcceptAsync(skinId -> {
            if (skinId != null) {
                MIAvatarCache.put(playerId, skinId);
                currentSkin = skinId;
            }
        }, MinecraftClient.getInstance()::execute);
    }

    public void setRadius(int radius) {
        this.radius = Math.max(0, Math.min(radius, width / 2));
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        if (currentSkin == null) return;

        ctx.drawTexture(RenderLayer::getGuiTextured, currentSkin,
                x, y,
                (int)u1, (int)v1,
                (int)(u2 - u1), (int)(v2 - v1),
                width, height,
                64, 64);
        
        drawBorder(ctx);
    }
}