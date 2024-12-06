package ru.mtuci.antiviruslicensesystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.mtuci.antiviruslicensesystem.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);
}