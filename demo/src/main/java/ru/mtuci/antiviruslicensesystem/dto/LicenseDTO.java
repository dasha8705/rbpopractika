package ru.mtuci.antiviruslicensesystem.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LicenseDTO {
    private String code;
    private Long userId;
    private Long productId;
    private Long typeId;
    private LocalDateTime firstActivationDate;
    private LocalDateTime endingDate;
    private Boolean blocked;
    private Integer deviceCount;
    private Long ownerId;
    private Integer duration;
    private String description;
}
