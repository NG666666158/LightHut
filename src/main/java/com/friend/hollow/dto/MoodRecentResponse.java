package com.friend.hollow.dto;

import java.util.List;

public class MoodRecentResponse {

    private List<MoodEntryResponse> items;

    public List<MoodEntryResponse> getItems() {
        return items;
    }

    public void setItems(List<MoodEntryResponse> items) {
        this.items = items;
    }
}
