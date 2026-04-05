package com.friend.hollow.model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 提醒领域对象（内存存储）。
 */
public class ReminderRecord {

    private Long id;
    private LocalDate remindDate;
    private LocalTime remindTime;
    private String title;
    private String note;
    private String icon;
    private boolean done;
    private Instant createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getRemindDate() {
        return remindDate;
    }

    public void setRemindDate(LocalDate remindDate) {
        this.remindDate = remindDate;
    }

    public LocalTime getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(LocalTime remindTime) {
        this.remindTime = remindTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
