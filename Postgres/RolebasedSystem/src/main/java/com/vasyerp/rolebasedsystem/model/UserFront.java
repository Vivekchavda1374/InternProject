package com.vasyerp.rolebasedsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_front")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFront {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_front_id")
    private Long userFrontId;

    @Column(name = "name", length = 150, nullable = false, unique = true)
    private String name;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "parent_company_id")
    private Long parentCompanyId;

    @Column(name = "gst_no", length = 20)
    private String gstNo;

    @Column(name = "phone_no", length = 15)
    private String phoneNo;

    @OneToOne(mappedBy = "userFront", cascade = CascadeType.ALL)
    private UserFrontAddress userFrontAddress;

}
