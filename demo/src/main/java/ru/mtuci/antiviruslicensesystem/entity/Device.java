package ru.mtuci.antiviruslicensesystem.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "devices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Device extends BaseEntity {
    private String name;

    @Column(name = "mac_address", unique = true)
    private String macAddress;

    @JsonBackReference("user-devices")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonManagedReference("device-license")
    @OneToOne
    @JoinColumn(name = "license_id")
    private License license;
}