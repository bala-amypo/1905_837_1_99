package com.example.demo.service.impl;
import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.AssetDisposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetDisposalServiceImpl implements AssetDisposalService {
    @Autowired private AssetDisposalRepository disposalRepo;
    @Autowired private AssetRepository assetRepo;

    public AssetDisposal requestDisposal(Long assetId, AssetDisposal disposal) {
        Asset asset = assetRepo.findById(assetId).orElseThrow(() -> new ResourceNotFoundException("Asset not found"));
        disposal.setAsset(asset);
        // Note: Actual status update happens on approval (skipped as requested)
        return disposalRepo.save(disposal);
    }
}
