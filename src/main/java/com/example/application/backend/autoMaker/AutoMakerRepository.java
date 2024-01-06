package com.example.application.backend.autoMaker;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutoMakerRepository extends JpaRepository<AutoMakerEntity, Long> {
    List<AutoMakerEntity> findAll();
    AutoMakerEntity findByName(String name);
}
