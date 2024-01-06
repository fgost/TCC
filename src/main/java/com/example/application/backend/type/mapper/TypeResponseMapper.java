package com.example.application.backend.type.mapper;

import com.example.application.backend.type.domain.TypeEntity;
import com.example.application.backend.type.model.TypeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TypeResponseMapper {
    TypeResponseMapper INSTANCE = Mappers.getMapper(TypeResponseMapper.class);

    TypeResponse mapEntityToResponse(TypeEntity entity);

    TypeResponse mapEntityToTypeResponse(TypeEntity entity);
}
