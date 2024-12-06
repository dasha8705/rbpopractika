package ru.mtuci.antiviruslicensesystem.service;

import lombok.RequiredArgsConstructor;
import ru.mtuci.antiviruslicensesystem.entity.Product;
import ru.mtuci.antiviruslicensesystem.exceptions.ApiException;
import ru.mtuci.antiviruslicensesystem.repository.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ApiException("Product not found with id: " + id));
    }

    @Transactional
    public Product create(Product product) {
        if (productRepository.existsByName(product.getName())) {
            throw new ApiException("Product with this name already exists");
        }
        return productRepository.save(product);
    }

    @Transactional
    public Product update(Long id, Product productDetails) {
        Product product = findById(id);

        if (!product.getName().equals(productDetails.getName()) &&
                productRepository.existsByName(productDetails.getName())) {
            throw new ApiException("Product with this name already exists");
        }

        product.setName(productDetails.getName());
        product.setIsBlocked(productDetails.getIsBlocked());

        return productRepository.save(product);
    }

    @Transactional
    public void delete(Long id) {
        Product product = findById(id);
        productRepository.delete(product);
    }
}