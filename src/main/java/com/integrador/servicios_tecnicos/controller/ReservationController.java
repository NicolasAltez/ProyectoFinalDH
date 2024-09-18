package com.integrador.servicios_tecnicos.controller;

import com.integrador.servicios_tecnicos.exceptions.ResourceNotFoundException;
import com.integrador.servicios_tecnicos.models.dtos.reservation.RequestDTO;
import com.integrador.servicios_tecnicos.models.entity.Reservation;
import com.integrador.servicios_tecnicos.service.impl.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }

    @PostMapping("/new")
    public ResponseEntity<Reservation> saveReservation(@RequestBody RequestDTO requestDTO) throws ResourceNotFoundException {
        return new ResponseEntity<>(reservationService.saveReservation(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Reservation>> getAllReservations(@PathVariable String email){
        return new ResponseEntity<>(reservationService.getAllReservations(email), HttpStatus.OK);
    }
 }
