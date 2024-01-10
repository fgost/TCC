package com.example.application.views.cars;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;

public class AddAutoModelDialog extends Dialog {
    public AddAutoModelDialog(String selectedCarMaker) {
        System.out.println("chegou: " + selectedCarMaker);
        TextField carMakerField = new TextField("Car Maker");
        carMakerField.setValue(selectedCarMaker);
        carMakerField.setReadOnly(true);

        TextField carModelField = new TextField("Car Model");
        FormLayout formLayout = new FormLayout(carModelField, carMakerField);

        Button saveButton = new Button("Save", new Icon("vaadin", "check"));
        Button cancelButton = new Button("Cancel", new Icon("vaadin", "times"));

        saveButton.addClickListener(event -> {
            // Adicione lógica para salvar o novo modelo de carro
            // ...

            close(); // Fecha o diálogo após salvar
        });

        cancelButton.addClickListener(event -> close()); // Fecha o diálogo ao cancelar

        add(formLayout, saveButton, cancelButton);
    }
}
