package com.example.application.backend.type;

import com.example.application.backend.type.domain.TypeEntity;
import com.example.application.backend.type.domain.TypeEnum;
import com.example.application.backend.type.mapper.TypeRequestMapper;
import com.example.application.backend.type.mapper.TypeResponseMapper;
import com.example.application.backend.type.model.TypeRequest;
import com.example.application.backend.type.model.TypeResponse;
import com.example.application.backend.type.service.TypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class TypeFacade {
    private  TypeService typeService;

    public List<TypeEntity> findAll(TypeEnum typeName) {
        return typeService.findAll(typeName);
    }

    public TypeResponse findByCode(String code) {
        var entity = typeService.findByCode(code);
        var dto = TypeResponseMapper.INSTANCE.mapEntityToTypeResponse(entity);
        return dto;
    }

    public TypeResponse insert(TypeRequest input) {
        var entity = TypeRequestMapper.INSTANCE.mapModelToEntity(input);
        var savedEntity = typeService.insert(entity);
        var dto = TypeResponseMapper.INSTANCE.mapEntityToResponse(savedEntity);
        return dto;
    }

    public TypeResponse update(String code, TypeRequest request) {
        var entity = TypeRequestMapper.INSTANCE.mapModelToEntity(request);
        var savedEntity = typeService.update(code, entity);
        var dto = TypeResponseMapper.INSTANCE.mapEntityToResponse(savedEntity);
        return dto;
    }

    public void deletedByCode(String code) {
        typeService.deleteByCode(code);
    }
}
