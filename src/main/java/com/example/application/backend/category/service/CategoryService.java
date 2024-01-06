package com.example.application.backend.category.service;

import com.example.application.backend.category.domain.CategoryEntity;
import com.example.application.backend.category.domain.CategoryEnum;
import com.example.application.backend.category.repository.CategoryRepository;
import com.example.application.domain.Constants;
import com.example.application.exception.domain.ObjectNotFoundException;
import com.example.application.exception.util.ExceptionUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    private final CategoryRepository repository;

    public List<CategoryEntity> findAll(CategoryEnum category) {
        return repository.findAll();
    }

    public CategoryEntity findByCode(String code) {
        var entity = repository.findByCode(code)
                .orElseThrow(() -> new ObjectNotFoundException(Constants.CATEGORY_NOT_FOUND));
        return entity;
    }

    @Transactional
    public CategoryEntity insert(CategoryEntity categoryEntity) {
        try{
            return repository.save(categoryEntity);
        }catch (DataIntegrityViolationException ex) {
            throw ExceptionUtils.buildSameIdentifierException(Constants.CATEGORY_DUPLICATED);
        }
    }

    @Transactional
    public CategoryEntity update(String code, CategoryEntity entity) {
        var existentEntity = findByCode(code);
        existentEntity.setCode(code);
        existentEntity.setCategory(entity.getCategory());

        var dto = existentEntity;
        try {
            return repository.save(dto);
        }catch (Exception e) {
            throw ExceptionUtils.buildNotPersistedException(Constants.CATEGORY_NOT_PERSISTED);
        }
    }

    @Transactional
    public void deleteByCode(String code) {
        try{
            var entity = findByCode(code);
            repository.delete(entity);
        }catch (Exception e) {
            throw ExceptionUtils.buildNotPersistedException(Constants.CATEGORY_DELETION_ERROR);
        }
    }
}
