package com.sirket.basvuru.dto.response;

import com.sirket.basvuru.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Long userId;
    private String name;
    private String surname;
    private String email;
    private Role role;
}