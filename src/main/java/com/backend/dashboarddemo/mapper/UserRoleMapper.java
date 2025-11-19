package com.backend.dashboarddemo.mapper;

import com.backend.dashboarddemo.dto.request.RoleRequestDto;
import com.backend.dashboarddemo.dto.response.RoleResponseDto;
import com.backend.dashboarddemo.model.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserRoleMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "dashboards", ignore = true)
    UserRole userRoleRequestDtoToUserRole(RoleRequestDto dto);

    RoleResponseDto userRoleToRoleResponseDto(UserRole role);

    List<RoleResponseDto> userRolesToRoleResponseDtos(List<UserRole> roles);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "dashboards", ignore = true)
    void updateRoleFromRequest(RoleRequestDto dto, @MappingTarget UserRole role);
}
