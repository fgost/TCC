package com.example.application.backend.car.mapper;

import com.example.application.backend.car.domain.CarEntity;
import com.example.application.backend.car.model.request.CarRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for mapping CarRequest to CarEntity. The CarRequestMapper interface uses MapStruct annotations to
 * generate implementation code for mapping.
 *
 * @author m.firmiano@aluno.ifsp.edu.br
 *
 * @see org.mapstruct.Mapper - MapStruct annotation for marking the interface as a mapper
 * @see org.mapstruct.Mapping - MapStruct annotation for defining mappings between properties
 * @see com.example.application.backend.car.model.request.CarRequest - DTO representing the request to update or
 * insert a car
 * @see com.example.application.backend.car.domain.CarEntity - Entity representing a car
 */
@Mapper
public interface CarRequestMapper {
    CarRequestMapper INSTANCE = Mappers.getMapper(CarRequestMapper.class);

    /**
     * Maps a CarRequest object to a CarEntity object, excluding 'id' and 'code' properties.
     *
     * @param request - CarRequest object to be mapped.
     * @return {@link CarEntity} - The mapped CarEntity object.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    CarEntity mapModelToEntity(CarRequest request);
}
