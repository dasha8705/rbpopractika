package ru.mtuci.antiviruslicensesystem.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Column(unique = true)
    private String login;

    private String password_hash;

    @Column(unique = true)
    private String email;

    private String role;

    @JsonManagedReference("user-licenses")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<License> licenses = new ArrayList<>();

    @JsonManagedReference("user-devices")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Device> devices = new ArrayList<>();
}