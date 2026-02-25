package com.vasyerp.rolebasedsystem.service;

import com.vasyerp.rolebasedsystem.dto.CompleteDataDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CompleteDataExcelExportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompleteDataExcelExportService.class);
    private static final DateTimeFormatter FILE_NAME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
    private static final List<String> HEADERS = List.of(
            "ID",
            "Company",
            "Branch",
            "Total Purchase",
            "Total Sales",
            "Total Products",
            "GST",
            "Phone",
            "Address",
            "City",
            "State",
            "Country"
    );

    private final CompleteDataService completeDataService;
    private final String exportDirectory;

    public CompleteDataExcelExportService(
            CompleteDataService completeDataService,
            @Value("${app.export.directory:exports}") String exportDirectory
    ) {
        this.completeDataService = completeDataService;
        this.exportDirectory = exportDirectory;
    }

    public Path exportAllDataToExcel() throws IOException {
        List<CompleteDataDTO> data = completeDataService.getAllData(null);
        return writeExcelFile(data, "scheduled-complete-data-admin", "scheduled");
    }

    public Path exportScheduledDataByUser(Long userId, boolean isAdmin, String country, String scopeLabel)
            throws IOException {
        if (!isAdmin && userId == null) {
            throw new IllegalArgumentException("User ID is required for non-admin scheduled export");
        }

        List<CompleteDataDTO> data = isAdmin
                ? completeDataService.getAllData(country)
                : completeDataService.getDataByUser(userId, false, country);

        String normalizedScopeLabel = normalizeScopeLabel(scopeLabel);
        String filePrefix = isAdmin
                ? "scheduled-complete-data-" + normalizedScopeLabel
                : "scheduled-complete-data-" + normalizedScopeLabel + "-user-" + userId;
        return writeExcelFile(data, filePrefix, "scheduled");
    }

    public Path exportDataByUserToExcel(Long userId, boolean isAdmin, String country) throws IOException {
        return exportDataByUserToExcel(userId, isAdmin, country, isAdmin ? "SYSTEM_ADMIN" : "USER");
    }

    public Path exportDataByUserToExcel(Long userId, boolean isAdmin, String country, String scopeLabel)
            throws IOException {
        if (!isAdmin && userId == null) {
            throw new IllegalArgumentException("User ID is required for non-admin export");
        }

        List<CompleteDataDTO> data = isAdmin
                ? completeDataService.getAllData(country)
                : completeDataService.getDataByUser(userId, false, country);

        String normalizedScopeLabel = normalizeScopeLabel(scopeLabel);
        String filePrefix = isAdmin
                ? "on-demand-complete-data-" + normalizedScopeLabel
                : "on-demand-complete-data-" + normalizedScopeLabel + "-user-" + userId;
        return writeExcelFile(data, filePrefix, "on-demand");
    }

    private Path writeExcelFile(List<CompleteDataDTO> data, String filePrefix, String subDirectory) throws IOException {
        Path exportDirPath = resolveExportDirectory(subDirectory);

        String fileName = filePrefix + "-" + LocalDateTime.now().format(FILE_NAME_FORMATTER) + ".xlsx";
        Path outputPath = exportDirPath.resolve(fileName);
        Path tempOutputPath = exportDirPath.resolve(fileName + ".tmp");

        try (Workbook workbook = new XSSFWorkbook();
             OutputStream outputStream = Files.newOutputStream(tempOutputPath)) {
            Sheet sheet = workbook.createSheet("Complete Data");
            createHeaderRow(sheet);
            populateDataRows(sheet, data);
            autoSizeColumns(sheet);
            workbook.write(outputStream);
        }

        Files.move(tempOutputPath, outputPath, StandardCopyOption.REPLACE_EXISTING);
        LOGGER.info("Complete data export created with {} rows at {}", data.size(), outputPath.toAbsolutePath());
        return outputPath;
    }

    private Path resolveExportDirectory(String subDirectory) throws IOException {
        Path configuredBasePath = Paths.get(exportDirectory).toAbsolutePath().normalize();
        Path configuredPath = configuredBasePath.resolve(subDirectory).normalize();

        try {
            Files.createDirectories(configuredPath);
            return configuredPath;
        } catch (IOException ex) {
            Path fallbackPath = Paths.get(System.getProperty("java.io.tmpdir"),
                    "rolebased-system", "exports", subDirectory);
            Files.createDirectories(fallbackPath);
            LOGGER.warn("Unable to use export directory {}. Falling back to {}",
                    configuredPath, fallbackPath.toAbsolutePath(), ex);
            return fallbackPath;
        }
    }

    private void createHeaderRow(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < HEADERS.size(); i++) {
            headerRow.createCell(i).setCellValue(HEADERS.get(i));
        }
    }

    private void populateDataRows(Sheet sheet, List<CompleteDataDTO> data) {
        int rowIndex = 1;
        for (CompleteDataDTO dto : data) {
            Row row = sheet.createRow(rowIndex++);
            int col = 0;

            setCellValue(row, col++, dto.getHierarchyOrder());
            setCellValue(row, col++, dto.getCompanyName());
            setCellValue(row, col++, dto.getBranchName());
            setCellValue(row, col++, dto.getTotalPurchaseAmount());
            setCellValue(row, col++, dto.getTotalSalesAmount());
            setCellValue(row, col++, dto.getTotalProducts());
            setCellValue(row, col++, dto.getGstNo());
            setCellValue(row, col++, dto.getPhoneNo());
            setCellValue(row, col++, buildAddress(dto));
            setCellValue(row, col++, dto.getCity());
            setCellValue(row, col++, dto.getState());
            setCellValue(row, col++, dto.getCountry());
        }
    }

    private void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i < HEADERS.size(); i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void setCellValue(Row row, int columnIndex, Object value) {
        Cell cell = row.createCell(columnIndex);
        if (value == null) {
            cell.setBlank();
            return;
        }
        if (value instanceof Number number) {
            cell.setCellValue(number.doubleValue());
            return;
        }
        cell.setCellValue(value.toString());
    }

    private String buildAddress(CompleteDataDTO dto) {
        String line1 = dto.getAddressLine1();
        String line2 = dto.getAddressLine2();

        boolean hasLine1 = line1 != null && !line1.isBlank();
        boolean hasLine2 = line2 != null && !line2.isBlank();

        if (hasLine1 && hasLine2) {
            return line1 + ", " + line2;
        }
        if (hasLine1) {
            return line1;
        }
        if (hasLine2) {
            return line2;
        }
        return "";
    }

    private String normalizeScopeLabel(String scopeLabel) {
        if (scopeLabel == null || scopeLabel.isBlank()) {
            return "unknown";
        }
        return scopeLabel.trim().toLowerCase().replace('_', '-');
    }
}
