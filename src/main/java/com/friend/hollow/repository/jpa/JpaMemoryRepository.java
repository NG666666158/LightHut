package com.friend.hollow.repository.jpa;

import com.friend.hollow.entity.MemoryEntity;
import com.friend.hollow.model.MemoryCategory;
import com.friend.hollow.model.MemoryLayout;
import com.friend.hollow.model.MemoryRecord;
import com.friend.hollow.repository.MemoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Primary
public class JpaMemoryRepository implements MemoryRepository {

    private final MemoryJpaDao dao;

    public JpaMemoryRepository(MemoryJpaDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional
    public void save(MemoryRecord record) {
        MemoryEntity e = new MemoryEntity();
        e.setTitle(record.getTitle());
        e.setDescription(record.getDescription());
        e.setImageUrl(record.getImageUrl());
        if (record.getCategory() != null) {
            e.setCategoryCode(record.getCategory().getCode());
        }
        e.setEventDate(record.getEventDate());
        e.setCompanions(record.getCompanions() != null ? new ArrayList<>(record.getCompanions()) : new ArrayList<>());
        e.setCreatedAt(record.getCreatedAt());
        e.setLayoutCode(record.getLayout() != null ? record.getLayout().name() : MemoryLayout.COMPACT.name());
        e = dao.save(e);
        record.setId(e.getId());
    }

    @Override
    public List<MemoryRecord> findAll() {
        return dao.findAll().stream().map(this::toRecord).collect(Collectors.toList());
    }

    @Override
    public Optional<MemoryRecord> findById(long id) {
        return dao.findById(id).map(this::toRecord);
    }

    @Override
    @Transactional
    public boolean deleteById(long id) {
        if (!dao.existsById(id)) {
            return false;
        }
        dao.deleteById(id);
        return true;
    }

    private MemoryRecord toRecord(MemoryEntity e) {
        MemoryRecord m = new MemoryRecord();
        m.setId(e.getId());
        m.setTitle(e.getTitle());
        m.setDescription(e.getDescription());
        m.setImageUrl(e.getImageUrl());
        if (e.getCategoryCode() != null) {
            m.setCategory(MemoryCategory.fromCode(e.getCategoryCode()));
        }
        m.setEventDate(e.getEventDate());
        m.setCompanions(e.getCompanions() != null ? new ArrayList<>(e.getCompanions()) : new ArrayList<>());
        m.setCreatedAt(e.getCreatedAt());
        m.setLayout(MemoryLayout.fromCode(e.getLayoutCode()));
        return m;
    }
}
