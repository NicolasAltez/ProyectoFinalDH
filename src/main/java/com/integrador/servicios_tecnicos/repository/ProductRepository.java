package com.integrador.servicios_tecnicos.repository;

import com.integrador.servicios_tecnicos.models.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Products, Long> {
}
