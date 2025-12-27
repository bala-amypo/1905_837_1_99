package com.example.demo.controller;

import com.example.demo.entity.Asset;
import com.example.demo.service.impl.AssetServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {
    private final AssetServiceImpl service;

    @PostMapping("/{vid}/{rid}")
    public Asset create(@PathVariable Long vid, @PathVariable Long rid, @RequestBody Asset a) {
        return service.createAsset(vid, rid, a);
    }
    @GetMapping
    public List<Asset> getAll() { return service.getAllAssets(); }
    
    @GetMapping("/{id}")
    public Asset get(@PathVariable Long id) { return service.getAsset(id); }
    
    @GetMapping("/status/{status}")
    public List<Asset> getByStatus(@PathVariable String status) { return service.getAssetsByStatus(status); }
}