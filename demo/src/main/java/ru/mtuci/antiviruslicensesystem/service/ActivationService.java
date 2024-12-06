package ru.mtuci.antiviruslicensesystem.service;

import lombok.RequiredArgsConstructor;
import ru.mtuci.antiviruslicensesystem.dto.ActivationRequest;
import ru.mtuci.antiviruslicensesystem.dto.RenewLicenseRequest;
import ru.mtuci.antiviruslicensesystem.entity.Device;
import ru.mtuci.antiviruslicensesystem.entity.License;
import ru.mtuci.antiviruslicensesystem.entity.Ticket;
import ru.mtuci.antiviruslicensesystem.entity.User;
import ru.mtuci.antiviruslicensesystem.exceptions.ApiException;
import ru.mtuci.antiviruslicensesystem.repository.DeviceRepository;
import ru.mtuci.antiviruslicensesystem.repository.LicenseRepository;
import ru.mtuci.antiviruslicensesystem.repository.UserRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActivationService {
    private final LicenseRepository licenseRepository;
    private final DeviceRepository deviceRepository;
    private final LicenseHistoryService licenseHistoryService;
    private final TicketService ticketService;
    private final UserRepository userRepository;
    @Transactional
    public Ticket activateLicense(ActivationRequest request) {
        org.springframework.security.core.userdetails.User springUser =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        User currentUser = userRepository.findByLogin(springUser.getUsername())
                .orElseThrow(() -> new ApiException("User not found"));
        // Находим лицензию по коду активации
        License license = licenseRepository.findByCode(request.getActivationCode())
                .orElseThrow(() -> new ApiException("License not found"));

        // Проверяем возможность активации
        validateActivation(license, currentUser);

        // Регистрируем или обновляем информацию об устройстве
        Device device = registerOrUpdateDevice(request.getDeviceInfo(), currentUser);

        // Активируем лицензию
        activateLicenseForDevice(license, device);

        // Записываем изменение в историю
        licenseHistoryService.recordLicenseChange(license, currentUser, "Activated",
                "License activated for device: " + device.getMacAddress());

        // Генерируем тикет
        return ticketService.generateTicket(license, device.getMacAddress());
    }

    private void validateActivation(License license, User user) {
        if (license.getBlocked()) {
            throw new ApiException("License is blocked");
        }
        if (license.getFirstActivationDate() != null &&
                license.getDeviceCount() >= license.getType().getDefaultDuration()) {
            throw new ApiException("License activation limit exceeded");
        }
    }

    private Device registerOrUpdateDevice(String deviceInfo, User user) {
        return deviceRepository.findByMacAddress(deviceInfo)
                .map(device -> {
                    device.setUser(user);
                    return deviceRepository.save(device);
                })
                .orElseGet(() -> deviceRepository.save(Device.builder()
                        .macAddress(deviceInfo)
                        .user(user)
                        .build()));
    }
    @Transactional(readOnly = true)
    public Ticket checkLicense(String deviceId) {
        // Получаем Spring Security principal
        org.springframework.security.core.userdetails.User springUser =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        // Находим нашего пользователя по логину
        User currentUser = userRepository.findByLogin(springUser.getUsername())
                .orElseThrow(() -> new ApiException("User not found"));

        // Находим устройство
        Device device = deviceRepository.findByMacAddress(deviceId)
                .orElseThrow(() -> new ApiException("Device not found"));

        // Проверяем принадлежность устройства пользователю
        if (!device.getUser().getId().equals(currentUser.getId())) {
            throw new ApiException("Device doesn't belong to current user");
        }

        // Получаем лицензию устройства
        License license = device.getLicense();
        if (license == null) {
            throw new ApiException("No active license found for this device");
        }

        // Проверяем статус лицензии
        if (license.getBlocked()) {
            throw new ApiException("License is blocked");
        }

        if (license.getEndingDate().isBefore(LocalDateTime.now())) {
            throw new ApiException("License has expired");
        }

        // Генерируем и возвращаем тикет
        return ticketService.generateTicket(license, deviceId);
    }

    @Transactional
    public Ticket renewLicense(RenewLicenseRequest request) {
        // Получаем текущего пользователя
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Находим устройство
        Device device = deviceRepository.findByMacAddress(request.getDeviceId())
                .orElseThrow(() -> new ApiException("Device not found"));

        // Проверяем принадлежность устройства пользователю
        if (!device.getUser().getId().equals(currentUser.getId())) {
            throw new ApiException("Device doesn't belong to current user");
        }

        // Находим лицензию
        License license = licenseRepository.findByCode(request.getLicenseKey())
                .orElseThrow(() -> new ApiException("License not found"));

        // Проверяем возможность продления
        validateRenewal(license, currentUser);

        // Продлеваем лицензию
        license.setEndingDate(license.getEndingDate()
                .plusDays(license.getType().getDefaultDuration()));
        licenseRepository.save(license);

        // Записываем изменение в историю
        licenseHistoryService.recordLicenseChange(
                license,
                currentUser,
                "Renewed",
                "License renewed for device: " + device.getMacAddress()
        );

        // Генерируем новый тикет
        return ticketService.generateTicket(license, device.getMacAddress());
    }

    private void validateRenewal(License license, User user) {
        if (license.getBlocked()) {
            throw new ApiException("License is blocked");
        }

        if (!license.getUser().getId().equals(user.getId())) {
            throw new ApiException("License doesn't belong to current user");
        }

        if (license.getFirstActivationDate() == null) {
            throw new ApiException("License has not been activated yet");
        }
    }

    private void activateLicenseForDevice(License license, Device device) {
        if (license.getFirstActivationDate() == null) {
            license.setFirstActivationDate(LocalDateTime.now());
            license.setEndingDate(LocalDateTime.now()
                    .plusDays(license.getType().getDefaultDuration()));
        }

        device.setLicense(license);
        license.setDeviceCount(license.getDeviceCount() + 1);

        licenseRepository.save(license);
        deviceRepository.save(device);
    }
}
