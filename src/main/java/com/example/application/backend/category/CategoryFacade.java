package com.example.application.backend.category;

import com.example.application.backend.category.domain.CategoryEnum;
import com.example.application.backend.category.mapper.CategoryRequestMapper;
import com.example.application.backend.category.mapper.CategoryResponseMapper;
import com.example.application.backend.category.model.request.CategoryRequest;
import com.example.application.backend.category.model.response.CategoryResponse;
import com.example.application.backend.category.model.response.CategoryResponseType;
import com.example.application.backend.category.service.CategoryService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryFacade {
    private final CategoryService categoryService;
    public CategoryFacade(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public List<CategoryResponse> findAll(CategoryEnum category) {
        var entities = categoryService.findAll(category);
        return entities.stream()
                .map(CategoryResponseMapper.INSTANCE::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    public CategoryResponse findByCode(String code) {
        var entity = categoryService.findByCode(code);
        var dto = CategoryResponseMapper.INSTANCE.mapEntityToCategoryResponse(entity);
        return dto;
    }

    public CategoryResponseType getTypes(String code) {
        var entity = categoryService.findByCode(code);
        var dto = CategoryResponseMapper.INSTANCE.mapEntityToType(entity);
        return dto;
    }

    public CategoryResponse insert(CategoryRequest input) {
        var entity = CategoryRequestMapper.INSTANCE.mapModelToEntity(input);
        var savedEntity = categoryService.insert(entity);
        var dto = CategoryResponseMapper.INSTANCE.mapEntityToResponse(savedEntity);
        return dto;
    }

    public CategoryResponse update(String code, CategoryRequest request) {
        var entity = CategoryRequestMapper.INSTANCE.mapModelToEntity(request);
        var savedEntity = categoryService.update(code, entity);
        var dto = CategoryResponseMapper.INSTANCE.mapEntityToResponse(savedEntity);
        return dto;
    }

    public void deleteByCode(String code) {
        categoryService.deleteByCode(code);
    }
}
