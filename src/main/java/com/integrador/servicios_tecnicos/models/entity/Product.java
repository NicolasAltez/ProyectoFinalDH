package com.integrador.servicios_tecnicos.models.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "SERVICIOS")
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 30)
    private String tipo;
    @Column(length = 30)
    private String nombre;
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    public Products() {
    }

    public Products(Long id, String tipo, String nombre, String descripcion) {
        this.id = id;
        this.tipo = tipo;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString(){
        return "id: "+ id + " - Tipo: "+ tipo + " - Nombre: "+ nombre + " - descripcion" + descripcion;
    }
}
