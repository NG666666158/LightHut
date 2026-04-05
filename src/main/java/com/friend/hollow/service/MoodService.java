package com.friend.hollow.service;

import com.friend.hollow.dto.MoodCreateRequest;
import com.friend.hollow.dto.MoodEntryResponse;
import com.friend.hollow.dto.MoodRecentResponse;

public interface MoodService {

    MoodEntryResponse add(MoodCreateRequest request);

    MoodRecentResponse recent(int limit);
}
