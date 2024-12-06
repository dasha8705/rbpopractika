package ru.mtuci.antiviruslicensesystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.mtuci.antiviruslicensesystem.entity.LicenseHistory;

public interface LicenseHistoryRepository extends JpaRepository<LicenseHistory, Long> {

}
