package ru.mtuci.antiviruslicensesystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LicenseResponseDTO {
    private Long id;
    private String code;
    private LocalDateTime firstActivationDate;
    private LocalDateTime endingDate;
    private Boolean blocked;
    private Integer deviceCount;
    private Long ownerId;
    private Integer duration;
    private String description;
    private ProductDTO product;
    private LicenseTypeDTO type;
}