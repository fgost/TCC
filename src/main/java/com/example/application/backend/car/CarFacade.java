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

/**
 * Facade class for handling operations related to cars. This class serves as an intermediary between the
 * controller layer and the service layer, providing methods for performing various operations on car entities.
 *
 * @author m.firmiano@aluno.ifsp.edu.br
 *
 * @see CarService - Service layer for car-related operations
 * @see CarResponseMapper - Mapper interface for mapping CarEntity to response DTOs
 * @see CarRequestMapper - Mapper interface for mapping CarRequest to CarEntity
 * @see CarEntity - Entity representing a car
 * @see CarResponse - DTO representing general car information
 * @see CarResponseCategory - DTO representing car information with categories
 */
@AllArgsConstructor
@Component
public class CarFacade {

    private CarService carService;

    /**
     * Retrieves a list of cars from the database based on the provided criteria. The method uses the provided
     * criteria to fetch the corresponding list of car entities from the database and maps each entity to
     * a CarResponse DTO.
     *
     * @param carModel - String that references the model of the car. Can be null for no filtering.
     * @param year - String that references the year of the car. Can be null for no filtering.
     *
     * @return List<CarResponse> - A list of DTOs representing the cars that match the given criteria.
     *
     * @see CarResponseMapper#mapEntityToResponse(CarEntity) - Mapping from entity to DTO
     * @see CarService#findAll(String, String) - Service method for fetching cars based on criteria
     */
    public List<CarResponse> findAll(String carModel, String year) {
        var entities = carService.findAll(carModel, year);
        return entities.stream()
                .map(CarResponseMapper.INSTANCE::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves detailed information about a car based on its external code (UUID). The method uses the provided
     * external code to fetch the corresponding car entity from the database and maps the entity to a CarResponse DTO.
     *
     * @param code - String representing the external code (UUID) of the car.
     *
     * @return CarResponse - A DTO representing the detailed information about the car.
     *
     * @see CarResponseMapper#mapEntityToCarResponse(CarEntity) - Mapping from entity to DTO
     * @see CarService#findByCode(String) - Service method for fetching a car by its external code
     */
    public CarResponse findByCode(String code) {
        var entity = carService.findByCode(code);
        return CarResponseMapper.INSTANCE.mapEntityToCarResponse(entity);
    }

    /**
     * Retrieves a car entity from the database based on the provided license plate. The method uses the provided
     * license plate to retrieve the corresponding car entity from the database. The fetched car entity is then mapped
     * to a CarResponse DTO to provide detailed information.
     *
     * @param licensePlate - String representing the license plate of the car.
     *
     * @return CarResponse - A DTO representing detailed information about the car.
     *
     * @see CarResponseMapper#mapEntityToCarResponse(CarEntity) - Mapping from entity to response DTO
     * @see CarService#findByLicensePlate(String) - Service method for finding a car by license plate
     */
    public CarResponse findByLicensePlate(String licensePlate) {
        var entity = carService.findByLicensePlate(licensePlate);
        return CarResponseMapper.INSTANCE.mapEntityToCarResponse(entity);
    }

    /**
     * Retrieves the categories associated with a car based on its external code (UUID). The method uses the provided
     * external code to fetch the corresponding car entity from the database and maps the entity to a
     * CarResponseCategory DTO.
     *
     * @param code - String representing the external code (UUID) of the car.
     *
     * @return CarResponseCategory - A DTO representing the categories associated with the car.
     *
     * @see CarResponseMapper#mapEntityToCategory(CarEntity) - Mapping from entity to category DTO
     * @see CarService#findByCode(String) - Service method for fetching a car by its external code
     */
    public CarResponseCategory getCategories(String code) {
        var entity = carService.findByCode(code);
        return CarResponseMapper.INSTANCE.mapEntityToCategory(entity);
    }

    /**
     * Inserts a new car into the database based on the provided entity and user. The method uses the provided
     * CarEntity and user information to insert a new car into the database. The saved car entity is then mapped
     * to a CarResponse DTO.
     *
     * @param input - CarEntity that will be persisted.
     * @param user - String representing the user that will be associated with the new car.
     *
     * @return CarResponse - A DTO representing the newly inserted car.
     *
     * @see CarResponseMapper#mapEntityToResponse(CarEntity) - Mapping from entity to response DTO
     * @see CarService#insert(CarEntity, String) - Service method for inserting a new car
     */
    public CarResponse insert(CarEntity input, String user) {
        var savedEntity = carService.insert(input, user);
        return CarResponseMapper.INSTANCE.mapEntityToResponse(savedEntity);
    }

    /**
     * Updates an existing car in the database based on the provided external code and request. The method uses the
     * provided external code and CarRequest to update the corresponding car entity in the database. The saved updated
     * car entity is then mapped to a CarResponse DTO.
     *
     * @param code - String representing the external code (UUID) of the car to be updated.
     * @param request - CarRequest containing the updated fields.
     *
     * @return CarResponse - A DTO representing the updated car.
     *
     * @see CarRequestMapper#mapModelToEntity(CarRequest) - Mapping from request to entity
     * @see CarResponseMapper#mapEntityToResponse(CarEntity) - Mapping from entity to response DTO
     * @see CarService#update(String, CarEntity) - Service method for updating an existing car
     */
    public CarResponse update(String code, CarRequest request) {
        var entity = CarRequestMapper.INSTANCE.mapModelToEntity(request);
        var savedEntity = carService.update(code, entity);
        return CarResponseMapper.INSTANCE.mapEntityToResponse(savedEntity);
    }

    /**
     * Updates the category of a car in the database based on the provided external code and category set. The method
     * uses the provided external code and category set to update the corresponding car entity's categories in the
     * database. The saved updated car entity is then mapped to a CarResponse DTO.
     *
     * @param code - String representing the external code (UUID) of the car.
     * @param inputList - Set<OnlyCodeDto> with the updated categories.
     *
     * @return CarResponse - A DTO representing the updated car.
     *
     * @see CarResponseMapper#mapEntityToCarResponse(CarEntity) - Mapping from entity to response DTO
     * @see CarService#updateCategory(String, Set) - Service method for updating car categories
     */
    public CarResponse updateCategory(String code, Set<OnlyCodeDto> inputList) {
        var entity = carService.updateCategory(code, inputList);
        return CarResponseMapper.INSTANCE.mapEntityToCarResponse(entity);
    }

    /**
     * Deletes a car from the database based on the provided external code (UUID).
     *
     * @param code - String representing the external code (UUID) of the car to be deleted.
     */
    public void deleteByCode(String code) {
        carService.deleteByCode(code);
    }
}
