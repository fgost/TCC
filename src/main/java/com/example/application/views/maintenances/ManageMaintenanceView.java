package com.example.application.views.maintenances;

import com.example.application.backend.car.domain.CarEntity;
import com.example.application.backend.car.repository.CarRepository;
import com.example.application.backend.maintenancePart.domain.MaintenancePartEntity;
import com.example.application.backend.maintenancePart.domain.MaintenancePartStatusEnum;
import com.example.application.backend.maintenancePart.repository.MaintenancePartRepository;
import com.example.application.backend.maintenancePart.service.MaintenancePartService;
import com.example.application.backend.type.domain.TypeEnum;
import com.example.application.backend.users.domain.UserEntity;
import com.example.application.backend.users.repository.UserRepositoryFront;
import com.example.application.backend.users.service.UserService;
import com.example.application.config.security.SecurityConfig;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@PageTitle("Manage Maintenances")
@Route(value = "manage-maintenances", layout = MainLayout.class)
@Uses(Icon.class)
@PermitAll
public class ManageMaintenanceView extends Composite<VerticalLayout> {
    private final Div noMaintenancesMessage = new Div();
    private final MaintenancePartService maintenancePartService;
    private final UserService userService;
    private final MaintenancePartRepository maintenancePartRepository;
    private final SecurityConfig securityConfig;
    private final UserRepositoryFront userRepositoryFront;
    private final Grid<MaintenancePartEntity> maintenanceGrid = new Grid<>(MaintenancePartEntity.class);
    private final ComboBox<CarEntity> carComboBox = new ComboBox<>("Select By Model");
    private final ComboBox<CarEntity> licensePlateComboBox = new ComboBox<>("Select By Licence Plate");

    @Autowired
    public ManageMaintenanceView(MaintenancePartRepository maintenancePartRepository, CarRepository carRepository, MaintenancePartService maintenancePartService, UserService userService, SecurityConfig securityConfig, UserRepositoryFront userRepositoryFront1) {
        this.maintenancePartRepository = maintenancePartRepository;
        this.maintenancePartService = maintenancePartService;
        this.userService = userService;
        this.securityConfig = securityConfig;
        this.userRepositoryFront = userRepositoryFront1;

        List<CarEntity> cars = carRepository.findByUserOwner(getAuthenticatedUser().getId());
        cars.sort(Comparator.comparing(CarEntity::getCarModel));

        maintenanceGrid.removeAllColumns();
        maintenanceGrid.addColumn(MaintenancePartEntity::getName).setHeader("Name");
        maintenanceGrid.addColumn(MaintenancePartEntity::getStatus).setHeader("Status");
        maintenanceGrid.addComponentColumn(maintenancePart -> createButtonWithIcon(maintenancePart, carComboBox.getValue())).setAutoWidth(true).setHeader("Edit");
        maintenanceGrid.addComponentColumn(maintenancePart -> createDeleteButton(maintenancePart, licensePlateComboBox.getValue())).setAutoWidth(true).setHeader("Delete");

        carComboBox.setItemLabelGenerator(CarEntity::getCarModel);
        carComboBox.setItems(cars);

        licensePlateComboBox.setItemLabelGenerator(CarEntity::getLicencePlate);
        licensePlateComboBox.setItems(cars);

        carComboBox.addValueChangeListener(event -> {
            updateMaintenanceGrid(event.getValue());
            licensePlateComboBox.setValue(event.getValue());
        });

        licensePlateComboBox.addValueChangeListener(event -> {
            updateMaintenanceGrid(event.getValue());
            carComboBox.setValue(event.getValue());
        });

        if (!cars.isEmpty()) {
            carComboBox.setValue(cars.get(0));
            licensePlateComboBox.setValue(cars.get(0));
            updateMaintenanceGrid(cars.get(0));
        }

        noMaintenancesMessage.setText("No maintenance records found for the selected vehicle.");
        noMaintenancesMessage.getStyle().set("color", "red");

        HorizontalLayout comboBoxLayout = new HorizontalLayout(carComboBox, licensePlateComboBox);
        comboBoxLayout.setWidthFull();
        comboBoxLayout.setSpacing(true);

        getContent().add(comboBoxLayout, noMaintenancesMessage, maintenanceGrid);
    }

    private Button createButtonWithIcon(MaintenancePartEntity maintenancePart, CarEntity carEntity) {
        Button button = new Button(new Icon(VaadinIcon.PENCIL));
        button.addClickListener(event -> showEditCarDialog(maintenancePart, carEntity));
        button.setMinWidth("10px");
        return button;
    }

    private Button createDeleteButton(MaintenancePartEntity maintenancePart, CarEntity carEntity) {
        Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
        deleteButton.addClickListener(event -> showConfirmationDialog(carEntity, maintenancePart));
        deleteButton.setMinWidth("10px");
        return deleteButton;
    }

