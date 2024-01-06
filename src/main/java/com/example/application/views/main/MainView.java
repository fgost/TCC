package com.example.application.views.main;

import com.example.application.backend.autoMaker.AutoMakerEntity;
import com.example.application.backend.autoMaker.AutoMakerRepository;
import com.example.application.backend.car.domain.CarEntity;
import com.example.application.backend.car.domain.CarTypeEnum;
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
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
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
    private final AutoMakerRepository autoMakerRepository;

    private CarEntity selectedCar;

    public MainView(MaintenancePartRepository maintenancePartRepository, CarRepository carRepository, SecurityConfig securityConfig, UserRepositoryFront userRepositoryFront, AutoMakerRepository autoMakerRepository) {
        this.maintenancePartRepository = maintenancePartRepository;
        this.carRepository = carRepository;
        this.securityConfig = securityConfig;
        this.userRepositoryFront = userRepositoryFront;
        this.autoMakerRepository = autoMakerRepository;

        addClassName("list-view");
        setSizeFull();

        // Configurar a grid de peças
        partsGrid.removeAllColumns();
        partsGrid.addColumn(MaintenancePartEntity::getName).setHeader("Name");
        partsGrid.addColumn(maintenancePart -> "CRÍTICO")
                .setHeader("Status");
        partsGrid.addComponentColumn(maintenancePart -> {
            Map<String, Double> averageCostByPartName = calculateAverageCostByPartName(loadAllMaintenanceParts());
            return new Text(String.valueOf(averageCostByPartName.getOrDefault(maintenancePart.getName(), 0.0)));
        }).setHeader("Average Cost");

        // Crie uma caixa de seleção para os licencePlate
        ComboBox<String> licencePlateComboBox = new ComboBox<>("Select By Licence Plate");
        licencePlateComboBox.setItemLabelGenerator(licencePlate -> licencePlate);
        List<String> licencePlates = locateLicencePlates();
        licencePlateComboBox.setItems(licencePlates);
        licencePlateComboBox.setValue(licencePlates.get(0)); // Seleciona o primeiro como padrão

        // Adicione um listener para a caixa de seleção de licencePlate
        licencePlateComboBox.addValueChangeListener(event -> {
            String selectedLicencePlate = event.getValue();
            if (selectedLicencePlate != null) {
                loadMaintenancePartsForLicencePlate(selectedLicencePlate);
            }
        });

        // Carregue as Maintenance Parts para a primeira licencePlate, já que é o padrão
        if (!licencePlates.isEmpty()) {
            loadMaintenancePartsForLicencePlate(licencePlates.get(0));
        }

        // Remova a configuração da coluna de modelo de carro da grid
        carsGrid.removeAllColumns();

        // Crie um layout horizontal para a caixa de seleção de carros
        HorizontalLayout carsLayout = new HorizontalLayout();
        carsLayout.setWidthFull(); // Define a largura total do layout

        List<CarEntity> cars = carRepository.findByUsuario(getAuthenticatedUser().getId());

        // Ordena a lista de carros por nome
        cars.sort(Comparator.comparing(CarEntity::getCarModel));


        // Se houver mais de 1 carro, adicione uma caixa de seleção

        ComboBox<CarEntity> carSelectionComboBox = new ComboBox<>("Select by Model");
        carSelectionComboBox.setItemLabelGenerator(CarEntity::getCarModel);
        carSelectionComboBox.setItems(cars);

        // Seleciona o primeiro da lista como padrão
        carSelectionComboBox.setValue(cars.get(0));

        carSelectionComboBox.addValueChangeListener(event -> {
            CarEntity selectedCar = event.getValue();
            if (selectedCar != null) {
                loadMaintenancePartsForCar(selectedCar);
            }
        });

        carsLayout.add(carSelectionComboBox);
        carsLayout.add(licencePlateComboBox);

        // Carregue as Maintenance Parts para o carro padrão
        loadMaintenancePartsForCar(cars.get(0));

        // Adicione o layout horizontal de carros ao layout vertical principal
        add(carsLayout, new H3("Maintenance Parts"), partsGrid);

        carsGrid.asSingleSelect().addValueChangeListener(event -> {
            selectedCar = event.getValue();
            if (selectedCar != null) {
                carSelectionComboBox.setValue(selectedCar);
                licencePlateComboBox.setValue(selectedCar.getLicencePlate());
                loadMaintenancePartsForCar(selectedCar);
            }
        });

        // Carregar dados
        loadCarsData();
        askForUpdateMileageCars();
    }

    private List<MaintenancePartEntity> loadAllMaintenanceParts() {
        List<CarEntity> cars = carRepository.findByUsuario(getAuthenticatedUser().getId());
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
        // Implemente a lógica para carregar Maintenance Parts com base na licencePlate
        CarEntity carEntity = carRepository.findByLicencePlate(licencePlate);
        selectedCar = carEntity; // Armazena a referência ao carro atualmente selecionado
        List<MaintenancePartEntity> maintenanceParts = loadMaintenancePartsForCar(carEntity);
        partsGrid.setItems(maintenanceParts);

        // Seleciona o carro correspondente na grade de carros
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
        // Implemente a lógica para obter a lista de licencePlates
        List<CarEntity> cars = carRepository.findByUsuario(getAuthenticatedUser().getId());

        return cars.stream().map(CarEntity::getLicencePlate).sorted()  // Ordena as licencePlates em ordem alfabética
                .collect(Collectors.toList());
    }

    private void askForUpdateMileageCars() {
        var currentUser = userRepositoryFront.findByEmail(securityConfig.getAuthenticatedUser());
        var lastUpdateMileage = currentUser.getLastUpdateMileage();
        var differenceLastUpdate = System.currentTimeMillis() - lastUpdateMileage;
        var lastAskForUpdate = currentUser.getLastAskForUpdateMileage();
        var differenceLastAskUpdate = lastAskForUpdate - lastUpdateMileage;
        if (carsGrid.getDataProvider().size(new Query<>()) > 0
                && differenceLastUpdate > (7 * 24 * 60 * 60 * 1000)
                && differenceLastAskUpdate > (24 * 60 * 60 * 1000)) {
            showMessageToUpdateTheMileageCars();
        }
    }

    private void showMessageToUpdateTheMileageCars() {
        // Seu código para exibir o diálogo
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
        var carEntity = carRepository.findByUsuario(getAuthenticatedUser().getId());
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
            handleEditCar(cars, editedCar);
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
        List<CarEntity> cars = carRepository.findByUsuario(getAuthenticatedUser().getId());
        carsGrid.setItems(cars);
    }

    private UserEntity getAuthenticatedUser() {
        var user = this.securityConfig.getAuthenticatedUser();
        return userRepositoryFront.findByEmail(user);
    }

    private void showConfirmationDialog(CarEntity carEntity) {
        Dialog dialog = new Dialog();
        dialog.setModal(true);

        VerticalLayout layout = new VerticalLayout(new Text("Do you really want to delete this car?"), new Button("Yes", event -> {
            handleDeleteCar(carEntity);
            dialog.close();
        }), new Button("Cancel", event -> dialog.close()));
        layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        dialog.add(layout);
        dialog.open();
    }

    private void handleDeleteCar(CarEntity carEntity) {
        carRepository.delete(carEntity);
        loadCarsData();
    }

    private void showEditCarDialog(CarEntity carEntity) {
        Dialog dialog = new Dialog();
        dialog.setModal(true);

        // Adicione os campos de edição necessários, como TextField, ComboBox, etc.
        TextField carModelField = new TextField("Model");
        carModelField.setValue(carEntity.getCarModel());

        TextField carYearField = new TextField("Year");
        carYearField.setValue(carEntity.getYear());

        TextField carColorField = new TextField("Color");
        carColorField.setValue(carEntity.getColor());

        TextField carMileageField = new TextField("Mileage");
        carMileageField.setValue(String.valueOf(carEntity.getMileage()));

        var autoMaker = autoMakerRepository.findByName(carEntity.getAutoMaker());
        ComboBox<AutoMakerEntity> carAutoMakerField = new ComboBox<>("Auto Maker");
        carAutoMakerField.setItemLabelGenerator(AutoMakerEntity::getName);
        ListDataProvider<AutoMakerEntity> dataProvider = DataProvider.ofCollection(autoMakerRepository.findAll());
        carAutoMakerField.setItems(dataProvider.getItems());
        carAutoMakerField.setValue(autoMaker);

        ComboBox<CarTypeEnum> carTypeField = new ComboBox<>("Type", Arrays.asList(CarTypeEnum.values()));
        carTypeField.setItemLabelGenerator(item -> {
            if (Objects.requireNonNull(item) == CarTypeEnum.PICKUP_TRUCK) {
                return "PICKUP/TRUCK";
            }
            return item.toString();
        });
        carTypeField.setValue(carEntity.getType());

        VerticalLayout layout = new VerticalLayout(carModelField, carYearField, carAutoMakerField, carColorField, carTypeField, carMileageField, new Button("Save", event -> {
            CarEntity editedCar = new CarEntity();
            editedCar.setCarModel(carModelField.getValue());
            editedCar.setYear(carYearField.getValue());
            editedCar.setAutoMaker(carAutoMakerField.getValue().getName());
            editedCar.setColor(carColorField.getValue());
            editedCar.setType(carTypeField.getValue());
            editedCar.setMileage(Double.parseDouble(carMileageField.getValue()));
            handleEditCar(carEntity, editedCar);
            dialog.close();
        }), new Button("Cancel", event -> dialog.close()));
        layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        dialog.add(layout);
        dialog.open();
    }

    private void handleEditCar(CarEntity carEntity, CarEntity editedCar) {
        // Implemente a lógica para editar o carro
        carEntity.setCarModel(editedCar.getCarModel());
        carEntity.setYear(editedCar.getYear());
        carEntity.setAutoMaker(editedCar.getAutoMaker());
        carEntity.setColor(editedCar.getColor());
        carEntity.setType(editedCar.getType());
        carEntity.setMileage(editedCar.getMileage());
        carRepository.save(carEntity);
        updateLastUpdateMileage();
        loadCarsData();
    }

    private void updateLastUpdateMileage() {
        var currentUser = securityConfig.getAuthenticatedUser();
        var user = userRepositoryFront.findByEmail(currentUser);
        user.setName(user.getName());
        user.setLastName(user.getLastName());
        user.setEmail(user.getEmail());
        user.setPassword(user.getPassword());
        user.setLastUpdateMileage(System.currentTimeMillis());
        userRepositoryFront.save(user);
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