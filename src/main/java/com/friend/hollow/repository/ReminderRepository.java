package com.friend.hollow.repository;

import com.friend.hollow.model.ReminderRecord;

import java.util.List;
import java.util.Optional;

public interface ReminderRepository {

    void save(ReminderRecord record);

    List<ReminderRecord> findAll();

    Optional<ReminderRecord> findById(long id);

    long count();
}
