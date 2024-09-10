package com.integrador.servicios_tecnicos.repository;

import com.integrador.servicios_tecnicos.models.entity.Characteristic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacteristicsRepository extends JpaRepository<Characteristic,Long> {
}
