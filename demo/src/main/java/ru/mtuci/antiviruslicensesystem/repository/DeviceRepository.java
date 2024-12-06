package ru.mtuci.antiviruslicensesystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.mtuci.antiviruslicensesystem.entity.Device;
import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByMacAddress(String macAddress);
    boolean existsByMacAddress(String macAddress);
    List<Device> findByUserId(Long userId);
}
