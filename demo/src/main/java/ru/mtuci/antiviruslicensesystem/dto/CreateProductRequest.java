package ru.mtuci.antiviruslicensesystem.dto;

import lombok.Data;

@Data
public class CreateProductRequest {
    private String name;
    private Boolean isBlocked;
}
