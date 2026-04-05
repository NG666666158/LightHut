package com.friend.hollow.dto;

import java.util.List;

public class ReminderDayResponse {

    private String date;
    private int totalCount;
    private List<ReminderItemResponse> items;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<ReminderItemResponse> getItems() {
        return items;
    }

    public void setItems(List<ReminderItemResponse> items) {
        this.items = items;
    }
}
