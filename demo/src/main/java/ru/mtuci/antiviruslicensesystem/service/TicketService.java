package ru.mtuci.antiviruslicensesystem.service;


import lombok.RequiredArgsConstructor;
import ru.mtuci.antiviruslicensesystem.entity.License;
import ru.mtuci.antiviruslicensesystem.entity.Ticket;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {
    private static final long DEFAULT_TICKET_LIFETIME = 3600L; // 1 час в секундах

    public Ticket generateTicket(License license, String deviceId) {
        LocalDateTime now = LocalDateTime.now();

        return Ticket.builder()
                .serverTime(now)
                .ticketLifetime(DEFAULT_TICKET_LIFETIME)
                .activationDate(license.getFirstActivationDate())
                .expirationDate(license.getEndingDate())
                .userId(license.getUser().getId())
                .deviceId(deviceId)
                .isLicenseBlocked(license.getBlocked())
                .digitalSignature(generateSignature(license, deviceId))
                .build();
    }

    private String generateSignature(License license, String deviceId) {
        // В реальном приложении здесь должна быть реализация цифровой подписи
        return UUID.randomUUID().toString();
    }
}
