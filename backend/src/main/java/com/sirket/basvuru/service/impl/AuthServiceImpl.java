package com.sirket.basvuru.service.impl;

import com.sirket.basvuru.dto.request.LoginRequest;
import com.sirket.basvuru.dto.request.RegisterRequest;
import com.sirket.basvuru.dto.response.LoginResponse;
import com.sirket.basvuru.entity.User;
import com.sirket.basvuru.enums.Role;
import com.sirket.basvuru.repository.UserRepository;
import com.sirket.basvuru.security.JwtUtil;
import com.sirket.basvuru.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Bu email adresi zaten kayitli: " + request.getEmail());
        }

        User user = User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.PERSONEL) // varsayilan olarak yeni kayitlar PERSONEL
                .build();

        User saved = userRepository.save(user);
        String token = jwtUtil.generateToken(saved.getEmail());

        return buildResponse(saved, token);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Kullanici bulunamadi"));

        String token = jwtUtil.generateToken(user.getEmail());
        return buildResponse(user, token);
    }

    private LoginResponse buildResponse(User user, String token) {
        return LoginResponse.builder()
                .token(token)
                .userId(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}