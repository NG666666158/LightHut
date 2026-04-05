package com.friend.hollow.model;

import java.util.Arrays;

/**
 * 星光墙回忆分类（与前端 Tab 对应）。
 */
public enum MemoryCategory {
    TRAVEL("TRAVEL", "旅行"),
    DAILY("DAILY", "日常"),
    BIRTHDAY("BIRTHDAY", "庆生"),
    SPORT("SPORT", "运动");

    private final String code;
    private final String label;

    MemoryCategory(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static MemoryCategory fromCode(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        String t = raw.trim().toUpperCase();
        return Arrays.stream(values())
                .filter(c -> c.code.equalsIgnoreCase(t))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("未知分类: " + raw));
    }
}
