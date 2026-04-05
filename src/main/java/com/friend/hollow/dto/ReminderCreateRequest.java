package com.friend.hollow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class ReminderCreateRequest {

    @NotBlank(message = "请填写提醒标题")
    @Size(max = 80, message = "标题请控制在 80 字以内")
    private String title;
    @Size(max = 300, message = "备注请控制在 300 字以内")
    private String note;
    /** HH:mm */
    @NotBlank(message = "请填写提醒时间（HH:mm）")
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "时间格式应为 HH:mm，例如 14:00")
    private String time;
    private LocalDate remindDate;
    @Size(max = 32, message = "图标标识过长")
    private String icon;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public LocalDate getRemindDate() {
        return remindDate;
    }

    public void setRemindDate(LocalDate remindDate) {
        this.remindDate = remindDate;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
