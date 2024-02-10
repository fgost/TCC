package com.example.application.backend.users.service;

import com.example.application.backend.car.service.CarService;
import com.example.application.backend.users.domain.UserEntity;
import com.example.application.backend.users.domain.UserPhotoEntity;
import com.example.application.backend.users.repository.UserPhotoRepository;
import com.example.application.backend.users.repository.UserRepository;
import com.example.application.backend.users.repository.UserRepositoryFront;
import com.example.application.config.security.SecurityConfig;
import com.example.application.domain.Constants;
import com.example.application.exception.domain.ObjectNotFoundException;
import com.example.application.exception.util.ExceptionUtils;
import com.example.application.utils.dto.OnlyCodeDto;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepositoryFront userRepositoryFront;
    private final SecurityConfig securityConfig;
    private final UserRepository repository;
    private final UserPhotoRepository photoRepository;
    private final CarService carService;

    public List<UserEntity> findAll(String name, String email) {
        boolean hasName = name != null && !name.isBlank();
        boolean hasEmail = email != null && !email.isBlank();
        boolean hasBoth = hasName && hasEmail;
        boolean noOne = !hasName && !hasEmail;

        if (hasBoth)
            return repository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(name, email);
        if (noOne)
            return repository.findAll();
        if (hasEmail)
            return repository.findByEmailContainingIgnoreCase(email);
        return repository.findByNameContainingIgnoreCase(name);
    }

    public UserEntity findByCode(String code) {
        return repository.findByCode(code)
                .orElseThrow(() -> new ObjectNotFoundException(Constants.USER_NOT_FOUND));
    }

    public UserEntity findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException(Constants.USER_NOT_FOUND));
    }

    public UserPhotoEntity getProfilePhotoInfo(String code) {
        var userEntity = findByCode(code);
        return photoRepository.findByUserId(userEntity.getId())
                .orElseThrow(() -> new ObjectNotFoundException(Constants.USER_PHOTO_NOT_FOUND));
    }

    @Transactional
    public UserEntity insert(UserEntity entity) {
        try {
            entity.setPassword(new BCryptPasswordEncoder().encode(entity.getPassword()));
            return repository.save(entity);
        } catch (Exception e) {
            throw ExceptionUtils.buildNotPersistedException(Constants.USER_NOT_PERSISTED);
        }
    }

    @Transactional
    public UserEntity update(String code, UserEntity entity) {
        var existentEntity = findByCode(code);
        entity.setCode(code);
        entity.setPassword(existentEntity.getPassword());
        entity.setId(existentEntity.getId());
        entity.setPreferences(existentEntity.getPreferences());

        try {
            return repository.save(entity);
        } catch (Exception e) {
            throw ExceptionUtils.buildNotPersistedException(Constants.USER_NOT_PERSISTED);
        }
    }

    @Transactional
    public UserEntity updateCar(String code, Set<OnlyCodeDto> inputList) {
        var entity = findByCode(code);
        entity.getCars().clear();
        inputList.forEach(input -> {
            var car = carService.findByCode(input.getCode());
            entity.getCars().add(car);
        });
        return repository.save(entity);
    }

    @Transactional
    public void deleteByCode(String code) {
        try {
            var entity = findByCode(code);
            repository.delete(entity);
        } catch (Exception e) {
            throw ExceptionUtils.buildNotPersistedException(Constants.USER_DELETION_ERROR);
        }
    }

    @Transactional
    public void updateLastUpdateMileage() {
        var currentUser = securityConfig.getAuthenticatedUser();
        var user = userRepositoryFront.findByEmail(currentUser);
        user.setName(user.getName());
        user.setLastName(user.getLastName());
        user.setEmail(user.getEmail());
        user.setPassword(user.getPassword());
        user.setLastUpdateMileage(System.currentTimeMillis());
        userRepositoryFront.save(user);
    }
}
