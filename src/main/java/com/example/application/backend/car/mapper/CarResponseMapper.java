package com.example.application.backend.car.mapper;

import com.example.application.backend.car.domain.CarEntity;
import com.example.application.backend.car.model.response.CarResponse;
import com.example.application.backend.car.model.response.CarResponseCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CarResponseMapper {
    CarResponseMapper INSTANCE = Mappers.getMapper(CarResponseMapper.class);

    CarResponse mapEntityToResponse(CarEntity entity);

    CarResponse mapEntityToCarResponse(CarEntity entity);

    CarResponseCategory mapEntityToCategory(CarEntity entity);
}
