package com.integrador.servicios_tecnicos.service.user;

import com.integrador.servicios_tecnicos.exceptions.ResourceNotFoundException;
import com.integrador.servicios_tecnicos.exceptions.SavedUserException;
import com.integrador.servicios_tecnicos.models.dtos.user.UserResponseDTO;
import com.integrador.servicios_tecnicos.models.entity.Role;
import com.integrador.servicios_tecnicos.models.entity.User;
import com.integrador.servicios_tecnicos.repository.UserRepository;
import com.integrador.servicios_tecnicos.service.role.RoleService;
import jakarta.transaction.Transactional;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final SecurityUserService securityService;
    private final ModelMapper modelMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository,
                       RoleService roleService,
                       SecurityUserService securityService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.securityService = securityService;
        this.modelMapper = modelMapper;
        configureMapping();
    }

    public List<UserResponseDTO> allUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserResponseDTO.class))
                .collect(Collectors.toList());
    }

    public User getUserByEmail(String email) throws ResourceNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    public User saveUser(User user) throws SavedUserException {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            LOGGER.error("Error saving user", e);
            throw new SavedUserException("Ocurrió un error al guardar el usuario");
        }
    }

    public UserResponseDTO getAuthenticatedUser() {
        User user = securityService.getAuthenticatedUser();
        return modelMapper.map(user, UserResponseDTO.class);
    }

    @Transactional
    public UserResponseDTO updateUserRoles(Long userId, List<Long> roleIds) throws ResourceNotFoundException {
        User user = getUserById(userId);
        List<Role> roles = roleService.getRolesByIds(roleIds);

        user.setRoles(roles);

        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserResponseDTO.class);
    }

    public User getUserById(Long userId) throws ResourceNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void configureMapping(){
        Converter<List<Role>, List<String>> rolesToNamesConverter = ctx ->
                ctx.getSource().stream().map(Role::getName).collect(Collectors.toList());

        modelMapper.typeMap(User.class, UserResponseDTO.class)
                .addMappings(mapper -> mapper.using(rolesToNamesConverter).map(User::getRoles, UserResponseDTO::setRoles));
    }
}
