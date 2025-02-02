package com.example.application.backend.component.mapper;

import com.example.application.backend.component.domain.ComponentEntity;
import com.example.application.backend.component.model.ComponentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ComponentRequestMapper {
    ComponentRequestMapper INSTANCE = Mappers.getMapper(ComponentRequestMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "componentName", ignore = true)
    ComponentEntity mapModelToEntity(ComponentRequest request);
}
