package com.example.application.backend.users.repository;

import com.example.application.backend.users.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByCode(String code);

    List<UserEntity> findAll();

    List<UserEntity> findByEmailContainingIgnoreCase(String email);

    List<UserEntity> findByNameContainingIgnoreCase(String name);

    UserEntity findByName(String name);

    List<UserEntity> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email);

}
