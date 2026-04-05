package com.friend.hollow.repository.jpa;

import com.friend.hollow.entity.CheerSurpriseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface CheerSurpriseJpaDao extends JpaRepository<CheerSurpriseEntity, Long> {

    long countByCreatedAtAfter(Instant after);

    List<CheerSurpriseEntity> findTop500ByOrderByCreatedAtDesc();
}
