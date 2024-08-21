package com.integrador.servicios_tecnicos.service.impl;

import com.integrador.servicios_tecnicos.exceptions.ResourceNotFoundException;
import com.integrador.servicios_tecnicos.exceptions.SavedUserException;
import com.integrador.servicios_tecnicos.models.entity.User;
import com.integrador.servicios_tecnicos.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> allUsers() {
        return userRepository.findAll().stream().toList();
    }

    public User getUserByEmail(String email) throws ResourceNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User saveUser(User user) throws SavedUserException {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            LOGGER.error("Error saving user", e);
            throw new SavedUserException("Ocurrio un error al guardar el usuario");
        }
    }

}
