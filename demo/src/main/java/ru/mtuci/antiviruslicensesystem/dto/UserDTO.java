package ru.mtuci.antiviruslicensesystem.dto;

import lombok.Data;


@Data
public class UserDTO {
    private String login;
    private String email;
    private String password;
    private String role;
}

