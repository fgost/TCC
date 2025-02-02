package com.example.application.backend.part.mapper;

import com.example.application.backend.part.domain.PartEntity;
import com.example.application.backend.part.model.PartResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PartResponseMapper {
    PartResponseMapper INSTANCE = Mappers.getMapper(PartResponseMapper.class);

    PartResponse mapEntityToResponse(PartEntity entity);

    PartResponse mapEntityToPartResponse(PartEntity entity);
}
