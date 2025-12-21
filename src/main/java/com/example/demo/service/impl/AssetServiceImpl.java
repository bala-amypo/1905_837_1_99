package com.example.demo.service.impl;
import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {
    @Autowired private AssetRepository assetRepo;
    @Autowired private VendorRepository vendorRepo;
    @Autowired private DepreciationRuleRepository ruleRepo;

    public Asset createAsset(Long vendorId, Long ruleId, Asset asset) {
        if(assetRepo.existsByAssetTag(asset.getAssetTag())) throw new IllegalArgumentException("Duplicate Tag");
        Vendor v = vendorRepo.findById(vendorId).orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        DepreciationRule r = ruleRepo.findById(ruleId).orElseThrow(() -> new ResourceNotFoundException("Rule not found"));
        
        asset.setVendor(v);
        asset.setDepreciationRule(r);
        asset.setStatus("ACTIVE");
        return assetRepo.save(asset);
    }

    public List<Asset> getAllAssets() { return assetRepo.findAll(); }
    public List<Asset> getAssetsByStatus(String status) { return assetRepo.findByStatus(status); }
    public Asset getAsset(Long id) {
        return assetRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Asset not found"));
    }
}
