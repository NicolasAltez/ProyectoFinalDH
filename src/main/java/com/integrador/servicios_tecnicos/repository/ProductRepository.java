package com.integrador.servicios_tecnicos.repository;

import com.integrador.servicios_tecnicos.models.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
