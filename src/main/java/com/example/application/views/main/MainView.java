package com.example.application.views.main;

import com.example.application.backend.autoModel.service.AutoModelService;
import com.example.application.backend.car.domain.CarEntity;
import com.example.application.backend.car.repository.CarRepository;
import com.example.application.backend.maintenancePart.domain.DetailedMaintenanceEnum;
import com.example.application.backend.maintenancePart.domain.LifeSpanEnum;
import com.example.application.backend.maintenancePart.domain.MaintenancePartEntity;
import com.example.application.backend.maintenancePart.domain.MaintenancePartStatusEnum;
import com.example.application.backend.maintenancePart.repository.MaintenancePartRepository;
import com.example.application.backend.maintenancePart.service.MaintenancePartService;
import com.example.application.backend.part.service.PartService;
import com.example.application.backend.type.domain.TypeEnum;
import com.example.application.backend.users.domain.UserEntity;
import com.example.application.backend.users.repository.UserRepositoryFront;
import com.example.application.config.security.SecurityConfig;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@PageTitle("Main")
@Route(value = "", layout = MainLayout.class)
@PermitAll
public class MainView extends VerticalLayout {

    private final Grid<MaintenancePartEntity> partsGrid = new Grid<>(MaintenancePartEntity.class);
    private final Grid<CarEntity> carsGrid = new Grid<>(CarEntity.class);
    private final MaintenancePartRepository maintenancePartRepository;
    private final MaintenancePartService maintenancePartService;
    private final CarRepository carRepository;
    private final SecurityConfig securityConfig;
    private final UserRepositoryFront userRepositoryFront;
    private final AutoModelService autoModelService;
    private final PartService partService;

    private CarEntity selectedCar;

