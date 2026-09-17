package com.sirket.basvuru.controller;

import com.sirket.basvuru.dto.request.UpdateProfileRequest;
import com.sirket.basvuru.dto.request.UpdateRoleRequest;
import com.sirket.basvuru.dto.response.UserResponse;
import com.sirket.basvuru.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Kullanicilar", description = "Profil ve kullanici yonetimi islemleri")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Giris yapmis kullanicinin profilini doner")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok(userService.getByEmail(authentication.getName()));
    }

    @PutMapping("/me")
    @Operation(summary = "Profil bilgilerini gunceller", description = "Sifre alani bos birakilirsa degistirilmez")
    public ResponseEntity<UserResponse> updateCurrentUser(@Valid @RequestBody UpdateProfileRequest request,
                                                           Authentication authentication) {
        return ResponseEntity.ok(userService.updateProfile(authentication.getName(), request));
    }

    @GetMapping
    @Operation(summary = "Tum kullanicilari listeler", description = "Sadece ADMIN rolu erisebilir")
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @PutMapping("/{id}/role")
    @Operation(summary = "Kullanicinin rolunu degistirir", description = "Sadece ADMIN rolu erisebilir")
    public ResponseEntity<UserResponse> updateRole(@PathVariable Long id,
                                                    @Valid @RequestBody UpdateRoleRequest request) {
        return ResponseEntity.ok(userService.updateRole(id, request.getRole()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Kullaniciyi siler", description = "Sadece ADMIN rolu erisebilir; kendi hesabini veya basvurusu olan kullaniciyi silemez")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        userService.delete(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}