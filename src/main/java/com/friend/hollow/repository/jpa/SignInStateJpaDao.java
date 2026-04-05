package com.friend.hollow.repository.jpa;

import com.friend.hollow.entity.SignInStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignInStateJpaDao extends JpaRepository<SignInStateEntity, Long> {
}
