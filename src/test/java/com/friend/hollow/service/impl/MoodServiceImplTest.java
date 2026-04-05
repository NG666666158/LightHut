package com.friend.hollow.service.impl;

import com.friend.hollow.dto.MoodCreateRequest;
import com.friend.hollow.repository.impl.InMemoryMoodRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MoodServiceImplTest {

    @Test
    void addRejectsBlankContent() {
        MoodServiceImpl service = new MoodServiceImpl(new InMemoryMoodRepository());
        MoodCreateRequest request = new MoodCreateRequest();
        request.setContent("   ");
        request.setMood("happy");

        Assertions.assertThrows(IllegalArgumentException.class, () -> service.add(request));
    }
}
