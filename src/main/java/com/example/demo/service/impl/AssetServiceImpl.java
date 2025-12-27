package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl {
    private final AssetRepository assetRepo;
    private final VendorRepository vendorRepo;
    private final DepreciationRuleRepository ruleRepo;

    public Asset createAsset(Long vendorId, Long ruleId, Asset asset) {
        Vendor v = vendorRepo.findById(vendorId).orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        DepreciationRule r = ruleRepo.findById(ruleId).orElseThrow(() -> new ResourceNotFoundException("Rule not found"));
        if(asset.getPurchaseCost() <= 0) throw new IllegalArgumentException("Purchase cost must be > 0");
        if(assetRepo.existsByAssetTag(asset.getAssetTag())) throw new IllegalArgumentException("Duplicate Tag");
        
        asset.setVendor(v);
        asset.setDepreciationRule(r);
        return assetRepo.save(asset);
    }

    public List<Asset> getAllAssets() { return assetRepo.findAll(); }
    public Asset getAsset(Long id) { return assetRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not Found")); }
    public List<Asset> getAssetsByStatus(String status) { return assetRepo.findByStatus(status); }
}