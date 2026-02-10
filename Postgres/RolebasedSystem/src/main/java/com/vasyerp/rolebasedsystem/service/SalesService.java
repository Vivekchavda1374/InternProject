package com.vasyerp.rolebasedsystem.service;

import com.vasyerp.rolebasedsystem.dto.CreateSalesRequest;
import com.vasyerp.rolebasedsystem.dto.SalesDTO;

import java.util.List;

public interface SalesService {
    SalesDTO createSales(CreateSalesRequest request);

    List<SalesDTO> getAllSales();

    List<SalesDTO> getSalesByBranch(Long branchId);

    List<SalesDTO> getSalesByCompany(Long companyId);

    void deleteSales(Long id);
}
