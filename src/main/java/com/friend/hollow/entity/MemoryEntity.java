package com.friend.hollow.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "memories")
public class MemoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 4000)
    private String description;

    @Column(nullable = false, length = 2000)
    private String imageUrl;

    @Column(length = 32)
    private String categoryCode;

    private LocalDate eventDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "memory_companions", joinColumns = @JoinColumn(name = "memory_id"))
    @Column(name = "companion", length = 120)
    private List<String> companions = new ArrayList<>();

    private Instant createdAt;

    @Column(length = 16)
    private String layoutCode;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getCategoryCode() { return categoryCode; }
    public void setCategoryCode(String categoryCode) { this.categoryCode = categoryCode; }
    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
    public List<String> getCompanions() { return companions; }
    public void setCompanions(List<String> companions) { this.companions = companions; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public String getLayoutCode() { return layoutCode; }
    public void setLayoutCode(String layoutCode) { this.layoutCode = layoutCode; }
}
