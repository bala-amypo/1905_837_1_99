package com.example.demo.service;

import com.example.demo.entity.Asset;
import java.util.List;

public interface AssetService {
    Asset createAsset(Long vendorId, Long ruleId, Asset asset);
    List<Asset> getAllAssets();
    List<Asset> getAssetsByStatus(String status);
    Asset getAsset(Long id);
}
