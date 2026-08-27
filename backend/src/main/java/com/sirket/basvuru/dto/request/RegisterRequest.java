package com.sirket.basvuru.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "Ad bos birakilamaz")
    private String name;

    @NotBlank(message = "Soyad bos birakilamaz")
    private String surname;

    @NotBlank(message = "Email bos birakilamaz")
    @Email(message = "Gecerli bir email giriniz")
    private String email;

    @NotBlank(message = "Sifre bos birakilamaz")
    @Size(message = "Sifre en az 8 karakter olmalidir")
    private String password;
}