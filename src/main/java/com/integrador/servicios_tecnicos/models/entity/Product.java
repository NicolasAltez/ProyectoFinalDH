package com.integrador.servicios_tecnicos.models.entity;

import com.integrador.servicios_tecnicos.enums.ProductType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private ProductType tipo;

    @Column(length = 30)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;


}


