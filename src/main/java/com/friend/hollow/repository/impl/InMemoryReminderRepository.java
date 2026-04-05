package com.friend.hollow.repository.impl;

import com.friend.hollow.model.ReminderRecord;
import com.friend.hollow.repository.ReminderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryReminderRepository implements ReminderRepository {

    private final List<ReminderRecord> store = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);

    @Override
    public synchronized void save(ReminderRecord record) {
        if (record.getId() == null) {
            record.setId(nextId.getAndIncrement());
            store.add(record);
            return;
        }
        Optional<ReminderRecord> existing = findById(record.getId());
        if (existing.isPresent()) {
            ReminderRecord r = existing.get();
            r.setRemindDate(record.getRemindDate());
            r.setRemindTime(record.getRemindTime());
            r.setTitle(record.getTitle());
            r.setNote(record.getNote());
            r.setIcon(record.getIcon());
            r.setDone(record.isDone());
            r.setCreatedAt(record.getCreatedAt());
        } else {
            store.add(record);
        }
    }

    @Override
    public synchronized List<ReminderRecord> findAll() {
        return new ArrayList<>(store);
    }

    @Override
    public synchronized Optional<ReminderRecord> findById(long id) {
        return store.stream().filter(x -> x.getId() != null && x.getId() == id).findFirst();
    }

    @Override
    public synchronized long count() {
        return store.size();
    }
}
