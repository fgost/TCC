package com.example.application.backend.part.repository;

import com.example.application.backend.part.domain.PartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartRepository extends JpaRepository<PartEntity, Long> {
    Optional<PartEntity> findByCode(String code);

    List<PartEntity> findAll();

    List<PartEntity> findByComponent(long component);
}
