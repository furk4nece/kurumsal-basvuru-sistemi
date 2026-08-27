package com.sirket.basvuru.service;

import com.sirket.basvuru.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse getByEmail(String email);
    List<UserResponse> getAll();
}