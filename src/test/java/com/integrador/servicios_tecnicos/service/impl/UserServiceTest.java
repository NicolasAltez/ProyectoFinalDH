package com.integrador.servicios_tecnicos.service.impl;

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
}