package com.friend.hollow.service.impl;

import com.friend.hollow.repository.impl.InMemoryReminderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ReminderServiceImplTest {

    @Test
    void parseTimeRequiredRejectsInvalidPattern() {
        ReminderServiceImpl service = new ReminderServiceImpl(new InMemoryReminderRepository());

        Assertions.assertThrows(IllegalArgumentException.class, () -> service.parseTimeRequired("25:88"));
    }
}
