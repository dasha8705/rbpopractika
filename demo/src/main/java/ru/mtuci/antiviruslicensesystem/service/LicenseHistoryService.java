package ru.mtuci.antiviruslicensesystem.service;

import lombok.RequiredArgsConstructor;
import ru.mtuci.antiviruslicensesystem.entity.License;
import ru.mtuci.antiviruslicensesystem.entity.LicenseHistory;
import ru.mtuci.antiviruslicensesystem.entity.User;
import ru.mtuci.antiviruslicensesystem.repository.LicenseHistoryRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LicenseHistoryService {
    private final LicenseHistoryRepository licenseHistoryRepository;

    @Transactional
    public LicenseHistory recordLicenseChange(License license, User user, String status, String description) {
        LicenseHistory history = LicenseHistory.builder()
                .license(license)
                .user(user)
                .status(status)
                .change_date(LocalDateTime.now())
                .description(description)
                .build();

        return licenseHistoryRepository.save(history);
    }
}