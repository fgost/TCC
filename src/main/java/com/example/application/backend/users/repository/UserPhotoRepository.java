package com.example.application.backend.users.repository;

import com.example.application.backend.users.domain.UserPhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPhotoRepository extends JpaRepository<UserPhotoEntity, Long> {

    Optional<UserPhotoEntity> findByUserId(Long id);
}
