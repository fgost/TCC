package com.example.application.backend.component.service;

import com.example.application.backend.component.domain.ComponentEntity;
import com.example.application.backend.component.repository.ComponentRepository;
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
public class ComponentService {
    private ComponentRepository repository;

    public List<ComponentEntity> findAll() {
        return repository.findAll();
    }

    public ComponentEntity findByCode(String code) {
        return repository.findByCode(code)
                .orElseThrow(() -> new ObjectNotFoundException(Constants.TYPE_NOT_FOUND));
    }

    @Transactional
    public ComponentEntity insert(ComponentEntity partEntity) {
        try {
            return repository.save(partEntity);
        } catch (DataIntegrityViolationException ex) {
            throw ExceptionUtils.buildSameIdentifierException(Constants.TYPE_DUPLICATED);
        }
    }

    @Transactional
    public ComponentEntity update(String code, ComponentEntity entity) {
        var existentEntity = findByCode(code);
        existentEntity.setCode(code);
        existentEntity.setComponentName(entity.getComponentName());

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
