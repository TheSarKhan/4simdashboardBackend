package com.backend.dashboarddemo.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface ExcelExportService {
    ByteArrayInputStream exportUsers() throws IOException;

    ByteArrayInputStream exportDashboards() throws IOException;

    ByteArrayInputStream exportRoles() throws IOException;
}
