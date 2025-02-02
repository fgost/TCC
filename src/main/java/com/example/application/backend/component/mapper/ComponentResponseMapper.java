package com.example.application.backend.component.mapper;

import com.example.application.backend.component.domain.ComponentEntity;
import com.example.application.backend.component.model.ComponentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ComponentResponseMapper {
    ComponentResponseMapper INSTANCE = Mappers.getMapper(ComponentResponseMapper.class);

    ComponentResponse mapEntityToResponse(ComponentEntity entity);

    ComponentResponse mapEntityToComponentResponse(ComponentEntity entity);
}
