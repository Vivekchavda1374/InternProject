package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.PurchaseItem;
import java.util.List;
import java.util.Optional;

public interface PurchaseItemRepository {

    PurchaseItem save(PurchaseItem purchaseItem);

    Optional<PurchaseItem> findById(Long id);

    List<PurchaseItem> findAll();

    void deleteById(Long id);

    List<PurchaseItem> findByPurchase_PurchaseId(Long purchaseId);
}
