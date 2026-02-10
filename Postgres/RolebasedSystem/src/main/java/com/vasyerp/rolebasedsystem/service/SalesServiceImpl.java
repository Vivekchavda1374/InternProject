package com.vasyerp.rolebasedsystem.service;

import com.vasyerp.rolebasedsystem.dto.CreateSalesRequest;
import com.vasyerp.rolebasedsystem.dto.SalesDTO;
import com.vasyerp.rolebasedsystem.model.Product;
import com.vasyerp.rolebasedsystem.model.Sales;
import com.vasyerp.rolebasedsystem.model.SalesItem;
import com.vasyerp.rolebasedsystem.model.UserFront;
import com.vasyerp.rolebasedsystem.repository.ProductRepository;
import com.vasyerp.rolebasedsystem.repository.SalesItemRepository;
import com.vasyerp.rolebasedsystem.repository.SalesRepository;
import com.vasyerp.rolebasedsystem.repository.UserFrontRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalesServiceImpl implements SalesService {

    private final SalesRepository salesRepository;
    private final SalesItemRepository salesItemRepository;
    private final UserFrontRepository userFrontRepository;
    private final ProductRepository productRepository;

    public SalesServiceImpl(SalesRepository salesRepository,
                            SalesItemRepository salesItemRepository,
                            UserFrontRepository userFrontRepository,
                            ProductRepository productRepository) {
        this.salesRepository = salesRepository;
        this.salesItemRepository = salesItemRepository;
        this.userFrontRepository = userFrontRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public SalesDTO createSales(CreateSalesRequest request) {
        UserFront company = userFrontRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));
        UserFront branch = userFrontRepository.findById(request.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));
        validateCompanyBranchRelation(company, branch);

        Sales sales = new Sales();
        sales.setContactId(request.getContactId());
        sales.setCompany(company);
        sales.setBranch(branch);
        sales.setPrefix(request.getPrefix());
        sales.setSalesNo(request.getSalesNo());
        sales.setTotalAmount(0.0);
        sales.setSalesDate(LocalDate.now());

        Sales savedSales = salesRepository.save(sales);
        double computedTotal = 0.0;

        for (CreateSalesRequest.SalesItemRequest item : request.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            validateProductBelongsToTransaction(product, request.getCompanyId(), request.getBranchId());
            validateAndReduceStock(product, item.getQuantity());
            double sellingPrice = resolveSalesPrice(product);
            SalesItem salesItem = new SalesItem();
            salesItem.setSales(savedSales);
            salesItem.setProduct(product);
            salesItem.setQuantity(item.getQuantity());
            salesItem.setSellingPrice(sellingPrice);
            salesItemRepository.save(salesItem);
            productRepository.save(product);
            computedTotal += item.getQuantity() * sellingPrice;
        }

        savedSales.setTotalAmount(computedTotal);
        salesRepository.save(savedSales);

        return toDTO(savedSales);
    }

    private void validateProductBelongsToTransaction(Product product, Long companyId, Long branchId) {
        Long ownerId = product.getCompanyId();
        if (ownerId == null || (!ownerId.equals(companyId) && !ownerId.equals(branchId))) {
            throw new RuntimeException("Selected product does not belong to the selected company/branch");
        }
    }

    private void validateCompanyBranchRelation(UserFront company, UserFront branch) {
        if (company.getUserFrontId().equals(branch.getUserFrontId())) {
            return;
        }

        if (branch.getParentCompany() == null ||
                !company.getUserFrontId().equals(branch.getParentCompany().getUserFrontId())) {
            throw new RuntimeException("Selected branch does not belong to selected company");
        }
    }

    private void validateAndReduceStock(Product product, Double quantity) {
        if (quantity == null || quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        double currentStock = product.getStockQuantity() == null ? 0.0 : product.getStockQuantity();
        if (currentStock < quantity) {
            throw new RuntimeException(
                    "Insufficient stock for product: " + product.getProductName() + ". Available: " + currentStock);
        }
        product.setStockQuantity(currentStock - quantity);
    }

    private double resolveSalesPrice(Product product) {
        Double price = product.getSellingPrice();
        if (price == null) {
            throw new RuntimeException("Selling price not set for product: " + product.getProductName());
        }
        return price;
    }

    @Override
    public List<SalesDTO> getAllSales() {
        return salesRepository.findAll().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<SalesDTO> getSalesByBranch(Long branchId) {
        return salesRepository.findByBranch_UserFrontId(branchId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<SalesDTO> getSalesByCompany(Long companyId) {
        return salesRepository.findByCompany_UserFrontId(companyId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteSales(Long id) {
        salesRepository.deleteById(id);
    }

    private SalesDTO toDTO(Sales sales) {
        SalesDTO dto = new SalesDTO();
        dto.setSalesId(sales.getSalesId());
        dto.setContactId(sales.getContactId());
        dto.setCompanyId(sales.getCompany().getUserFrontId());
        dto.setBranchId(sales.getBranch().getUserFrontId());
        dto.setPrefix(sales.getPrefix());
        dto.setSalesNo(sales.getSalesNo());
        dto.setTotalAmount(sales.getTotalAmount());
        dto.setSalesDate(sales.getSalesDate());
        return dto;
    }
}
