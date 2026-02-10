package com.vasyerp.rolebasedsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSalesRequest {
    private Integer contactId;
    private Long companyId;
    private Long branchId;
    private String prefix;
    private String salesNo;
    private Double totalAmount;
    private List<SalesItemRequest> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SalesItemRequest {
        private Long productId;
        private Double quantity;
        private Double sellingPrice;
    }
}
