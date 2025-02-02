package com.example.application.backend.maintenancePart.service;

import com.example.application.backend.car.domain.CarEntity;
import com.example.application.backend.car.service.CarService;
import com.example.application.backend.maintenancePart.domain.MaintenancePartEntity;
import com.example.application.backend.maintenancePart.domain.MaintenancePartStatusEnum;
import com.example.application.backend.maintenancePart.repository.MaintenancePartRepository;
import com.example.application.domain.Constants;
import com.example.application.exception.domain.ObjectNotFoundException;
import com.example.application.exception.util.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MaintenancePartService {

    private final CarService carService;
    private final MaintenancePartRepository repository;

    private static final float marcaAlertaPeca = 20;

    public MaintenancePartService(CarService carService, MaintenancePartRepository repository) {
        this.carService = carService;
        this.repository = repository;
    }

    public List<MaintenancePartEntity> findAll() {
        return repository.findAll();
    }

    public MaintenancePartEntity findByCode(String code) {
        MaintenancePartEntity entity = repository.findByCode(code)
                .orElseThrow(() -> new ObjectNotFoundException(Constants.PART_NOT_FOUND));
        return entity;
    }

    @Transactional
    public MaintenancePartEntity insert(MaintenancePartEntity entity) {
        try {
            entity.setLimiteParaAlerta(definirLimiteParaAlerta(entity.getLifeSpan(), entity.getCar()));
            entity.setLimiteParaUrgencia(definirLimiteParaUrgencia(entity.getLifeSpan(), entity.getCar()));
            return repository.save(entity);
        } catch (DataIntegrityViolationException ex) {
            throw ExceptionUtils.buildSameIdentifierException(Constants.TYPE_DUPLICATED);
        }
    }

    @Transactional
    public MaintenancePartEntity update(String code, MaintenancePartEntity entity) {
        MaintenancePartEntity existentEntity = findByCode(code);
        existentEntity.setCode(code);
        existentEntity.setPart(entity.getPart());
        existentEntity.setDescription(entity.getDescription());
        existentEntity.setSerialNumber(entity.getSerialNumber());
        existentEntity.setManufacturer(entity.getManufacturer());
        existentEntity.setModel(entity.getModel());
        existentEntity.setInstallationDate(entity.getInstallationDate());
        existentEntity.setLifeSpan(entity.getLifeSpan());
        existentEntity.setCost(entity.getCost());
        existentEntity.setStatus(entity.getStatus());
        existentEntity.setType(entity.getType());
        existentEntity.setSerialNumber(entity.getSerialNumber());

        MaintenancePartEntity dto = existentEntity;
        try {
            return repository.save(dto);
        } catch (Exception e) {
            throw ExceptionUtils.buildNotPersistedException(Constants.TYPE_NOT_PERSISTED);
        }
    }

    @Transactional
    public void deleteByCode(String code) {
        try {
            MaintenancePartEntity entity = findByCode(code);
            repository.delete(entity);
        } catch (Exception e) {
            throw ExceptionUtils.buildNotPersistedException(Constants.TYPE_DELETION_ERROR);
        }
    }

    public void ajustarStatusManutencoes(CarEntity car) {
        List<MaintenancePartEntity> maintenances = this.findByCar(car.getId());
        for (MaintenancePartEntity maintenance : maintenances) {
            if (car.getMileage() > maintenance.getLimiteParaAlerta() &&
                    car.getMileage() < maintenance.getLimiteParaUrgencia()) {
                maintenance.setStatus(MaintenancePartStatusEnum.USED);
            }
            if (car.getMileage() > maintenance.getLimiteParaUrgencia()) {
                maintenance.setStatus(MaintenancePartStatusEnum.URGENT_REPLACEMENT);
            }
            update(maintenance.getCode(), maintenance);
        }
    }

    private double definirLimiteParaAlerta(double lifeSpan, Long carId) {
        double carMileage = carService.findById(carId).getMileage();
        double futureChange = carMileage + lifeSpan;

        double precisaTrocar = futureChange * (marcaAlertaPeca / 100);

        return futureChange - precisaTrocar;
    }

    private double definirLimiteParaUrgencia(double lifeSpan, Long carId) {
        double carMileage = carService.findById(carId).getMileage();
        return carMileage + lifeSpan;
    }

    public void processarManutencao(MaintenancePartEntity maintenancePart, String code, String licensePlate, String carsMileage) {
        Long carId = carService.findByLicensePlate(licensePlate).getId();
        repository.updateByCode(code, carId);

        carService.updateMileage(carId, Double.parseDouble(carsMileage));

        maintenancePart.setCar(carId);
        insert(maintenancePart);
    }

    public List<MaintenancePartEntity> findByCar(long carId) {
        return repository.findByCar(carId);
    }
}
