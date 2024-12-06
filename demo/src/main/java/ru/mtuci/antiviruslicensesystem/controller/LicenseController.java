package ru.mtuci.antiviruslicensesystem.controller;
import lombok.RequiredArgsConstructor;
import ru.mtuci.antiviruslicensesystem.dto.CreateLicenseRequest;
import ru.mtuci.antiviruslicensesystem.dto.LicenseResponseDTO;
import ru.mtuci.antiviruslicensesystem.entity.License;
import ru.mtuci.antiviruslicensesystem.service.LicenseService;
import ru.mtuci.antiviruslicensesystem.service.LicenseTypeService;
import ru.mtuci.antiviruslicensesystem.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/licenses")
@RequiredArgsConstructor
public class LicenseController {
    private final LicenseService licenseService;
    private final ProductService productService;
    private final LicenseTypeService licenseTypeService;

    @GetMapping("/{id}")
    public ResponseEntity<LicenseResponseDTO> getLicenseById(@PathVariable Long id) {
        License license = licenseService.findById(id);
        return ResponseEntity.ok(licenseService.convertToDTO(license));
    }

    @GetMapping
    public ResponseEntity<List<LicenseResponseDTO>> getAllLicenses() {
        List<License> licenses = licenseService.findAll();
        return ResponseEntity.ok(licenseService.convertToDTOList(licenses));
    }

    @PostMapping
    public ResponseEntity<LicenseResponseDTO> createLicense(@RequestBody CreateLicenseRequest request) {
        License license = licenseService.createLicense(request);
        return ResponseEntity.ok(licenseService.convertToDTO(license));
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<LicenseResponseDTO> blockLicense(@PathVariable Long id) {
        License license = licenseService.blockLicense(id);
        return ResponseEntity.ok(licenseService.convertToDTO(license));
    }

    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/types")
    public ResponseEntity<?> getAllLicenseTypes() {
        return ResponseEntity.ok(licenseTypeService.findAll());
    }
}