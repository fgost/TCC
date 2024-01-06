package com.example.application.backend.maintenancePart.model;

import com.example.application.backend.maintenancePart.domain.MaintenancePartStatusEnum;
import com.example.application.backend.type.domain.TypeEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class MaintenancePartRequest {

    @NotBlank(message = "{maintenance.part.name.not.null}")
    @Size(max = 100, message = "{maintenance.part.name.max.size}")
    private String name;

    @NotBlank(message = "{maintenance.part.description.not.null}")
    @Size(max = 100, message = "{maintenance.part.description.max.size}")
    private String description;

    @NotBlank(message = "{maintenance.part.serial.number.not.null}")
    @Size(max = 100, message = "{maintenance.part.serial.number.max.size}")
    private String serialNumber;

   @NotBlank(message = "{maintenance.part.manufacturer.not.null}")
    @Size(max = 100, message = "{maintenance.part.manufacturer.max.size}")
    private String manufacturer;

    @NotBlank(message = "{maintenance.part.model.not.null}")
    @Size(max = 100, message = "{maintenance.part.model.max.size}")
    private String model;

    @NotBlank(message = "{maintenance.part.installation.date.not.null}")
    @Size(max = 100, message = "{maintenance.part.installation.date.max.size}")
    private String installationDate;

    @NotNull(message = "{maintenance.part.life.span.not.null}")
    private int lifeSpan;

    @DecimalMin(value = "0.0", inclusive = true, message = "{maintenance.part.cost.positive}")
    @Digits(integer = 10, fraction = 2, message = "{maintenance.part.cost.format}")
    private double cost;

    @NotNull(message = "{maintenance.part.status.not.null}")
    private MaintenancePartStatusEnum status;

    @NotNull(message = "{category.not.null}")
    private TypeEnum type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(String installationDate) {
        this.installationDate = installationDate;
    }

    public int getLifeSpan() {
        return lifeSpan;
    }

    public void setLifeSpan(int lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public MaintenancePartStatusEnum getStatus() {
        return status;
    }

    public void setStatus(MaintenancePartStatusEnum status) {
        this.status = status;
    }

    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }
}