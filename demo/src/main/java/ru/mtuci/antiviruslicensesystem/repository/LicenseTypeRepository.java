package ru.mtuci.antiviruslicensesystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.mtuci.antiviruslicensesystem.entity.LicenseType;


public interface LicenseTypeRepository extends JpaRepository<LicenseType, Long> {
    boolean existsByName(String name);
}