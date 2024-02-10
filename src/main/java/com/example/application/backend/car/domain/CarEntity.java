package com.example.application.backend.car.domain;

import com.example.application.backend.category.domain.CategoryEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Entity class representing a car in the application. Each instance of this class corresponds to a record in the
 * "cars" table in the database. The class includes JPA annotations for entity mapping and defines the relationships
 * with other entities. The `PrePersist` annotated method is used to set a unique code (UUID) before persisting a new
 * entity.
 *
 * @author m.firmiano@aluno.ifsp.edu.br
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cars")
public class CarEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String carModel;
    private String autoMaker;
    private String year;
    private String color;
    private String motor;
    private String licencePlate;
    private double mileage;
    private CarTypeEnum type;
    private long userOwner;
    @ManyToMany
    @JoinTable(
            name = "categories_cars",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<CategoryEntity> categories;

    /**
     * Sets a unique code (UUID) before persisting a new entity. This method is annotated with `PrePersist` to ensure
     * that the code is set before saving the entity to the database.
     */
    @PrePersist
    private void setCode() {
        this.code = UUID.randomUUID().toString();
    }
}
