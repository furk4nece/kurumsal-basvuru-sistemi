package com.sirket.basvuru.service;

import com.sirket.basvuru.dto.request.LoginRequest;
import com.sirket.basvuru.dto.request.RegisterRequest;
import com.sirket.basvuru.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}