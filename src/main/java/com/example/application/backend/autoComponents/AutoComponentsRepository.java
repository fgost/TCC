package com.example.application.backend.autoComponents;

import com.example.application.backend.autoMaker.AutoMakerEntity;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@NonNullApi
public interface AutoComponentsRepository extends JpaRepository<AutoComponentsEntity, Long> {
    List<AutoComponentsEntity> findAll();

    List<AutoComponentsEntity> findAllByOrderByComponentNameAsc();

    AutoMakerEntity findByComponentName(String autoComponent);
}






