package com.example.application.views.newUserForm;

import com.example.application.backend.users.UserFacade;
import com.example.application.backend.users.domain.UserEntity;
import com.example.application.backend.users.repository.UserRepository;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("new-user")
@Uses(Icon.class)
@PageTitle("New User | Carview")
@AnonymousAllowed
public class NewUserFormView extends Composite<VerticalLayout> {

    private final UserFacade userFacade;
    private String latitudeCaptured;
    private String longitudeCaptured;

    public NewUserFormView(UserFacade userFacade, UserRepository userRepository) {
        this.userFacade = userFacade;
        VerticalLayout mainLayout = new VerticalLayout();
        H3 firstMessage = new H3();

        TextField firstName = new TextField("First Name");
        TextField lastName = new TextField("Last Name");
        EmailField emailField = new EmailField("Email");
        PasswordField password = new PasswordField("Password");
        PasswordField confirmPassword = new PasswordField("Confirm Password");

        Button saveButton = new Button("SIGN UP");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancelButton = new Button("CANCEL");

        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        mainLayout.setWidthFull();
        mainLayout.addClassName(LumoUtility.Padding.LARGE);

        firstMessage.setText("Register");

        saveButton.addClickListener(event -> {
            if (latitudeCaptured == null || longitudeCaptured == null) {
                Notification.show("Location not captured yet. Please wait and try again.", 3000, Notification.Position.TOP_CENTER);
            } else if (firstName.isEmpty() || lastName.isEmpty() || emailField.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Notification.show("Please fill in all fields.", 3000, Notification.Position.TOP_CENTER);
            } else if (userRepository.findByEmail(emailField.getValue()).isPresent()) {
                Notification.show("Email already exists.", 3000, Notification.Position.TOP_CENTER);
            } else if (!password.getValue().equals(confirmPassword.getValue())) {
                Notification.show("The passwords do not match. Please try again.", 3000, Notification.Position.TOP_CENTER);
            } else {
                UserEntity user = new UserEntity();
                user.setName(firstName.getValue());
                user.setLastName(lastName.getValue());
                user.setEmail(emailField.getValue());
                user.setPassword(password.getValue());
                user.setLastUpdateMileage(System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000));
                user.setLastAskForUpdateMileage(System.currentTimeMillis());
                user.setLatitude(latitudeCaptured);
                user.setLongitude(longitudeCaptured);

                this.userFacade.insert(user);

                Notification.show("User registered successfully!", 3000, Notification.Position.TOP_CENTER);

                UI.getCurrent().navigate("/login");
            }
        });

        cancelButton.addClickListener(event -> {
            firstName.setValue("");
            lastName.setValue("");
            emailField.setValue("");
            password.setValue("");
            confirmPassword.setValue("");
            UI.getCurrent().navigate("/login");
        });

        mainLayout.add(firstMessage, firstName, lastName, emailField, password, confirmPassword, saveButton, cancelButton);

        getContent().add(mainLayout);

        UI.getCurrent().getPage().executeJs(
                "navigator.geolocation.getCurrentPosition(" +
                        "  function(position) {" +
                        "    var latitude = position.coords.latitude;" +
                        "    var longitude = position.coords.longitude;" +
                        "    $0.$server.setLocation(latitude, longitude);" +
                        "  }," +
                        "  function(error) {" +
                        "    alert('Error getting location: ' + error.message);" +
                        "    console.error('Geolocation error:', error);" +
                        "  }," +
                        "  { enableHighAccuracy: true, timeout: 5000, maximumAge: 0 }" +
                        ");", this);
    }

    @ClientCallable
    public void setLocation(double latitude, double longitude) {
        latitudeCaptured = String.valueOf(latitude);
        longitudeCaptured = String.valueOf(longitude);
        Notification.show("Location captured!");
    }
}
