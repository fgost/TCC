package com.example.application.backend.autoModel.repository;

import com.example.application.backend.autoModel.domain.AutoModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AutoModelRepository extends JpaRepository<AutoModelEntity, Long> {

    List<AutoModelEntity> findByAutoMaker (long autoMaker);
}
