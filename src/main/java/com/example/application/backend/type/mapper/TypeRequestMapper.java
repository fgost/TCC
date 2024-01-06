package com.example.application.backend.type.mapper;

import com.example.application.backend.type.domain.TypeEntity;
import com.example.application.backend.type.model.TypeRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TypeRequestMapper {
    TypeRequestMapper INSTANCE = Mappers.getMapper(TypeRequestMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    TypeEntity mapModelToEntity(TypeRequest request);
}
