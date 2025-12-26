package com.example.demo.controller;
import com.example.demo.entity.AssetLifecycleEvent;
import com.example.demo.service.impl.AssetLifecycleEventServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class AssetLifecycleEventController {
    private final AssetLifecycleEventServiceImpl service;
    public AssetLifecycleEventController(AssetLifecycleEventServiceImpl service) { this.service = service; }

    @PostMapping("/{assetId}")
    public ResponseEntity<AssetLifecycleEvent> create(@PathVariable Long assetId, @RequestBody AssetLifecycleEvent event) {
        return ResponseEntity.ok(service.logEvent(assetId, event));
    }

    @GetMapping("/asset/{assetId}")
    public ResponseEntity<?> getByAsset(@PathVariable Long assetId) { return ResponseEntity.ok(service.getEvents(assetId)); }
}