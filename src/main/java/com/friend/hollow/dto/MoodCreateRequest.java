package com.friend.hollow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 记录此刻心情（请求体）。
 */
public class MoodCreateRequest {

    @NotBlank(message = "请填写心情内容")
    @Size(max = 500, message = "内容请控制在 500 字以内")
    private String content;
    /** 可选：如 calm / happy / tired */
    @Size(max = 32, message = "心情标签过长")
    private String mood;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }
}
