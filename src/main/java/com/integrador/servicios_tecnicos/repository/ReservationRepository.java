package com.integrador.servicios_tecnicos.repository;

import com.integrador.servicios_tecnicos.models.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByEmail(String email);
}
