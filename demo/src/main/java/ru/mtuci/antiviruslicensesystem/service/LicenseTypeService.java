package ru.mtuci.antiviruslicensesystem.service;

import lombok.RequiredArgsConstructor;
import ru.mtuci.antiviruslicensesystem.entity.LicenseType;
import ru.mtuci.antiviruslicensesystem.exceptions.ApiException;
import ru.mtuci.antiviruslicensesystem.repository.LicenseTypeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LicenseTypeService {
    private final LicenseTypeRepository licenseTypeRepository;

    @Transactional(readOnly = true)
    public List<LicenseType> findAll() {
        return licenseTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public LicenseType findById(Long id) {
        return licenseTypeRepository.findById(id)
                .orElseThrow(() -> new ApiException("License type not found with id: " + id));
    }

    @Transactional
    public LicenseType create(LicenseType licenseType) {
        if (licenseTypeRepository.existsByName(licenseType.getName())) {
            throw new ApiException("License type with this name already exists");
        }
        return licenseTypeRepository.save(licenseType);
    }

    @Transactional
    public LicenseType update(Long id, LicenseType typeDetails) {
        LicenseType type = findById(id);

        if (!type.getName().equals(typeDetails.getName()) &&
                licenseTypeRepository.existsByName(typeDetails.getName())) {
            throw new ApiException("License type with this name already exists");
        }

        type.setName(typeDetails.getName());
        type.setDefaultDuration(typeDetails.getDefaultDuration());
        type.setDescription(typeDetails.getDescription());

        return licenseTypeRepository.save(type);
    }

    @Transactional
    public void delete(Long id) {
        LicenseType type = findById(id);
        licenseTypeRepository.delete(type);
    }
}
