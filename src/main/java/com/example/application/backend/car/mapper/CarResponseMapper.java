package com.example.application.backend.car.mapper;

import com.example.application.backend.car.domain.CarEntity;
import com.example.application.backend.car.model.response.CarResponse;
import com.example.application.backend.car.model.response.CarResponseCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for mapping CarEntity to response DTOs. The CarResponseMapper interface uses MapStruct annotations
 * to generate implementation code for mapping.
 *
 * @author m.firmiano@aluno.ifsp.edu.br
 *
 * @see org.mapstruct.Mapper - MapStruct annotation for marking the interface as a mapper
 * @see org.mapstruct.Mapping - MapStruct annotation for defining mappings between properties
 * @see com.example.application.backend.car.domain.CarEntity - Entity representing a car
 * @see com.example.application.backend.car.model.response.CarResponse - DTO representing general car information
 * @see com.example.application.backend.car.model.response.CarResponseCategory - DTO representing car information with categories
 */
@Mapper
public interface CarResponseMapper {

    CarResponseMapper INSTANCE = Mappers.getMapper(CarResponseMapper.class);

    /**
     * Maps a CarEntity object to a CarResponse object.
     *
     * @param entity - CarEntity object to be mapped.
     * @return {@link CarResponse} - The mapped CarResponse object.
     */
    CarResponse mapEntityToResponse(CarEntity entity);

    /**
     * Maps a CarEntity object to a CarResponse object.
     *
     * @param entity - CarEntity object to be mapped.
     * @return {@link CarResponse} - The mapped CarResponse object.
     */
    CarResponse mapEntityToCarResponse(CarEntity entity);

    /**
     * Maps a CarEntity object to a CarResponseCategory object.
     *
     * @param entity - CarEntity object to be mapped.
     * @return {@link CarResponseCategory} - The mapped CarResponseCategory object.
     */
    CarResponseCategory mapEntityToCategory(CarEntity entity);
}
