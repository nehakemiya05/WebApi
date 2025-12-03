package com.project.CrudAPIs.controller;

import com.project.CrudAPIs.dto.LoginRequestDTO;
import com.project.CrudAPIs.dto.ResponseDTO;
import com.project.CrudAPIs.dto.UserDTO;
import com.project.CrudAPIs.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.createUser(userDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> loginUser(@RequestBody LoginRequestDTO loginDTO) {
        ResponseDTO response = userService.login(loginDTO);
        return ResponseEntity.ok(response);
    }

}
