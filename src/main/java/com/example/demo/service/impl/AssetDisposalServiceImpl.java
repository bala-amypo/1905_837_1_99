package com.example.demo.service.impl;
import com.example.demo.entity.AssetDisposal;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import org.springframework.stereotype.Service;

@Service
public class AssetDisposalServiceImpl {
    private final AssetDisposalRepository disposalRepo;
    private final AssetRepository assetRepo;
    private final UserRepository userRepo;

    public AssetDisposalServiceImpl(AssetDisposalRepository disposalRepo, AssetRepository assetRepo, UserRepository userRepo) {
        this.disposalRepo = disposalRepo;
        this.assetRepo = assetRepo;
        this.userRepo = userRepo;
    }

    public AssetDisposal requestDisposal(Long assetId, AssetDisposal disposal) {
        var asset = assetRepo.findById(assetId).orElseThrow(() -> new ResourceNotFoundException("Asset not found"));
        if (disposal.getDisposalValue() < 0) throw new IllegalArgumentException("Invalid value");
        disposal.setAsset(asset);
        return disposalRepo.save(disposal);
    }

    public AssetDisposal approveDisposal(Long disposalId, Long adminId) {
        var disposal = disposalRepo.findById(disposalId).orElseThrow(() -> new ResourceNotFoundException("Disposal not found"));
        var admin = userRepo.findById(adminId).orElseThrow(() -> new ResourceNotFoundException("Admin not found"));
        
        // Ensure only admin can approve (Double check logic, usually handled by SecurityConfig too)
        boolean isAdmin = admin.getRoles().stream().anyMatch(r -> r.getName().equals("ADMIN"));
        if (!isAdmin) throw new IllegalArgumentException("User is not admin");

        disposal.setApprovedBy(admin);
        disposal.getAsset().setStatus("DISPOSED");
        assetRepo.save(disposal.getAsset());
        return disposalRepo.save(disposal);
    }
}