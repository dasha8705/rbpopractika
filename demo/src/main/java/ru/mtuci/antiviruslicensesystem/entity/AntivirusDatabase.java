package ru.mtuci.antiviruslicensesystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "antivirus_databases")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AntivirusDatabase extends BaseEntity {

    @Column(nullable = false)
    private String version;

    @Column(nullable = false)
    private LocalDateTime releaseDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String fileLocation;

    private Long fileSize;

    private String checksum;

    @Column(nullable = false)
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}