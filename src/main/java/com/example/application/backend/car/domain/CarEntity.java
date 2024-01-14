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

    @PrePersist
    private void setCode() {
        this.code = UUID.randomUUID().toString();
    }
}
