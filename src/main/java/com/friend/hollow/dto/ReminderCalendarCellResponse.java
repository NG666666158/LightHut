package com.friend.hollow.dto;

/**
 * 月历单格（空格或某日）。
 */
public class ReminderCalendarCellResponse {

    /** 当月第几天，null 表示空白占位 */
    private Integer day;
    /** 有日期时 yyyy-MM-dd，便于前端点击加载 */
    private String date;
    private boolean inMonth;
    private boolean today;
    private boolean hasReminder;

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isInMonth() {
        return inMonth;
    }

    public void setInMonth(boolean inMonth) {
        this.inMonth = inMonth;
    }

    public boolean isToday() {
        return today;
    }

    public void setToday(boolean today) {
        this.today = today;
    }

    public boolean isHasReminder() {
        return hasReminder;
    }

    public void setHasReminder(boolean hasReminder) {
        this.hasReminder = hasReminder;
    }
}
