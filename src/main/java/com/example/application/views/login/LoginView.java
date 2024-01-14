package com.example.application.views.login;

import com.example.application.views.newUserForm.NewUserFormView;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

@Route("login")
@PageTitle("Login | Carview")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    private final LoginForm login = new LoginForm();

    public LoginView() {
        LoginI18n i18n = LoginI18n.createDefault();
        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setUsername("Email");

        addClassName("login-view");
        setSizeFull();

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        login.setI18n(i18n);
        login.setAction("login");

        VerticalLayout customFormLayout = new VerticalLayout(login);
        customFormLayout.setAlignItems(Alignment.CENTER);
        customFormLayout.setSizeUndefined();

        add(new H1("Carview"), customFormLayout);

        RouterLink newUserLink = new RouterLink("Create a new account", NewUserFormView.class);
        newUserLink.addClassName("button");
        add(newUserLink);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            login.setError(true);
        }
    }
}
