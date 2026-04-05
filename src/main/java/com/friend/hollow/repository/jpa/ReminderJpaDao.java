package com.friend.hollow.repository.jpa;

import com.friend.hollow.entity.ReminderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReminderJpaDao extends JpaRepository<ReminderEntity, Long> {
}
