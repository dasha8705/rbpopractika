package ru.mtuci.antiviruslicensesystem.service;

import lombok.RequiredArgsConstructor;
import ru.mtuci.antiviruslicensesystem.dto.DeviceDTO;
import ru.mtuci.antiviruslicensesystem.dto.DeviceResponseDTO;
import ru.mtuci.antiviruslicensesystem.entity.Device;
import ru.mtuci.antiviruslicensesystem.entity.License;
import ru.mtuci.antiviruslicensesystem.entity.User;
import ru.mtuci.antiviruslicensesystem.exceptions.ApiException;
import ru.mtuci.antiviruslicensesystem.repository.DeviceRepository;
import ru.mtuci.antiviruslicensesystem.repository.LicenseRepository;
import ru.mtuci.antiviruslicensesystem.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

//TODO: 1. device.setMacAddress(dto.getMacAddress()); - зачем?

@Service
@RequiredArgsConstructor
public class DeviceService {
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;
    private final LicenseRepository licenseRepository;

    @Transactional(readOnly = true)
    public List<Device> findAll() {
        return deviceRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Device findById(Long id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new ApiException("Device not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<DeviceResponseDTO> findAllDTO() {
        return deviceRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public DeviceResponseDTO createDevice(DeviceDTO dto) {
        if (deviceRepository.existsByMacAddress(dto.getMacAddress())) {
            throw new ApiException("Device with this MAC address already exists");
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ApiException("User not found"));

        Device device = Device.builder()
                .name(dto.getName())
                .macAddress(dto.getMacAddress())
                .user(user)
                .build();

        if (dto.getLicenseId() != null) {
            License license = licenseRepository.findById(dto.getLicenseId())
                    .orElseThrow(() -> new ApiException("License not found"));
            device.setLicense(license);
        }

        return mapToResponseDTO(deviceRepository.save(device));
    }

    private DeviceResponseDTO mapToResponseDTO(Device device) {
        DeviceResponseDTO.UserMinDTO userMin = device.getUser() != null ? DeviceResponseDTO.UserMinDTO.builder()
                .id(device.getUser().getId())
                .login(device.getUser().getLogin())
                .email(device.getUser().getEmail())
                .build() : null;

        DeviceResponseDTO.LicenseMinDTO licenseMin = device.getLicense() != null ? DeviceResponseDTO.LicenseMinDTO.builder()
                .id(device.getLicense().getId())
                .code(device.getLicense().getCode())
                .build() : null;

        return DeviceResponseDTO.builder()
                .id(device.getId())
                .name(device.getName())
                .macAddress(device.getMacAddress())
                .user(userMin)
                .license(licenseMin)
                .build();
    }

    @Transactional(readOnly = true)
    public DeviceResponseDTO findByIdDTO(Long id) {
        return mapToResponseDTO(findById(id));
    }

    @Transactional(readOnly = true)
    public List<Device> findByUserId(Long userId) {
        return deviceRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public long countAll() {
        return deviceRepository.count();
    }

    @Transactional
    public Device registerDevice(DeviceDTO deviceDTO) {
        // Проверяем, существует ли уже устройство с таким MAC-адресом
        if (deviceRepository.existsByMacAddress(deviceDTO.getMacAddress())) {
            throw new ApiException("Device with this MAC address already exists");
        }

        User user = userRepository.findById(deviceDTO.getUserId())
                .orElseThrow(() -> new ApiException("User not found"));

        Device device = Device.builder()
                .name(deviceDTO.getName())
                .macAddress(deviceDTO.getMacAddress())
                .user(user)
                .build();

        return deviceRepository.save(device);
    }

    @Transactional
    public DeviceResponseDTO updateDevice(Long id, DeviceDTO dto) {
        Device device = findById(id);

        if (!device.getMacAddress().equals(dto.getMacAddress()) &&
                deviceRepository.existsByMacAddress(dto.getMacAddress())) {
            throw new ApiException("Device with this MAC address already exists");
        }

        device.setName(dto.getName());
        device.setMacAddress(dto.getMacAddress());

        if (dto.getUserId() != null) {
            User newUser = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ApiException("User not found"));
            device.setUser(newUser);
        }

        if (dto.getLicenseId() != null) {
            License license = licenseRepository.findById(dto.getLicenseId())
                    .orElseThrow(() -> new ApiException("License not found"));
            device.setLicense(license);
        }

        return mapToResponseDTO(deviceRepository.save(device));
    }

    @Transactional
    public void deleteDevice(Long id) {
        Device device = findById(id);
        deviceRepository.delete(device);
    }

    @Transactional(readOnly = true)
    public DeviceDTO mapToDTO(Device device) {
        return DeviceDTO.builder()
                .id(device.getId())
                .name(device.getName())
                .macAddress(device.getMacAddress())
                .userId(device.getUser() != null ? device.getUser().getId() : null)
                .build();
    }

    @Transactional(readOnly = true)
    public List<DeviceDTO> mapToDTOList(List<Device> devices) {
        return devices.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}