package com.vasyerp.rolebasedsystem.service;

import com.vasyerp.rolebasedsystem.dto.CreatePurchaseRequest;
import com.vasyerp.rolebasedsystem.dto.PurchaseDTO;
import com.vasyerp.rolebasedsystem.model.Product;
import com.vasyerp.rolebasedsystem.model.Purchase;
import com.vasyerp.rolebasedsystem.model.PurchaseItem;
import com.vasyerp.rolebasedsystem.model.UserFront;
import com.vasyerp.rolebasedsystem.repository.ProductRepository;
import com.vasyerp.rolebasedsystem.repository.PurchaseItemRepository;
import com.vasyerp.rolebasedsystem.repository.PurchaseRepository;
import com.vasyerp.rolebasedsystem.repository.UserFrontRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final UserFrontRepository userFrontRepository;
    private final ProductRepository productRepository;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository,
                               PurchaseItemRepository purchaseItemRepository,
                               UserFrontRepository userFrontRepository,
                               ProductRepository productRepository) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseItemRepository = purchaseItemRepository;
        this.userFrontRepository = userFrontRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "purchasesAll", allEntries = true),
            @CacheEvict(value = "purchasesByBranch", allEntries = true),
            @CacheEvict(value = "purchasesByCompany", allEntries = true),
            @CacheEvict(value = "productsAll", allEntries = true),
            @CacheEvict(value = "productsByCompany", allEntries = true),
            @CacheEvict(value = "productsByBranch", allEntries = true),
            @CacheEvict(value = "productById", allEntries = true),
            @CacheEvict(value = "productSearch", allEntries = true),
            @CacheEvict(value = "completeDataAll", allEntries = true),
            @CacheEvict(value = "completeDataByUser", allEntries = true)
    })
    public PurchaseDTO createPurchase(CreatePurchaseRequest request) {
        UserFront company = userFrontRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));
        UserFront branch = userFrontRepository.findById(request.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));
        validateCompanyBranchRelation(company, branch);
        boolean allowExternal = Boolean.TRUE.equals(request.getAllowExternalProducts());
        Long destinationOwnerId = request.getBranchId() != null ? request.getBranchId() : request.getCompanyId();

        Purchase purchase = new Purchase();
        purchase.setContactId(request.getContactId());
        purchase.setCompany(company);
        purchase.setBranch(branch);
        purchase.setPrefix(request.getPrefix());
        purchase.setPurchaseNo(request.getPurchaseNo());
        purchase.setTotalAmount(0.0);
        purchase.setPurchaseDate(LocalDate.now());

        Purchase savedPurchase = purchaseRepository.save(purchase);
        double computedTotal = 0.0;

        for (CreatePurchaseRequest.PurchaseItemRequest item : request.getItems()) {
            Product sourceProduct = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            validateQuantity(item.getQuantity());

            Product targetProduct;
            if (allowExternal) {
                targetProduct = resolveExternalPurchaseTarget(sourceProduct, destinationOwnerId);
            } else {
                validateProductBelongsToTransaction(sourceProduct, request.getCompanyId(), request.getBranchId());
                targetProduct = sourceProduct;
            }

            increaseStock(targetProduct, item.getQuantity());
            double purchasePrice = resolvePurchasePrice(sourceProduct);
            PurchaseItem purchaseItem = new PurchaseItem();
            purchaseItem.setPurchase(savedPurchase);
            purchaseItem.setProduct(targetProduct);
            purchaseItem.setQuantity(item.getQuantity());
            purchaseItem.setPurchasePrice(purchasePrice);
            purchaseItemRepository.save(purchaseItem);

            productRepository.save(targetProduct);
            if (!targetProduct.getProductId().equals(sourceProduct.getProductId())) {
                reduceStockWithCheck(sourceProduct, item.getQuantity());
                productRepository.save(sourceProduct);
            }
            computedTotal += item.getQuantity() * purchasePrice;
        }

        savedPurchase.setTotalAmount(computedTotal);
        purchaseRepository.save(savedPurchase);

        return toDTO(savedPurchase);
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

    private void increaseStock(Product product, Double quantity) {
        double currentStock = product.getStockQuantity() == null ? 0.0 : product.getStockQuantity();
        product.setStockQuantity(currentStock + quantity);
    }

    private void validateQuantity(Double quantity) {
        if (quantity == null || quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }
    }

    private void reduceStockWithCheck(Product product, Double quantity) {
        double currentStock = product.getStockQuantity() == null ? 0.0 : product.getStockQuantity();
        if (currentStock < quantity) {
            throw new RuntimeException(
                    "Insufficient stock for product: " + product.getProductName() + ". Available: " + currentStock);
        }
        product.setStockQuantity(currentStock - quantity);
    }

    private Product resolveExternalPurchaseTarget(Product sourceProduct, Long destinationOwnerId) {
        if (sourceProduct.getCompanyId().equals(destinationOwnerId)) {
            return sourceProduct;
        }

        Optional<Product> existingTarget = Optional.empty();
        if (sourceProduct.getItemCode() != null && !sourceProduct.getItemCode().isBlank()) {
            existingTarget = productRepository.findByItemCodeAndCompanyId(sourceProduct.getItemCode(), destinationOwnerId);
        }
        if (existingTarget.isEmpty()) {
            existingTarget = productRepository.findByProductNameAndCompanyId(sourceProduct.getProductName(), destinationOwnerId);
        }

        if (existingTarget.isPresent()) {
            return existingTarget.get();
        }

        Product clonedProduct = new Product();
        clonedProduct.setProductName(sourceProduct.getProductName());
        clonedProduct.setCompanyId(destinationOwnerId);
        clonedProduct.setItemCode(sourceProduct.getItemCode());
        clonedProduct.setMrp(sourceProduct.getMrp());
        clonedProduct.setSellingPrice(sourceProduct.getSellingPrice());
        clonedProduct.setPurchasePrice(sourceProduct.getPurchasePrice());
        clonedProduct.setProductVariantName(sourceProduct.getProductVariantName());
        clonedProduct.setDescription(sourceProduct.getDescription());
        clonedProduct.setStockQuantity(0.0);
        return productRepository.save(clonedProduct);
    }

    private double resolvePurchasePrice(Product product) {
        Double price = product.getMrp() != null ? product.getMrp() : product.getSellingPrice();
        if (price == null) {
            throw new RuntimeException("Price not set for product: " + product.getProductName());
        }
        return price;
    }

    @Override
    @Cacheable("purchasesAll")
    public List<PurchaseDTO> getAllPurchases() {
        return purchaseRepository.findAll().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "purchasesByBranch", key = "#branchId")
    public List<PurchaseDTO> getPurchasesByBranch(Long branchId) {
        return purchaseRepository.findByBranch_UserFrontId(branchId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "purchasesByCompany", key = "#companyId")
    public List<PurchaseDTO> getPurchasesByCompany(Long companyId) {
        return purchaseRepository.findByCompany_UserFrontId(companyId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "purchasesAll", allEntries = true),
            @CacheEvict(value = "purchasesByBranch", allEntries = true),
            @CacheEvict(value = "purchasesByCompany", allEntries = true)
    })
    public void deletePurchase(Long id) {
        purchaseRepository.deleteById(id);
    }

    private PurchaseDTO toDTO(Purchase purchase) {
        PurchaseDTO dto = new PurchaseDTO();
        dto.setPurchaseId(purchase.getPurchaseId());
        dto.setContactId(purchase.getContactId());
        dto.setCompanyId(purchase.getCompany().getUserFrontId());
        dto.setBranchId(purchase.getBranch().getUserFrontId());
        dto.setPrefix(purchase.getPrefix());
        dto.setPurchaseNo(purchase.getPurchaseNo());
        dto.setTotalAmount(purchase.getTotalAmount());
        dto.setPurchaseDate(purchase.getPurchaseDate());
        return dto;
    }
}
