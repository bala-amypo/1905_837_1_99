package com.example.demo.service.impl;

import com.example.demo.entity.Asset;
import com.example.demo.entity.DepreciationRule;
import com.example.demo.entity.Vendor;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AssetRepository;
import com.example.demo.repository.DepreciationRuleRepository;
import com.example.demo.repository.VendorRepository;
import com.example.demo.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {
    private final AssetRepository assetRepository;
    private final VendorRepository vendorRepository;
    private final DepreciationRuleRepository ruleRepository;

    @Override
    public Asset createAsset(Long vendorId, Long ruleId, Asset asset) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found")); // [cite: 760]
        DepreciationRule rule = ruleRepository.findById(ruleId)
                .orElseThrow(() -> new ResourceNotFoundException("Rule not found"));   // [cite: 760]

        if (asset.getPurchaseCost() <= 0) {
            throw new IllegalArgumentException("Purchase cost must be > 0"); // [cite: 761]
        }
        if (assetRepository.existsByAssetTag(asset.getAssetTag())) {
            throw new IllegalArgumentException("Duplicate Tag"); // [cite: 762]
        }

        asset.setVendor(vendor);
        asset.setDepreciationRule(rule);
        // Status and createdAt handled by @PrePersist or entity default
        return assetRepository.save(asset);
    }

    @Override
    public List<Asset> getAssetsByStatus(String status) {
        return assetRepository.findByStatus(status);
    }

    @Override
    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    @Override
    public Asset getAsset(Long id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found")); // [cite: 766]
    }
}