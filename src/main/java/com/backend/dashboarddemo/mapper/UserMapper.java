package com.backend.dashboarddemo.mapper;

import com.backend.dashboarddemo.dto.request.UserRequestDto;
import com.backend.dashboarddemo.dto.response.UserResponseDto;
import com.backend.dashboarddemo.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User userRequestDtoToUser(UserRequestDto userRequestDto);

    UserResponseDto userToUserResponseDto(User user);

    List<UserResponseDto> usersToUserResponseDtos(List<User> users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void updateUserFromRequest(UserRequestDto userRequestDto, @MappingTarget User user);
}
