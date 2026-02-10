package com.vasyerp.rolebasedsystem.repository;

import com.vasyerp.rolebasedsystem.model.SalesItem;
import java.util.List;
import java.util.Optional;

public interface SalesItemRepository {

    SalesItem save(SalesItem salesItem);

    Optional<SalesItem> findById(Long id);

    List<SalesItem> findAll();

    void deleteById(Long id);

    List<SalesItem> findBySales_SalesId(Long salesId);
}
