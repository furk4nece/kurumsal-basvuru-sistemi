package com.sirket.basvuru.controller;

import com.sirket.basvuru.dto.response.DashboardResponse;
import com.sirket.basvuru.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Basvuru istatistikleri")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    @Operation(summary = "Ozet istatistikleri doner",
            description = "ADMIN tum sistemi, PERSONEL kendi basvurularinin ozetini gorur")
    public ResponseEntity<DashboardResponse> getSummary(Authentication authentication) {
        return ResponseEntity.ok(dashboardService.getSummary(authentication.getName()));
    }
}