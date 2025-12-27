package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssetDisposalServiceImpl {
    private final AssetDisposalRepository disposalRepo;
    private final AssetRepository assetRepo;
    private final UserRepository userRepo;

    public AssetDisposal requestDisposal(Long assetId, AssetDisposal d) {
        Asset a = assetRepo.findById(assetId).orElseThrow(() -> new ResourceNotFoundException("Asset not found"));
        d.setAsset(a);
        return disposalRepo.save(d);
    }

    public AssetDisposal approveDisposal(Long disposalId, Long adminId) {
        AssetDisposal d = disposalRepo.findById(disposalId).orElseThrow(() -> new ResourceNotFoundException("Disposal not found"));
        User admin = userRepo.findById(adminId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Simple role check logic
        boolean isAdmin = admin.getRoles().stream().anyMatch(r -> r.getName().equals("ADMIN"));
        if(!isAdmin) throw new RuntimeException("Not Admin");

        d.setApprovedBy(admin);
        d.getAsset().setStatus("DISPOSED");
        assetRepo.save(d.getAsset());
        return disposalRepo.save(d);
    }
}