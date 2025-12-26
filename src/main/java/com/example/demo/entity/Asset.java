package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "assets")
@Data
@NoArgsConstructor
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String assetTag;

    private String assetName;

    @ManyToOne
    @JoinColumn(name = "vendor_id")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private Vendor vendor;

    private LocalDate purchaseDate;

    private Double purchaseCost;

    @ManyToOne
    @JoinColumn(name = "depreciation_rule_id")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private DepreciationRule depreciationRule;

    private String status = "ACTIVE";

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "asset" })
    private List<AssetLifecycleEvent> events;

    @OneToOne(mappedBy = "asset", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "asset" })
    private AssetDisposal disposal;

    public Asset(String assetTag, String assetName, Vendor vendor, LocalDate purchaseDate, Double purchaseCost,
            DepreciationRule depreciationRule, String status) {
        this.assetTag = assetTag;
        this.assetName = assetName;
        this.vendor = vendor;
        this.purchaseDate = purchaseDate;
        this.purchaseCost = purchaseCost;
        this.depreciationRule = depreciationRule;
        this.status = status != null ? status : "ACTIVE";
        this.createdAt = LocalDateTime.now();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAssetTag(String assetTag) {
        this.assetTag = assetTag;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setPurchaseCost(Double purchaseCost) {
        this.purchaseCost = purchaseCost;
    }

    public void setDepreciationRule(DepreciationRule depreciationRule) {
        this.depreciationRule = depreciationRule;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
