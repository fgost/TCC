package com.example.application.backend.maintenancePart.service;

import com.example.application.backend.maintenancePart.domain.MaintenancePartEntity;
import com.example.application.backend.maintenancePart.repository.MaintenancePartRepository;
import com.example.application.domain.Constants;
import com.example.application.exception.domain.ObjectNotFoundException;
import com.example.application.exception.util.ExceptionUtils;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
public class MaintenancePartService {
    private MaintenancePartRepository repository;

    public List<MaintenancePartEntity> findAll() {
        return repository.findAll();
    }

    public MaintenancePartEntity findByCode(String code) {
        var entity = repository.findByCode(code)
                .orElseThrow(() -> new ObjectNotFoundException(Constants.TYPE_NOT_FOUND));
        return entity;
    }

    @Transactional
    public MaintenancePartEntity insert(MaintenancePartEntity entity) {
        try {
            return repository.save(entity);
        } catch (DataIntegrityViolationException ex) {
            throw ExceptionUtils.buildSameIdentifierException(Constants.TYPE_DUPLICATED);
        }
    }

    @Transactional
    public MaintenancePartEntity update(String code, MaintenancePartEntity entity) {
        var existentEntity = findByCode(code);
        existentEntity.setCode(code);
        existentEntity.setName(entity.getName());
        existentEntity.setDescription(entity.getDescription());
        existentEntity.setSerialNumber(entity.getSerialNumber());
        existentEntity.setManufacturer(entity.getManufacturer());
        existentEntity.setModel(entity.getModel());
        existentEntity.setInstallationDate(entity.getInstallationDate());
        existentEntity.setLifeSpan(entity.getLifeSpan());
        existentEntity.setCost(entity.getCost());
        existentEntity.setStatus(entity.getStatus());
        existentEntity.setType(entity.getType());
        var dto = existentEntity;
        try {
            return repository.save(dto);
        } catch (Exception e) {
            throw ExceptionUtils.buildNotPersistedException(Constants.TYPE_NOT_PERSISTED);
        }
    }

    @Transactional
    public void deleteByCode(String code) {
        try {
            var entity = findByCode(code);
            repository.delete(entity);
        } catch (Exception e) {
            throw ExceptionUtils.buildNotPersistedException(Constants.TYPE_DELETION_ERROR);
        }
    }
}
