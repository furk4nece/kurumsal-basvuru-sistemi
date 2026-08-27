package com.sirket.basvuru.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Email bos birakilamaz")
    @Email(message = "Gecerli bir email giriniz")
    private String email;

    @NotBlank(message = "Sifre bos birakilamaz")
    private String password;
}