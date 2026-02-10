package com.vasyerp.rolebasedsystem.service;

import com.vasyerp.rolebasedsystem.dto.CreatePurchaseRequest;
import com.vasyerp.rolebasedsystem.dto.PurchaseDTO;

import java.util.List;

public interface PurchaseService {
    PurchaseDTO createPurchase(CreatePurchaseRequest request);

    List<PurchaseDTO> getAllPurchases();

    List<PurchaseDTO> getPurchasesByBranch(Long branchId);

    List<PurchaseDTO> getPurchasesByCompany(Long companyId);

    void deletePurchase(Long id);
}
