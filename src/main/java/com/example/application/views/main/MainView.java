package com.example.application.views.main;

import com.example.application.backend.autoMaker.AutoMakerEntity;
import com.example.application.backend.autoMaker.AutoMakerRepository;
import com.example.application.backend.car.domain.CarEntity;
import com.example.application.backend.car.domain.CarTypeEnum;
import com.example.application.backend.car.repository.CarRepository;
import com.example.application.backend.maintenancePart.domain.MaintenancePartEntity;
import com.example.application.backend.maintenancePart.repository.MaintenancePartRepository;
import com.example.application.backend.users.domain.UserEntity;
import com.example.application.backend.users.repository.UserRepositoryFront;
import com.example.application.config.security.SecurityConfig;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
        partsGrid.addColumn(MaintenancePartEntity::getStatus).setHeader("Status");

        // Remova a configuração da coluna de modelo de carro da grid
        carsGrid.removeAllColumns();

        // Crie um layout horizontal para os botões de carros
        HorizontalLayout carsLayout = new HorizontalLayout();
        carsLayout.setWidthFull(); // Define a largura total do layout

        // Adicione botões para cada carro ao layout horizontal
        List<CarEntity> cars = locateCars();
        for (CarEntity carEntity : cars) {
            Button carButton = new Button(carEntity.getCarModel());
            carButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            carButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            carButton.addClickListener(event -> loadMaintenancePartsForCar(carEntity));

            carsLayout.add(carButton);
            carsLayout.setFlexGrow(1, carButton); // Define o botão para ocupar o espaço disponível
        }

        // Adicione o layout horizontal de carros ao layout vertical principal
        add(new H3("Cars"), carsLayout, new H3("Maintenance Parts"), partsGrid);

        carsGrid.asSingleSelect().addValueChangeListener(event -> {
            CarEntity selectedCar = event.getValue();
            if (selectedCar != null) {
                loadMaintenancePartsForCar(selectedCar);
            }
        });

        // Carregar dados
        loadPartsData();
        loadCarsData();
        askForUpdateMileageCars();



    }

    private void askForUpdateMileageCars() {
        var currentUser = userRepositoryFront.findByEmail(securityConfig.getAuthenticatedUser());
        var lastUpdateMileage = currentUser.getLastUpdateMileage();
        var differenceLastUpdate = System.currentTimeMillis() - lastUpdateMileage;
        var lastAskForUpdate = currentUser.getLastAskForUpdateMileage();
        var differenceLastAskUpdate = currentUser.getLastAskForUpdateMileage() - lastAskForUpdate;
        if (carsGrid.getDataProvider().size(new Query<>()) > 0 && differenceLastUpdate > (7 * 24 * 60 * 60 * 1000) && differenceLastAskUpdate > (24 * 60 * 60 * 1000)) {
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

                }),
                new Button("Cancel", event -> {
                    registerNonUpdateMileage();
                    dialog.close();
                })
        );
        layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        dialog.add(layout);
        dialog.open();
    }

    private void updateMileageCars() {
        var carEntity = locateCars();
        for (CarEntity cars: carEntity) {
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
        VerticalLayout layout = new VerticalLayout(
                carNameField, carMileageField,
                new Button("Save", event -> {
                    CarEntity editedCar = new CarEntity();
                    editedCar.setMileage(Double.parseDouble(carMileageField.getValue()));
                    editedCar.setCarModel(cars.getCarModel());
                    editedCar.setYear(cars.getYear());
                    editedCar.setAutoMaker(cars.getAutoMaker());
                    editedCar.setColor(cars.getColor());
                    editedCar.setType(cars.getType());
                    handleEditCar(cars, editedCar);
                    dialog.close();
                }),
                new Button("Cancel", event -> {
                    dialog.close();
                    registerNonUpdateMileage();
                })
        );
        layout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        return layout;
    }

    private void registerNonUpdateMileage() {
        updateLastAskForUpdateMileage();
    }

    private void loadPartsData() {
        List<CarEntity> cars = locateCars();
        List<MaintenancePartEntity> allMaintenanceParts = new ArrayList<>();

        for (CarEntity car : cars) {
            List<MaintenancePartEntity> maintenancePartsForCar = maintenancePartRepository.findByCar(car.getId());
            allMaintenanceParts.addAll(maintenancePartsForCar);
        }

        partsGrid.setItems(allMaintenanceParts);
    }

    private void loadCarsData() {
        List<CarEntity> cars = locateCars();
        carsGrid.setItems(cars);
    }

    private void loadMaintenancePartsForCar(CarEntity carEntity) {
        List<MaintenancePartEntity> maintenanceParts = maintenancePartRepository.findByCar(carEntity.getId());
        partsGrid.setItems(maintenanceParts);
    }

    private List<CarEntity> locateCars() {
        return carRepository.findByUsuario(getAuthenticatedUser().getId());
    }

    private UserEntity getAuthenticatedUser() {
        var user = this.securityConfig.getAuthenticatedUser();
        return userRepositoryFront.findByEmail(user);
    }

    private void showConfirmationDialog(CarEntity carEntity) {
        Dialog dialog = new Dialog();
        dialog.setModal(true);

        VerticalLayout layout = new VerticalLayout(
                new Text("Do you really want to delete this car?"),
                new Button("Yes", event -> {
                    handleDeleteCar(carEntity);
                    dialog.close();
                }),
                new Button("Cancel", event -> dialog.close())
        );
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


        VerticalLayout layout = new VerticalLayout(
                carModelField, carYearField, carAutoMakerField, carColorField, carTypeField, carMileageField,
                new Button("Save", event -> {
                    CarEntity editedCar = new CarEntity();
                    editedCar.setCarModel(carModelField.getValue());
                    editedCar.setYear(carYearField.getValue());
                    editedCar.setAutoMaker(carAutoMakerField.getValue().getName());
                    editedCar.setColor(carColorField.getValue());
                    editedCar.setType(carTypeField.getValue());
                    editedCar.setMileage(Double.parseDouble(carMileageField.getValue()));
                    handleEditCar(carEntity, editedCar);
                    dialog.close();
                }),
                new Button("Cancel", event -> dialog.close())
        );
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