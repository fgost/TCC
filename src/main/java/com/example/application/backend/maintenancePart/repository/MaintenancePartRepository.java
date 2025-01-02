package com.example.application.backend.maintenancePart.repository;

import com.example.application.backend.maintenancePart.domain.MaintenancePartEntity;
import com.example.application.backend.maintenancePart.domain.MaintenancePartStatusEnum;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaintenancePartRepository extends JpaRepository<MaintenancePartEntity, Long> {
    Optional<MaintenancePartEntity> findByCode(String code);

    List<MaintenancePartEntity> findAll();

    List<MaintenancePartEntity> findByCar(Long car);

    List<MaintenancePartEntity> findByCarAndStatus(Long car, MaintenancePartStatusEnum status);

    @Modifying
    @Transactional
    @Query("UPDATE MaintenancePartEntity mpe set mpe.status = 4 where mpe.code = :code and mpe.car = :carId")
    void updateByCode(@Param("code") String code, @Param("carId") Long carId);
}
