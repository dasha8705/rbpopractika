package ru.mtuci.antiviruslicensesystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceResponseDTO {
    private Long id;
    private String name;
    private String macAddress;
    private UserMinDTO user;
    private LicenseMinDTO license;

    @Data
    @Builder
    public static class UserMinDTO {
        private Long id;
        private String login;
        private String email;
    }

    @Data
    @Builder
    public static class LicenseMinDTO {
        private Long id;
        private String code;
    }
}