package com.project.CrudAPIs.repository;

import com.project.CrudAPIs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRoles(String role);
    List<User> findByRolesIn(List<String> roles);

}
