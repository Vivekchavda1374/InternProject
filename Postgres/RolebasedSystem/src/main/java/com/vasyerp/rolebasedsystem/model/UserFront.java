package com.vasyerp.rolebasedsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Set;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_company_id")
    private UserFront parentCompany;

    @OneToMany(mappedBy = "parentCompany", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserFront> branches;

    @Column(name = "gst_no", length = 20)
    private String gstNo;

    @Column(name = "phone_no", length = 15)
    private String phoneNo;

    @OneToMany(mappedBy = "userFront", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserFrontAddress> addresses;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_role_new",
        joinColumns = @JoinColumn(name = "user_front_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<UserRole> roles;

}
