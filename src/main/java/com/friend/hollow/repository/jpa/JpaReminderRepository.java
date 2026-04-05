package com.friend.hollow.repository.jpa;

import com.friend.hollow.entity.ReminderEntity;
import com.friend.hollow.model.ReminderRecord;
import com.friend.hollow.repository.ReminderRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Primary
public class JpaReminderRepository implements ReminderRepository {

    private final ReminderJpaDao dao;

    public JpaReminderRepository(ReminderJpaDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional
    public void save(ReminderRecord record) {
        ReminderEntity e;
        if (record.getId() == null) {
            e = new ReminderEntity();
        } else {
            e = dao.findById(record.getId())
                    .orElseThrow(() -> new IllegalStateException("reminder not found: " + record.getId()));
        }
        e.setRemindDate(record.getRemindDate());
        e.setRemindTime(record.getRemindTime());
        e.setTitle(record.getTitle());
        e.setNote(record.getNote());
        e.setIcon(record.getIcon());
        e.setDone(record.isDone());
        e.setCreatedAt(record.getCreatedAt());
        e = dao.save(e);
        record.setId(e.getId());
    }

    @Override
    public Optional<ReminderRecord> findById(long id) {
        return dao.findById(id).map(this::toRecord);
    }

    @Override
    public List<ReminderRecord> findAll() {
        return dao.findAll().stream().map(this::toRecord).collect(Collectors.toList());
    }

    @Override
    public long count() {
        return dao.count();
    }

    private ReminderRecord toRecord(ReminderEntity e) {
        ReminderRecord r = new ReminderRecord();
        r.setId(e.getId());
        r.setRemindDate(e.getRemindDate());
        r.setRemindTime(e.getRemindTime());
        r.setTitle(e.getTitle());
        r.setNote(e.getNote());
        r.setIcon(e.getIcon());
        r.setDone(e.isDone());
        r.setCreatedAt(e.getCreatedAt());
        return r;
    }
}
