package com.example.application.views.cars;

import com.example.application.backend.autoMaker.AutoMakerEntity;
import com.example.application.backend.autoMaker.AutoMakerRepository;
import com.example.application.backend.autoModel.domain.AutoModelEntity;
import com.example.application.backend.autoModel.service.AutoModelService;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;

public class AddAutoModelDialog extends Dialog {
    private final AutoModelService autoModelService;
    private final AutoMakerRepository autoMakerRepository;

    public AddAutoModelDialog(String selectedCarMaker, AutoModelService autoModelService, AutoMakerRepository autoMakerRepository) {
        this.autoModelService = autoModelService;
        this.autoMakerRepository = autoMakerRepository;

        System.out.println("Chegou: " + selectedCarMaker);

        // Campos de entrada
        TextField carMakerField = new TextField("Car Maker");
        carMakerField.setValue(selectedCarMaker);
        carMakerField.setReadOnly(true);

        TextField carModelField = new TextField("Car Model");
        carModelField.setPlaceholder("Enter the car model");

        // Layout do formulário
        FormLayout formLayout = new FormLayout(carModelField, carMakerField);

        // Botões
        Button saveButton = new Button("Save", new Icon("vaadin", "check"));
        Button cancelButton = new Button("Cancel", new Icon("vaadin", "times"));

        // Configuração do botão de salvar
        saveButton.addClickListener(event -> {
            String carModelName = carModelField.getValue();
            if (carModelName == null || carModelName.trim().isEmpty()) {
                Notification.show("Car Model cannot be empty.", 3000, Notification.Position.MIDDLE);
                return;
            }

            AutoMakerEntity carMaker = autoMakerRepository.findByAutoMaker(carMakerField.getValue());
            if (carMaker == null) {
                Notification.show("Car Maker not found.", 3000, Notification.Position.MIDDLE);
                return;
            }

            try {
                AutoModelEntity newAutoModel = new AutoModelEntity();
                newAutoModel.setAutoModel(carModelName);
                newAutoModel.setAutoMaker(carMaker.getId());
                autoModelService.insert(newAutoModel);

                Notification.show("Car Model added successfully!", 3000, Notification.Position.MIDDLE);
                close();
            } catch (Exception e) {
                Notification.show("Failed to save the Car Model.", 3000, Notification.Position.MIDDLE);
                e.printStackTrace();
            }
        });

        // Configuração do botão de cancelar
        cancelButton.addClickListener(event -> close());

        // Adicionar componentes ao diálogo
        add(formLayout, saveButton, cancelButton);
    }
}
