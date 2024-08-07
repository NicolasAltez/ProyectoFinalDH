package com.integrador.servicios_tecnicos.service.impl;

import com.integrador.servicios_tecnicos.exceptions.ResourceNotFoundException;
import com.integrador.servicios_tecnicos.exceptions.SavedUserException;
import com.integrador.servicios_tecnicos.models.entity.User;
import com.integrador.servicios_tecnicos.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");

        user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
    }

    @Nested
    class getAllUsers{
        @Test
        void testAllUsersWithResults() {
            when(userRepository.findAll()).thenReturn(List.of(user1, user2));

            List<User> result = userService.allUsers();

            assertEquals(2, result.size());
            assertEquals("user1", result.get(0).getUsername());
            assertEquals("user2", result.get(1).getUsername());

            verify(userRepository, times(1)).findAll();
        }

        @Test
        void testAllUsersEmpty() {
            when(userRepository.findAll()).thenReturn(Collections.emptyList());

            List<User> result = userService.allUsers();

            assertEquals(0, result.size());
            verify(userRepository, times(1)).findAll();
        }
    }

    @Nested
    class getUserByEmail{
        @Test
        void testGetUserByEmailUserFound() throws ResourceNotFoundException {
            when(userRepository.findByEmail("email_prueba")).thenReturn(java.util.Optional.of(user1));
            User result = userService.getUserByEmail("email_prueba");
            assertNotNull(result);
            assertEquals("user1", result.getUsername());
            verify(userRepository, times(1)).findByEmail("email_prueba");
        }

        @Test
        void testGetUserByEmailUserNotFound() {
            when(userRepository.findByEmail("email_prueba")).thenReturn(java.util.Optional.empty());
            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                userService.getUserByEmail("email_prueba");
            });
            assertEquals("User not found", exception.getMessage());
            verify(userRepository, times(1)).findByEmail("email_prueba");
        }
    }

    @Nested
    class savedUser{
        @Test
        void testSaveUserSuccessfully() throws SavedUserException {
            User user = User.builder()
                    .email("test@example.com")
                    .username("testuser")
                    .password("password")
                    .enabled(true)
                    .build();

            when(userRepository.save(any(User.class))).thenReturn(user);

            User savedUser = userService.saveUser(user);

            assertEquals(user.getEmail(), savedUser.getEmail());
            assertEquals(user.getUsername(), savedUser.getUsername());

            verify(userRepository, times(1)).save(user);
        }

        @Test
        void testSaveUserThrowsSavedUserException() {

            User user = User.builder()
                    .email("test@example.com")
                    .username("testuser")
                    .password("password")
                    .enabled(true)
                    .build();

            when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database error"));


            SavedUserException exception = assertThrows(SavedUserException.class, () -> {
                userService.saveUser(user);
            });

            assertEquals("Ocurrio un error al guardar el usuario", exception.getMessage());

            verify(userRepository, times(1)).save(user);
        }
    }
}