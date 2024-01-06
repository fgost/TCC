package com.example.application.backend.users;

import com.example.application.backend.users.domain.UserEntity;
import com.example.application.backend.users.dto.UserDtoUpdate;
import com.example.application.backend.users.mapper.UserRequestMapper;
import com.example.application.backend.users.mapper.UserResponseMapper;
import com.example.application.backend.users.model.response.UserResponse;
import com.example.application.backend.users.model.response.UserResponseCar;
import com.example.application.backend.users.model.response.UserResponsePreference;
import com.example.application.backend.users.model.response.UserResponseSummary;
import com.example.application.backend.users.service.UserService;
import com.example.application.utils.dto.OnlyCodeDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Component
public class UserFacade {

    private UserService usersService;

    public List<UserEntity> findAll(String name, String email) {
        return usersService.findAll(name, email);
    }

    public UserResponse findByCode(String code) {
        var entity = usersService.findByCode(code);
        var dto = UserResponseMapper.INSTANCE.mapEntityToUserResponse(entity);
        return dto;
    }

    public UserResponsePreference getPreferences(String code) {
        var entity = usersService.findByCode(code);
        var dto = UserResponseMapper.INSTANCE.mapEntityToPreference(entity);
        return dto;
    }

    public UserResponseCar getCars(String code) {
        var entity = usersService.findByCode(code);
        var dto = UserResponseMapper.INSTANCE.mapEntityToCar(entity);
        return dto;
    }

    public UserResponseSummary insert(UserEntity userEntity) {
        var savedEntity = usersService.insert(userEntity);
        var dto = UserResponseMapper.INSTANCE.mapEntityToResponse(savedEntity);
        return dto;
    }

    public UserResponseSummary update(String code, UserDtoUpdate input) {
        var entity = UserRequestMapper.INSTANCE.updateMapModelToEntity(input);
        var savedEntity = usersService.update(code, entity);
        var dto = UserResponseMapper.INSTANCE.mapEntityToResponse(savedEntity);
        return dto;
    }

    public UserResponse updateCar(String code, Set<OnlyCodeDto> inputList) {
        var entity = usersService.updateCar(code, inputList);
        var dto = UserResponseMapper.INSTANCE.mapEntityToUserResponse(entity);
        return dto;
    }

    public void deleteByCode(String code) {
        usersService.deleteByCode(code);
    }
}
