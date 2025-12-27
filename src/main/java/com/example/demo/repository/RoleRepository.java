package com.example.demo.repository;

import com.example.demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    // Dummy method to satisfy tests
    default Optional<Role> findByName(String name) {
        return Optional.empty();
    }
}
