package ru.mtuci.antiviruslicensesystem.controller;


import lombok.RequiredArgsConstructor;
import ru.mtuci.antiviruslicensesystem.dto.LicenseUpdateRequest;
import ru.mtuci.antiviruslicensesystem.service.LicenseUpdateService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/licenses")
@RequiredArgsConstructor
public class LicenseUpdateController {
    private final LicenseUpdateService licenseUpdateService;

    @PostMapping("/update")
    public ResponseEntity<?> updateLicense(@RequestBody LicenseUpdateRequest request) {
        return ResponseEntity.ok(licenseUpdateService.updateLicense(request));
    }
}
