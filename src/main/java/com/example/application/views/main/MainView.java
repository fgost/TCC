package com.example.application.views.main;

import com.example.application.backend.car.domain.CarEntity;
import com.example.application.backend.car.repository.CarRepository;
import com.example.application.backend.maintenancePart.domain.MaintenancePartEntity;
import com.example.application.backend.maintenancePart.domain.MaintenancePartStatusEnum;
import com.example.application.backend.maintenancePart.repository.MaintenancePartRepository;
import com.example.application.backend.users.domain.UserEntity;
import com.example.application.backend.users.repository.UserRepositoryFront;
import com.example.application.config.security.SecurityConfig;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.*;
import java.util.stream.Collectors;

@PageTitle("Main")
@Route(value = "", layout = MainLayout.class)
@PermitAll
public class MainView extends VerticalLayout {

    private final Grid<MaintenancePartEntity> partsGrid = new Grid<>(MaintenancePartEntity.class);
    private final Grid<CarEntity> carsGrid = new Grid<>(CarEntity.class);
    private final MaintenancePartRepository maintenancePartRepository;
    private final CarRepository carRepository;
    private final SecurityConfig securityConfig;
    private final UserRepositoryFront userRepositoryFront;

    private CarEntity selectedCar;

    public MainView(MaintenancePartRepository maintenancePartRepository, CarRepository carRepository, SecurityConfig securityConfig, UserRepositoryFront userRepositoryFront) {
        this.maintenancePartRepository = maintenancePartRepository;
        this.carRepository = carRepository;
        this.securityConfig = securityConfig;
        this.userRepositoryFront = userRepositoryFront;

        addClassName("list-view");
        setSizeFull();

        partsGrid.removeAllColumns();
        partsGrid.addColumn(MaintenancePartEntity::getName).setHeader("Name");
        partsGrid.addColumn(maintenancePart -> "CRITICAL")
                .setHeader("Status");
        partsGrid.addComponentColumn(maintenancePart -> {
            Map<String, Double> averageCostByPartName = calculateAverageCostByPartName(loadAllMaintenanceParts());
            return new Text(String.valueOf(averageCostByPartName.getOrDefault(maintenancePart.getName(), 0.0)));
        }).setHeader("Average Cost");

        List<String> licencePlates = locateLicencePlates();
        ComboBox<String> licencePlateComboBox = new ComboBox<>("Select By Licence Plate");
        licencePlateComboBox.setItemLabelGenerator(licencePlate -> licencePlate);

        licencePlateComboBox.setItems(licencePlates);
        if(!licencePlates.isEmpty()) {
            licencePlateComboBox.setValue(licencePlates.get(0));
        }

        licencePlateComboBox.addValueChangeListener(event -> {
            String selectedLicencePlate = event.getValue();
            if (selectedLicencePlate != null) {
                loadMaintenancePartsForLicencePlate(selectedLicencePlate);
            }
        });

        if (!licencePlates.isEmpty()) {
            loadMaintenancePartsForLicencePlate(licencePlates.get(0));
        }

        carsGrid.removeAllColumns();

        HorizontalLayout carsLayout = new HorizontalLayout();
        carsLayout.setWidthFull();

        List<CarEntity> cars = carRepository.findByUserOwner(getAuthenticatedUser().getId());

        cars.sort(Comparator.comparing(CarEntity::getCarModel));

        ComboBox<CarEntity> carSelectionComboBox = new ComboBox<>("Select by Model");
        carSelectionComboBox.setItemLabelGenerator(CarEntity::getCarModel);
        carSelectionComboBox.setItems(cars);

        if(!cars.isEmpty()) {
            carSelectionComboBox.setValue(cars.get(0));
        }

        carSelectionComboBox.addValueChangeListener(event -> {
            CarEntity selectedCar = event.getValue();
            if (selectedCar != null) {
                loadMaintenancePartsForCar(selectedCar);
            }
        });

        carsLayout.add(carSelectionComboBox);
        carsLayout.add(licencePlateComboBox);

        if(!cars.isEmpty()) {
            loadMaintenancePartsForCar(cars.get(0));
        }

        add(carsLayout, new H3("Maintenance Parts"), partsGrid);

        carsGrid.asSingleSelect().addValueChangeListener(event -> {
            selectedCar = event.getValue();
            if (selectedCar != null) {
                carSelectionComboBox.setValue(selectedCar);
                licencePlateComboBox.setValue(selectedCar.getLicencePlate());
                loadMaintenancePartsForCar(selectedCar);
            }
        });

        loadCarsData();
        askForUpdateMileageCars();
    }

    private List<MaintenancePartEntity> loadAllMaintenanceParts() {
        List<CarEntity> cars = carRepository.findByUserOwner(getAuthenticatedUser().getId());
        List<MaintenancePartEntity> allMaintenanceParts = new ArrayList<>();

        for (CarEntity car : cars) {
            allMaintenanceParts.addAll(maintenancePartRepository.findByCar(car.getId()));
        }

        return allMaintenanceParts;
    }

