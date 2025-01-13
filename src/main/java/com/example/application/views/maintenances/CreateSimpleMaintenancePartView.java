package com.example.application.views.maintenances;

import com.example.application.backend.car.domain.CarEntity;
import com.example.application.backend.car.service.CarService;
import com.example.application.backend.maintenancePart.MaintenancePartFacade;
import com.example.application.backend.maintenancePart.domain.DetailedMaintenanceEnum;
import com.example.application.backend.maintenancePart.domain.LifeSpanEnum;
import com.example.application.backend.maintenancePart.domain.MaintenancePartEntity;
import com.example.application.backend.maintenancePart.domain.MaintenancePartStatusEnum;
import com.example.application.backend.type.domain.TypeEnum;
import com.example.application.backend.users.domain.UserEntity;
import com.example.application.backend.users.service.UserService;
import com.example.application.config.security.SecurityConfig;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.AbstractField;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@PageTitle("Maintenance Register")
@Route(value = "simple-maintenance", layout = MainLayout.class)
@Uses(Icon.class)
@PermitAll
public class CreateSimpleMaintenancePartView extends Composite<VerticalLayout> {

    @Autowired
    private final SecurityConfig securityConfig;
    @Autowired
    private final CarService carService;
    @Autowired
    private final UserService userService;
    private final VerticalLayout mainLayout = new VerticalLayout();

    private final TextField partNameField = new TextField("Part Name");
    private final DatePicker installationDatePicker = new DatePicker("Installation Date");
    private final TextField lifeSpanField = new TextField("Life Span");
    private final ComboBox<LifeSpanEnum> lifeSpanType = getLifeSpanEnumComboBox();
    private final TextField costField = new TextField("Cost");
    private final ComboBox<MaintenancePartStatusEnum> statusPartField;
    private final ComboBox<TypeEnum> typeField;
    private final TextField mileageField = new TextField("Mileage");
    private final ComboBox<CarEntity> carField = new ComboBox<>("Car");

    private final Button saveButton;
    private final Button cancelButton;

    public CreateSimpleMaintenancePartView(UserService userService, SecurityConfig securityConfig, MaintenancePartFacade maintenancePartFacade, CarService carService) {
        this.securityConfig = securityConfig;
        this.carService = carService;
        this.userService = userService;

        mainLayout.setWidthFull();
        mainLayout.addClassName(LumoUtility.Padding.LARGE);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        H3 h3 = new H3("Maintenance Register");

        carField.setItems(locateCars());
        carField.setItemLabelGenerator(CarEntity::getAutoMaker);
        carField.setVisible(true);
        carField.setEnabled(true);

        typeField = new ComboBox<>("Maintenance Type", Arrays.asList(TypeEnum.values()));
        typeField.setItemLabelGenerator(CreateSimpleMaintenancePartView::setTypeAirConditioningOtherSpecialist);
        typeField.setVisible(true);
        typeField.setEnabled(true);

        partNameField.setVisible(true);
        partNameField.setEnabled(true);

        statusPartField = getMaintenancePartStatusEnumComboBox();
        statusPartField.setVisible(false);
        statusPartField.setEnabled(false);

        installationDatePicker.setVisible(false);
        installationDatePicker.setEnabled(false);

        lifeSpanField.setVisible(false);
        lifeSpanField.setEnabled(false);

        lifeSpanType.setVisible(false);
        lifeSpanType.setEnabled(false);

        costField.setVisible(false);
        costField.setEnabled(false);

        mileageField.setVisible(false);
        mileageField.setEnabled(false);

        saveButton = new Button("Save");
        saveButton.setEnabled(false);
        cancelButton = new Button("Cancel");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        saveButton.addClickListener(event ->
                saveNewMaintenance(maintenancePartFacade, carService));

        cancelButton.addClickListener(event -> cleanForm());

        partNameField.addValueChangeListener(this::enableStatusPartField);
        lifeSpanType.addValueChangeListener(this::enableCostField);
        costField.addValueChangeListener(this::enableSaveButton);

        FormLayout formLayout = new FormLayout();
        formLayout.add(carField, typeField, partNameField, lifeSpanField, lifeSpanType, statusPartField, costField, installationDatePicker, mileageField);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("900px", 3)
        );

        FormLayout buttonLayout = new FormLayout();
        buttonLayout.add(saveButton, cancelButton);

        mainLayout.add(h3, formLayout, buttonLayout);

