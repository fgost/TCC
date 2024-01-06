package com.example.application.backend.users.mapper;

import com.example.application.backend.users.domain.UserEntity;
import com.example.application.backend.users.dto.UserDtoUpdate;
import com.example.application.backend.users.model.request.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserRequestMapper {
    UserRequestMapper INSTANCE = Mappers.getMapper(UserRequestMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    UserEntity mapModelToEntity(UserRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "password", ignore = true)
    UserEntity updateMapModelToEntity(UserDtoUpdate dtoUpdate);

}
