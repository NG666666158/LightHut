package com.friend.hollow.repository.jpa;

import com.friend.hollow.entity.HugClickDailyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface HugClickDailyJpaDao extends JpaRepository<HugClickDailyEntity, LocalDate> {
}
