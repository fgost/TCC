package com.example.application.backend.autoMaker;

import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
@NonNullApi
public interface AutoMakerRepository extends JpaRepository<AutoMakerEntity, Long> {
    List<AutoMakerEntity> findAll();

    AutoMakerEntity findByAutoMaker(String autoMaker);
}
