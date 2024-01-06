package com.example.application.backend.maintenancePart.mapper;

import com.example.application.backend.maintenancePart.domain.MaintenancePartEntity;
import com.example.application.backend.maintenancePart.model.MaintenancePartResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MaintenancePartResponseMapper {

    MaintenancePartResponseMapper INSTANCE = Mappers.getMapper(MaintenancePartResponseMapper.class);

    MaintenancePartResponse mapEntityToResponse(MaintenancePartEntity entity);

    MaintenancePartResponse mapEntityToCategoryResponse(MaintenancePartEntity entity);
}
