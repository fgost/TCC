package com.example.application.views.logout;

import com.example.application.config.security.SecurityConfig;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@Route("logout")
@PermitAll
public class LogoutView extends VerticalLayout {
    private final SecurityConfig securityConfig;

    public LogoutView(@Autowired SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
        logout();
    }

    private void logout() {
        securityConfig.logout();
    }
}
