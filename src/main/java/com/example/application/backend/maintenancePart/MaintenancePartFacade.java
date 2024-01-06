package com.example.application.backend.maintenancePart;

import com.example.application.backend.maintenancePart.domain.MaintenancePartEntity;
import com.example.application.backend.maintenancePart.mapper.MaintenancePartRequestMapper;
import com.example.application.backend.maintenancePart.mapper.MaintenancePartResponseMapper;
import com.example.application.backend.maintenancePart.model.MaintenancePartRequest;
import com.example.application.backend.maintenancePart.model.MaintenancePartResponse;
import com.example.application.backend.maintenancePart.service.MaintenancePartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class MaintenancePartFacade {

    private MaintenancePartService maintenancePartService;

    public List<MaintenancePartResponse> findAll() {
        var entities = maintenancePartService.findAll();
        return entities.stream()
                .map(MaintenancePartResponseMapper.INSTANCE::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    public MaintenancePartResponse findByCode(String code) {
        var entity = maintenancePartService.findByCode(code);
        var dto = MaintenancePartResponseMapper.INSTANCE.mapEntityToResponse(entity);
        return dto;
    }

    public MaintenancePartResponse insert(MaintenancePartEntity input) {
        var savedEntity = maintenancePartService.insert(input);
        var dto = MaintenancePartResponseMapper.INSTANCE.mapEntityToResponse(savedEntity);
        return dto;
    }

    public MaintenancePartResponse update(String code, MaintenancePartRequest request) {
        var entity = MaintenancePartRequestMapper.INSTANCE.mapModelToEntity(request);
        var savedEntity = maintenancePartService.update(code, entity);
        var dto = MaintenancePartResponseMapper.INSTANCE.mapEntityToResponse(savedEntity);
        return dto;
    }

    public void deletedByCode(String code) {
        maintenancePartService.deleteByCode(code);
    }
}
