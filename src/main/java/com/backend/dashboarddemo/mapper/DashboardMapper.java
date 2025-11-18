package com.backend.dashboarddemo.mapper;

import com.backend.dashboarddemo.dto.request.DashboardRequestDto;
import com.backend.dashboarddemo.dto.request.UpdatedDashboardRequestDto;
import com.backend.dashboarddemo.dto.response.DashboardResponseDto;
import com.backend.dashboarddemo.model.Dashboard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DashboardMapper {

    Dashboard dashboardRequestToDashboard(DashboardRequestDto dashboardRequestDto);

    DashboardResponseDto dashbordToDashboardResponseDto(Dashboard dashboard);

    List<DashboardResponseDto> dashboardsToDashboardResponseDtos(List<Dashboard> dashboards);

    @Mapping(target = "id", ignore = true)
    void updateDashboardFromDashboardRequest(UpdatedDashboardRequestDto dashboardRequest, @MappingTarget Dashboard dashboard);
}
