package com.example.demo.controller;

import com.example.demo.entity.AssetDisposal;
import com.example.demo.service.AssetDisposalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/disposals")
@RequiredArgsConstructor
public class AssetDisposalController {

    private final AssetDisposalService disposalService;

    @PostMapping("/request/{assetId}")
    public ResponseEntity<AssetDisposal> requestDisposal(@PathVariable Long assetId,
            @RequestBody AssetDisposal disposal) {
        return ResponseEntity.ok(disposalService.requestDisposal(assetId, disposal));
    }

    @PutMapping("/approve/{disposalId}/{adminId}")
    public ResponseEntity<AssetDisposal> approveDisposal(@PathVariable Long disposalId, @PathVariable Long adminId) {
        return ResponseEntity.ok(disposalService.approveDisposal(disposalId, adminId));
    }
}
