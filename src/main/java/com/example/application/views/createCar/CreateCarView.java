package com.example.application.views.createCar;

import com.example.application.backend.autoMaker.AutoMakerEntity;
import com.example.application.backend.autoMaker.AutoMakerRepository;
import com.example.application.backend.car.CarFacade;
import com.example.application.backend.car.domain.CarEntity;
import com.example.application.backend.car.domain.CarTypeEnum;
import com.example.application.config.security.SecurityConfig;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@PageTitle("Create Car")
@Route(value = "create-car", layout = MainLayout.class)
@Uses(Icon.class)
@PermitAll
public class CreateCarView extends Composite<VerticalLayout> {

    private final CarFacade carFacade;
    private final SecurityConfig securityConfig;


    public CreateCarView(CarFacade carFacade, AutoMakerRepository autoMakerRepository, SecurityConfig securityConfig) {
        this.carFacade = carFacade;
        this.securityConfig = securityConfig;
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setWidthFull();
        mainLayout.addClassName(LumoUtility.Padding.XSMALL);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        mainLayout.setWidth("100%");
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        H3 h3 = new H3("Insert a Car");

        TextField modelField = new TextField("Model");

        LocalDate now = LocalDate.now(ZoneId.systemDefault());

        List<Integer> selectableYears = IntStream
                .rangeClosed(now.getYear() - 99, now.getYear())
                .boxed()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        ComboBox<Integer> yearPicker = new ComboBox<>("Year", selectableYears);

        ComboBox<CarTypeEnum> typeField = new ComboBox<>("Type", Arrays.asList(CarTypeEnum.values()));
        typeField.setItemLabelGenerator(item -> {
            if (Objects.requireNonNull(item) == CarTypeEnum.PICKUP_TRUCK) {
                return "PICKUP/TRUCK";
            }
            return item.toString();
        });

        ComboBox<AutoMakerEntity> autoMakerField = new ComboBox<>("Auto Maker");
        autoMakerField.setItemLabelGenerator(AutoMakerEntity::getName);

        ListDataProvider<AutoMakerEntity> dataProvider = DataProvider.ofCollection(autoMakerRepository.findAll());
        autoMakerField.setItems(dataProvider.getItems());

        TextField colorField = new TextField("Color");

        TextField mileageField = new TextField("Mileage");

        TextField motorField = new TextField("Motor");

        TextField licencePlateField = new TextField("Licence Plate");

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


        saveButton.addClickListener(event -> {
            if (modelField.isEmpty() || yearPicker.isEmpty() || autoMakerField.isEmpty() || colorField.isEmpty() ||
                    typeField.isEmpty() || mileageField.isEmpty() || motorField.isEmpty() || licencePlateField.isEmpty()) {
                Notification.show("Please fill in all fields.", 3000, Notification.Position.TOP_CENTER);
            } else {
                CarEntity car = new CarEntity();
                car.setCarModel(modelField.getValue());
                car.setYear(String.valueOf(yearPicker.getValue()));
                car.setAutoMaker(autoMakerField.getValue().getName());
                car.setColor(colorField.getValue());
                car.setType(typeField.getValue());
                car.setMileage(Double.parseDouble(mileageField.getValue()));
                car.setMotor(motorField.getValue());
                car.setLicencePlate(licencePlateField.getValue());

                this.carFacade.insert(car, this.securityConfig.getAuthenticatedUser());

                Notification.show("Car Created successfully!", 3000, Notification.Position.TOP_CENTER);

                UI.getCurrent().navigate("/");
            }
        });

        cancelButton.addClickListener(event -> {
            modelField.clear();
            yearPicker.clear();
            autoMakerField.clear();
            colorField.clear();
            typeField.clear();
            mileageField.clear();
            motorField.clear();
            licencePlateField.clear();
        });

        FormLayout formLayout = new FormLayout();
        formLayout.add(modelField, yearPicker, autoMakerField, colorField, typeField, mileageField, motorField, licencePlateField, saveButton, cancelButton);
        formLayout.setResponsiveSteps(
                // Use one column by default
                new FormLayout.ResponsiveStep("0", 1),
                // Use two columns, if layout's width exceeds 500px
                new FormLayout.ResponsiveStep("500px", 2));
// Stretch the username field over 2 columns
        //formLayout.setColspan(modelField, 1);

        mainLayout.add(h3, formLayout);

        getContent().add(mainLayout);


    }
}