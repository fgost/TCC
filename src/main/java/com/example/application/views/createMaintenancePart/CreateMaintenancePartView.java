package com.example.application.views.createMaintenancePart;

import com.example.application.backend.car.domain.CarEntity;
import com.example.application.backend.car.repository.CarRepository;
import com.example.application.backend.maintenancePart.MaintenancePartFacade;
import com.example.application.backend.maintenancePart.domain.MaintenancePartEntity;
import com.example.application.backend.maintenancePart.domain.MaintenancePartStatusEnum;
import com.example.application.backend.type.domain.TypeEnum;
import com.example.application.backend.users.repository.UserRepositoryFront;
import com.example.application.config.security.SecurityConfig;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@PageTitle("Maintenance Register")
@Route(value = "maintenance", layout = MainLayout.class)
@Uses(Icon.class)
@PermitAll
public class CreateMaintenancePartView extends Composite<VerticalLayout> {

    private final CarRepository carRepository;
    private final SecurityConfig securityConfig;
    private final UserRepositoryFront userRepositoryFront;

    public CreateMaintenancePartView(CarRepository carRepository, SecurityConfig securityConfig, UserRepositoryFront userRepositoryFront, MaintenancePartFacade maintenancePartFacade) {
        this.carRepository = carRepository;
        this.securityConfig = securityConfig;
        this.userRepositoryFront = userRepositoryFront;

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setWidthFull();
        mainLayout.addClassName(LumoUtility.Padding.LARGE);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        H3 h3 = new H3("Maintenance Register");

        TextField partNameField = new TextField("Part Name");
        TextField descriptionField = new TextField("Description");
        TextField serialNumberField = new TextField("Serial Number");
        TextField manufacturerField = new TextField("Manufacturer");
        TextField modelField = new TextField("Model");
        DatePicker installationDatePicker = new DatePicker("Installation Date");
        TextField lifeSpanField = new TextField("Life Span");
        TextField costField = new TextField("Cost");

        ComboBox<MaintenancePartStatusEnum> statusPartField = getMaintenancePartStatusEnumComboBox();

        ComboBox<TypeEnum> typeField = new ComboBox<>("Maintenance Type", Arrays.asList(TypeEnum.values()));
        typeField.setItemLabelGenerator(itemType -> {
            if (Objects.requireNonNull(itemType) == TypeEnum.OTHERSPECIALTIES) {
                return "OTHER SPECIALTIES";
            }
            if (Objects.requireNonNull(itemType) == TypeEnum.AIRCONDITIONING) {
                return "AIR CONDITIONING";
            }
            return itemType.toString();
        });

        TextField mileageField = new TextField("Mileage");

        ComboBox<CarEntity> carField = new ComboBox<>("Car");
        List<CarEntity> cars = locateCars();  // Substitua isso pelo método que retorna suas entidades Car
        carField.setItems(cars);
        carField.setItemLabelGenerator(CarEntity::getAutoMaker);

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        saveButton.addClickListener(event -> {
            if (partNameField.isEmpty() || descriptionField.isEmpty() || serialNumberField.isEmpty() ||
                    manufacturerField.isEmpty() || modelField.isEmpty() || installationDatePicker.isEmpty() ||
                    lifeSpanField.isEmpty() || costField.isEmpty() || statusPartField.isEmpty() ||
                    typeField.isEmpty() || mileageField.isEmpty() || carField.isEmpty()) {
                Notification.show("Please fill in all fields.", 3000, Notification.Position.TOP_CENTER);
            } else {
                MaintenancePartEntity partEntity = new MaintenancePartEntity();
                partEntity.setName(partNameField.getValue());
                partEntity.setDescription(descriptionField.getValue());
                partEntity.setSerialNumber(serialNumberField.getValue());
                partEntity.setManufacturer(manufacturerField.getValue());
                partEntity.setModel(modelField.getValue());
                partEntity.setInstallationDate(installationDatePicker.getValue().toString());
                partEntity.setLifeSpan(Integer.parseInt(lifeSpanField.getValue()));
                partEntity.setCost(Double.parseDouble(costField.getValue()));
                partEntity.setStatus(statusPartField.getValue());
                partEntity.setType(typeField.getValue());
                partEntity.setCar(carField.getValue().getId());

                maintenancePartFacade.insert(partEntity);

                Notification.show("Maintenance Registered successfully!", 3000, Notification.Position.TOP_CENTER);

                UI.getCurrent().navigate("/");
            }
        });

        cancelButton.addClickListener(event -> {
            partNameField.clear();
            descriptionField.clear();
            serialNumberField.clear();
            manufacturerField.clear();
            modelField.clear();
            installationDatePicker.clear();
            lifeSpanField.clear();
            costField.clear();
            statusPartField.clear();
            typeField.clear();
            mileageField.clear();
            carField.clear();
        });

        FormLayout formLayout = new FormLayout();
        formLayout.add(partNameField, descriptionField, serialNumberField, manufacturerField, modelField, installationDatePicker, lifeSpanField, costField, statusPartField, typeField, mileageField, carField);
        formLayout.setResponsiveSteps(
                // Use one column by default
                new FormLayout.ResponsiveStep("0", 1),
                // Use two columns, if layout's width exceeds 500px
                new FormLayout.ResponsiveStep("900px", 3));
// Stretch the username field over 2 columns
        //formLayout.setColspan(modelField, 1);

        FormLayout buttonLayout = new FormLayout();
        buttonLayout.add(saveButton, cancelButton);

        mainLayout.add(h3, formLayout, buttonLayout);

        getContent().add(mainLayout);


    }

    private static ComboBox<MaintenancePartStatusEnum> getMaintenancePartStatusEnumComboBox() {
        ComboBox<MaintenancePartStatusEnum> statusPartField = new ComboBox<>("Status Part", Arrays.asList(MaintenancePartStatusEnum.values()));
        statusPartField.setItemLabelGenerator(itemStatus -> {
            if (Objects.requireNonNull(itemStatus) == MaintenancePartStatusEnum.URGENT_REPLACEMENT) {
                return "URGENT REPLACEMENT";
            }
            if (Objects.requireNonNull(itemStatus) == MaintenancePartStatusEnum.HALF_LIFE) {
                return "HALF LIFE";
            }
            return itemStatus.toString();
        });
        return statusPartField;
    }

    private List<CarEntity> locateCars() {
        var user = this.securityConfig.getAuthenticatedUser();
        var idUser = userRepositoryFront.findByEmail(user);
        return carRepository.findByUsuario(idUser.getId());
    }
}


