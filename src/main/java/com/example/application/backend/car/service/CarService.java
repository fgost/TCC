package com.example.application.backend.car.service;

import com.example.application.backend.car.domain.CarEntity;
import com.example.application.backend.car.repository.CarRepository;
import com.example.application.backend.category.domain.CategoryEntity;
import com.example.application.backend.category.repository.CategoryRepository;
import com.example.application.backend.category.service.CategoryService;
import com.example.application.backend.users.repository.UserRepositoryFront;
import com.example.application.domain.Constants;
import com.example.application.exception.domain.ObjectNotFoundException;
import com.example.application.exception.util.ExceptionUtils;
import com.example.application.utils.dto.OnlyCodeDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Service class for handling business logic related to cars. This class includes methods to interact with the
 * underlying data layer for performing CRUD operations on car entities.
 *
 * @author m.firmiano@aluno.ifsp.edu.br
 * @see CarRepository - Repository for accessing car entities in the database
 * @see CategoryRepository - Repository for accessing category entities in the database
 * @see CategoryService - Service for handling business logic related to categories
 * @see JdbcTemplate - Spring JDBC template for executing SQL queries
 * @see UserRepositoryFront - Repository for accessing user entities in the database
 * @see CarEntity - Entity representing a car
 * @see OnlyCodeDto - DTO representing a code field
 */
@AllArgsConstructor
@Service
public class CarService {
    private static final Logger LOGGER = LogManager.getLogger(CarService.class);

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepositoryFront userRepositoryFront;

    /**
     * Find all cars in the database
     *
     * @param carModel - String representing the model of the car.
     * @param year     -  String representing the year of the car.
     * @return {@link List<CarEntity> } - List of entities present in the database.
     */
    public List<CarEntity> findAll(String carModel, String year) {
        boolean hasCarModel = carModel != null && !carModel.isBlank();
        boolean hasYear = year != null && !year.isBlank();
        boolean hasBoth = hasCarModel && hasYear;
        boolean noOne = !hasYear && !hasCarModel;

        if (hasBoth) {
            return carRepository.findByCarModelContainingIgnoreCaseOrYearContainingIgnoreCase(carModel, year);
        } else if (noOne) {
            return carRepository.findAll();
        } else if (hasYear) {
            return carRepository.findByYearContainingIgnoreCase(year);
        } else {
            return carRepository.findByCarModelContainingIgnoreCase(carModel);
        }
    }

    /**
     * Finds a car in the database based on a given external code (UUID).
     *
     * @param code - String representing the external code (UUID) of the car.
     * @return {@link CarEntity } - Entity present in the database.
     */
    public CarEntity findByCode(String code) {
        return carRepository.findByCode(code)
                .orElseThrow(() -> new ObjectNotFoundException(Constants.CAR_NOT_FOUND));
    }

    public CarEntity findById(Long code) {
        return carRepository.findById(code)
                .orElseThrow(() -> new ObjectNotFoundException(Constants.CAR_NOT_FOUND));
    }

    /**
     * Finds a car in the database based on a given license plate.
     *
     * @param licensePlate - String representing the license plate of the car.
     * @return {@link CarEntity } - Entity present in the database.
     */
    public CarEntity findByLicensePlate(String licensePlate) {
        return carRepository.findByLicencePlate(licensePlate);
    }

    /**
     * Finds all cars associated with a user in the database.
     *
     * @param id - long representing the ID of the user.
     * @return {@link List<CarEntity> } - List of entities present in the database.
     */
    public List<CarEntity> findByUser(long id) {
        return carRepository.findByUserOwner(id);
    }

    /**
     * Inserts a car into the database based on the provided entity and associated user.
     *
     * @param carEntity - Entity to be persisted.
     * @param user      - String representing the user to be associated with the new car.
     * @return {@link CarEntity } - Entity that was inserted into the database.
     */
    @Transactional
    public CarEntity insert(CarEntity carEntity, String user) {
        try {
            var userDb = userRepositoryFront.findByEmail(user);
            carEntity.setUserOwner(userDb.getId());
            CarEntity savedCar = carRepository.save(carEntity);
            List<Integer> categoryIds = categoryRepository.findAllIds();

            for (Integer categoryId : categoryIds) {
                String sql = "INSERT INTO categories_cars (car_id, category_id) VALUES (?, ?)";
                jdbcTemplate.update(sql, savedCar.getId(), categoryId);
            }

            return savedCar;
        } catch (DataIntegrityViolationException ex) {
            LOGGER.error("Error to insert a car.", ex);
            throw ExceptionUtils.buildSameIdentifierException(Constants.CAR_DUPLICATED);
        }
    }

    /**
     * Updates a car in the database based on the provided entity and the code of the existing entity.
     *
     * @param entity - Entity with the updated fields.
     * @param code   - String representing the code of the existing entity.
     * @return {@link CarEntity } - Entity that was updated in the database.
     */
    @Transactional
    public CarEntity update(String code, CarEntity entity) {
        var existentEntity = findByCode(code);
        existentEntity.setCarModel(entity.getCarModel());
        existentEntity.setType(entity.getType());
        existentEntity.setColor(entity.getColor());
        existentEntity.setAutoMaker(entity.getAutoMaker());
        existentEntity.setYear(entity.getYear());
        existentEntity.setMileage(entity.getMileage());
        existentEntity.setMotor(entity.getMotor());
        existentEntity.setLicencePlate(entity.getLicencePlate());
        try {
            return carRepository.save(existentEntity);
        } catch (Exception e) {
            throw ExceptionUtils.buildNotPersistedException(Constants.CAR_NOT_PERSISTED);
        }
    }

    /**
     * Updates the category of a car in the database based on the provided Set<OnlyCodeDto> and the code of the existing entity.
     *
     * @param code      - String representing the code of the existing entity.
     * @param inputList - Set<OnlyCodeDto> with the updated categories.
     * @return {@link CarEntity } - Entity that was updated in the database.
     */
    @Transactional
    public CarEntity updateCategory(String code, Set<OnlyCodeDto> inputList) {
        var entity = findByCode(code);
        entity.getCategories().clear();
        List<CategoryEntity> categoriesToAdd = new ArrayList<>();
        inputList.forEach(input -> {
            var category = categoryService.findByCode(input.getCode());
            if (!categoriesToAdd.contains(category)) {
                categoriesToAdd.add(category);
            }
        });
        entity.setCategories(categoriesToAdd);
        return carRepository.save(entity);
    }

    /**
     * Deletes a car in the database based on the provided external code (UUID).
     *
     * @param code - String representing the external code (UUID) of the car.
     */
    @Transactional
    public void deleteByCode(String code) {
        try {
            var entity = findByCode(code);
            carRepository.delete(entity);
        } catch (Exception e) {
            throw ExceptionUtils.buildNotPersistedException(Constants.CAR_DELETION_ERROR);
        }
    }

    public void updateMileage(Long carId, Double carsMileage) {
        carRepository.updateMileage(carId, carsMileage);
    }
}
