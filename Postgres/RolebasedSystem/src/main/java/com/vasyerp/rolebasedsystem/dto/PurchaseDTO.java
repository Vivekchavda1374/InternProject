package com.vasyerp.rolebasedsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDTO {
    private Long purchaseId;
    private Integer contactId;
    private Long companyId;
    private Long branchId;
    private String prefix;
    private String purchaseNo;
    private Double totalAmount;
    private LocalDate purchaseDate;
}
