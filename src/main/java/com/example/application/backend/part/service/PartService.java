package com.example.application.backend.part.service;

import com.example.application.backend.part.domain.PartEntity;
import com.example.application.backend.part.repository.PartRepository;
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
public class PartService {
    private PartRepository repository;

    public List<PartEntity> findAll() {
        return repository.findAll();
    }

    public PartEntity findByCode(String code) {
        return repository.findByCode(code)
                .orElseThrow(() -> new ObjectNotFoundException(Constants.TYPE_NOT_FOUND));
    }

    public List<PartEntity> findByComponent(long component) {
        return repository.findByComponent(component);
    }

    public PartEntity findById(long part) {
        return repository.findById(part)
                .orElseThrow(() -> new ObjectNotFoundException(Constants.TYPE_NOT_FOUND));
    }

    @Transactional
    public PartEntity insert(PartEntity partEntity) {
        try {
            return repository.save(partEntity);
        } catch (DataIntegrityViolationException ex) {
            throw ExceptionUtils.buildSameIdentifierException(Constants.TYPE_DUPLICATED);
        }
    }

    @Transactional
    public PartEntity update(String code, PartEntity entity) {
        var existentEntity = findByCode(code);
        existentEntity.setCode(code);
        existentEntity.setPartName(entity.getPartName());
        existentEntity.setComponent(entity.getComponent());

        try {
            return repository.save(existentEntity);
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
