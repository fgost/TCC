package com.example.application.backend.category.mapper;

import com.example.application.backend.category.domain.CategoryEntity;
import com.example.application.backend.category.model.response.CategoryResponse;
import com.example.application.backend.category.model.response.CategoryResponseType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryResponseMapper {
    CategoryResponseMapper INSTANCE = Mappers.getMapper(CategoryResponseMapper.class);

    CategoryResponse mapEntityToResponse(CategoryEntity entity);

    CategoryResponse mapEntityToCategoryResponse(CategoryEntity entity);

    CategoryResponseType mapEntityToType(CategoryEntity entity);
}
