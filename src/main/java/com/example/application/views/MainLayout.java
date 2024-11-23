package com.example.application.views;

import com.example.application.config.security.SecurityConfig;
import com.example.application.views.cars.CreateCarView;
import com.example.application.views.cars.ManageCarView;
import com.example.application.views.maintenances.CreateMaintenancePartView;
import com.example.application.views.logout.LogoutView;
import com.example.application.views.main.MainView;
import com.example.application.views.maintenances.ManageMaintenanceView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIcon;

public class MainLayout extends AppLayout {
    private final SecurityConfig securityConfig;
    private H2 viewTitle;

    public MainLayout(@Autowired SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();

    }
    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");
        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        String user = securityConfig.getAuthenticatedUser();

        Notification.show("Welcome " + user, 3000, Notification.Position.TOP_CENTER);

        addToNavbar(true, toggle, viewTitle);

    }

    private void addDrawerContent() {
        H1 appName = new H1("Car-View");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
        setDrawerOpened(false);
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        SideNavItem carsNavItem = new SideNavItem("Cars", "manage-cars");
        carsNavItem.addItem(new SideNavItem("Insert Car", CreateCarView.class, LineAwesomeIcon.CAR_SOLID.create()));
        carsNavItem.addItem(new SideNavItem("Manage Car", ManageCarView.class, LineAwesomeIcon.COG_SOLID.create()));

        SideNavItem maintenanceNavItem = new SideNavItem("Maintenances", "manage-maintenances");
        maintenanceNavItem.addItem(new SideNavItem("Insert Maintenance", CreateMaintenancePartView.class, LineAwesomeIcon.HAMMER_SOLID.create()));
        maintenanceNavItem.addItem(new SideNavItem("Manage Maintenance", ManageMaintenanceView.class, LineAwesomeIcon.COG_SOLID.create()));

        SideNavItem mainNav = new SideNavItem("Main", MainView.class, LineAwesomeIcon.CHECK_DOUBLE_SOLID.create());

        nav.addItem(mainNav);
        nav.addItem(carsNavItem);
        nav.addItem(maintenanceNavItem);
        nav.addItem(new SideNavItem("Logout", LogoutView.class, LineAwesomeIcon.EXPEDITEDSSL.create()));

        return nav;
    }

    private Footer createFooter() {
        return new Footer();
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
