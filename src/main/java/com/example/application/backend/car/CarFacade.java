package com.example.application.backend.car;

import com.example.application.backend.car.domain.CarEntity;
import com.example.application.backend.car.mapper.CarRequestMapper;
import com.example.application.backend.car.mapper.CarResponseMapper;
import com.example.application.backend.car.model.request.CarRequest;
import com.example.application.backend.car.model.response.CarResponse;
import com.example.application.backend.car.model.response.CarResponseCategory;
import com.example.application.backend.car.service.CarService;
import com.example.application.utils.dto.OnlyCodeDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class CarFacade {

    private CarService carService;

    public List<CarResponse> findAll(String carModel, String year) {
        var entities = carService.findAll(carModel, year);
        return entities.stream()
                .map(CarResponseMapper.INSTANCE::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    public CarResponse findByCode(String code) {
        var entity = carService.findByCode(code);
        return CarResponseMapper.INSTANCE.mapEntityToCarResponse(entity);
    }

    public CarEntity findByLicensePlate(String licensePlate) {
        return carService.findByLicensePlate(licensePlate);
    }

    public CarResponseCategory getCategories(String code) {
        var entity = carService.findByCode(code);
        return CarResponseMapper.INSTANCE.mapEntityToCategory(entity);
    }

    public CarResponse insert(CarEntity input, String user) {
        var savedEntity = carService.insert(input, user);
        return CarResponseMapper.INSTANCE.mapEntityToResponse(savedEntity);
    }

    public CarResponse update(String code, CarRequest request) {
        var entity = CarRequestMapper.INSTANCE.mapModelToEntity(request);
        var savedEntity = carService.update(code, entity);
        return CarResponseMapper.INSTANCE.mapEntityToResponse(savedEntity);
    }

    public CarResponse updateCategory(String code, Set<OnlyCodeDto> inputList) {
        var entity = carService.updateCategory(code, inputList);
        return CarResponseMapper.INSTANCE.mapEntityToCarResponse(entity);
    }

    public void deleteByCode(String code) {
        carService.deleteByCode(code);
    }
}