    public MainView(PartService partService, AutoModelService autoModelService, MaintenancePartRepository maintenancePartRepository, MaintenancePartService maintenancePartService, CarRepository carRepository, SecurityConfig securityConfig, UserRepositoryFront userRepositoryFront) {
        this.maintenancePartRepository = maintenancePartRepository;
        this.maintenancePartService = maintenancePartService;
        this.carRepository = carRepository;
        this.securityConfig = securityConfig;
        this.userRepositoryFront = userRepositoryFront;
        this.autoModelService = autoModelService;
        this.partService = partService;
        addClassName("list-view");
        setSizeFull();

        autoModelService.verificarDuplicidade();

        partsGrid.removeAllColumns();
        partsGrid.addColumn(maintenancePart -> partService.findById(maintenancePart.getPart()).getPartName())
                .setHeader("Name");
        partsGrid.addColumn(maintenancePart -> "CRITICAL")
                .setHeader("Status");
        partsGrid.addComponentColumn(maintenancePart -> {
            Map<Long, Double> averageCostByPartName = calculateAverageCostByPartName(loadAllMaintenanceParts());
            return new Text(String.valueOf(averageCostByPartName.getOrDefault(maintenancePart.getPart(), 0.0)));
        }).setHeader("Average Cost");

        List<String> licencePlates = locateLicencePlates();
        ComboBox<String> licencePlateComboBox = new ComboBox<>("Licence Plate");
        licencePlateComboBox.setItemLabelGenerator(licencePlate -> licencePlate);

        licencePlateComboBox.setItems(licencePlates);
        if (!licencePlates.isEmpty()) {
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

        ComboBox<CarEntity> carSelectionComboBox = new ComboBox<>("Model");
        carSelectionComboBox.setItemLabelGenerator(CarEntity::getCarModel);
        carSelectionComboBox.setItems(cars);

        if (!cars.isEmpty()) {
            carSelectionComboBox.setValue(cars.get(0));
        }

        carSelectionComboBox.addValueChangeListener(event -> {
            CarEntity selectedCar = event.getValue();
            if (selectedCar != null) {
                loadMaintenancePartsForCar(selectedCar);
            }
        });

        partsGrid.addComponentColumn(maintenancePart -> {
            Button maintenanceButton = new Button();
            maintenanceButton.setIcon(VaadinIcon.COG.create());
            maintenanceButton.addClickListener(event -> {
                String licensePlate = licencePlateComboBox.getValue();
                if (!licensePlate.isEmpty()) {
                    Dialog dialog = new Dialog();
                    dialog.setModal(true);
                    dialog.setHeaderTitle("Registrar Manutenção");

                    TextField nameField = new TextField("Nome da Peça");
                    TextField descriptionField = new TextField("Descrição");
                    ComboBox<TypeEnum> typeField = new ComboBox<>("Type");
                    TextField serialNumberField = new TextField("Número de Série");
                    TextField manufacturerField = new TextField("Fabricante");
                    TextField modelField = new TextField("Modelo");
                    DatePicker installationDatePicker = new DatePicker("Installation Date");
                    TextField lifeSpanField = new TextField("Vida Útil");
                    ComboBox<LifeSpanEnum> lifeSpanTypeField = new ComboBox<>("Tipo de Vida Útil");ComboBox<MaintenancePartStatusEnum> statusPartField = getMaintenancePartStatusEnumComboBox();
                    TextField costField = new TextField("Valor Gasto");
                    TextField carsMileage = new TextField("car KM");

                    if(maintenancePart.getDetailedMaintenance().equals(DetailedMaintenanceEnum.DETAILED)) {
                        descriptionField.setValue(maintenancePart.getDescription());
                        serialNumberField.setValue(maintenancePart.getSerialNumber());
                        manufacturerField.setValue(maintenancePart.getManufacturer());
                        modelField.setValue(maintenancePart.getModel());
                    }

                    nameField.setValue(localePart(maintenancePart.getPart()));
                    nameField.setEnabled(false);

                    descriptionField.setEnabled(false);

                    typeField.setItems(TypeEnum.values());
                    typeField.setValue(maintenancePart.getType());

                    installationDatePicker.setValue(LocalDate.parse(maintenancePart.getInstallationDate()));

                    lifeSpanField.setValue(String.valueOf(maintenancePart.getLifeSpan()));

                    lifeSpanTypeField.setItems(LifeSpanEnum.values());
                    lifeSpanTypeField.setValue(maintenancePart.getLifeSpanType());

                    statusPartField.setValue(MaintenancePartStatusEnum.NEW);

                    costField.setValue(String.valueOf(maintenancePart.getCost()));
                    costField.setRequired(true);

                    carsMileage.setValue(String.valueOf(carSelectionComboBox.getValue().getMileage()));
                    carsMileage.setRequired(true);

                    String code = maintenancePart.getCode();

                    Button saveButton = new Button("Salvar", eventSave -> {
                        MaintenancePartEntity maintenance = new MaintenancePartEntity();
                        maintenance.setLifeSpanType(lifeSpanTypeField.getValue());
                        maintenance.setLifeSpan(Double.parseDouble(lifeSpanField.getValue()));
                        maintenance.setCost(Double.parseDouble(costField.getValue()));
                        maintenance.setDescription(descriptionField.getValue());
                        maintenance.setInstallationDate(installationDatePicker.getValue().toString());
                        maintenance.setManufacturer(manufacturerField.getValue());
                        maintenance.setModel(modelField.getValue());
                        maintenance.setType(typeField.getValue());
                        maintenance.setSerialNumber(serialNumberField.getValue());
                        maintenance.setPart(Long.parseLong(nameField.getValue()));
                        maintenance.setStatus(statusPartField.getValue());

                        String carMileage = carsMileage.toString();

                        maintenancePartService.processarManutencao(maintenance, code, licensePlate, carMileage);
                        Notification.show("Manutenção salva para a peça: " + maintenancePart.getPart());
                        dialog.close();
                    });

                    Button cancelButton = new Button("Cancelar", eventCancel -> {
                        dialog.close();
                    });
                    VerticalLayout layout = new VerticalLayout();
                    if(maintenancePart.getDetailedMaintenance().equals(DetailedMaintenanceEnum.DETAILED)) {
                        layout = new VerticalLayout(nameField, descriptionField, typeField,
                                serialNumberField, manufacturerField, modelField, installationDatePicker, lifeSpanField, lifeSpanTypeField,
                                statusPartField, costField, carsMileage,
                                saveButton, cancelButton);
                    } else {
                        layout = new VerticalLayout(nameField, typeField,
                                installationDatePicker, lifeSpanField, lifeSpanTypeField,
                                statusPartField, costField, carsMileage,
                                saveButton, cancelButton);
                    }

                    layout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

                    dialog.add(layout);
                    dialog.open();
                } else {
                    Notification.show("Por favor, selecione um carro antes de realizar a manutenção.");
                }
            });
            return maintenanceButton;
        }).setHeader("Action");

        carsLayout.add(carSelectionComboBox);
        carsLayout.add(licencePlateComboBox);

        if (!cars.isEmpty()) {
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

    private String localePart(long part) {
        return partService.findById(part).getPartName();
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

    private void realizarManutencao(MaintenancePartEntity maintenancePart) {
        // Lógica para realizar a manutenção
        Notification.show("Manutenção realizada para a peça: " + maintenancePart.getPart());
    }


    private List<MaintenancePartEntity> loadAllMaintenanceParts() {
        List<CarEntity> cars = carRepository.findByUserOwner(getAuthenticatedUser().getId());
        List<MaintenancePartEntity> allMaintenanceParts = new ArrayList<>();

        for (CarEntity car : cars) {
            allMaintenanceParts.addAll(maintenancePartService.findByCar(car.getId()));
        }

        return allMaintenanceParts;
    }

    private Map<Long, Double> calculateAverageCostByPartName(List<MaintenancePartEntity> maintenanceParts) {
        if (maintenanceParts.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<Long, List<MaintenancePartEntity>> partsByName = maintenanceParts.stream()
                .collect(Collectors.groupingBy(MaintenancePartEntity::getPart));

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
        maintenancePartService.ajustarStatusManutencoes(carEntity);
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
        UserEntity currentUser = userRepositoryFront.findByEmail(securityConfig.getAuthenticatedUser());
        Date createdAt = currentUser.getCreatedAt();
        Long lastUpdateMileage = currentUser.getLastUpdateMileage();
        long differenceLastUpdate = System.currentTimeMillis() - lastUpdateMileage;
        Long lastAskForUpdate = currentUser.getLastAskForUpdateMileage();
        long differenceLastAskUpdate = lastUpdateMileage - lastAskForUpdate;
        if (carsGrid.getDataProvider().size(new Query<>()) > 0
                && createdAt != null
                && (createdAt.getTime() - currentDate.getTime()) > (0)
                && differenceLastUpdate > (70)
                && differenceLastAskUpdate > (70)) {
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

    private VerticalLayout getVerticalLayout(CarEntity cars, TextField carMileageField, TextField
            carNameField, Dialog dialog) {
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
        String currentUser = securityConfig.getAuthenticatedUser();
        UserEntity user = userRepositoryFront.findByEmail(currentUser);
        user.setName(user.getName());
        user.setLastName(user.getLastName());
        user.setEmail(user.getEmail());
        user.setPassword(user.getPassword());
        user.setLastUpdateMileage(user.getLastUpdateMileage());
        user.setLastAskForUpdateMileage(System.currentTimeMillis());
        userRepositoryFront.save(user);
    }
}