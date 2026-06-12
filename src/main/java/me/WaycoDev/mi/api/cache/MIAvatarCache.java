package me.WaycoDev.mi.api.cache;

import net.minecraft.util.Identifier;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MIAvatarCache {
    private static final Map<UUID, Identifier> CACHE = new ConcurrentHashMap<>();

    public static Identifier get(UUID playerId) {
        return CACHE.get(playerId);
    }

    public static void put(UUID playerId, Identifier texture) {
        CACHE.put(playerId, texture);
    }

    public static boolean contains(UUID playerId) {
        return CACHE.containsKey(playerId);
    }

    public static void remove(UUID playerId) {
        CACHE.remove(playerId);
    }

    public static void clear() {
        CACHE.clear();
    }
}