package ru.mtuci.antiviruslicensesystem.controller;

import lombok.RequiredArgsConstructor;
import ru.mtuci.antiviruslicensesystem.dto.ActivationRequest;
import ru.mtuci.antiviruslicensesystem.dto.RenewLicenseRequest;
import ru.mtuci.antiviruslicensesystem.service.ActivationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/activation")
@RequiredArgsConstructor
public class ActivationController {
    private final ActivationService activationService;

    @PostMapping("/activate")
    public ResponseEntity<?> activateLicense(@RequestBody ActivationRequest request) {
        return ResponseEntity.ok(activationService.activateLicense(request));
    }

    @GetMapping("/check/{deviceId}")
    public ResponseEntity<?> checkLicense(@PathVariable String deviceId) {
        return ResponseEntity.ok(activationService.checkLicense(deviceId));
    }

    @PostMapping("/renew")
    public ResponseEntity<?> renewLicense(@RequestBody RenewLicenseRequest request) {
        return ResponseEntity.ok(activationService.renewLicense(request));
    }
}
