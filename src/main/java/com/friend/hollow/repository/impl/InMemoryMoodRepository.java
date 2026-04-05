package com.friend.hollow.repository.impl;

import com.friend.hollow.dto.MoodEntryResponse;
import com.friend.hollow.repository.MoodRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryMoodRepository implements MoodRepository {

    private final List<MoodEntryResponse> store = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);

    @Override
    public synchronized void save(MoodEntryResponse entry) {
        if (entry.getId() == null) {
            entry.setId(nextId.getAndIncrement());
        }
        store.add(entry);
    }

    @Override
    public synchronized List<MoodEntryResponse> findAll() {
        return new ArrayList<>(store);
    }

    @Override
    public synchronized int size() {
        return store.size();
    }

    @Override
    public synchronized void removeOldest() {
        store.stream().min(Comparator.comparing(MoodEntryResponse::getCreatedAt)).ifPresent(store::remove);
    }
}
