package com.example.application.backend.car.repository;

import com.example.application.backend.car.domain.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<CarEntity, Long> {

    Optional<CarEntity> findByCode(String code);

    List<CarEntity> findAll();

    List<CarEntity> findByuserOwner(Long id);

    List<CarEntity> findByYearContainingIgnoreCase(String year);

    List<CarEntity> findByCarModelContainingIgnoreCase(String carModel);

    List<CarEntity> findByCarModelContainingIgnoreCaseOrYearContainingIgnoreCase(String carModel, String year);

    CarEntity findByLicencePlate(String licencePlate);
}
