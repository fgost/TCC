package com.example.application.backend.part;

import com.example.application.backend.part.domain.PartEntity;
import com.example.application.backend.part.mapper.PartRequestMapper;
import com.example.application.backend.part.mapper.PartResponseMapper;
import com.example.application.backend.part.model.PartRequest;
import com.example.application.backend.part.model.PartResponse;
import com.example.application.backend.part.service.PartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class PartFacade {
    private PartService partService;

    public List<PartEntity> findAll() {
        return partService.findAll();
    }

    public PartResponse findByCode(String code) {
        return PartResponseMapper.INSTANCE.mapEntityToPartResponse(partService.findByCode(code));
    }

    public PartResponse insert(PartRequest input) {
        PartEntity entity = PartRequestMapper.INSTANCE.mapModelToEntity(input);
        PartEntity savedEntity = partService.insert(entity);
        return PartResponseMapper.INSTANCE.mapEntityToResponse(savedEntity);
    }

    public PartResponse update(String code, PartRequest request) {
        PartEntity entity = PartRequestMapper.INSTANCE.mapModelToEntity(request);
        PartEntity savedEntity = partService.update(code, entity);
        return PartResponseMapper.INSTANCE.mapEntityToResponse(savedEntity);
    }

    public void deletedByCode(String code) {
        partService.deleteByCode(code);
    }
}
