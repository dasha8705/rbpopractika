package ru.mtuci.antiviruslicensesystem.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.mtuci.antiviruslicensesystem.dto.RegisterUserDTO;
import ru.mtuci.antiviruslicensesystem.dto.UserDTO;
import ru.mtuci.antiviruslicensesystem.service.UserService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.createUser(userDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/by-login/{login}")
    public ResponseEntity<?> getUserByLogin(@PathVariable String login) {
        return ResponseEntity.ok(userService.findByLogin(login));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserDTO registerDTO) {
        return ResponseEntity.ok(userService.registerUser(registerDTO));
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }
}