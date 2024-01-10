package com.example.application.views.maintenances;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PageTitle("Manage Maintenances")
@Route(value = "manage-maintenances", layout = MainLayout.class)
@Uses(Icon.class)
@PermitAll
public class ManageMaintenanceView extends Composite<VerticalLayout> {
}