        getContent().add(mainLayout);
    }

    private void enableCostField(AbstractField.ComponentValueChangeEvent<ComboBox<LifeSpanEnum>, LifeSpanEnum> event) {
        LifeSpanEnum cost = event.getValue();
        if (cost != null) {
            costField.setVisible(true);
            costField.setEnabled(true);

            installationDatePicker.setVisible(true);
            installationDatePicker.setEnabled(true);

            mileageField.setVisible(true);
            mileageField.setEnabled(true);
        } else {
            lifeSpanField.setVisible(false);
            lifeSpanField.setEnabled(false);

            lifeSpanType.setVisible(false);
            lifeSpanType.setEnabled(false);

            mileageField.setVisible(false);
            mileageField.setEnabled(false);

            carField.setVisible(false);
            carField.setEnabled(false);

            saveButton.setEnabled(false);
        }
    }

    private void enableSaveButton(AbstractField.ComponentValueChangeEvent<TextField, String> event) {
        String mileage = event.getValue();
        saveButton.setEnabled(mileage != null);
    }



    private void enableStatusPartField(AbstractField.ComponentValueChangeEvent<TextField, String> event) {
        String partName = event.getValue();
        if (partName != null && partName.length() >= 3) {
            statusPartField.setVisible(true);
            statusPartField.setEnabled(true);

            lifeSpanField.setVisible(true);
            lifeSpanField.setEnabled(true);

            lifeSpanType.setVisible(true);
            lifeSpanType.setEnabled(true);
        } else {
            carField.setVisible(true);
            carField.setEnabled(true);

            getMaintenancePartStatusEnumComboBox().setVisible(true);
            getMaintenancePartStatusEnumComboBox().setEnabled(true);

            partNameField.setVisible(true);
            partNameField.setEnabled(true);

            statusPartField.setVisible(false);
            statusPartField.setEnabled(false);

            costField.setVisible(false);
            costField.setEnabled(false);

            installationDatePicker.setVisible(false);
            installationDatePicker.setEnabled(false);

            lifeSpanField.setVisible(false);
            lifeSpanField.setEnabled(false);

            mileageField.setVisible(false);
            mileageField.setEnabled(false);

            saveButton.setEnabled(false);
        }
    }

    private void cleanForm() {
        partNameField.clear();
        installationDatePicker.clear();
        lifeSpanField.clear();
        lifeSpanType.clear();
        costField.clear();
        statusPartField.clear();
        typeField.clear();
        mileageField.clear();
        carField.clear();
    }

    private void saveNewMaintenance(MaintenancePartFacade maintenancePartFacade, CarService carService) {
        if (partNameField.isEmpty() || installationDatePicker.isEmpty() ||
                lifeSpanField.isEmpty() || lifeSpanType.isEmpty() || costField.isEmpty() || statusPartField.isEmpty() ||
                typeField.isEmpty() || mileageField.isEmpty() || carField.isEmpty()) {
            Notification.show("Please fill in all fields.", 3000, Notification.Position.TOP_CENTER);
        } else {
            MaintenancePartEntity partEntity = getMaintenancePartEntity();
            double mileageUpdated = Double.parseDouble(mileageField.getValue());
            CarEntity car = carField.getValue();
            car.setMileage(mileageUpdated);

            carService.update(car.getCode(), car);
            maintenancePartFacade.insert(partEntity);

            Notification.show("Maintenance Registered successfully!", 3000, Notification.Position.TOP_CENTER);

            UI.getCurrent().navigate("/");
        }
    }

    private MaintenancePartEntity getMaintenancePartEntity() {
        MaintenancePartEntity partEntity = new MaintenancePartEntity();
        partEntity.setName(partNameField.getValue());
        partEntity.setInstallationDate(installationDatePicker.getValue().toString());
        partEntity.setLifeSpan(Double.parseDouble(lifeSpanField.getValue()));
        partEntity.setLifeSpanType(lifeSpanType.getValue());
        partEntity.setCost(Double.parseDouble(costField.getValue()));
        partEntity.setStatus(statusPartField.getValue());
        partEntity.setType(typeField.getValue());
        partEntity.setCar(carField.getValue().getId());
        partEntity.setDetailedMaintenance(DetailedMaintenanceEnum.SIMPLE);
        return partEntity;
    }

    private static String setTypeAirConditioningOtherSpecialist(TypeEnum itemType) {
        if (Objects.requireNonNull(itemType) == TypeEnum.OTHERSPECIALTIES) {
            return "OTHER SPECIALTIES";
        }
        if (Objects.requireNonNull(itemType) == TypeEnum.AIRCONDITIONING) {
            return "AIR CONDITIONING";
        }
        return itemType.toString();
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

    private static ComboBox<LifeSpanEnum> getLifeSpanEnumComboBox() {
        return new ComboBox<>("Life Span Type", Arrays.asList(LifeSpanEnum.values()));
    }

    private List<CarEntity> locateCars() {
        String user = this.securityConfig.getAuthenticatedUser();
        UserEntity idUser = userService.findByEmail(user);
        return carService.findByUserOwner(idUser.getId());
    }
}
