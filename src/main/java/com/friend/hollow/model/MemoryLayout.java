package com.friend.hollow.model;

import java.util.Arrays;

/**
 * 卡片布局样式（与前端三种卡片对应）。
 */
public enum MemoryLayout {
    HERO,
    MEDIUM,
    COMPACT;

    public static MemoryLayout fromCode(String raw) {
        if (raw == null || raw.isBlank()) {
            return COMPACT;
        }
        String t = raw.trim().toUpperCase();
        return Arrays.stream(values())
                .filter(v -> v.name().equals(t))
                .findFirst()
                .orElse(COMPACT);
    }
}
