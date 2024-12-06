package ru.mtuci.antiviruslicensesystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "license_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicenseHistory extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "license_id")
    private License license;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String status;

    private LocalDateTime change_date;

    private String description;
}
