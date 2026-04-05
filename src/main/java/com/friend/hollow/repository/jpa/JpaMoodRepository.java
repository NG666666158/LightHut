package com.friend.hollow.repository.jpa;

import com.friend.hollow.dto.MoodEntryResponse;
import com.friend.hollow.entity.MoodEntity;
import com.friend.hollow.repository.MoodRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Primary
public class JpaMoodRepository implements MoodRepository {

    private final MoodJpaDao dao;

    public JpaMoodRepository(MoodJpaDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional
    public void save(MoodEntryResponse entry) {
        MoodEntity e = new MoodEntity();
        e.setContent(entry.getContent());
        e.setMood(entry.getMood());
        e.setCreatedAt(entry.getCreatedAt());
        e = dao.save(e);
        entry.setId(e.getId());
    }

    @Override
    public List<MoodEntryResponse> findAll() {
        return dao.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public int size() {
        return (int) dao.count();
    }

    @Override
    @Transactional
    public void removeOldest() {
        dao.findTopByOrderByCreatedAtAsc().ifPresent(dao::delete);
    }

    private MoodEntryResponse toDto(MoodEntity e) {
        MoodEntryResponse r = new MoodEntryResponse();
        r.setId(e.getId());
        r.setContent(e.getContent());
        r.setMood(e.getMood());
        r.setCreatedAt(e.getCreatedAt());
        return r;
    }
}
