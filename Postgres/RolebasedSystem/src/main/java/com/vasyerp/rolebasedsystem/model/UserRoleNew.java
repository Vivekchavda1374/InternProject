package com.vasyerp.rolebasedsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_role_new")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleNew {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_role_new_id")
    private Long userRoleNewId;

    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @Column(name = "user_front_id", nullable = false)
    private Long userFrontId;
}
