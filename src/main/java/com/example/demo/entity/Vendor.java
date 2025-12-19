package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "vendors", uniqueConstraints = @UniqueConstraint(columnNames = "vendor_name"))
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vendor_name", nullable = false, unique = true)
    private String vendorName;

    @Column(nullable = false)
    private String contactEmail;

    private String phone;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "vendor")
    private List<Asset> assets;

    public Vendor() {
        this.createdAt = LocalDateTime.now();
    }

    public Vendor(String vendorName, String contactEmail, String phone) {
        this.vendorName = vendorName;
        this.contactEmail = contactEmail;
        this.phone = phone;
        this.createdAt = LocalDateTime.now();
    }
}
