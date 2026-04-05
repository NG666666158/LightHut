package com.friend.hollow.repository.impl;

import com.friend.hollow.model.MemoryRecord;
import com.friend.hollow.repository.MemoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryMemoryRepository implements MemoryRepository {

    private final List<MemoryRecord> store = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);

    @Override
    public synchronized void save(MemoryRecord record) {
        if (record.getId() != null) {
            for (int i = 0; i < store.size(); i++) {
                if (record.getId().equals(store.get(i).getId())) {
                    store.set(i, record);
                    return;
                }
            }
        }
        if (record.getId() == null) {
            record.setId(nextId.getAndIncrement());
        }
        store.add(record);
    }

    @Override
    public synchronized List<MemoryRecord> findAll() {
        return new ArrayList<>(store);
    }

    @Override
    public synchronized Optional<MemoryRecord> findById(long id) {
        return store.stream().filter(m -> m.getId() != null && m.getId() == id).findFirst();
    }

    @Override
    public synchronized boolean deleteById(long id) {
        return store.removeIf(m -> m.getId() != null && m.getId() == id);
    }
}
