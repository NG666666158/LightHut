package com.friend.hollow.repository.jpa;

import com.friend.hollow.entity.MoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MoodJpaDao extends JpaRepository<MoodEntity, Long> {
    Optional<MoodEntity> findTopByOrderByCreatedAtAsc();
}
