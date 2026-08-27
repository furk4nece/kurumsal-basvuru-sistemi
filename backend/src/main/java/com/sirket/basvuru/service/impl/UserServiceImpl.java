package com.sirket.basvuru.service.impl;

import com.sirket.basvuru.dto.response.UserResponse;
import com.sirket.basvuru.entity.User;
import com.sirket.basvuru.mapper.UserMapper;
import com.sirket.basvuru.repository.UserRepository;
import com.sirket.basvuru.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse getByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Kullanici bulunamadi: " + email));
        return userMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }
}