    private Map<String, Double> calculateAverageCostByPartName(List<MaintenancePartEntity> maintenanceParts) {
        if (maintenanceParts.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, List<MaintenancePartEntity>> partsByName = maintenanceParts.stream()
                .collect(Collectors.groupingBy(MaintenancePartEntity::getName));

        return partsByName.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> calculateAverageCost(entry.getValue())
                ));
    }

    private Double calculateAverageCost(List<MaintenancePartEntity> maintenanceParts) {
        if (maintenanceParts.isEmpty()) {
            return 0.0;
        }

        double totalCost = maintenanceParts.stream()
                .mapToDouble(MaintenancePartEntity::getCost)
                .sum();

        return totalCost / maintenanceParts.size();
    }

    private void loadMaintenancePartsForLicencePlate(String licencePlate) {
        CarEntity carEntity = carRepository.findByLicencePlate(licencePlate);
        selectedCar = carEntity;
        List<MaintenancePartEntity> maintenanceParts = loadMaintenancePartsForCar(carEntity);
        partsGrid.setItems(maintenanceParts);

        carsGrid.asSingleSelect().setValue(carEntity);
    }

    private List<MaintenancePartEntity> loadMaintenancePartsForCar(CarEntity carEntity) {
        selectedCar = carEntity;
        List<MaintenancePartEntity> maintenanceParts = maintenancePartRepository.findByCarAndStatus(carEntity.getId(), MaintenancePartStatusEnum.URGENT_REPLACEMENT);
        partsGrid.setItems(maintenanceParts);
        carsGrid.asSingleSelect().setValue(carEntity);
        return maintenanceParts;
    }

    private List<String> locateLicencePlates() {
        List<CarEntity> cars = carRepository.findByUserOwner(getAuthenticatedUser().getId());

        return cars.stream().map(CarEntity::getLicencePlate).sorted()
                .collect(Collectors.toList());
    }

    private void askForUpdateMileageCars() {

        Date currentDate = new Date();
        var currentUser = userRepositoryFront.findByEmail(securityConfig.getAuthenticatedUser());
        var createdAt = currentUser.getCreatedAt();
        var lastUpdateMileage = currentUser.getLastUpdateMileage();
        var differenceLastUpdate = System.currentTimeMillis() - lastUpdateMileage;
        var lastAskForUpdate = currentUser.getLastAskForUpdateMileage();
        var differenceLastAskUpdate = lastAskForUpdate - lastUpdateMileage;
        if (carsGrid.getDataProvider().size(new Query<>()) > 0
                && createdAt != null
                && (currentDate.getTime() - createdAt.getTime()) > (7L * 24 * 60 * 60 * 1000)
                && differenceLastUpdate > (7 * 24 * 60 * 60 * 1000)
                && differenceLastAskUpdate > (24 * 60 * 60 * 1000)) {

            showMessageToUpdateTheMileageCars();
        }
    }

    private void showMessageToUpdateTheMileageCars() {
        Dialog dialog = new Dialog();
        dialog.setModal(true);
        dialog.setHeaderTitle("Updating your car's mileage enhances your experience.");

        VerticalLayout layout = new VerticalLayout(
                new Text("Would you like to update it?"),
                new Button("Yes", event -> {
                    updateMileageCars();
                    dialog.close();

                }), new Button("Cancel", event -> {
            registerNonUpdateMileage();
            dialog.close();
        }));
        layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        dialog.add(layout);
        dialog.open();
    }

    private void updateMileageCars() {
        var carEntity = carRepository.findByUserOwner(getAuthenticatedUser().getId());
        for (CarEntity cars : carEntity) {
            Dialog dialog = new Dialog();
            dialog.setModal(true);

            TextField carNameField = new TextField("Car");
            carNameField.setValue(String.valueOf(cars.getCarModel()));
            carNameField.setEnabled(false);

            TextField carMileageField = new TextField("Mileage");
            carMileageField.setValue(String.valueOf(cars.getMileage()));

            VerticalLayout layout = getVerticalLayout(cars, carMileageField, carNameField, dialog);

            dialog.add(layout);
            dialog.open();
        }
    }

    private VerticalLayout getVerticalLayout(CarEntity cars, TextField carMileageField, TextField carNameField, Dialog dialog) {
        VerticalLayout layout = new VerticalLayout(carNameField, carMileageField, new Button("Save", event -> {
            CarEntity editedCar = new CarEntity();
            editedCar.setMileage(Double.parseDouble(carMileageField.getValue()));
            editedCar.setCarModel(cars.getCarModel());
            editedCar.setYear(cars.getYear());
            editedCar.setAutoMaker(cars.getAutoMaker());
            editedCar.setColor(cars.getColor());
            editedCar.setType(cars.getType());
            dialog.close();
        }), new Button("Cancel", event -> {
            dialog.close();
            registerNonUpdateMileage();
        }));
        layout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        return layout;
    }

    private void registerNonUpdateMileage() {
        updateLastAskForUpdateMileage();
    }

    private void loadCarsData() {
        List<CarEntity> cars = carRepository.findByUserOwner(getAuthenticatedUser().getId());
        carsGrid.setItems(cars);
    }

    private UserEntity getAuthenticatedUser() {
        var user = this.securityConfig.getAuthenticatedUser();
        return userRepositoryFront.findByEmail(user);
    }

    private void updateLastAskForUpdateMileage() {
        var currentUser = securityConfig.getAuthenticatedUser();
        var user = userRepositoryFront.findByEmail(currentUser);
        user.setName(user.getName());
        user.setLastName(user.getLastName());
        user.setEmail(user.getEmail());
        user.setPassword(user.getPassword());
        user.setLastUpdateMileage(user.getLastUpdateMileage());
        user.setLastAskForUpdateMileage(System.currentTimeMillis());
        userRepositoryFront.save(user);
    }
}