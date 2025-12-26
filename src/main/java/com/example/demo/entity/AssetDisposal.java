package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "asset_disposals")
@Data
@NoArgsConstructor
public class AssetDisposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "asset_id", nullable = false)
    @JsonIgnoreProperties({ "events", "disposal" })
    private Asset asset;

    private String disposalMethod;

    private Double disposalValue;

    private LocalDate disposalDate;

    @ManyToOne
    @JoinColumn(name = "approved_by_user_id")
    @JsonIgnoreProperties({ "password", "roles" })
    private User approvedBy;

    private LocalDateTime createdAt;

    public AssetDisposal(Asset asset, String disposalMethod, Double disposalValue, LocalDate disposalDate,
            User approvedBy) {
        this.asset = asset;
        this.disposalMethod = disposalMethod;
        this.disposalValue = disposalValue;
        this.disposalDate = disposalDate;
        this.approvedBy = approvedBy;
        this.createdAt = LocalDateTime.now();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public void setDisposalMethod(String disposalMethod) {
        this.disposalMethod = disposalMethod;
    }

    public void setDisposalValue(Double disposalValue) {
        this.disposalValue = disposalValue;
    }

    public void setDisposalDate(LocalDate disposalDate) {
        this.disposalDate = disposalDate;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
