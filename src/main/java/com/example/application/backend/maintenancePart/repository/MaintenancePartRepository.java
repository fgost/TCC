package com.example.application.backend.maintenancePart.repository;

import com.example.application.backend.maintenancePart.domain.MaintenancePartEntity;
import com.example.application.backend.maintenancePart.domain.MaintenancePartStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaintenancePartRepository extends JpaRepository<MaintenancePartEntity, Long> {
    Optional<MaintenancePartEntity> findByCode(String code);

    List<MaintenancePartEntity> findAll();

    List<MaintenancePartEntity> findByCar(Long car);

    List<MaintenancePartEntity> findByCarAndStatus(Long car, MaintenancePartStatusEnum status);
}
