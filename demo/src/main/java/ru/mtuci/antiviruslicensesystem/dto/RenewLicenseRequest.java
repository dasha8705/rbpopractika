package ru.mtuci.antiviruslicensesystem.dto;


import lombok.Data;

@Data
public class RenewLicenseRequest {
    private String deviceId;
    private String licenseKey;
}