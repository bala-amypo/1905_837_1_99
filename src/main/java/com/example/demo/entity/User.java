package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonIgnore // Prevent JSON serialization of password
    private String password;

    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonIgnore // Prevent Recursion in JSON
    private Set<Role> roles = new HashSet<>();

    public User() {}

    @PrePersist
    public void prePersist() { this.createdAt = LocalDateTime.now(); }

    // --- MANUAL GETTERS AND SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    // --- SAFETY METHODS (PREVENT STACK OVERFLOW) ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        // If IDs are null, they are not equal unless it's the exact same object instance
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        // Use a constant or class-based hash if ID is null to be safe
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        // NEVER include 'roles' here
        return "User{id=" + id + ", email='" + email + "'}";
    }
}