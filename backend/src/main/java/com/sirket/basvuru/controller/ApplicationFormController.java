package com.sirket.basvuru.controller;

import com.sirket.basvuru.dto.request.ApplicationFormRequest;
import com.sirket.basvuru.dto.response.ApplicationFormResponse;
import com.sirket.basvuru.enums.ApplicationStatus;
import com.sirket.basvuru.service.ApplicationFormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/forms")
@RequiredArgsConstructor
@Tag(name = "Basvurular", description = "Basvuru olusturma, listeleme, guncelleme, silme ve onay islemleri")
public class ApplicationFormController {

    private final ApplicationFormService applicationFormService;

    @PostMapping
    @Operation(summary = "Yeni basvuru olusturur", description = "Olusturulan basvuru NEW durumunda baslar")
    public ResponseEntity<ApplicationFormResponse> create(@Valid @RequestBody ApplicationFormRequest request,
                                                          Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(applicationFormService.create(request, authentication.getName()));
    }

    @GetMapping
    @Operation(summary = "Basvurulari listeler",
            description = "Personel sadece kendi basvurularini gorur. Durum, tur, tarih araligi ve anahtar kelime ile filtrelenebilir")
    public ResponseEntity<Page<ApplicationFormResponse>> getAll(
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(required = false) Long formTypeId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
            Authentication authentication) {

        return ResponseEntity.ok(applicationFormService.getAll(
                status, formTypeId, keyword, startDate, endDate, pageable, authentication.getName()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Tek bir basvurunun detayini doner")
    public ResponseEntity<ApplicationFormResponse> getById(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(applicationFormService.getById(id, authentication.getName()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Basvuruyu gunceller", description = "Sadece NEW veya IN_REVIEW durumundaki basvurular guncellenebilir")
    public ResponseEntity<ApplicationFormResponse> update(@PathVariable Long id,
                                                          @Valid @RequestBody ApplicationFormRequest request,
                                                          Authentication authentication) {
        return ResponseEntity.ok(applicationFormService.update(id, request, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Basvuruyu siler")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        applicationFormService.delete(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "Basvuruyu onaylar", description = "Sadece ADMIN rolu erisebilir")
    public ResponseEntity<ApplicationFormResponse> approve(@PathVariable Long id) {
        return ResponseEntity.ok(applicationFormService.approve(id));
    }

    @PutMapping("/{id}/reject")
    @Operation(summary = "Basvuruyu reddeder", description = "Sadece ADMIN rolu erisebilir")
    public ResponseEntity<ApplicationFormResponse> reject(@PathVariable Long id) {
        return ResponseEntity.ok(applicationFormService.reject(id));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Basvuru durumunu degistirir", description = "Ornegin NEW -> IN_REVIEW. Sadece ADMIN rolu erisebilir")
    public ResponseEntity<ApplicationFormResponse> changeStatus(@PathVariable Long id,
                                                                 @RequestParam ApplicationStatus status) {
        return ResponseEntity.ok(applicationFormService.changeStatus(id, status));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Basvuruyu iptal eder", description = "Basvuru sahibi kendi basvurusunu iptal edebilir")
    public ResponseEntity<ApplicationFormResponse> cancel(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(applicationFormService.cancel(id, authentication.getName()));
    }
}