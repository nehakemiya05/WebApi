package com.project.CrudAPIs.services.impl;

import com.project.CrudAPIs.dto.LoginRequestDTO;
import com.project.CrudAPIs.dto.ResponseDTO;
import com.project.CrudAPIs.dto.UserDTO;
import com.project.CrudAPIs.model.User;
import com.project.CrudAPIs.model.enums.Role;
import com.project.CrudAPIs.repository.UserRepository;
import com.project.CrudAPIs.security.JwtUtil;
import com.project.CrudAPIs.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j  // <-- Logger Enabled
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private UserDTO mapToDTO(User user) {
        log.debug("Mapping User to UserDTO: {}", user.getEmail());
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setAddress(user.getAddress());
        dto.setRoles(user.getRoles());
        return dto;
    }

    private User mapToEntity(UserDTO dto) {
        log.debug("Mapping UserDTO to User: {}", dto.getEmail());
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        if (dto.getPassword() != null) {
            log.debug("Encrypting password for user: {}", dto.getEmail());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setRoles(dto.getRoles());
        return user;
    }

    @Override
    public UserDTO createUser(UserDTO dto) {
        log.info("Creating new user: {}", dto.getEmail());
        if (userRepository.existsByEmail(dto.getEmail())) {
            log.error("User already exists: {}", dto.getEmail());
            throw new RuntimeException("Email already in use");
        }
        User user = mapToEntity(dto);
        return mapToDTO(userRepository.save(user));
    }

    @Override
    public UserDTO getUserById(Long id) {
        log.info("Fetching user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToDTO(user);
    }

    @Override
    public List<UserDTO> getAllEmployee() {
        log.info("Fetching all EMPLOYEES");

        return userRepository.findAll().stream()
                .filter(user -> user.getRoles().contains(Role.EMPLOYEE))
                .map(this::mapToDTO)
                .toList();
    }
    @Override
    public List<UserDTO> getAllEmployeeAndManager() {
        log.info("Fetching All EMPLOYEE and MANAGER");
        List<UserDTO> list = userRepository.findAll().stream()
                .filter(user -> user.getRoles().contains(Role.EMPLOYEE) || user.getRoles().contains(Role.MANAGER))
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        log.debug("EMPLOYEE + MANAGER Found: {}", list.size());
        return list;
    }

    @Override
    public List<UserDTO> getAllUser() {
        log.info("Fetching all users");
        List<UserDTO> list = userRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        log.debug("Users Found: {}", list.size());
        return list;
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO dto) {
        log.info("Updating user ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPassword() != null) user.setPassword(passwordEncoder.encode(dto.getPassword()));
        if (dto.getPhoneNumber() != null) user.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getAddress() != null) user.setAddress(dto.getAddress());
        if (dto.getRoles() != null) user.setRoles(dto.getRoles());

        return mapToDTO(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        log.warn("Deleting user with ID: {}", id);
        if (!userRepository.existsById(id)) {
            log.error("User not found ID: {}", id);
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public ResponseDTO login(LoginRequestDTO loginDTO) {
        log.info("Login attempt for email: {}", loginDTO.getEmail());
        User user = userRepository.findByEmail(loginDTO.getEmail()).get();

        if (user == null) {
            log.error("User not found: {}", loginDTO.getEmail());
            throw new RuntimeException("User not found");
        }
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            log.error("Invalid password for email: {}", loginDTO.getEmail());
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRoles());
        log.info("Token created for user {}", user.getEmail());

        return new ResponseDTO(token, user.getId(), user.getFirstName(),
                user.getLastName(), user.getEmail(), user.getRoles());
    }

    public List<UserDTO> getUsersByRole(String role) {
        log.info("Fetching users by role: {}", role);
        List<User> users = new ArrayList<>();

        switch (role.toUpperCase()) {
            case "ADMIN":
                users = userRepository.findAll();
                break;
            case "MANAGER":
                users = userRepository.findByRolesIn(List.of("MANAGER", "EMPLOYEE"));
                break;
            case "EMPLOYEE":
                users = userRepository.findByRoles("EMPLOYEE");
                break;
            default:
                log.error("Invalid role: {}", role);
                throw new RuntimeException("Invalid role: " + role);
        }

        log.debug("Users Found: {}", users.size());
        return users.stream().map(this::mapToDTO).collect(Collectors.toList());
    }
}
