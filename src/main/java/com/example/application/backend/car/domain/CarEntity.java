package com.example.application.backend.car.domain;

import com.example.application.backend.category.domain.CategoryEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
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
    private long usuario;
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

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public void setUsuario(long usuario) {
        this.usuario = usuario;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public void setAutoMaker(String autoMaker) {
        this.autoMaker = autoMaker;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setType(CarTypeEnum type) {
        this.type = type;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public void setCategories(List<CategoryEntity> categories) {
        this.categories = categories;
    }
}
