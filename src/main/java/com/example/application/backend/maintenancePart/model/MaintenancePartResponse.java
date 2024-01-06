package com.example.application.backend.maintenancePart.model;

import com.example.application.backend.maintenancePart.domain.MaintenancePartStatusEnum;
import com.example.application.backend.type.domain.TypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MaintenancePartResponse {

    private String code;
    private String name;
    private String description;
    private String serialNumber;
    private String manufacturer;
    private String model;
    private String installationDate;
    private int lifeSpan;
    private double cost;
    private MaintenancePartStatusEnum status;
    private TypeEnum type;

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setInstallationDate(String installationDate) {
        this.installationDate = installationDate;
    }

    public void setLifeSpan(int lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setStatus(MaintenancePartStatusEnum status) {
        this.status = status;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }
}
