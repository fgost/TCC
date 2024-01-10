package com.example.application.backend.autoModel;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AutoModelRepository extends JpaRepository<AutoModelEntity, Long> {

    List<AutoModelEntity> findByAutoMaker (long autoMaker);
}
