package com.sirket.basvuru.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequest {

    @NotBlank(message = "Ad bos birakilamaz")
    @Size(max = 50, message = "Ad en fazla 50 karakter olabilir")
    private String name;

    @NotBlank(message = "Soyad bos birakilamaz")
    @Size(max = 50, message = "Soyad en fazla 50 karakter olabilir")
    private String surname;

    @Size(min = 8, message = "Sifre en az 8 karakter olmalidir")
    private String password;
}