package com.friend.hollow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

/**
 * 创建回忆（JSON 体，在已有图片 URL 时使用）。
 */
public class MemoryCreateRequest {

    @NotBlank(message = "标题不能为空")
    @Size(max = 120, message = "标题请控制在 120 字以内")
    private String title;
    @Size(max = 600, message = "描述请控制在 600 字以内")
    private String description;
    @NotBlank(message = "分类不能为空")
    private String category;
    private LocalDate eventDate;
    private List<String> companions;
    /** HERO | MEDIUM | COMPACT */
    @Size(max = 20, message = "布局参数过长")
    private String layout;
    @NotBlank(message = "请先上传图片或填写 imageUrl")
    private String imageUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public List<String> getCompanions() {
        return companions;
    }

    public void setCompanions(List<String> companions) {
        this.companions = companions;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
