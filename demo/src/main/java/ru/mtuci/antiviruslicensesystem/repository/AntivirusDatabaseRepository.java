package ru.mtuci.antiviruslicensesystem.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import ru.mtuci.antiviruslicensesystem.entity.AntivirusDatabase;

import java.util.List;
import java.util.Optional;

public interface AntivirusDatabaseRepository extends JpaRepository<AntivirusDatabase, Long> {
    Optional<AntivirusDatabase> findByVersionAndProductId(String version, Long productId);
    List<AntivirusDatabase> findByProductIdAndIsActiveTrue(Long productId);
    boolean existsByVersionAndProductId(String version, Long productId);
}
