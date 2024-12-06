package ru.mtuci.antiviruslicensesystem.controller;


import lombok.RequiredArgsConstructor;
import ru.mtuci.antiviruslicensesystem.dto.AntivirusDatabaseUploadRequest;
import ru.mtuci.antiviruslicensesystem.exceptions.ApiException;
import ru.mtuci.antiviruslicensesystem.service.AntivirusDatabaseService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/antivirus-bases")
@RequiredArgsConstructor
public class AntivirusDatabaseController {
    private final AntivirusDatabaseService databaseService;

    @GetMapping
    public ResponseEntity<?> getAllDatabases() {
        return ResponseEntity.ok(databaseService.findAll());
    }

    @PostMapping
    public ResponseEntity<?> uploadDatabase(
            @RequestParam("file") MultipartFile file,
            @RequestParam("version") String version,
            @RequestParam("productId") Long productId,
            @RequestParam(value = "description", required = false) String description
    ) {
        try {
            AntivirusDatabaseUploadRequest request = new AntivirusDatabaseUploadRequest();
            request.setDatabaseFile(file);
            request.setVersion(version);
            request.setProductId(productId);
            request.setDescription(description);
            return ResponseEntity.ok(databaseService.uploadDatabase(request));
        } catch (IOException e) {
            throw new ApiException("Failed to upload database: " + e.getMessage());
        }
    }

    @GetMapping("/{productId}/active")
    public ResponseEntity<?> getActiveDataBasesForProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(databaseService.getActiveDataBasesForProduct(productId));
    }
}