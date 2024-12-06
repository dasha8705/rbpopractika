package ru.mtuci.antiviruslicensesystem.controller;
import lombok.RequiredArgsConstructor;
import ru.mtuci.antiviruslicensesystem.dto.CreateLicenseTypeRequest;
import ru.mtuci.antiviruslicensesystem.entity.LicenseType;
import ru.mtuci.antiviruslicensesystem.service.LicenseTypeService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/license-types")
@RequiredArgsConstructor
public class LicenseTypeController {
    private final LicenseTypeService licenseTypeService;

    @GetMapping
    public ResponseEntity<?> getAllLicenseTypes() {
        return ResponseEntity.ok(licenseTypeService.findAll());
    }

    @PostMapping
    public ResponseEntity<?> createLicenseType(@RequestBody CreateLicenseTypeRequest request) {
        LicenseType licenseType = LicenseType.builder()
                .name(request.getName())
                .defaultDuration(request.getDefaultDuration())
                .description(request.getDescription())
                .build();
        return ResponseEntity.ok(licenseTypeService.create(licenseType));
    }
}