package com.example.application.backend.component.repository;

import com.example.application.backend.component.domain.ComponentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ComponentRepository extends JpaRepository<ComponentEntity, Long> {
    Optional<ComponentEntity> findByCode(String code);

    List<ComponentEntity> findAll();


}
