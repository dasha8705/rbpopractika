package ru.mtuci.antiviruslicensesystem.entity;

import lombok.Builder;
import lombok.Data;
import ru.mtuci.antiviruslicensesystem.exceptions.ApiException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;

//TODO: 1. Методы класса не используются

@Data
@Builder
public class Ticket {
    private LocalDateTime serverTime;
    private Long ticketLifetime;
    private LocalDateTime activationDate;
    private LocalDateTime expirationDate;
    private Long userId;
    private String deviceId;
    private Boolean isLicenseBlocked;
    private String digitalSignature;

    public void signTicket(String secretKey) {
        try {
            String content = String.format("%s|%d|%s|%s|%d|%s|%b",
                    serverTime, ticketLifetime, activationDate,
                    expirationDate, userId, deviceId, isLicenseBlocked);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((content + secretKey).getBytes(StandardCharsets.UTF_8));
            this.digitalSignature = Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new ApiException("Failed to generate digital signature");
        }
    }

    public boolean verifySignature(String secretKey) {
        try {
            String content = String.format("%s|%d|%s|%s|%d|%s|%b",
                    serverTime, ticketLifetime, activationDate,
                    expirationDate, userId, deviceId, isLicenseBlocked);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] expectedHash = digest.digest((content + secretKey).getBytes(StandardCharsets.UTF_8));
            String expectedSignature = Base64.getEncoder().encodeToString(expectedHash);

            return MessageDigest.isEqual(
                    Base64.getDecoder().decode(this.digitalSignature),
                    Base64.getDecoder().decode(expectedSignature)
            );
        } catch (NoSuchAlgorithmException e) {
            throw new ApiException("Failed to verify digital signature");
        }
    }
}