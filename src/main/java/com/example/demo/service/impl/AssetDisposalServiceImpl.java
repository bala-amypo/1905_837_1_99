package com.example.demo.service.impl;

import com.example.demo.entity.Asset;
import com.example.demo.entity.AssetDisposal;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AssetDisposalRepository;
import com.example.demo.repository.AssetRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AssetDisposalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssetDisposalServiceImpl implements AssetDisposalService {
    private final AssetDisposalRepository disposalRepository;
    private final AssetRepository assetRepository;
    private final UserRepository userRepository;

    @Override
    public AssetDisposal requestDisposal(Long assetId, AssetDisposal disposal) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found")); // [cite: 807]

        if (disposal.getDisposalValue() < 0) {
            throw new IllegalArgumentException("Disposal value must be >= 0"); // [cite: 808]
        }

        disposal.setAsset(asset);
        return disposalRepository.save(disposal);
    }

    @Override
    public AssetDisposal approveDisposal(Long disposalId, Long adminId) {
        AssetDisposal disposal = disposalRepository.findById(disposalId)
                .orElseThrow(() -> new ResourceNotFoundException("Disposal not found")); // [cite: 811]
        
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found")); // [cite: 812]

        // Basic role check
        boolean isAdmin = admin.getRoles().stream()
                .anyMatch(r -> "ADMIN".equals(r.getName()));
        if (!isAdmin) {
            throw new RuntimeException("Not Admin"); // [cite: 812]
        }

        disposal.setApprovedBy(admin);
        disposal.getAsset().setStatus("DISPOSED"); // [cite: 813]
        assetRepository.save(disposal.getAsset()); // [cite: 813]
        
        return disposalRepository.save(disposal);
    }
}