package com.vasyerp.rolebasedsystem.service;

import com.vasyerp.rolebasedsystem.dto.CompleteDataDTO;

import java.util.List;

public interface CompleteDataService {
    
    List<CompleteDataDTO> getAllData(String country);

    List<CompleteDataDTO> getDataByUser(Long userId, boolean isAdmin, String country);
}
