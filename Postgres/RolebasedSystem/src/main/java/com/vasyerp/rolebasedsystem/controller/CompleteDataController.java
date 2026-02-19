package com.vasyerp.rolebasedsystem.controller;

import com.vasyerp.rolebasedsystem.dto.CompleteDataDTO;
import com.vasyerp.rolebasedsystem.service.CompleteDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/complete")
@RequiredArgsConstructor
public class CompleteDataController {

    private final CompleteDataService service;

    @GetMapping
    public ResponseEntity<List<CompleteDataDTO>> getAllData(
            @RequestHeader(value = "userId", required = false) Long userId,
            @RequestHeader(value = "isAdmin", required = false, defaultValue = "false") boolean isAdmin,
            @RequestParam(value = "country", required = false) String country) {
        List<CompleteDataDTO> data;
        String countryFilter = (country == null || country.trim().isEmpty()) ? null : country.trim();
        if (userId == null) {
            data = service.getAllData(countryFilter);
        } else {
            data = service.getDataByUser(userId, isAdmin, countryFilter);
        }

        return ResponseEntity.ok(data);
    }
}
