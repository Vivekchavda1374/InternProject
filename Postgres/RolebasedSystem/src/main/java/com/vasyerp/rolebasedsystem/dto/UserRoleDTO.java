package com.vasyerp.rolebasedsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDTO {
    private Long userFrontId;
    private String userName;
    private List<Long> roleIds;
    private List<String> roleNames;
}
