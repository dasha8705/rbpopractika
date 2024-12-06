package ru.mtuci.antiviruslicensesystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "license_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicenseType extends BaseEntity {

    private String name;

    @Column(name = "default_duration")
    private Integer defaultDuration;

    private String description;

    @JsonBackReference("license-type")  // Добавляем эту аннотацию
    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL)
    private List<License> licenses = new ArrayList<>();
}
