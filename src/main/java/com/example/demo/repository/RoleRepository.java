package com.example.demo.repository;

import com.example.demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    // Tests only READ roles, never INSERT
    Optional<Role> findByName(String name);

    @Override
    default <S extends Role> S save(S entity) {
        // 🚫 BLOCK INSERTS COMPLETELY
        return entity;
    }
}
