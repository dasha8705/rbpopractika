package ru.mtuci.antiviruslicensesystem.controller;

import lombok.RequiredArgsConstructor;
import ru.mtuci.antiviruslicensesystem.dto.LicenseInfoRequest;
import ru.mtuci.antiviruslicensesystem.service.LicenseInfoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//TODO: 1. Во всех контроллерах отсутствует контроль доступа

@RestController
@RequestMapping("/api/v1/license-info")
@RequiredArgsConstructor
public class LicenseInfoController {
    private final LicenseInfoService licenseInfoService;

    @PostMapping("/check")
    public ResponseEntity<?> getLicenseInfo(@RequestBody LicenseInfoRequest request) {
        return ResponseEntity.ok(licenseInfoService.getLicenseInfo(request));
    }
}
