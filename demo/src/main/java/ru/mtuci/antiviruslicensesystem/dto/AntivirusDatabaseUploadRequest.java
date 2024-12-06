package ru.mtuci.antiviruslicensesystem.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AntivirusDatabaseUploadRequest {
    private String version;
    private String description;
    private Long productId;
    private MultipartFile databaseFile;
}

