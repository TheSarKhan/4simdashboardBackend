package com.backend.dashboarddemo.controller;


import com.backend.dashboarddemo.service.ExcelExportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/export")
@RequiredArgsConstructor
@Tag(name = "Excel Export", description = "Excel Export API")
@PreAuthorize("hasRole('ADMIN')")
public class ExcelExportController {

    private final ExcelExportService excelExportService;

    @GetMapping("/users")
    public ResponseEntity<Resource> exportUsers() throws IOException {
        ByteArrayInputStream in = excelExportService.exportUsers();
        return buildExcelResponse(in, "users.xlsx");
    }

    @GetMapping("/roles")
    public ResponseEntity<Resource> exportRoles() throws IOException {
        ByteArrayInputStream in = excelExportService.exportRoles();
        return buildExcelResponse(in, "roles.xlsx");
    }

    @GetMapping("/dashboards")
    public ResponseEntity<Resource> exportDashboards() throws IOException {
        ByteArrayInputStream in = excelExportService.exportDashboards();
        return buildExcelResponse(in, "dashboards.xlsx");
    }

    private ResponseEntity<Resource> buildExcelResponse(ByteArrayInputStream in, String filename) {
        InputStreamResource file = new InputStreamResource(in);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .body(file);
    }
}

