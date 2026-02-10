package com.vasyerp.rolebasedsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "sales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sales_id")
    private Long salesId;

    @Column(name = "contact_id")
    private Integer contactId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private UserFront company;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private UserFront branch;

    @Column(name = "prefix", length = 10)
    private String prefix;

    @Column(name = "sales_no", length = 50)
    private String salesNo;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "sales_date")
    private LocalDate salesDate;

    @JsonIgnore
    @OneToMany(mappedBy = "sales", cascade = CascadeType.ALL)
    private List<SalesItem> salesItems;
}
