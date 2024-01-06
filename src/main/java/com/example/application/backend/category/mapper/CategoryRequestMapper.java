package com.example.application.backend.category.mapper;

import com.example.application.backend.category.domain.CategoryEntity;
import com.example.application.backend.category.model.request.CategoryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryRequestMapper {
    CategoryRequestMapper INSTANCE = Mappers.getMapper(CategoryRequestMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    CategoryEntity mapModelToEntity(CategoryRequest request);
}
