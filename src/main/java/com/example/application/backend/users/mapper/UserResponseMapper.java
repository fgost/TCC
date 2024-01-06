package com.example.application.backend.users.mapper;

import com.example.application.backend.users.domain.UserEntity;
import com.example.application.backend.users.model.response.UserResponse;
import com.example.application.backend.users.model.response.UserResponseCar;
import com.example.application.backend.users.model.response.UserResponsePreference;
import com.example.application.backend.users.model.response.UserResponseSummary;
import com.example.application.backend.users.model.response.permissions.UserResponsePermissionWrapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserResponseMapper {

    UserResponseMapper INSTANCE = Mappers.getMapper(UserResponseMapper.class);

    @Mapping(target = "nomeCompleto", expression = "java(entity.getNomeCompleto())")
    UserResponseSummary mapEntityToResponse(UserEntity entity);

    @Mapping(target = "fullName", expression = "java(entity.getNomeCompleto())")
    UserResponsePreference mapEntityToPreference(UserEntity entity);


    @Mapping(target = "fullName", expression = "java(entity.getNomeCompleto())")
    UserResponseCar mapEntityToCar(UserEntity entity);

    UserResponsePermissionWrapper mapEntityToPermission(UserEntity entity);

    UserResponse mapEntityToUserResponse(UserEntity entity);


    //UsersResponseMapper mapResponseMapperToModelO(UserEntity entity);
}
