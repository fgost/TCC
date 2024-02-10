package com.example.application.backend.car.repository;

import com.example.application.backend.car.domain.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing car entities in the database. The CarRepository interface defines methods to
 * interact with car entities in the database, such as finding cars by code, user owner, year, car model, or
 * license plate.
 *
 * @author m.firmiano@aluno.ifsp.edu.br
 *
 * @see org.springframework.data.jpa.repository.JpaRepository - Spring Data JPA interface for generic CRUD operations
 * @see com.example.application.backend.car.domain.CarEntity - Entity representing a car
 * @see java.util.List - Java interface for ordered collections
 * @see java.util.Optional - Java container object that may or may not contain a non-null value
 */
public interface CarRepository extends JpaRepository<CarEntity, Long> {

    /**
     * Retrieves an optional car entity based on the provided external code.
     *
     * @param code - String representing the external code (UUID) of the car.
     * @return {@link Optional<CarEntity>} - An optional containing the car entity, or empty if not found.
     */
    Optional<CarEntity> findByCode(String code);

    /**
     * Retrieves a list of all car entities in the database.
     *
     * @return {@link List<CarEntity>} - A list containing all car entities.
     */
    List<CarEntity> findAll();

    /**
     * Retrieves a list of car entities owned by the specified user.
     *
     * @param id - Long representing the ID of the user owner.
     * @return {@link List<CarEntity>} - A list containing car entities owned by the user.
     */
    List<CarEntity> findByUserOwner(Long id);

    /**
     * Retrieves a list of car entities with years containing the specified string, ignoring case.
     *
     * @param year - String representing the year to search for in car entities.
     * @return {@link List<CarEntity>} - A list containing car entities with matching years.
     */
    List<CarEntity> findByYearContainingIgnoreCase(String year);

    /**
     * Retrieves a list of car entities with car models containing the specified string, ignoring case.
     *
     * @param carModel - String representing the car model to search for in car entities.
     * @return {@link List<CarEntity>} - A list containing car entities with matching car models.
     */
    List<CarEntity> findByCarModelContainingIgnoreCase(String carModel);

    /**
     * Retrieves a list of car entities with car models or years containing the specified strings, ignoring case.
     *
     * @param carModel - String representing the car model to search for in car entities.
     * @param year - String representing the year to search for in car entities.
     * @return {@link List<CarEntity>} - A list containing car entities with matching car models or years.
     */
    List<CarEntity> findByCarModelContainingIgnoreCaseOrYearContainingIgnoreCase(String carModel, String year);

    /**
     * Retrieves a car entity based on the provided license plate.
     *
     * @param licencePlate - String representing the license plate of the car.
     * @return {@link CarEntity} - The car entity with the specified license plate.
     */
    CarEntity findByLicencePlate(String licencePlate);
}
