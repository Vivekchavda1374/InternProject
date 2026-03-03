package com.vasyerp.rolebasedsystem.service;

import com.vasyerp.rolebasedsystem.dto.CreateProductRequest;
import com.vasyerp.rolebasedsystem.dto.ProductDTO;
import com.vasyerp.rolebasedsystem.dto.UpdateProductRequest;
import com.vasyerp.rolebasedsystem.model.Product;
import com.vasyerp.rolebasedsystem.model.UserFront;
import com.vasyerp.rolebasedsystem.repository.ImageRepository;
import com.vasyerp.rolebasedsystem.repository.ProductRepository;
import com.vasyerp.rolebasedsystem.repository.UserFrontRepository;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserFrontRepository userFrontRepository;
    private final ImageRepository imageRepository;
    private static final List<String> IMPORT_HEADERS = List.of(
            "Column Name 1",
            "Product Name",
            "Item Code",
            "MRP",
            "Purchase Price",
            "Product Variant Name"
    );
    private static final String IMPORT_SHEET_NAME = "Products";
    private static final int COL_COLUMN_NAME_1 = 0;
    private static final int COL_PRODUCT_NAME = 1;
    private static final int COL_ITEM_CODE = 2;
    private static final int COL_MRP = 3;
    private static final int COL_PURCHASE_PRICE = 4;
    private static final int COL_PRODUCT_VARIANT = 5;

    public ProductServiceImpl(
            ProductRepository productRepository,
            UserFrontRepository userFrontRepository,
            ImageRepository imageRepository
    ) {
        this.productRepository = productRepository;
        this.userFrontRepository = userFrontRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "productsAll", allEntries = true),
            @CacheEvict(value = "productsByCompany", allEntries = true),
            @CacheEvict(value = "productsByBranch", allEntries = true),
            @CacheEvict(value = "productById", allEntries = true),
            @CacheEvict(value = "productSearch", allEntries = true),
            @CacheEvict(value = "completeDataAll", allEntries = true),
            @CacheEvict(value = "completeDataByUser", allEntries = true)
    })
    public ProductDTO createProduct(Long userId, Long companyId, CreateProductRequest request) {
        if (request == null) {
            throw new RuntimeException("Request body is required");
        }

        Long actualCompanyId = resolveActualCompanyIdForWrite(userId, companyId);
        String productName = normalizeText(request.getProductName());
        if (productName == null) {
            throw new RuntimeException("Product name is required");
        }
        String itemCode = normalizeItemCode(request.getItemCode());
        if (itemCode != null && productRepository.findByItemCodeAndCompanyId(itemCode, actualCompanyId).isPresent()) {
            throw new RuntimeException("Item code already exists in same company: " + itemCode);
        }

        Product product = new Product();
        product.setProductName(productName);
        product.setItemCode(itemCode);
        product.setCompanyId(actualCompanyId);
        product.setMrp(request.getMrp());
        product.setSellingPrice(request.getSellingPrice());
        product.setPurchasePrice(request.getPurchasePrice());
        product.setProductVariantName(normalizeText(request.getProductVariantName()));
        product.setDescription(request.getDescription());
        product.setStockQuantity(request.getStockQuantity());

        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "productsAll", allEntries = true),
            @CacheEvict(value = "productsByCompany", allEntries = true),
            @CacheEvict(value = "productsByBranch", allEntries = true),
            @CacheEvict(value = "productById", allEntries = true),
            @CacheEvict(value = "productSearch", allEntries = true),
            @CacheEvict(value = "completeDataAll", allEntries = true),
            @CacheEvict(value = "completeDataByUser", allEntries = true)
    })
    public ProductDTO updateProduct(Long userId, Long productId, UpdateProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!hasProductUpdatePermission(userId, product.getCompanyId())) {
            throw new RuntimeException("User does not have permission to update products for this company");
        }

        if (request.getProductName() != null && !request.getProductName().isEmpty()) {
            product.setProductName(request.getProductName());
        }
        if (request.getItemCode() != null && !request.getItemCode().isEmpty()) {
            String newItemCode = normalizeItemCode(request.getItemCode());
            if (newItemCode != null && !newItemCode.equalsIgnoreCase(product.getItemCode())) {
                productRepository.findByItemCodeAndCompanyId(newItemCode, product.getCompanyId())
                        .ifPresent(existing -> {
                            if (!existing.getProductId().equals(product.getProductId())) {
                                throw new RuntimeException("Item code already exists in same company: " + newItemCode);
                            }
                        });
            }
            product.setItemCode(newItemCode);
        }
        if (request.getMrp() != null) {
            product.setMrp(request.getMrp());
        }
        if (request.getSellingPrice() != null) {
            product.setSellingPrice(request.getSellingPrice());
        }
        if (request.getPurchasePrice() != null) {
            product.setPurchasePrice(request.getPurchasePrice());
        }
        if (request.getProductVariantName() != null) {
            product.setProductVariantName(request.getProductVariantName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getStockQuantity() != null) {
            product.setStockQuantity(request.getStockQuantity());
        }

        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "productsAll", allEntries = true),
            @CacheEvict(value = "productsByCompany", allEntries = true),
            @CacheEvict(value = "productsByBranch", allEntries = true),
            @CacheEvict(value = "productById", allEntries = true),
            @CacheEvict(value = "productSearch", allEntries = true),
            @CacheEvict(value = "completeDataAll", allEntries = true),
            @CacheEvict(value = "completeDataByUser", allEntries = true)
    })
    public void deleteProduct(Long userId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!hasProductDeletePermission(userId, product.getCompanyId())) {
            throw new RuntimeException("User does not have permission to delete products for this company");
        }

        if (imageRepository.existsById(productId)) {
            imageRepository.deleteById(productId);
        }
        productRepository.deleteById(productId);
    }

    @Override
    @Cacheable(value = "productsByCompany", key = "#userId + ':' + #targetCompanyId")
    public List<ProductDTO> getProductsByCompany(Long userId, Long targetCompanyId) {
        UserFront currentUser = userFrontRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isAllowed = false;
        if ("admin".equals(currentUser.getName())) {
            isAllowed = true;
        } else if (currentUser.getParentCompany() == null) {
            if (targetCompanyId.equals(currentUser.getUserFrontId())) {
                isAllowed = true;
            } else {
                UserFront targetUser = userFrontRepository.findById(targetCompanyId).orElse(null);
                if (targetUser != null && currentUser.getUserFrontId()
                        .equals(targetUser.getParentCompany() != null ? targetUser.getParentCompany().getUserFrontId()
                                : null)) {
                    isAllowed = true;
                }
            }
        } else {
            if (targetCompanyId.equals(currentUser.getUserFrontId())) {
                isAllowed = true;
            }
        }

        if (!isAllowed) {
            throw new RuntimeException("Access denied to view products of this company/branch");
        }

        List<Product> products = productRepository.findByCompanyId(targetCompanyId);
        return products.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Cacheable("productsAll")
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getVisibleProducts(Long userId) {
        UserFront currentUser = userFrontRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isSystemAdmin = "admin".equalsIgnoreCase(currentUser.getName())
                && currentUser.getParentCompany() == null;
        if (isSystemAdmin) {
            return getAllProducts();
        }

        Set<Long> ownerIds = new HashSet<>();
        Long currentUserId = currentUser.getUserFrontId();
        ownerIds.add(currentUserId);

        if (currentUser.getParentCompany() == null) {
            List<UserFront> branches = userFrontRepository.findByParentCompany(currentUser);
            for (UserFront branch : branches) {
                if (branch.getUserFrontId() != null) {
                    ownerIds.add(branch.getUserFrontId());
                }
            }
        } else if (currentUser.getParentCompany().getUserFrontId() != null) {
            ownerIds.add(currentUser.getParentCompany().getUserFrontId());
        }

        List<Product> products = productRepository.findByCompanyIdIn(new ArrayList<>(ownerIds));
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "productById", key = "#productId")
    public ProductDTO getProductById(Long userId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return convertToDTO(product);
    }

    @Override
    @Cacheable(value = "productSearch", key = "#userId + ':' + #companyId + ':' + #searchTerm")
    public List<ProductDTO> searchProductsByName(Long userId, Long companyId, String searchTerm) {
        List<Product> products = productRepository.searchProductsByName(companyId, searchTerm);
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "productsByBranch", key = "#userId + ':' + #companyId + ':' + #branchId")
    public List<ProductDTO> getProductsByBranch(Long userId, Long companyId, Long branchId) {
        List<Product> products = productRepository.findByCompanyId(companyId);
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public byte[] generateProductImportTemplate() {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(IMPORT_SHEET_NAME);
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < IMPORT_HEADERS.size(); i++) {
                headerRow.createCell(i).setCellValue(IMPORT_HEADERS.get(i));
                sheet.autoSizeColumn(i);
            }
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate import template: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> validateProductImportExcel(Long userId, Long companyId, MultipartFile file) {
        Long actualCompanyId = resolveActualCompanyIdForWrite(userId, companyId);
        ValidationContext validationContext = parseAndValidateExcel(file, actualCompanyId);
        return buildValidationResponse(validationContext, 0);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "productsAll", allEntries = true),
            @CacheEvict(value = "productsByCompany", allEntries = true),
            @CacheEvict(value = "productsByBranch", allEntries = true),
            @CacheEvict(value = "productById", allEntries = true),
            @CacheEvict(value = "productSearch", allEntries = true),
            @CacheEvict(value = "completeDataAll", allEntries = true),
            @CacheEvict(value = "completeDataByUser", allEntries = true)
    })
    public Map<String, Object> importProductsFromExcel(Long userId, Long companyId, MultipartFile file) {
        Long actualCompanyId = resolveActualCompanyIdForWrite(userId, companyId);
        ValidationContext validationContext = parseAndValidateExcel(file, actualCompanyId);
        if (!validationContext.errors.isEmpty()) {
            return buildValidationResponse(validationContext, 0);
        }

        int importedCount = 0;
        for (ImportRowData rowData : validationContext.validRows) {
            Product product = new Product();
            product.setProductName(rowData.productName());
            product.setItemCode(rowData.itemCode());
            product.setCompanyId(actualCompanyId);
            product.setMrp(rowData.mrp());
            product.setSellingPrice(rowData.mrp());
            product.setPurchasePrice(rowData.purchasePrice());
            product.setProductVariantName(rowData.productVariantName());
            product.setStockQuantity(0.0);
            productRepository.save(product);
            importedCount++;
        }

        return buildValidationResponse(validationContext, importedCount);
    }

    private boolean hasProductCreatePermission(Long userId, Long companyId) {
        return isCompany(userId) || isBranch(userId);
    }

    private boolean hasProductUpdatePermission(Long userId, Long companyId) {
        return isCompany(userId) || isBranch(userId);
    }

    private boolean hasProductDeletePermission(Long userId, Long companyId) {
        return isCompany(userId);
    }

    private boolean isCompany(Long userId) {
        return userFrontRepository.findById(userId)
                .map(user -> user.getParentCompany() == null)
                .orElse(false);
    }

    private boolean isBranch(Long userId) {
        return userFrontRepository.findById(userId)
                .map(user -> user.getParentCompany() != null)
                .orElse(false);
    }

    private Long resolveActualCompanyIdForWrite(Long userId, Long companyId) {
        if (!hasProductCreatePermission(userId, companyId)) {
            throw new RuntimeException("User does not have permission to create products for this company");
        }

        UserFront currentUser = userFrontRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (currentUser.getParentCompany() == null) {
            return currentUser.getUserFrontId();
        }
        return currentUser.getParentCompany().getUserFrontId();
    }

    private ValidationContext parseAndValidateExcel(MultipartFile file, Long companyId) {
        ValidationContext context = new ValidationContext();
        if (file == null || file.isEmpty()) {
            context.errors.add(buildError(0, null, null, "Excel file is required"));
            return context;
        }

        try (InputStream inputStream = file.getInputStream(); Workbook workbook = WorkbookFactory.create(inputStream)) {
            if (workbook.getNumberOfSheets() == 0) {
                context.errors.add(buildError(0, null, null, "Excel file has no sheet"));
                return context;
            }

            Sheet sheet = workbook.getSheetAt(0);
            if (!isHeaderValid(sheet.getRow(0), context)) {
                return context;
            }

            DataFormatter dataFormatter = new DataFormatter();
            Set<String> seenItemCodes = new HashSet<>();

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null || isBlankRow(row, dataFormatter)) {
                    continue;
                }
                context.totalRows++;

                String columnName1 = normalizeText(dataFormatter.formatCellValue(row.getCell(COL_COLUMN_NAME_1)));
                String productName = normalizeText(dataFormatter.formatCellValue(row.getCell(COL_PRODUCT_NAME)));
                String itemCode = normalizeItemCode(dataFormatter.formatCellValue(row.getCell(COL_ITEM_CODE)));
                String productVariantName = normalizeText(
                        dataFormatter.formatCellValue(row.getCell(COL_PRODUCT_VARIANT))
                );

                List<String> rowErrors = new ArrayList<>();
                if (productName == null) {
                    rowErrors.add("Product Name is required");
                }
                if (itemCode == null) {
                    rowErrors.add("Item Code is required");
                }

                Double mrp = parsePositiveNumber(
                        dataFormatter.formatCellValue(row.getCell(COL_MRP)),
                        "MRP",
                        rowErrors
                );
                Double purchasePrice = parsePositiveNumber(
                        dataFormatter.formatCellValue(row.getCell(COL_PURCHASE_PRICE)),
                        "Purchase Price",
                        rowErrors
                );

                if (itemCode != null) {
                    String normalizedCode = itemCode.toLowerCase(Locale.ROOT);
                    if (!seenItemCodes.add(normalizedCode)) {
                        rowErrors.add("Duplicate item code in uploaded file: " + itemCode);
                    }

                    if (productRepository.findByItemCodeAndCompanyId(itemCode, companyId).isPresent()) {
                        rowErrors.add("Item code already exists in same company: " + itemCode);
                    }
                }

                if (!rowErrors.isEmpty()) {
                    context.errors.add(buildError(rowIndex + 1, productName, itemCode, rowErrors.toArray(String[]::new)));
                    continue;
                }

                context.validRows.add(new ImportRowData(
                        rowIndex + 1,
                        columnName1,
                        productName,
                        itemCode,
                        mrp,
                        purchasePrice,
                        productVariantName
                ));
            }
        } catch (Exception e) {
            context.errors.add(buildError(0, null, null, "Failed to read Excel file: " + e.getMessage()));
        }

        return context;
    }

    private boolean isHeaderValid(Row headerRow, ValidationContext context) {
        if (headerRow == null) {
            context.errors.add(buildError(1, null, null, "Header row is missing"));
            return false;
        }

        DataFormatter dataFormatter = new DataFormatter();
        List<String> mismatchMessages = new ArrayList<>();
        for (int i = 0; i < IMPORT_HEADERS.size(); i++) {
            String expected = IMPORT_HEADERS.get(i);
            String actual = normalizeText(dataFormatter.formatCellValue(headerRow.getCell(i)));
            if (actual == null || !expected.equalsIgnoreCase(actual)) {
                mismatchMessages.add(
                        "Invalid header at column " + (i + 1) + ". Expected '" + expected + "' but found '" +
                                (actual == null ? "" : actual) + "'"
                );
            }
        }

        if (!mismatchMessages.isEmpty()) {
            context.errors.add(buildError(1, null, null, mismatchMessages.toArray(String[]::new)));
            return false;
        }
        return true;
    }

    private boolean isBlankRow(Row row, DataFormatter dataFormatter) {
        for (int i = COL_COLUMN_NAME_1; i <= COL_PRODUCT_VARIANT; i++) {
            String value = normalizeText(dataFormatter.formatCellValue(row.getCell(i)));
            if (value != null) {
                return false;
            }
        }
        return true;
    }

    private Double parsePositiveNumber(String cellValue, String fieldName, List<String> rowErrors) {
        String value = normalizeText(cellValue);
        if (value == null) {
            rowErrors.add(fieldName + " is required");
            return null;
        }

        String normalizedNumber = value.replace(",", "");
        try {
            Double parsed = Double.parseDouble(normalizedNumber);
            if (parsed < 0) {
                rowErrors.add(fieldName + " must be 0 or greater");
                return null;
            }
            return parsed;
        } catch (NumberFormatException ex) {
            rowErrors.add(fieldName + " must be a valid number");
            return null;
        }
    }

    private String normalizeText(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String normalizeItemCode(String itemCode) {
        String normalized = normalizeText(itemCode);
        return normalized == null ? null : normalized.toUpperCase(Locale.ROOT);
    }

    private Map<String, Object> buildValidationResponse(ValidationContext context, int importedCount) {
        Map<String, Object> response = new LinkedHashMap<>();
        boolean valid = context.errors.isEmpty();
        response.put("valid", valid);
        response.put("totalRows", context.totalRows);
        response.put("validRows", context.validRows.size());
        response.put("invalidRows", context.errors.size());
        response.put("importedCount", importedCount);
        response.put("errors", context.errors);
        return response;
    }

    private Map<String, Object> buildError(int rowNumber, String productName, String itemCode, String... messages) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("rowNumber", rowNumber);
        error.put("productName", productName);
        error.put("itemCode", itemCode);
        error.put("messages", List.of(messages));
        return error;
    }

    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(
                product.getProductId(),
                product.getProductName(),
                product.getCompanyId(),
                product.getItemCode(),
                product.getMrp(),
                product.getSellingPrice(),
                product.getPurchasePrice(),
                product.getProductVariantName(),
                product.getDescription(),
                product.getStockQuantity());
    }

    private static class ValidationContext {
        private int totalRows;
        private final List<ImportRowData> validRows = new ArrayList<>();
        private final List<Map<String, Object>> errors = new ArrayList<>();
    }

    private record ImportRowData(
            int rowNumber,
            String columnName1,
            String productName,
            String itemCode,
            Double mrp,
            Double purchasePrice,
            String productVariantName
    ) {}
}
