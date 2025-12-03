package com.project.CrudAPIs.dto;

import com.project.CrudAPIs.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class ResponseDTO {
    private String token;
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<Role> roles;
}
