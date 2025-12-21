package com.example.demo.service.impl;
import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.AssetLifecycleEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AssetLifecycleEventServiceImpl implements AssetLifecycleEventService {
    @Autowired private AssetLifecycleEventRepository eventRepo;
    @Autowired private AssetRepository assetRepo;

    public AssetLifecycleEvent logEvent(Long assetId, AssetLifecycleEvent event) {
        Asset asset = assetRepo.findById(assetId).orElseThrow(() -> new ResourceNotFoundException("Asset not found"));
        event.setAsset(asset);
        return eventRepo.save(event);
    }
    public List<AssetLifecycleEvent> getEvents(Long assetId) {
        return eventRepo.findByAssetIdOrderByEventDateDesc(assetId);
    }
}
