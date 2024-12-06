package ru.mtuci.antiviruslicensesystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDTO {
    private Long id;
    private String name;
    private String macAddress;
    private Long userId;
    private Long licenseId;
    private String userName;
    private String licenseName;
}