package com.sirket.basvuru.dto.request;

import com.sirket.basvuru.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRoleRequest {

    @NotNull(message = "Rol secilmelidir")
    private Role role;
}