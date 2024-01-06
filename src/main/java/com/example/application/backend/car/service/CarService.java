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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class CarService {
    private CarRepository carRepository;
    private CategoryRepository categoryRepository;
    private CategoryService categoryService;
    private JdbcTemplate jdbcTemplate;
    private UserRepositoryFront userRepositoryFront;

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
        } else if (hasCarModel) {
            return carRepository.findByCarModelContainingIgnoreCase(carModel);
        } else {
            return carRepository.findAll();
        }
    }

    public CarEntity findByCode(String code) {
        var entity = carRepository.findByCode(code)
                .orElseThrow(() -> new ObjectNotFoundException(Constants.CAR_NOT_FOUND));
        return entity;
    }

    @Transactional
    public CarEntity insert(CarEntity carEntity, String user) {
        try {
            var userDb = userRepositoryFront.findByEmail(user);
            carEntity.setUsuario(userDb.getId());
            CarEntity savedCar = carRepository.save(carEntity);

            List<Integer> categoryIds = categoryRepository.findAllIds();

            for (Integer categoryId : categoryIds) {
                String sql = "INSERT INTO categories_cars (car_id, category_id) VALUES (?, ?)";
                jdbcTemplate.update(sql, savedCar.getId(), categoryId);
            }

            return savedCar;
        } catch (DataIntegrityViolationException ex) {
            throw ExceptionUtils.buildSameIdentifierException(Constants.CAR_DUPLICATED);
        }
    }

    @Transactional
    public CarEntity update(String code, CarEntity entity) {
        var existentEntity = findByCode(code);
        existentEntity.setCode(code);
        existentEntity.setCarModel(entity.getCarModel());
        existentEntity.setType(entity.getType());
        existentEntity.setColor(entity.getColor());
        existentEntity.setAutoMaker(entity.getAutoMaker());
        existentEntity.setYear(entity.getYear());
        existentEntity.setMileage(entity.getMileage());
        try {
            return carRepository.save(existentEntity);
        } catch (Exception e) {
            throw ExceptionUtils.buildNotPersistedException(Constants.CAR_NOT_PERSISTED);
        }
    }

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

    @Transactional
    public void deleteByCode(String code) {
        try {
            var entity = findByCode(code);
            carRepository.delete(entity);
        } catch (Exception e) {
            throw ExceptionUtils.buildNotPersistedException(Constants.CAR_DELETION_ERROR);
        }
    }
}
