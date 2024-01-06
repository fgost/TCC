package com.example.application.views.login;

import com.example.application.views.newUserForm.view.NewUserFormView;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

@Route("login")
@PageTitle("Login | Carview")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    private final LoginForm login = new LoginForm();

    public LoginView() {
        addClassName("login-view");
        setSizeFull();

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        login.setAction("login");

        add(new H1("Carview"), login);

        // Adiciona um link para o endpoint /new-user
        RouterLink newUserLink = new RouterLink("Create a new account", NewUserFormView.class);
        newUserLink.addClassName("button");  // Aplica a classe de estilo Button
        add(newUserLink);

    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            login.setError(true);

        }
    }
}
