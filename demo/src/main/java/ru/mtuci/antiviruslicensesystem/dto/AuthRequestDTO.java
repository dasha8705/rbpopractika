package ru.mtuci.antiviruslicensesystem.dto;

import lombok.Data;

// AuthRequestDTO.java
@Data
public class AuthRequestDTO {
    private String login;
    private String password;
}

