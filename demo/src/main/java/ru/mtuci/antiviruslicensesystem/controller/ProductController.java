package ru.mtuci.antiviruslicensesystem.controller;

import lombok.RequiredArgsConstructor;
import ru.mtuci.antiviruslicensesystem.dto.CreateProductRequest;
import ru.mtuci.antiviruslicensesystem.entity.Product;
import ru.mtuci.antiviruslicensesystem.service.ProductService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody CreateProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .isBlocked(request.getIsBlocked())
                .build();
        return ResponseEntity.ok(productService.create(product));
    }
}
