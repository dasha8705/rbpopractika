package ru.mtuci.antiviruslicensesystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "licenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class License extends BaseEntity {

    private String code;

    @Column(name = "first_activation_date")
    private LocalDateTime firstActivationDate;

    @Column(name = "ending_date")
    private LocalDateTime endingDate;

    private Boolean blocked;

    @Column(name = "device_count")
    private Integer deviceCount;

    @Column(name = "owner_id")
    private Long ownerId;

    private Integer duration;

    private String description;

    @JsonBackReference("user-licenses")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonBackReference("license-product")
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @JsonManagedReference("license-type")
    @ManyToOne
    @JoinColumn(name = "type_id")
    private LicenseType type;
}