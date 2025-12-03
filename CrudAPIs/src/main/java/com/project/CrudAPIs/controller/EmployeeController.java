package com.project.CrudAPIs.controller;

import com.project.CrudAPIs.dto.UserDTO;
import com.project.CrudAPIs.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<UserDTO>> getAllEmployees() {
        List<UserDTO> employees = userService.getAllEmployee();
        return ResponseEntity.ok(employees);
    }
}
