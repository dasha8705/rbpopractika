package ru.mtuci.antiviruslicensesystem.controller;

import lombok.RequiredArgsConstructor;
import ru.mtuci.antiviruslicensesystem.dto.AuthRequestDTO;
import ru.mtuci.antiviruslicensesystem.dto.AuthResponseDTO;
import ru.mtuci.antiviruslicensesystem.security.JwtTokenProvider;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtTokenProvider.createToken(userDetails.getUsername(),
                userDetails.getAuthorities().iterator().next().getAuthority());

        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
}