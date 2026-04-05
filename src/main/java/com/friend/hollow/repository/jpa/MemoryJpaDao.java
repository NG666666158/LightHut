package com.friend.hollow.repository.jpa;

import com.friend.hollow.entity.MemoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoryJpaDao extends JpaRepository<MemoryEntity, Long> {
}
