package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "depreciation_rules", uniqueConstraints = @UniqueConstraint(columnNames = "rule_name"))
public class DepreciationRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rule_name", nullable = false, unique = true)
    private String ruleName;

    @Column(nullable = false)
    private String method;

    @Column(name = "useful_life_years", nullable = false)
    private Integer usefulLifeYears;

    @Column(nullable = false)
    private Double salvageValue;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "depreciationRule")
    private List<Asset> assets;

    public DepreciationRule() {
        this.createdAt = LocalDateTime.now();
    }

    public DepreciationRule(String ruleName, String method, Integer usefulLifeYears, Double salvageValue) {
        this.ruleName = ruleName;
        this.method = method;
        this.usefulLifeYears = usefulLifeYears;
        this.salvageValue = salvageValue;
        this.createdAt = LocalDateTime.now();
    }
}
