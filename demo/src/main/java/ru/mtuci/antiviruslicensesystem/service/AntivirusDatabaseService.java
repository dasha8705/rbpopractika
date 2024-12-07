package ru.mtuci.antiviruslicensesystem.service;

import lombok.RequiredArgsConstructor;
import ru.mtuci.antiviruslicensesystem.dto.AntivirusDatabaseResponse;
import ru.mtuci.antiviruslicensesystem.dto.AntivirusDatabaseUploadRequest;
import ru.mtuci.antiviruslicensesystem.entity.AntivirusDatabase;
import ru.mtuci.antiviruslicensesystem.entity.Product;
import ru.mtuci.antiviruslicensesystem.exceptions.ApiException;
import ru.mtuci.antiviruslicensesystem.repository.AntivirusDatabaseRepository;
import ru.mtuci.antiviruslicensesystem.repository.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//TODO: 1. Интересное решение. Нужно обсудить

@Service
@RequiredArgsConstructor
public class AntivirusDatabaseService {
    private final AntivirusDatabaseRepository databaseRepository;
    private final ProductRepository productRepository;
    private static final String UPLOAD_DIR = "antivirus-databases";

    @Transactional(readOnly = true)
    public List<AntivirusDatabase> findAll() {
        return databaseRepository.findAll();
    }
    @Transactional
    public AntivirusDatabaseResponse uploadDatabase(AntivirusDatabaseUploadRequest request) throws IOException {
        // Проверяем существование продукта
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ApiException("Product not found"));

        // Проверяем уникальность версии для продукта
        if (databaseRepository.existsByVersionAndProductId(request.getVersion(), request.getProductId())) {
            throw new ApiException("Database version already exists for this product");
        }

        // Создаем директорию если не существует
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Сохраняем файл
        String fileName = String.format("%s_%s_%s",
                product.getId(),
                request.getVersion().replace('.', '_'),
                request.getDatabaseFile().getOriginalFilename());
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(request.getDatabaseFile().getInputStream(), filePath);

        // Считаем контрольную сумму
        String checksum = calculateMD5(request.getDatabaseFile());

        // Создаем запись в базе
        AntivirusDatabase database = AntivirusDatabase.builder()
                .version(request.getVersion())
                .description(request.getDescription())
                .releaseDate(LocalDateTime.now())
                .fileLocation(filePath.toString())
                .fileSize(request.getDatabaseFile().getSize())
                .checksum(checksum)
                .isActive(true)
                .product(product)
                .build();

        database = databaseRepository.save(database);

        return mapToResponse(database);
    }

    @Transactional(readOnly = true)
    public List<AntivirusDatabaseResponse> getActiveDataBasesForProduct(Long productId) {
        return databaseRepository.findByProductIdAndIsActiveTrue(productId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private String calculateMD5(MultipartFile file) throws IOException {
        return DigestUtils.md5DigestAsHex(file.getInputStream());
    }

    private AntivirusDatabaseResponse mapToResponse(AntivirusDatabase database) {
        return AntivirusDatabaseResponse.builder()
                .id(database.getId())
                .version(database.getVersion())
                .description(database.getDescription())
                .fileLocation(database.getFileLocation())
                .fileSize(database.getFileSize())
                .checksum(database.getChecksum())
                .isActive(database.getIsActive())
                .build();
    }
}