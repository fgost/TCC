package com.example.application.config.DataBaseSeeder;

import com.example.application.backend.users.domain.UserEntity;
import com.example.application.backend.users.repository.UserRepository;
import com.example.application.exception.util.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserSeeder implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;

    @Override
    public void run(String... args) {
        try {
            databaseUserSeeder();
        } catch (Exception e) {
            throw ExceptionUtils.buildNotPersistedException("Error: " + e);
        }
    }

    private void databaseUserSeeder() {
        if (userRepository.count() == 0) {
            UserEntity admin = new UserEntity();
            admin.setName("matheus");
            admin.setCode("57b67e0d-4861-433c-99a7-f45a835d4b9c");
            admin.setLastName("firmiano");
            admin.setEmail("admin@fghost.net");
            admin.setPassword(new BCryptPasswordEncoder().encode("admin@fghost"));
            admin.setLongitude("231");
            admin.setLatitude("132");
            userRepository.save(admin);
        }
    }
}