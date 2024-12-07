package ru.mtuci.antiviruslicensesystem.service;

import lombok.RequiredArgsConstructor;
import ru.mtuci.antiviruslicensesystem.dto.*;
import ru.mtuci.antiviruslicensesystem.entity.License;
import ru.mtuci.antiviruslicensesystem.entity.LicenseType;
import ru.mtuci.antiviruslicensesystem.entity.Product;
import ru.mtuci.antiviruslicensesystem.entity.User;
import ru.mtuci.antiviruslicensesystem.exceptions.ApiException;
import ru.mtuci.antiviruslicensesystem.repository.LicenseRepository;
import ru.mtuci.antiviruslicensesystem.repository.LicenseTypeRepository;
import ru.mtuci.antiviruslicensesystem.repository.ProductRepository;
import ru.mtuci.antiviruslicensesystem.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

//TODO: 1. createLicense дублируется, зачем?
//TODO: 2. .deviceCount(0) - максимально возможное число устройств

// LicenseService.java
@Service
@RequiredArgsConstructor
public class LicenseService {
    private final LicenseRepository licenseRepository;
    private final UserService userService;
    private final ProductRepository productRepository;
    private final LicenseTypeRepository licenseTypeRepository;
    private final UserRepository userRepository;
    private final LicenseHistoryService licenseHistoryService;
    @Transactional(readOnly = true)
    public long countAll() {
        return licenseRepository.count();
    }

    @Transactional(readOnly = true)
    public List<License> findAll() {
        return licenseRepository.findAll();
    }
    @Transactional(readOnly = true)
    public License findById(Long id) {
        return licenseRepository.findById(id)
                .orElseThrow(() -> new ApiException("License not found with id: " + id));
    }

    public LicenseResponseDTO convertToDTO(License license) {
        if (license == null) {
            return null;
        }

        // Создаем ProductDTO
        ProductDTO productDTO = null;
        if (license.getProduct() != null) {
            productDTO = ProductDTO.builder()
                    .id(license.getProduct().getId())
                    .name(license.getProduct().getName())
                    .is_blocked(license.getProduct().getIsBlocked())
                    .build();
        }

        // Создаем LicenseTypeDTO
        LicenseTypeDTO typeDTO = null;
        if (license.getType() != null) {
            typeDTO = LicenseTypeDTO.builder()
                    .id(license.getType().getId())
                    .name(license.getType().getName())
                    .defaultDuration(license.getType().getDefaultDuration())
                    .description(license.getType().getDescription())
                    .build();
        }

        // Создаем и возвращаем LicenseResponseDTO
        return LicenseResponseDTO.builder()
                .id(license.getId())
                .code(license.getCode())
                .firstActivationDate(license.getFirstActivationDate())
                .endingDate(license.getEndingDate())
                .blocked(license.getBlocked())
                .deviceCount(license.getDeviceCount())
                .ownerId(license.getOwnerId())
                .duration(license.getDuration())
                .description(license.getDescription())
                .product(productDTO)
                .type(typeDTO)
                .build();
    }

    // Вспомогательный метод для конвертации списка лицензий
    public List<LicenseResponseDTO> convertToDTOList(List<License> licenses) {
        if (licenses == null) {
            return Collections.emptyList();
        }
        return licenses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public License createLicense(LicenseDTO licenseDTO) {
        User user = userService.findById(licenseDTO.getUserId());
        Product product = productRepository.findById(licenseDTO.getProductId())
                .orElseThrow(() -> new ApiException("Product not found"));
        LicenseType type = licenseTypeRepository.findById(licenseDTO.getTypeId())
                .orElseThrow(() -> new ApiException("License type not found"));

        License license = License.builder()
                .code(licenseDTO.getCode())
                .user(user)
                .product(product)
                .type(type)
                .firstActivationDate(licenseDTO.getFirstActivationDate())
                .endingDate(licenseDTO.getEndingDate())
                .blocked(licenseDTO.getBlocked())
                .deviceCount(licenseDTO.getDeviceCount())
                .ownerId(licenseDTO.getOwnerId())
                .duration(licenseDTO.getDuration())
                .description(licenseDTO.getDescription())
                .build();

        return licenseRepository.save(license);
    }

    @Transactional(readOnly = true)
    public long countActive() {
        return licenseRepository.countByBlockedFalseAndEndingDateAfter(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public License findByCode(String code) {
        return licenseRepository.findByCode(code)
                .orElseThrow(() -> new ApiException("License not found with code: " + code));
    }

    @Transactional
    public License blockLicense(Long id) {
        License license = findById(id);
        license.setBlocked(true);
        return licenseRepository.save(license); 
    }
    @Transactional
    public License createLicense(CreateLicenseRequest request) {
        // Проверяем существование продукта
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ApiException("Product not found"));

        // Проверяем существование владельца
        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new ApiException("Owner not found"));

        // Проверяем существование типа лицензии
        LicenseType licenseType = licenseTypeRepository.findById(request.getLicenseTypeId())
                .orElseThrow(() -> new ApiException("License type not found"));

        // Создаем новую лицензию
        License license = License.builder()
                .code(generateActivationCode())
                .user(owner)
                .product(product)
                .type(licenseType)
                .firstActivationDate(null)
                .endingDate(null)
                .blocked(false)
                .deviceCount(0)  // Используем camelCase
                .ownerId(owner.getId())
                .duration(licenseType.getDefaultDuration())
                .description(request.getDescription())
                .build();

        license = licenseRepository.save(license);

        // Записываем создание в историю
        licenseHistoryService.recordLicenseChange(
                license,
                owner,
                "Created",
                "License created"
        );

        return license;
    }

    private String generateActivationCode() {
        // Генерация уникального кода активации
        String code;
        do {
            code = UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
        } while (licenseRepository.existsByCode(code));
        return code;
    }
}