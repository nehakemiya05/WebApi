package com.project.CrudAPIs.controller;

import com.project.CrudAPIs.dto.UserDTO;
import com.project.CrudAPIs.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final UserService userService;

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getManagersAndEmployees() {
        List<UserDTO> users = userService.getAllEmployeeAndManager();
        return ResponseEntity.ok(users);
    }
}
