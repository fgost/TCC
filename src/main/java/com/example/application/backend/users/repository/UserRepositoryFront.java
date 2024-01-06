package com.example.application.backend.users.repository;

import com.example.application.backend.users.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryFront extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
}
