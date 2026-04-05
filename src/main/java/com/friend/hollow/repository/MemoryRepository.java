package com.friend.hollow.repository;

import com.friend.hollow.model.MemoryRecord;

import java.util.List;
import java.util.Optional;

public interface MemoryRepository {

    void save(MemoryRecord record);

    List<MemoryRecord> findAll();

    Optional<MemoryRecord> findById(long id);

    boolean deleteById(long id);
}
