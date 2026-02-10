package com.vasyerp.rolebasedsystem.controller;

import com.vasyerp.rolebasedsystem.dto.ApiResponse;
import com.vasyerp.rolebasedsystem.dto.CreatePurchaseRequest;
import com.vasyerp.rolebasedsystem.dto.PurchaseDTO;
import com.vasyerp.rolebasedsystem.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<ApiResponse<PurchaseDTO>> createPurchase(@RequestBody CreatePurchaseRequest request) {
        PurchaseDTO purchase = purchaseService.createPurchase(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Purchase created successfully", purchase));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PurchaseDTO>>> getAllPurchases() {
        List<PurchaseDTO> purchases = purchaseService.getAllPurchases();
        return ResponseEntity.ok(new ApiResponse<>(true, "All purchases retrieved successfully", purchases));
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<ApiResponse<List<PurchaseDTO>>> getPurchasesByBranch(@PathVariable Long branchId) {
        List<PurchaseDTO> purchases = purchaseService.getPurchasesByBranch(branchId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Purchases retrieved successfully", purchases));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<ApiResponse<List<PurchaseDTO>>> getPurchasesByCompany(@PathVariable Long companyId) {
        List<PurchaseDTO> purchases = purchaseService.getPurchasesByCompany(companyId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Purchases retrieved successfully", purchases));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePurchase(@PathVariable Long id) {
        purchaseService.deletePurchase(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Purchase deleted successfully", null));
    }
}
