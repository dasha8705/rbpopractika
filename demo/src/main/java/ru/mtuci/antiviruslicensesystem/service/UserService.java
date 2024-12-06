package ru.mtuci.antiviruslicensesystem.service;


import lombok.RequiredArgsConstructor;
import ru.mtuci.antiviruslicensesystem.dto.RegisterUserDTO;
import ru.mtuci.antiviruslicensesystem.dto.UserDTO;
import ru.mtuci.antiviruslicensesystem.entity.User;
import ru.mtuci.antiviruslicensesystem.exceptions.ApiException;
import ru.mtuci.antiviruslicensesystem.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ApiException("User not found with id: " + id));
    }
    @Transactional(readOnly = true)
    public long countAll() {
        return userRepository.count();
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new ApiException("User not found with login: " + login));
    }

    @Transactional
    public User createUser(UserDTO userDTO) {
        if (userRepository.existsByLogin(userDTO.getLogin())) {
            throw new ApiException("Login already exists");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new ApiException("Email already exists");
        }

        User user = User.builder()
                .login(userDTO.getLogin())
                .email(userDTO.getEmail())
                .password_hash(userDTO.getPassword()) // В будущем здесь будет хеширование
                .role(userDTO.getRole())
                .build();

        return userRepository.save(user);
    }


    @Transactional
    public User registerUser(RegisterUserDTO registerDTO) {
        if (userRepository.existsByLogin(registerDTO.getLogin())) {
            throw new ApiException("Login already exists");
        }
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new ApiException("Email already exists");
        }

        User user = User.builder()
                .login(registerDTO.getLogin())
                .password_hash(passwordEncoder.encode(registerDTO.getPassword()))
                .email(registerDTO.getEmail())
                .role("ROLE_USER")  // По умолчанию даем роль USER
                .build();

        return userRepository.save(user);
    }
}

