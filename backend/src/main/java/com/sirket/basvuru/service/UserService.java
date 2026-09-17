package com.sirket.basvuru.service;

import com.sirket.basvuru.dto.request.UpdateProfileRequest;
import com.sirket.basvuru.dto.response.UserResponse;
import com.sirket.basvuru.enums.Role;

import java.util.List;

public interface UserService {
    UserResponse getByEmail(String email);
    List<UserResponse> getAll();
    UserResponse updateProfile(String email, UpdateProfileRequest request);
    UserResponse updateRole(Long id, Role role);
    void delete(Long id, String requesterEmail);
}