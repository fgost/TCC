package com.example.application.backend.type.repository;

import com.example.application.backend.type.domain.TypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TypeRepository extends JpaRepository<TypeEntity, Long> {
    Optional<TypeEntity> findByCode(String code);

    List<TypeEntity> findAll();


}
