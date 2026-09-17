package com.sirket.basvuru.config;

import com.sirket.basvuru.entity.FormType;
import com.sirket.basvuru.entity.User;
import com.sirket.basvuru.enums.Role;
import com.sirket.basvuru.repository.FormTypeRepository;
import com.sirket.basvuru.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final FormTypeRepository formTypeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (formTypeRepository.count() == 0) {
            List.of("Izin", "Egitim", "Avans", "Malzeme", "Gorev")
                    .forEach(name -> formTypeRepository.save(FormType.builder().name(name).build()));
        }

        if (!userRepository.existsByEmail("admin@sirket.com")) {
            userRepository.save(User.builder()
                    .name("Sistem")
                    .surname("Yonetici")
                    .email("admin@sirket.com")
                    .password(passwordEncoder.encode("admin1234"))
                    .role(Role.ADMIN)
                    .build());
        }
    }
}