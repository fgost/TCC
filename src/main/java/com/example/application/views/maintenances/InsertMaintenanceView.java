package com.example.application.views.maintenances;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route("insert-maintenance")
@PageTitle("Insert Maintenance")
@PermitAll
public class InsertMaintenanceView extends HorizontalLayout {

    public InsertMaintenanceView() {
        Dialog dialog = new Dialog();
        Button completeButton = new Button("Create Complete Maintenance", e -> {
            dialog.close();
            UI.getCurrent().navigate(CreateCompleteMaintenancePartView.class);
        });
        Button simpleButton = new Button("Create Simple Maintenance", e -> {
            dialog.close();
            UI.getCurrent().navigate(CreateSimpleMaintenancePartView.class);
        });
        dialog.add(new HorizontalLayout(completeButton, simpleButton));
        dialog.open();
    }
}
