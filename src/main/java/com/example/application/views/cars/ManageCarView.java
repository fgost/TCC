package com.example.application.views.cars;

import com.example.application.backend.autoMaker.AutoMakerEntity;
import com.example.application.backend.autoMaker.AutoMakerRepository;
import com.example.application.backend.car.domain.CarEntity;
import com.example.application.backend.car.domain.CarTypeEnum;
import com.example.application.backend.car.service.CarService;
import com.example.application.backend.users.domain.UserEntity;
import com.example.application.backend.users.repository.UserRepositoryFront;
import com.example.application.backend.users.service.UserService;
import com.example.application.config.security.SecurityConfig;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@PageTitle("Manage Cars")
@Route(value = "manage-cars", layout = MainLayout.class)
@Uses(Icon.class)
@PermitAll
public class ManageCarView extends Composite<VerticalLayout> {

    private final CarService carService;
    private final UserService userService;
    private final SecurityConfig securityConfig;
    private final AutoMakerRepository autoMakerRepository;
    private final UserRepositoryFront userRepositoryFront;
    private final Grid<CarEntity> carGrid = new Grid<>(CarEntity.class);

    public ManageCarView(CarService carService, UserService userService, SecurityConfig securityConfig, AutoMakerRepository autoMakerRepository, UserRepositoryFront userRepositoryFront) {
        this.carService = carService;
        this.userService = userService;
        this.securityConfig = securityConfig;
        this.autoMakerRepository = autoMakerRepository;
        this.userRepositoryFront = userRepositoryFront;

        carGrid.setColumns("carModel", "licencePlate");
        carGrid.addComponentColumn(this::createButtonWithIcon).setHeader("Edit");
        carGrid.addComponentColumn(this::createDeleteButton).setHeader("Delete");
        loadCarsData();
        getContent().add(carGrid);
    }

    private Button createButtonWithIcon(CarEntity carEntity) {
        Button button = new Button(new Icon(VaadinIcon.PENCIL));
        button.addClickListener(event -> handleEditButtonClick(carEntity));
        return button;
    }

    private Button createDeleteButton(CarEntity carEntity) {
        Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
        deleteButton.addClickListener(event -> showConfirmationDialog(carEntity));
        return deleteButton;
    }

    private void handleEditButtonClick(CarEntity carEntity) {
        showEditCarDialog(carEntity);
    }

    private void showEditCarDialog(CarEntity carEntity) {
        Dialog dialog = new Dialog();
        dialog.setModal(true);

        TextField carModelField = new TextField("Model");
        carModelField.setValue(carEntity.getCarModel());

        TextField carYearField = new TextField("Year");
        carYearField.setValue(carEntity.getYear());

        TextField carColorField = new TextField("Color");
        carColorField.setValue(carEntity.getColor());

        TextField carMileageField = new TextField("Mileage");
        carMileageField.setValue(String.valueOf(carEntity.getMileage()));

        TextField carLicensePlate = new TextField("License Plate");
        carLicensePlate.setValue(carEntity.getLicencePlate());

        TextField motorField = new TextField("Motor");
        motorField.setValue(carEntity.getMotor());

        var autoMaker = autoMakerRepository.findByAutoMaker(carEntity.getAutoMaker());
        ComboBox<AutoMakerEntity> carAutoMakerField = new ComboBox<>("Auto Maker");
        carAutoMakerField.setItemLabelGenerator(AutoMakerEntity::getAutoMaker);
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

        VerticalLayout layout = new VerticalLayout(carAutoMakerField, carModelField, carYearField, carColorField, carTypeField, carMileageField, carLicensePlate, motorField, new Button("Save", event -> {
            CarEntity editedCar = new CarEntity();
            editedCar.setCarModel(carModelField.getValue());
            editedCar.setYear(carYearField.getValue());
            editedCar.setAutoMaker(carAutoMakerField.getValue().getAutoMaker());
            editedCar.setColor(carColorField.getValue());
            editedCar.setType(carTypeField.getValue());
            editedCar.setMileage(Double.parseDouble(carMileageField.getValue()));
            editedCar.setLicencePlate(carLicensePlate.getValue());
            editedCar.setMotor(motorField.getValue());
            handleEditCar(carEntity, editedCar);
            dialog.close();
        }), new Button("Cancel", event -> dialog.close()));
        layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        dialog.add(layout);
        dialog.open();
    }

    private void handleEditCar(CarEntity carEntity, CarEntity editedCar) {
        carEntity.setCarModel(editedCar.getCarModel());
        carEntity.setYear(editedCar.getYear());
        carEntity.setAutoMaker(editedCar.getAutoMaker());
        carEntity.setColor(editedCar.getColor());
        carEntity.setType(editedCar.getType());
        carEntity.setMileage(editedCar.getMileage());
        carEntity.setLicencePlate(editedCar.getLicencePlate());
        carEntity.setMotor(editedCar.getMotor());
        carService.update(carEntity.getCode(), carEntity);
        updateLastUpdateMileage();
        loadCarsData();
    }

    private void loadCarsData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserEntity userEntity = userService.findByEmail(authentication.getName());
            carService.findByUser(userEntity.getId());
            List<CarEntity> cars = carService.findByUser(userEntity.getId());
            carGrid.setItems(cars);
        } else {
            securityConfig.logout();
        }
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

    private void showConfirmationDialog(CarEntity carEntity) {
        Dialog confirmationDialog = new Dialog();
        confirmationDialog.setCloseOnEsc(false);
        confirmationDialog.setCloseOnOutsideClick(false);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);

        Button confirmButton = new Button("Delete Anyway");
        confirmButton.addClickListener(event -> {
            handleDeleteButtonClick(carEntity);
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

    private void handleDeleteButtonClick(CarEntity carEntity) {
        carService.deleteByCode(carEntity.getCode());
        loadCarsData();
    }
}