    private void showEditCarDialog(MaintenancePartEntity maintenancePartEntity, CarEntity carEntity) {
        Dialog dialog = new Dialog();
        dialog.setModal(true);

        TextField nameField = new TextField("Name");
        nameField.setValue(maintenancePartEntity.getName());

        TextField description = new TextField("Description");
        description.setValue(maintenancePartEntity.getDescription());

        TextField serialNumber = new TextField("Serial Number");
        serialNumber.setValue(maintenancePartEntity.getSerialNumber());

        TextField manufacturer = new TextField("Manufacturer");
        manufacturer.setValue(maintenancePartEntity.getSerialNumber());

        TextField model = new TextField("Model");
        model.setValue(maintenancePartEntity.getSerialNumber());

        DatePicker installationDatePicker = new DatePicker("Installation Date");
        installationDatePicker.setValue(LocalDate.parse(maintenancePartEntity.getInstallationDate()));

        TextField lifeSpan = new TextField("LifeSpan");
        lifeSpan.setValue(String.valueOf(maintenancePartEntity.getLifeSpan()));

        TextField cost = new TextField("Cost");
        cost.setValue(String.valueOf(maintenancePartEntity.getCost()));

        ComboBox<MaintenancePartStatusEnum> status = new ComboBox<>("Status", Arrays.asList(MaintenancePartStatusEnum.values()));
        status.setValue(maintenancePartEntity.getStatus());

        ComboBox<TypeEnum > type = new ComboBox<>("Type", Arrays.asList(TypeEnum.values()));
        type.setValue(maintenancePartEntity.getType());


        VerticalLayout layout = new VerticalLayout(nameField, description, serialNumber, manufacturer, model, installationDatePicker, lifeSpan, cost, status, type, new Button("Save", event -> {
            MaintenancePartEntity editedCar = new MaintenancePartEntity();
            editedCar.setName(nameField.getValue());
            editedCar.setDescription(description.getValue());
            editedCar.setSerialNumber(serialNumber.getValue());
            editedCar.setManufacturer(manufacturer.getValue());
            editedCar.setModel(model.getValue());
            editedCar.setInstallationDate(String.valueOf(installationDatePicker.getValue()));
            editedCar.setLifeSpan(Double.parseDouble(lifeSpan.getValue()));
            editedCar.setCost(Double.parseDouble(cost.getValue()));
            editedCar.setStatus(status.getValue());
            editedCar.setType(type.getValue());

            handleEditCar(maintenancePartEntity, editedCar, carEntity);
            dialog.close();
        }), new Button("Cancel", event -> dialog.close()));
        layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        dialog.add(layout);
        dialog.open();
    }

    private void handleEditCar(MaintenancePartEntity maintenancePartEntity, MaintenancePartEntity editedMaintenancePart, CarEntity carEntity) {
        maintenancePartEntity.setCar(editedMaintenancePart.getCar());
        maintenancePartEntity.setStatus(editedMaintenancePart.getStatus());
        maintenancePartEntity.setCost(editedMaintenancePart.getCost());
        maintenancePartEntity.setType(editedMaintenancePart.getType());
        maintenancePartEntity.setModel(editedMaintenancePart.getModel());
        maintenancePartEntity.setLifeSpan(editedMaintenancePart.getLifeSpan());
        maintenancePartEntity.setInstallationDate(editedMaintenancePart.getInstallationDate());
        maintenancePartEntity.setSerialNumber(editedMaintenancePart.getSerialNumber());
        maintenancePartService.update(maintenancePartEntity.getCode(), maintenancePartEntity);
        userService.updateLastUpdateMileage();
        loadCarsData(carEntity);
    }

    private void showConfirmationDialog(CarEntity carEntity, MaintenancePartEntity maintenancePart) {
        Dialog confirmationDialog = new Dialog();
        confirmationDialog.setCloseOnEsc(false);
        confirmationDialog.setCloseOnOutsideClick(false);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);

        Button confirmButton = new Button("Delete Anyway");
        confirmButton.addClickListener(event -> {
            maintenancePartService.deleteByCode(maintenancePart.getCode());
            loadCarsData(carEntity);
            confirmationDialog.close();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(event -> confirmationDialog.close());

        buttonLayout.add(confirmButton, cancelButton);

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        H1 bigMessage = new H1("This action is irreversible!!!\n");
        bigMessage.getStyle().set("font-size", "1.2em");
        layout.add(bigMessage);

        layout.add("\nDo you wish to continue?");
        layout.add(buttonLayout);

        confirmationDialog.add(layout);
        confirmationDialog.open();
    }

    private void updateMaintenanceGrid(CarEntity selectedCar) {
        if (selectedCar != null) {
            List<MaintenancePartEntity> maintenances = maintenancePartRepository.findByCar(selectedCar.getId());

            noMaintenancesMessage.setVisible(maintenances.isEmpty());

            maintenanceGrid.setItems(maintenances);
        } else {
            noMaintenancesMessage.setVisible(false);
            maintenanceGrid.setItems();
        }
    }

    private void loadCarsData(CarEntity carEntity) {
        if (carEntity != null) {
            List<MaintenancePartEntity> maintenancePartEntities = maintenancePartRepository.findByCar(carEntity.getId());
            maintenanceGrid.setItems(maintenancePartEntities);
        }
    }

    private UserEntity getAuthenticatedUser() {
        var user = this.securityConfig.getAuthenticatedUser();
        return userRepositoryFront.findByEmail(user);
    }
}
