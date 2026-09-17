package com.sirket.basvuru.service.impl;

import com.sirket.basvuru.dto.request.UpdateProfileRequest;
import com.sirket.basvuru.dto.response.UserResponse;
import com.sirket.basvuru.entity.User;
import com.sirket.basvuru.enums.Role;
import com.sirket.basvuru.mapper.UserMapper;
import com.sirket.basvuru.repository.ApplicationFormRepository;
import com.sirket.basvuru.repository.UserRepository;
import com.sirket.basvuru.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ApplicationFormRepository applicationFormRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getByEmail(String email) {
        return userMapper.toResponse(findByEmail(email));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAll() {
        return userRepository.findAll().stream().map(userMapper::toResponse).toList();
    }

    @Override
    public UserResponse updateProfile(String email, UpdateProfileRequest request) {
        User user = findByEmail(email);
        user.setName(request.getName());
        user.setSurname(request.getSurname());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateRole(Long id, Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kullanici bulunamadi: id=" + id));
        user.setRole(role);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public void delete(Long id, String requesterEmail) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kullanici bulunamadi: id=" + id));

        if (user.getEmail().equals(requesterEmail)) {
            throw new IllegalArgumentException("Kendi hesabinizi silemezsiniz");
        }

        if (applicationFormRepository.existsByApplicant_Id(id)) {
            throw new IllegalStateException(
                    "Bu kullanicinin basvurulari oldugu icin silinemez, once basvurulari kaldirin");
        }

        userRepository.delete(user);
    }

    private User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Kullanici bulunamadi: " + email));
    }
}