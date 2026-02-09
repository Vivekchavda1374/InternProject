package com.vasyerp.rolebasedsystem.controller;

import com.vasyerp.rolebasedsystem.dto.CompleteDataDTO;
import com.vasyerp.rolebasedsystem.service.CompleteDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/complete")
@RequiredArgsConstructor
public class CompleteDataController {

    private final CompleteDataService service;

    @GetMapping
    public ResponseEntity<List<CompleteDataDTO>> getAllData(
            @RequestHeader(value = "userId", required = false) Long userId,
            @RequestHeader(value = "isAdmin", required = false, defaultValue = "false") boolean isAdmin) {
        if (userId == null) {
            return ResponseEntity.ok(service.getAllData());
        }
        return ResponseEntity.ok(service.getDataByUser(userId, isAdmin));
    }
}
