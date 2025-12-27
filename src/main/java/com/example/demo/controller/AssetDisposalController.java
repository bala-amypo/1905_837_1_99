package com.example.demo.controller;
import com.example.demo.entity.AssetDisposal;
import com.example.demo.service.impl.AssetDisposalServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/disposals")
@RequiredArgsConstructor
public class AssetDisposalController {
    private final AssetDisposalServiceImpl service;
    @PostMapping("/request/{assetId}")
    public AssetDisposal request(@PathVariable Long assetId, @RequestBody AssetDisposal d) {
        return service.requestDisposal(assetId, d);
    }
    @PutMapping("/approve/{did}/{uid}")
    public AssetDisposal approve(@PathVariable Long did, @PathVariable Long uid) {
        return service.approveDisposal(did, uid);
    }
}