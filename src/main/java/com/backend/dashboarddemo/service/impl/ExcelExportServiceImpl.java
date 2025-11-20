package com.backend.dashboarddemo.service.impl;

import com.backend.dashboarddemo.model.Dashboard;
import com.backend.dashboarddemo.model.User;
import com.backend.dashboarddemo.model.UserRole;
import com.backend.dashboarddemo.repository.DashboardRepository;
import com.backend.dashboarddemo.repository.UserRepository;
import com.backend.dashboarddemo.repository.UserRoleRepository;
import com.backend.dashboarddemo.service.ExcelExportService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExcelExportServiceImpl implements ExcelExportService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final DashboardRepository dashboardRepository;

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public ByteArrayInputStream exportUsers() throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Users");

            int rowIdx = 0;

            Row header = sheet.createRow(rowIdx++);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Full Name");
            header.createCell(2).setCellValue("Email");
            header.createCell(3).setCellValue("Phone");
            header.createCell(4).setCellValue("Active");
            header.createCell(5).setCellValue("Created At");
            header.createCell(6).setCellValue("Roles");

            for (User user : userRepository.findAll()) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(user.getId());
                row.createCell(1).setCellValue(nullSafe(user.getFullName()));
                row.createCell(2).setCellValue(nullSafe(user.getEmail()));
                row.createCell(3).setCellValue(nullSafe(user.getPhoneNumber()));
                row.createCell(4).setCellValue(user.isActive());

                String createdAt = user.getCreatedAt() != null
                        ? DATE_TIME_FORMATTER.format(user.getCreatedAt())
                        : "";
                row.createCell(5).setCellValue(createdAt);

                String roles = user.getRoles().stream()
                        .map(UserRole::getName)
                        .collect(Collectors.joining(", "));
                row.createCell(6).setCellValue(roles);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }


    public ByteArrayInputStream exportRoles() throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Roles");

            int rowIdx = 0;

            Row header = sheet.createRow(rowIdx++);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Description");
            header.createCell(3).setCellValue("Created At");
            header.createCell(4).setCellValue("Users Count");
            header.createCell(5).setCellValue("Dashboards Count");

            for (UserRole role : userRoleRepository.findAll()) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(role.getId());
                row.createCell(1).setCellValue(nullSafe(role.getName()));
                row.createCell(2).setCellValue(nullSafe(role.getDescription()));

                String createdAt = role.getCreatedAt() != null
                        ? DATE_TIME_FORMATTER.format(role.getCreatedAt())
                        : "";
                row.createCell(3).setCellValue(createdAt);

                row.createCell(4).setCellValue(role.getUsers().size());
                row.createCell(5).setCellValue(role.getDashboards().size());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }


    public ByteArrayInputStream exportDashboards() throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Dashboards");

            int rowIdx = 0;

            Row header = sheet.createRow(rowIdx++);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Embed Url");
            header.createCell(3).setCellValue("Active");
            header.createCell(4).setCellValue("Roles");

            for (Dashboard dashboard : dashboardRepository.findAll()) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(dashboard.getId());
                row.createCell(1).setCellValue(nullSafe(dashboard.getName()));
                row.createCell(2).setCellValue(nullSafe(dashboard.getEmbedUrl()));
                row.createCell(3).setCellValue(dashboard.isActive());

                String roles = dashboard.getRoles().stream()
                        .map(UserRole::getName)
                        .collect(Collectors.joining(", "));
                row.createCell(4).setCellValue(roles);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    private String nullSafe(String s) {
        return s == null ? "" : s;
    }
}

