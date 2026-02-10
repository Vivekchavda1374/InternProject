package com.vasyerp.rolebasedsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesDTO {
    private Long salesId;
    private Integer contactId;
    private Long companyId;
    private Long branchId;
    private String prefix;
    private String salesNo;
    private Double totalAmount;
    private LocalDate salesDate;
}
