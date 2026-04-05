package com.friend.hollow.dto;

/**
 * 挚友打气站「今日拥抱传递」：真实累计值与展示文案（&gt;999 时为 999+）。
 */
public record HugTodayResponse(long count, String display) {
}
