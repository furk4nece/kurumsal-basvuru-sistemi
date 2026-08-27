package com.sirket.basvuru.controller;

import com.sirket.basvuru.dto.request.FormTypeRequest;
import com.sirket.basvuru.dto.response.FormTypeResponse;
import com.sirket.basvuru.service.FormTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/form-types")
@RequiredArgsConstructor
public class FormTypeController {

    private final FormTypeService formTypeService;

    @PostMapping
    public ResponseEntity<FormTypeResponse> create(@Valid @RequestBody FormTypeRequest request) {
        return ResponseEntity.ok(formTypeService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<FormTypeResponse>> getAll() {
        return ResponseEntity.ok(formTypeService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormTypeResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(formTypeService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FormTypeResponse> update(@PathVariable Long id, @Valid @RequestBody FormTypeRequest request) {
        return ResponseEntity.ok(formTypeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        formTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}