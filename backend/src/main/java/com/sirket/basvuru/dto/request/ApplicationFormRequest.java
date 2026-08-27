package com.sirket.basvuru.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationFormRequest {

    @NotBlank(message = "Baslik bos birakilamaz")
    @Size(max = 100, message = "Baslik en fazla 100 karakter olabilir")
    private String title;

    @Size(max = 1000, message = "Aciklama en fazla 1000 karakter olabilir")
    private String description;

    @NotNull(message = "Form turu secilmelidir")
    private Long formTypeId;
}