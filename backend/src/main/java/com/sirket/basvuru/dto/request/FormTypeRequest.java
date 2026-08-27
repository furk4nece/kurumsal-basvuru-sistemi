package com.sirket.basvuru.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormTypeRequest {

    @NotBlank(message = "Form turu adi bos birakilamaz")
    private String name;
}