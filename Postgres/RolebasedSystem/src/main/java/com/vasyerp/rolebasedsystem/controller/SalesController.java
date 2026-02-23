package com.vasyerp.rolebasedsystem.controller;

import com.vasyerp.rolebasedsystem.dto.ApiResponse;
import com.vasyerp.rolebasedsystem.dto.CreateSalesRequest;
import com.vasyerp.rolebasedsystem.dto.SalesDTO;
import com.vasyerp.rolebasedsystem.service.SalesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SalesController {

    private SalesService salesService;

    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SalesDTO>> createSales(@RequestBody CreateSalesRequest request) {
        SalesDTO sales = salesService.createSales(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Sales created successfully", sales));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SalesDTO>>> getAllSales() {
        List<SalesDTO> sales = salesService.getAllSales();
        return ResponseEntity.ok(new ApiResponse<>(true, "All sales retrieved successfully", sales));
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<ApiResponse<List<SalesDTO>>> getSalesByBranch(@PathVariable Long branchId) {
        List<SalesDTO> sales = salesService.getSalesByBranch(branchId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Sales retrieved successfully", sales));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<ApiResponse<List<SalesDTO>>> getSalesByCompany(@PathVariable Long companyId) {
        List<SalesDTO> sales = salesService.getSalesByCompany(companyId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Sales retrieved successfully", sales));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSales(@PathVariable Long id) {
        salesService.deleteSales(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Sales deleted successfully", null));
    }
}
