package ru.mtuci.antiviruslicensesystem.dto;

import lombok.Data;

@Data
public class CreateLicenseRequest {
    private Long productId;
    private Long ownerId;
    private Long licenseTypeId;
    private String description;
}