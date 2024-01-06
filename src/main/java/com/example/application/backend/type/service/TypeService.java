package com.example.application.backend.type.service;

import com.example.application.backend.type.domain.TypeEntity;
import com.example.application.backend.type.domain.TypeEnum;
import com.example.application.backend.type.repository.TypeRepository;
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
public class TypeService {
    private TypeRepository repository;

    public List<TypeEntity> findAll(TypeEnum typeName) {
        return repository.findAll();
    }

    public TypeEntity findByCode(String code) {
        var entity = repository.findByCode(code)
                .orElseThrow(() -> new ObjectNotFoundException(Constants.TYPE_NOT_FOUND));
        return entity;
    }

    @Transactional
    public TypeEntity insert(TypeEntity typeEntity) {
        try {
            return repository.save(typeEntity);
        } catch (DataIntegrityViolationException ex) {
            throw ExceptionUtils.buildSameIdentifierException(Constants.TYPE_DUPLICATED);
        }
    }

    @Transactional
    public TypeEntity update(String code, TypeEntity entity) {
        var existentEntity = findByCode(code);
        existentEntity.setCode(code);
        existentEntity.setTypeName(entity.getTypeName());

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
