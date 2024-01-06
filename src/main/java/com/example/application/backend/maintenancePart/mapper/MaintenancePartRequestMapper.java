package com.example.application.backend.maintenancePart.mapper;

import com.example.application.backend.maintenancePart.domain.MaintenancePartEntity;
import com.example.application.backend.maintenancePart.model.MaintenancePartRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MaintenancePartRequestMapper {
    MaintenancePartRequestMapper INSTANCE = Mappers.getMapper(MaintenancePartRequestMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    MaintenancePartEntity mapModelToEntity(MaintenancePartRequest request);
}
