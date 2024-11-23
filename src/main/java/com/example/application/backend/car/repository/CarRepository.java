package com.example.application.backend.car.repository;

import com.example.application.backend.car.domain.CarEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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


    @Modifying
    @Transactional
    @Query("UPDATE CarEntity ce set ce.mileage = :carsMileage where ce.id = :carId")
    void updateMileage(@Param("carId") Long carId, @Param("carsMileage") Double carsMileage);
}
