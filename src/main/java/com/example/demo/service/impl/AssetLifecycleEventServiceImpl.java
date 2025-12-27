package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetLifecycleEventServiceImpl {
    private final AssetLifecycleEventRepository eventRepo;
    private final AssetRepository assetRepo;

    public AssetLifecycleEvent logEvent(Long assetId, AssetLifecycleEvent ev) {
        Asset a = assetRepo.findById(assetId).orElseThrow(() -> new ResourceNotFoundException("Asset not found"));
        if(ev.getEventDescription() == null || ev.getEventDescription().isBlank()) throw new IllegalArgumentException("Desc required");
        if(ev.getEventDate().isAfter(LocalDate.now())) throw new IllegalArgumentException("Future date");
        ev.setAsset(a);
        return eventRepo.save(ev);
    }
    public List<AssetLifecycleEvent> getEvents(Long assetId) { return eventRepo.findByAssetIdOrderByEventDateDesc(assetId); }
}