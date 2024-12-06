package ru.mtuci.antiviruslicensesystem.dto;

import lombok.Data;
import ru.mtuci.antiviruslicensesystem.entity.Ticket;

@Data
public class LicenseInfoResponse {
    private String status;
    private Ticket ticket;
    private String message;
}
