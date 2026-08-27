package com.sirket.basvuru.controller;

import com.sirket.basvuru.dto.request.ApplicationFormRequest;
import com.sirket.basvuru.dto.response.ApplicationFormResponse;
import com.sirket.basvuru.service.ApplicationFormService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forms")
@RequiredArgsConstructor
public class ApplicationFormController {

    private final ApplicationFormService applicationFormService;

    @PostMapping
    public ResponseEntity<ApplicationFormResponse> create(@Valid @RequestBody ApplicationFormRequest request,
                                                            Authentication authentication) {
        return ResponseEntity.ok(applicationFormService.create(request, authentication.getName()));
    }

    @GetMapping
    public ResponseEntity<List<ApplicationFormResponse>> getAll() {
        return ResponseEntity.ok(applicationFormService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationFormResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(applicationFormService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationFormResponse> update(@PathVariable Long id,
                                                            @Valid @RequestBody ApplicationFormRequest request,
                                                            Authentication authentication) {
        return ResponseEntity.ok(applicationFormService.update(id, request, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        applicationFormService.delete(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<ApplicationFormResponse> approve(@PathVariable Long id) {
        return ResponseEntity.ok(applicationFormService.approve(id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ApplicationFormResponse> reject(@PathVariable Long id) {
        return ResponseEntity.ok(applicationFormService.reject(id));
    }
}