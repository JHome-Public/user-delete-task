package com.jhome.user.repository;

import com.jhome.user.domain.UserEntity;
import com.jhome.user.domain.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Page<UserEntity> findByStatus(UserStatus status, Pageable pageable);
}
