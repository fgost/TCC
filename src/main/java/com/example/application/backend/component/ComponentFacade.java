package com.example.application.backend.component;

import com.example.application.backend.component.domain.ComponentEntity;
import com.example.application.backend.component.mapper.ComponentRequestMapper;
import com.example.application.backend.component.mapper.ComponentResponseMapper;
import com.example.application.backend.component.model.ComponentRequest;
import com.example.application.backend.component.model.ComponentResponse;
import com.example.application.backend.component.service.ComponentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class ComponentFacade {
    private ComponentService componentService;

    public List<ComponentEntity> findAll() {
        return componentService.findAll();
    }

    public ComponentResponse findByCode(String code) {
        return ComponentResponseMapper.INSTANCE.mapEntityToComponentResponse(componentService.findByCode(code));
    }

    public ComponentResponse insert(ComponentRequest input) {
        ComponentEntity entity = ComponentRequestMapper.INSTANCE.mapModelToEntity(input);
        ComponentEntity savedEntity = componentService.insert(entity);
        return ComponentResponseMapper.INSTANCE.mapEntityToResponse(savedEntity);
    }

    public ComponentResponse update(String code, ComponentRequest request) {
        ComponentEntity entity = ComponentRequestMapper.INSTANCE.mapModelToEntity(request);
        ComponentEntity savedEntity = componentService.update(code, entity);
        return ComponentResponseMapper.INSTANCE.mapEntityToResponse(savedEntity);
    }

    public void deletedByCode(String code) {
        componentService.deleteByCode(code);
    }
}
