package com.example.demo.service.impl;

import com.example.demo.entity.Asset;
import com.example.demo.entity.AssetLifecycleEvent;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AssetLifecycleEventRepository;
import com.example.demo.repository.AssetRepository;
import com.example.demo.service.AssetLifecycleEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetLifecycleEventServiceImpl implements AssetLifecycleEventService {
    private final AssetLifecycleEventRepository eventRepository;
    private final AssetRepository assetRepository;

    @Override
    public AssetLifecycleEvent logEvent(Long assetId, AssetLifecycleEvent event) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found")); // [cite: 795]

        if (event.getEventDescription() == null || event.getEventDescription().isBlank()) {
            throw new IllegalArgumentException("Desc required"); // [cite: 796]
        }
        if (event.getEventDate() != null && event.getEventDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Future date"); // [cite: 797]
        }

        event.setAsset(asset);
        // loggedAt handled by @PrePersist
        return eventRepository.save(event);
    }

    @Override
    public List<AssetLifecycleEvent> getEventsForAsset(Long assetId) {
        if (!assetRepository.existsById(assetId)) {
            throw new ResourceNotFoundException("Asset not found");
        }
        return eventRepository.findByAssetIdOrderByEventDateDesc(assetId); // [cite: 799]
    }
}