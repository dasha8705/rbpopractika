package ru.mtuci.antiviruslicensesystem.service;

import lombok.RequiredArgsConstructor;
import ru.mtuci.antiviruslicensesystem.dto.LicenseInfoRequest;
import ru.mtuci.antiviruslicensesystem.dto.LicenseInfoResponse;
import ru.mtuci.antiviruslicensesystem.entity.Device;
import ru.mtuci.antiviruslicensesystem.entity.License;
import ru.mtuci.antiviruslicensesystem.entity.Ticket;
import ru.mtuci.antiviruslicensesystem.entity.User;
import ru.mtuci.antiviruslicensesystem.exceptions.ApiException;
import ru.mtuci.antiviruslicensesystem.repository.DeviceRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LicenseInfoService {
    private final DeviceRepository deviceRepository;
    private final TicketService ticketService;

    @Transactional(readOnly = true)
    public LicenseInfoResponse getLicenseInfo(LicenseInfoRequest request) {
        // Получаем текущего пользователя
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Находим устройство
        Device device = deviceRepository.findByMacAddress(request.getDeviceInfo())
                .orElseThrow(() -> new ApiException("Device not found"));

        // Проверяем принадлежность устройства пользователю
        if (!device.getUser().getId().equals(currentUser.getId())) {
            throw new ApiException("Device doesn't belong to current user");
        }

        // Получаем активные лицензии для устройства
        License license = device.getLicense();
        if (license == null) {
            return createErrorResponse("No active license found for this device");
        }

        // Проверяем статус лицензии
        if (license.getBlocked()) {
            return createErrorResponse("License is blocked");
        }

        if (license.getEndingDate().isBefore(LocalDateTime.now())) {
            return createErrorResponse("License has expired");
        }

        // Генерируем тикет
        Ticket ticket = ticketService.generateTicket(license, device.getMacAddress());

        return createSuccessResponse(ticket);
    }

    private LicenseInfoResponse createErrorResponse(String message) {
        LicenseInfoResponse response = new LicenseInfoResponse();
        response.setStatus("ERROR");
        response.setMessage(message);
        return response;
    }

    private LicenseInfoResponse createSuccessResponse(Ticket ticket) {
        LicenseInfoResponse response = new LicenseInfoResponse();
        response.setStatus("SUCCESS");
        response.setTicket(ticket);
        return response;
    }
}
