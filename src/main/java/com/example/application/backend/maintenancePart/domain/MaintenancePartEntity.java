package com.example.application.backend.maintenancePart.domain;

import com.example.application.backend.type.domain.TypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parts")
public class MaintenancePartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    private String description;
    private String serialNumber;
    private String manufacturer;
    private String model;
    private String installationDate;
    private double lifeSpan;
    private double cost;
    private MaintenancePartStatusEnum status;
    private TypeEnum type;
    private long car;
    private LifeSpanEnum lifeSpanType;
    private double limiteParaAlerta;
    private double limiteParaUrgencia;

    @PrePersist
    private void setCode() {
        this.code = UUID.randomUUID().toString();
    }
}
