package com.example.application.backend.part.mapper;

import com.example.application.backend.part.domain.PartEntity;
import com.example.application.backend.part.model.PartRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PartRequestMapper {
    PartRequestMapper INSTANCE = Mappers.getMapper(PartRequestMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "component", ignore = true)
    PartEntity mapModelToEntity(PartRequest request);
}
