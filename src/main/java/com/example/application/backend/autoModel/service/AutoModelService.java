package com.example.application.backend.autoModel.service;

import com.example.application.backend.autoModel.domain.AutoModelEntity;
import com.example.application.backend.autoModel.repository.AutoModelRepository;
import com.example.application.domain.Constants;
import com.example.application.exception.domain.ObjectNotFoundException;
import com.example.application.exception.util.ExceptionUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class AutoModelService {
    private AutoModelRepository autoModelRepository;

    public List<AutoModelEntity> findAll(Long carMaker) {
        boolean hasCarMaker = carMaker != null;

        if (hasCarMaker)
            return autoModelRepository.findByAutoMaker(carMaker);
         else
            return autoModelRepository.findAll();
    }

    public AutoModelEntity findByCode(String code) {
        return autoModelRepository.findByCode(code)
                .orElseThrow(() -> new ObjectNotFoundException(Constants.AUTO_MODEL_NOT_FOUND));
    }

    @Transactional
    public AutoModelEntity insert(AutoModelEntity autoModelEntity) {
        try {
            return autoModelRepository.save(autoModelEntity);
        } catch (DataIntegrityViolationException ex) {
            throw ExceptionUtils.buildSameIdentifierException(Constants.AUTO_MODEL_DUPLICATED);
        }
    }

    @Transactional
    public AutoModelEntity update(String code, AutoModelEntity entity) {
        AutoModelEntity existentEntity = findByCode(code);
        existentEntity.setAutoModel(entity.getAutoModel());
        try {
            return autoModelRepository.save(existentEntity);
        } catch (Exception e) {
            throw ExceptionUtils.buildNotPersistedException(Constants.AUTO_MODEL_NOT_PERSISTED);
        }
    }

    @Transactional
    public void deleteByCode(String code) {
        try {
            AutoModelEntity entity = findByCode(code);
            autoModelRepository.delete(entity);
        } catch (Exception e) {
            throw ExceptionUtils.buildNotPersistedException(Constants.AUTO_MODEL_DELETION_ERROR);
        }
    }
}
