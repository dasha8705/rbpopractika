package ru.mtuci.antiviruslicensesystem.util;

import lombok.RequiredArgsConstructor;
import ru.mtuci.antiviruslicensesystem.entity.User;
import ru.mtuci.antiviruslicensesystem.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

//TODO: 1. Опасно делать такие классы

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            if (!userRepository.existsByLogin("admin")) {
                User admin = User.builder()
                        .login("admin")
                        .password_hash(passwordEncoder.encode("admin"))
                        .email("admin@example.com")
                        .role("ROLE_ADMIN")
                        .build();
                userRepository.save(admin);
            }
        };
    }
}
