package com.integrador.servicios_tecnicos.service.impl;

import com.integrador.servicios_tecnicos.exceptions.ResourceNotFoundException;
import com.integrador.servicios_tecnicos.models.dtos.reservation.RequestDTO;
import com.integrador.servicios_tecnicos.models.entity.Product;
import com.integrador.servicios_tecnicos.models.entity.Reservation;
import com.integrador.servicios_tecnicos.models.entity.User;
import com.integrador.servicios_tecnicos.repository.ReservationRepository;
import com.integrador.servicios_tecnicos.service.email.EmailService;
import com.integrador.servicios_tecnicos.service.user.UserService;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {
    private final UserService userService;
    private final ProductService productService;
    private final ReservationRepository reservationRepository;
    private final ModelMapper modelMapper;
    private final EmailService emailService;


    ReservationService(UserService userService, ProductService productService, ReservationRepository reservationRepository, ModelMapper modelMapper, EmailService emailService) {
        this.userService = userService;
        this.productService = productService;
        this.reservationRepository = reservationRepository;
        this.modelMapper = modelMapper;
        this.emailService = emailService;

    }

    public Reservation saveReservation(RequestDTO reservation) throws ResourceNotFoundException, MessagingException {
        User user = userService.getUserByEmail(reservation.getEmail());
        Product product = productService.getProductById(reservation.getProductId());
        Reservation saved = reservationRepository.save(
                Reservation.builder()
                        .reservationDate(reservation.getReservationDate())
                        .createdAt(LocalDateTime.now())
                        .product(product)
                        .user(user)
                        .address(reservation.getAddress())
                        .build()
        );
        emailService.sendReservationConfirmationEmail(user.getEmail(), "Date : " + saved.getReservationDate() + " Product : " + saved.getProduct().getName());
        return saved;
    }

    public List<Reservation> getAllReservations(String email) throws ResourceNotFoundException {
        User user = userService.getUserByEmail(email);
        return reservationRepository.findAllByUser(user);
    }


}
