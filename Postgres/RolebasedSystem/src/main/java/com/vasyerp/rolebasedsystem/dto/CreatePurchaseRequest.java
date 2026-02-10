package com.vasyerp.rolebasedsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePurchaseRequest {
    private Integer contactId;
    private Long companyId;
    private Long branchId;
    private Boolean allowExternalProducts;
    private String prefix;
    private String purchaseNo;
    private Double totalAmount;
    private List<PurchaseItemRequest> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PurchaseItemRequest {
        private Long productId;
        private Double quantity;
        private Double purchasePrice;
    }
}
