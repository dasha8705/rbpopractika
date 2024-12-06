package ru.mtuci.antiviruslicensesystem.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {

    private String name;

    @Column(name = "is_blocked")
    private Boolean isBlocked;

    @JsonManagedReference("license-product")
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<License> licenses = new ArrayList<>();
}
