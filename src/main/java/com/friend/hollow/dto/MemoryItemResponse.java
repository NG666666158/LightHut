package com.friend.hollow.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * 单条「回忆」卡片数据（与星光墙前端字段对齐）。
 */
public class MemoryItemResponse {

    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    /** 分类编码：TRAVEL / DAILY / BIRTHDAY / SPORT */
    private String category;
    /** 分类中文展示，如「旅行」 */
    private String categoryLabel;
    private LocalDate eventDate;
    private List<String> companions;
    private Instant createdAt;
    /** HERO | MEDIUM | COMPACT */
    private String layout;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryLabel() {
        return categoryLabel;
    }

    public void setCategoryLabel(String categoryLabel) {
        this.categoryLabel = categoryLabel;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }
}
