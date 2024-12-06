package ru.mtuci.antiviruslicensesystem.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import ru.mtuci.antiviruslicensesystem.entity.License;

import java.time.LocalDateTime;
import java.util.Optional;

public interface LicenseRepository extends JpaRepository<License, Long> {
    Optional<License> findByCode(String code);
    boolean existsByCode(String code);
    long countByBlockedFalseAndEndingDateAfter(LocalDateTime date);
}