package com.example.application.views.cars;

import com.example.application.backend.autoMaker.AutoMakerEntity;
import com.example.application.backend.autoMaker.AutoMakerRepository;
import com.example.application.backend.autoModel.domain.AutoModelEntity;
import com.example.application.backend.autoModel.repository.AutoModelRepository;
import com.example.application.backend.autoModel.service.AutoModelService;
import com.example.application.backend.car.CarFacade;
import com.example.application.backend.car.domain.CarEntity;
import com.example.application.backend.car.domain.CarTypeEnum;
import com.example.application.backend.car.model.response.CarResponse;
import com.example.application.config.security.SecurityConfig;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.AbstractField;
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
import org.springframework.beans.factory.annotation.Autowired;

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

    @Autowired
    private final CarFacade carFacade;
    @Autowired
    private final SecurityConfig securityConfig;
    private final ComboBox<AutoMakerEntity> autoMakerField;
    private final ListDataProvider<AutoMakerEntity> dataProvider;
    private final VerticalLayout mainLayout = new VerticalLayout();
    private final ComboBox<AutoModelEntity> modelField = new ComboBox<>("Model");
    private final ComboBox<Integer> yearPicker;
    private final ComboBox<CarTypeEnum> typeField;
    private final TextField colorField;
    private final TextField mileageField;
    private final TextField motorField;
    private final TextField licensePlateField;
    private final Button saveButton;
    private final Button cancelButton;
    Button addAutoMakerButton = new Button(new Icon("vaadin", "plus"));

    public CreateCarView(CarFacade carFacade, SecurityConfig securityConfig, AutoMakerRepository autoMakerRepository, AutoModelRepository autoModelRepository, AutoModelService autoModelService) {
        this.carFacade = carFacade;
        this.securityConfig = securityConfig;

        mainLayout.setWidthFull();
        mainLayout.addClassName(LumoUtility.Padding.XSMALL);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.setWidth("100%");
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        H3 h3 = new H3("Insert a Car");

        autoMakerField = new ComboBox<>("Auto Maker");
        autoMakerField.setItemLabelGenerator(AutoMakerEntity::getAutoMaker);

        dataProvider = DataProvider.ofCollection(autoMakerRepository.findAll());
        autoMakerField.setItems(dataProvider.getItems());

        addAutoMakerButton.addClickListener(event -> Notification.show("Add Auto Maker functionality goes here!"));

        HorizontalLayout autoMakerLayout = new HorizontalLayout(autoMakerField, addAutoMakerButton);
        autoMakerLayout.setAlignItems(FlexComponent.Alignment.BASELINE);
        autoMakerLayout.setWidth("100%");

        modelField.setItemLabelGenerator(AutoModelEntity::getAutoModel);
        modelField.setReadOnly(true);

        ListDataProvider<AutoModelEntity> modelDataProvider = DataProvider.ofCollection(autoModelRepository.findAll());
        modelField.setItems(modelDataProvider.getItems());

        Button addAutoModelButton = new Button(new Icon("vaadin", "plus"));
        addAutoModelButton.setEnabled(false);

        addAutoModelButton.addClickListener(event -> {
            if (autoMakerField.isEmpty()) {
                autoMakerField.setValue(new AutoMakerEntity());
            }
            AddAutoModelDialog addAutoModelDialog = new AddAutoModelDialog(autoMakerField.getValue().getAutoMaker(), autoModelService, autoMakerRepository);
            addAutoModelDialog.open();
        });

        HorizontalLayout autoModelLayout = new HorizontalLayout(modelField, addAutoModelButton);
        autoModelLayout.setAlignItems(FlexComponent.Alignment.BASELINE);

        autoMakerLayout.add(autoModelLayout);

        yearPicker = new ComboBox<>("Year", getSelectableYears(LocalDate.now(ZoneId.systemDefault())));
        yearPicker.setVisible(false);

        typeField = new ComboBox<>("Type", Arrays.asList(CarTypeEnum.values()));
        typeField.setItemLabelGenerator(CreateCarView::setTypePickupTruck);
        typeField.setVisible(false);

        colorField = new TextField("Color");
        colorField.setVisible(false);
        colorField.setValueChangeMode(ValueChangeMode.EAGER);

        mileageField = new TextField("Mileage");
        mileageField.setVisible(false);
        mileageField.setValueChangeMode(ValueChangeMode.EAGER);

        mileageField.addValueChangeListener(event -> {
            String newValue = event.getValue();
            if (newValue != null && !newValue.matches("\\d*")) {
                mileageField.clear();
            }
        });

        motorField = new TextField("Motor");
        motorField.setVisible(false);
        motorField.setValueChangeMode(ValueChangeMode.EAGER);

        licensePlateField = new TextField("Licence Plate");
        licensePlateField.setVisible(false);
        licensePlateField.setValueChangeMode(ValueChangeMode.EAGER);

        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.setEnabled(false);

        saveButton.addClickListener(event ->
                SaveNewCar(carFacade, yearPicker, colorField, typeField, mileageField, motorField, licensePlateField));
        cancelButton.addClickListener(event ->
                cleanForm(yearPicker, colorField, typeField, mileageField, motorField, licensePlateField, addAutoModelButton));

        autoMakerField.addValueChangeListener(event ->
                enableAutoModelField(autoModelRepository, event, addAutoModelButton));
        modelField.addValueChangeListener(this::enableYearField);
        yearPicker.addValueChangeListener(this::enableTypeField);
        typeField.addValueChangeListener(this::enableColorField);
        colorField.addValueChangeListener(this::enableMileageField);
        mileageField.addValueChangeListener(this::enableMotorField);
        motorField.addValueChangeListener(this::enableLicencePlateField);
        licensePlateField.addValueChangeListener(this::enableSaveButton);

        FormLayout formLayout = new FormLayout();
        formLayout.add(autoMakerLayout, autoModelLayout, yearPicker, typeField, colorField, mileageField, motorField, licensePlateField);

        VerticalLayout fieldLayout = new VerticalLayout(formLayout);
        fieldLayout.setPadding(false);
        mainLayout.add(h3, fieldLayout, saveButton, cancelButton);
        getContent().add(mainLayout);
    }

    private void enableSaveButton(AbstractField.ComponentValueChangeEvent<TextField, String> event) {
        String informedLicensePlate = event.getValue();
        informedLicensePlate = informedLicensePlate.toUpperCase();
        licensePlateField.setValue(informedLicensePlate);
        if (informedLicensePlate.length() <= 8) {
            saveButton.setEnabled(informedLicensePlate.length() >= 7);
        } else {
            licensePlateField.setValue(informedLicensePlate.substring(0, 8));
        }
    }

    private void enableLicencePlateField(AbstractField.ComponentValueChangeEvent<TextField, String> event) {
        String informedMotor = event.getValue();
        if (informedMotor != null && !informedMotor.isEmpty()) {
            licensePlateField.setVisible(true);
        } else {
            licensePlateField.setVisible(false);
            saveButton.setEnabled(false);
        }
    }

    private void enableMotorField(AbstractField.ComponentValueChangeEvent<TextField, String> event) {
        String informedMileage = event.getValue();
        if (informedMileage != null && !informedMileage.isEmpty()) {
            motorField.setVisible(true);
        } else {
            motorField.setVisible(false);
            licensePlateField.setVisible(false);
            saveButton.setEnabled(false);
        }
    }

    private void enableMileageField(AbstractField.ComponentValueChangeEvent<TextField, String> event) {
        String informedColor = event.getValue();
        if (informedColor != null && informedColor.length() >= 3) {
            mileageField.setVisible(true);
            mileageField.setEnabled(true);
        } else {
            mileageField.setVisible(false);
            mileageField.setEnabled(false);
            motorField.setVisible(false);
            licensePlateField.setVisible(false);
            saveButton.setEnabled(false);
        }
    }

    private void enableColorField(AbstractField.ComponentValueChangeEvent<ComboBox<CarTypeEnum>, CarTypeEnum> event) {
        CarTypeEnum selectedType = event.getValue();
        if (selectedType != null) {
            colorField.setVisible(true);
        } else {
            colorField.setVisible(false);
            mileageField.setVisible(false);
            motorField.setVisible(false);
            licensePlateField.setVisible(false);
            saveButton.setEnabled(false);
        }
    }

    private void enableTypeField(AbstractField.ComponentValueChangeEvent<ComboBox<Integer>, Integer> event) {
        Integer selectedYear = event.getValue();
        if (selectedYear != null) {
            typeField.setVisible(true);
        } else {
            typeField.setVisible(false);
            colorField.setVisible(false);
            mileageField.setVisible(false);
            motorField.setVisible(false);
            licensePlateField.setVisible(false);
            saveButton.setEnabled(false);
        }
    }

    private void enableYearField(AbstractField.ComponentValueChangeEvent<ComboBox<AutoModelEntity>, AutoModelEntity> event) {
        AutoModelEntity selectedModel = event.getValue();
        if (selectedModel != null) {
            yearPicker.setVisible(true);
        } else {
            yearPicker.setVisible(false);
            typeField.setVisible(false);
            colorField.setVisible(false);
            mileageField.setVisible(false);
            motorField.setVisible(false);
            licensePlateField.setVisible(false);
            saveButton.setEnabled(false);
        }
    }

    private void enableAutoModelField(AutoModelRepository autoModelRepository, AbstractField.ComponentValueChangeEvent<ComboBox<AutoMakerEntity>, AutoMakerEntity> event, Button addAutoModelButton) {
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
    }

    private List<Integer> getSelectableYears(LocalDate now) {
        final List<Integer> selectableYears;
        selectableYears = IntStream
                .rangeClosed(now.getYear() - 99, now.getYear())
                .boxed()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        return selectableYears;
    }

    private static String setTypePickupTruck(CarTypeEnum item) {
        if (Objects.requireNonNull(item) == CarTypeEnum.PICKUP_TRUCK) {
            return "PICKUP/TRUCK";
        }
        return item.toString();
    }

    private void SaveNewCar(CarFacade carFacade, ComboBox<Integer> yearPicker, TextField colorField, ComboBox<CarTypeEnum> typeField, TextField mileageField, TextField motorField, TextField licensePlateField) {
        if (modelField.isEmpty() || yearPicker.isEmpty() || autoMakerField.isEmpty() || colorField.isEmpty() ||
                typeField.isEmpty() || mileageField.isEmpty() || motorField.isEmpty() || licensePlateField.isEmpty()) {
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
            car.setLicencePlate(licensePlateField.getValue());

            CarResponse carFound = carFacade.findByLicensePlate(licensePlateField.getValue());

            if (carFound != null && carFound.getLicensePlate().equals(licensePlateField.getValue())) {
                Notification.show("License Plate already exist!", 3000, Notification.Position.TOP_CENTER);
            } else {
                this.carFacade.insert(car, this.securityConfig.getAuthenticatedUser());
                Notification.show("Car Created successfully!", 3000, Notification.Position.TOP_CENTER);
                UI.getCurrent().navigate("/");
            }
        }
    }

    private void cleanForm(ComboBox<Integer> yearPicker, TextField colorField, ComboBox<CarTypeEnum> typeField, TextField mileageField, TextField motorField, TextField licensePlateField, Button addAutoModelButton) {
        modelField.clear();
        yearPicker.clear();
        autoMakerField.clear();
        colorField.clear();
        typeField.clear();
        mileageField.clear();
        motorField.clear();
        licensePlateField.clear();
        modelField.setReadOnly(true);
        addAutoModelButton.setEnabled(false);
    }
}
