package ru.mtuci.antiviruslicensesystem.service;


import lombok.RequiredArgsConstructor;
import ru.mtuci.antiviruslicensesystem.dto.LicenseUpdateRequest;
import ru.mtuci.antiviruslicensesystem.dto.LicenseUpdateResponse;
import ru.mtuci.antiviruslicensesystem.entity.License;
import ru.mtuci.antiviruslicensesystem.entity.User;
import ru.mtuci.antiviruslicensesystem.exceptions.ApiException;
import ru.mtuci.antiviruslicensesystem.repository.LicenseRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LicenseUpdateService {
    private final LicenseRepository licenseRepository;
    private final LicenseHistoryService licenseHistoryService;
    private final TicketService ticketService;

    @Transactional
    public LicenseUpdateResponse updateLicense(LicenseUpdateRequest request) {
        // Получаем текущего пользователя
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Находим лицензию
        License license = licenseRepository.findByCode(request.getLicenseKey())
                .orElseThrow(() -> new ApiException("License not found"));

        // Проверяем возможность обновления
        validateLicenseUpdate(license, currentUser);

        // Обновляем лицензию
        license.setEndingDate(license.getEndingDate()
                .plusDays(license.getType().getDefaultDuration()));
        license = licenseRepository.save(license);

        // Записываем изменение в историю
        licenseHistoryService.recordLicenseChange(
                license,
                currentUser,
                "Updated",
                "License period extended"
        );

        return createSuccessResponse(license);
    }

    private void validateLicenseUpdate(License license, User currentUser) {
        if (!license.getUser().getId().equals(currentUser.getId())) {
            throw new ApiException("License doesn't belong to current user");
        }

        if (license.getBlocked()) {
            throw new ApiException("License is blocked");
        }

        if (license.getFirstActivationDate() == null) {
            throw new ApiException("License has not been activated yet");
        }
    }

    private LicenseUpdateResponse createSuccessResponse(License license) {
        LicenseUpdateResponse response = new LicenseUpdateResponse();
        response.setStatus("SUCCESS");
        response.setMessage("License successfully updated");
        response.setTicket(ticketService.generateTicket(license, ""));
        return response;
    }
}