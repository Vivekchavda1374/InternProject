package com.vasyerp.rolebasedsystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserFrontRequest {
    
    @NonNull()
    private String name;
    
    @JsonProperty("parentCompanyId")
    private Long parentCompanyId;
}
