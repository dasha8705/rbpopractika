package ru.mtuci.antiviruslicensesystem.dto;

import lombok.Data;

@Data
public class ActivationRequest {
    private String activationCode;
    private String deviceInfo;
}