package com.project.CrudAPIs.services;

import com.project.CrudAPIs.dto.LoginRequestDTO;
import com.project.CrudAPIs.dto.ResponseDTO;
import com.project.CrudAPIs.dto.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    UserDTO getUserById(Long id);

    List<UserDTO> getAllEmployee();
    List<UserDTO> getAllEmployeeAndManager();
    List<UserDTO> getAllUser();


    UserDTO updateUser(Long id, UserDTO userDTO);

    void deleteUser(Long id);

    ResponseDTO login(LoginRequestDTO loginDTO); // JWT generation

    // Role-based fetching
    List<UserDTO> getUsersByRole(String role);  // returns users based on role
}
