package ru.mtuci.antiviruslicensesystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LicenseTypeDTO {
    private Long id;
    private String name;
    private Integer defaultDuration;
    private String description;
}