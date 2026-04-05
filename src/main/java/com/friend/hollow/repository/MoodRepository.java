package com.friend.hollow.repository;

import com.friend.hollow.dto.MoodEntryResponse;

import java.util.List;

public interface MoodRepository {

    void save(MoodEntryResponse entry);

    List<MoodEntryResponse> findAll();

    int size();

    void removeOldest();
}
