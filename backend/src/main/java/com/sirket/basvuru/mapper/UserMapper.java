package com.sirket.basvuru.mapper;

import com.sirket.basvuru.dto.response.UserResponse;
import com.sirket.basvuru.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);
}