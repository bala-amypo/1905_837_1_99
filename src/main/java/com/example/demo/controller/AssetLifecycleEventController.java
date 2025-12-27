package com.example.demo.controller;

import com.example.demo.entity.AssetLifecycleEvent;
import com.example.demo.service.impl.AssetLifecycleEventServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class AssetLifecycleEventController {
    private final AssetLifecycleEventServiceImpl service;

    @PostMapping("/{assetId}")
    public AssetLifecycleEvent create(@PathVariable Long assetId, @RequestBody AssetLifecycleEvent e) {
        return service.logEvent(assetId, e);
    }

    @GetMapping("/asset/{assetId}")
    public List<AssetLifecycleEvent> get(@PathVariable Long assetId) {
        // FIX: The method name in the service is 'getEventsForAsset'
        return service.getEventsForAsset(assetId);
    }
}