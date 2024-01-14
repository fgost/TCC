package com.example.application.views.cars;

import com.example.application.backend.autoMaker.AutoMakerEntity;
import com.example.application.backend.autoMaker.AutoMakerRepository;
import com.example.application.backend.autoModel.AutoModelEntity;
import com.example.application.backend.autoModel.AutoModelRepository;
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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
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

    public CreateCarView(CarFacade carFacade, SecurityConfig securityConfig, AutoMakerRepository autoMakerRepository, AutoModelRepository autoModelRepository) {
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

        ComboBox<AutoMakerEntity> autoMakerField = new ComboBox<>("Auto Maker");
        autoMakerField.setItemLabelGenerator(AutoMakerEntity::getAutoMaker);

        ListDataProvider<AutoMakerEntity> dataProvider = DataProvider.ofCollection(autoMakerRepository.findAll());
        autoMakerField.setItems(dataProvider.getItems());

        Button addAutoMakerButton = new Button(new Icon("vaadin", "plus"));
        addAutoMakerButton.addClickListener(event -> Notification.show("Add Auto Maker functionality goes here!"));

        HorizontalLayout autoMakerLayout = new HorizontalLayout(autoMakerField, addAutoMakerButton);
        autoMakerLayout.setAlignItems(FlexComponent.Alignment.BASELINE);
        autoMakerLayout.setWidth("100%");

        ComboBox<AutoModelEntity> modelField = new ComboBox<>("Model");
        modelField.setItemLabelGenerator(AutoModelEntity::getAutoModel);
        modelField.setReadOnly(true);

        ListDataProvider<AutoModelEntity> modelDataProvider = DataProvider.ofCollection(autoModelRepository.findAll());
        modelField.setItems(modelDataProvider.getItems());

        Button addAutoModelButton = new Button(new Icon("vaadin", "plus"));
        addAutoModelButton.setEnabled(false);

        addAutoModelButton.addClickListener(event -> {
            if(autoMakerField.isEmpty()) {
                autoMakerField.setValue(new AutoMakerEntity());
            }
            AddAutoModelDialog addAutoModelDialog = new AddAutoModelDialog(autoMakerField.getValue().getAutoMaker());
            addAutoModelDialog.open();
        });

        HorizontalLayout autoModelLayout = new HorizontalLayout(modelField, addAutoModelButton);
        autoModelLayout.setAlignItems(FlexComponent.Alignment.BASELINE);

        autoMakerLayout.add(autoModelLayout);

        LocalDate now = LocalDate.now(ZoneId.systemDefault());
        List<Integer> selectableYears = IntStream
                .rangeClosed(now.getYear() - 99, now.getYear())
                .boxed()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        ComboBox<Integer> yearPicker = new ComboBox<>("Year", selectableYears);
        yearPicker.setVisible(false);

        ComboBox<CarTypeEnum> typeField = new ComboBox<>("Type", Arrays.asList(CarTypeEnum.values()));
        typeField.setItemLabelGenerator(item -> {
            if (Objects.requireNonNull(item) == CarTypeEnum.PICKUP_TRUCK) {
                return "PICKUP/TRUCK";
            }
            return item.toString();
        });
        typeField.setVisible(false);

        TextField colorField = new TextField("Color");
        colorField.setVisible(false);
        colorField.setValueChangeMode(ValueChangeMode.EAGER);

        TextField mileageField = new TextField("Mileage");
        mileageField.setVisible(false);
        mileageField.setValueChangeMode(ValueChangeMode.EAGER);

        mileageField.addValueChangeListener(event -> {
            String newValue = event.getValue();
            if (newValue != null && !newValue.matches("\\d*")) {
                mileageField.clear();
            }
        });

        TextField motorField = new TextField("Motor");
        motorField.setVisible(false);
        motorField.setValueChangeMode(ValueChangeMode.EAGER);

        TextField licencePlateField = new TextField("Licence Plate");
        licencePlateField.setVisible(false);
        licencePlateField.setValueChangeMode(ValueChangeMode.EAGER);

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.setEnabled(false);

        saveButton.addClickListener(event -> {
            if (modelField.isEmpty() || yearPicker.isEmpty() || autoMakerField.isEmpty() || colorField.isEmpty() ||
                    typeField.isEmpty() || mileageField.isEmpty() || motorField.isEmpty() || licencePlateField.isEmpty()) {
                Notification.show("Please fill in all fields.", 3000, Notification.Position.TOP_CENTER);
            } else {
                CarEntity car = new CarEntity();
                car.setCarModel(modelField.getValue().getAutoModel());
                car.setYear(String.valueOf(yearPicker.getValue()));
                car.setAutoMaker(autoMakerField.getValue().getAutoMaker());
                car.setColor(colorField.getValue());
                car.setType(typeField.getValue());
                car.setMileage(Double.parseDouble(mileageField.getValue()));
                car.setMotor(motorField.getValue());
                car.setLicencePlate(licencePlateField.getValue());

                CarEntity carFound = carFacade.findByLicensePlate(licencePlateField.getValue());

                if (carFound != null && carFound.getLicencePlate().equals(licencePlateField.getValue())) {
                    Notification.show("License Plate already exist!", 3000, Notification.Position.TOP_CENTER);
                } else {
                    this.carFacade.insert(car, this.securityConfig.getAuthenticatedUser());
                    Notification.show("Car Created successfully!", 3000, Notification.Position.TOP_CENTER);
                    UI.getCurrent().navigate("/");
                }
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
            modelField.setReadOnly(true);
            addAutoModelButton.setEnabled(false);
        });

        autoMakerField.addValueChangeListener(event -> {
            AutoMakerEntity selectedMaker = event.getValue();

            if (selectedMaker != null) {
                List<AutoModelEntity> modelsForMaker = autoModelRepository.findByAutoMaker(selectedMaker.getId());
                modelField.setItems(modelsForMaker);
                modelField.setReadOnly(false);
                addAutoModelButton.setEnabled(true);
            } else {
                modelField.setItems();
                modelField.setReadOnly(true);
                addAutoModelButton.setEnabled(false);
            }
        });

        modelField.addValueChangeListener(event -> {
            AutoModelEntity selectedModel = event.getValue();
            if (selectedModel != null) {
                yearPicker.setVisible(true);
            } else {
                yearPicker.setVisible(false);
                typeField.setVisible(false);
                colorField.setVisible(false);
                mileageField.setVisible(false);
                motorField.setVisible(false);
                licencePlateField.setVisible(false);
                saveButton.setEnabled(false);
            }
        });

        yearPicker.addValueChangeListener(event -> {
            Integer selectedYear = event.getValue();
            if (selectedYear != null) {
                typeField.setVisible(true);
            } else {
                typeField.setVisible(false);
                colorField.setVisible(false);
                mileageField.setVisible(false);
                motorField.setVisible(false);
                licencePlateField.setVisible(false);
                saveButton.setEnabled(false);
            }
        });

        typeField.addValueChangeListener(event -> {
            CarTypeEnum selectedType = event.getValue();
            if (selectedType != null) {
                colorField.setVisible(true);
            } else {
                colorField.setVisible(false);
                mileageField.setVisible(false);
                motorField.setVisible(false);
                licencePlateField.setVisible(false);
                saveButton.setEnabled(false);
            }
        });

        colorField.addValueChangeListener(event -> {
            String informedColor = event.getValue();
            if (informedColor != null && informedColor.length() >= 3) {
                mileageField.setVisible(true);
                mileageField.setEnabled(true);
            } else {
                mileageField.setVisible(false);
                mileageField.setEnabled(false);
                motorField.setVisible(false);
                licencePlateField.setVisible(false);
                saveButton.setEnabled(false);
            }
        });

        mileageField.addValueChangeListener(event -> {
            String informedMileage = event.getValue();
            if (informedMileage != null && !informedMileage.isEmpty()) {
                motorField.setVisible(true);
            } else {
                motorField.setVisible(false);
                licencePlateField.setVisible(false);
                saveButton.setEnabled(false);
            }
        });

        motorField.addValueChangeListener(event -> {
            String informedMotor = event.getValue();
            if (informedMotor != null && !informedMotor.isEmpty()) {
                licencePlateField.setVisible(true);
            } else {
                licencePlateField.setVisible(false);
                saveButton.setEnabled(false);
            }
        });

        licencePlateField.addValueChangeListener(event -> {
            String informedLicensePlate = event.getValue();
            informedLicensePlate = informedLicensePlate.toUpperCase();
            licencePlateField.setValue(informedLicensePlate);
            if (informedLicensePlate.length() <= 8) {
                saveButton.setEnabled(informedLicensePlate.length() >= 7);
            } else {
                licencePlateField.setValue(informedLicensePlate.substring(0, 8));
            }
        });

        autoMakerField.addValueChangeListener(event -> {
            AutoMakerEntity selectedMaker = event.getValue();
            if (selectedMaker != null) {
                List<AutoModelEntity> modelsForMaker = autoModelRepository.findByAutoMaker(selectedMaker.getId());
                modelField.setItems(modelsForMaker);
            } else {
                modelField.setItems();
            }
        });

        FormLayout formLayout = new FormLayout();
        formLayout.add(autoMakerLayout, autoModelLayout, yearPicker, typeField, colorField, mileageField, motorField, licencePlateField);

        VerticalLayout fieldLayout = new VerticalLayout(formLayout);
        fieldLayout.setPadding(false);
        mainLayout.add(h3, fieldLayout, saveButton, cancelButton);
        getContent().add(mainLayout);
    }
}
