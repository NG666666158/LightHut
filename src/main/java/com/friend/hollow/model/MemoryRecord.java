package com.friend.hollow.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 回忆领域对象（内存存储）。
 */
public class MemoryRecord {

    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private MemoryCategory category;
    private LocalDate eventDate;
    private List<String> companions = new ArrayList<>();
    private Instant createdAt;
    private MemoryLayout layout;

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

    public MemoryCategory getCategory() {
        return category;
    }

    public void setCategory(MemoryCategory category) {
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public MemoryLayout getLayout() {
        return layout;
    }

    public void setLayout(MemoryLayout layout) {
        this.layout = layout;
    }
}
