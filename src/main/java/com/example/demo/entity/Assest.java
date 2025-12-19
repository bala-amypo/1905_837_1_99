package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "assets", uniqueConstraints = @UniqueConstraint(columnNames = "asset_tag"))
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "asset_tag", nullable = false, unique = true)
    private String assetTag;

    @Column(nullable = false)
    private String assetName;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    @Column(name = "purchase_cost", nullable = false)
    private Double purchaseCost;

    @ManyToOne
    @JoinColumn(name = "rule_id", nullable = false)
    private DepreciationRule depreciationRule;

    @Column(nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "asset")
    private List<AssetLifecycleEvent> lifecycleEvents;

    @OneToOne(mappedBy = "asset")
    private AssetDisposal assetDisposal;

    public Asset() {
        this.status = "ACTIVE";
        this.createdAt = LocalDateTime.now();
    }

    public Asset(String assetTag, String assetName, Vendor vendor, LocalDate purchaseDate, Double purchaseCost, DepreciationRule depreciationRule) {
        this.assetTag = assetTag;
        this.assetName = assetName;
        this.vendor = vendor;
        this.purchaseDate = purchaseDate;
        this.purchaseCost = purchaseCost;
        this.depreciationRule = depreciationRule;
        this.status = "ACTIVE";
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getAssetTag() { return assetTag; }
    public String getAssetName() { return assetName; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}
