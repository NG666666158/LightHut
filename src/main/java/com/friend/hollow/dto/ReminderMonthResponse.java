package com.friend.hollow.dto;

import java.util.List;

public class ReminderMonthResponse {

    private int year;
    private int month;
    /** 如「2026年 4月」 */
    private String monthTitle;
    /** 副标题，如「今天 4月2日，一个适合慢下来的周四」 */
    private String subtitle;
    private List<ReminderCalendarCellResponse> cells;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getMonthTitle() {
        return monthTitle;
    }

    public void setMonthTitle(String monthTitle) {
        this.monthTitle = monthTitle;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public List<ReminderCalendarCellResponse> getCells() {
        return cells;
    }

    public void setCells(List<ReminderCalendarCellResponse> cells) {
        this.cells = cells;
    }
}
