package com.project.CrudAPIs.dto;

import com.project.CrudAPIs.model.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;  // optional in responses
    private String phoneNumber;
    private String address;
    private Set<Role> roles;
}
