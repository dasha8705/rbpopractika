package ru.mtuci.antiviruslicensesystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AntivirusDatabaseResponse {
    private Long id;
    private String version;
    private String description;
    private String fileLocation;
    private Long fileSize;
    private String checksum;
    private Boolean isActive;
}
