package com.example.demo.controller;

import com.example.demo.entity.AssetDisposal;
import com.example.demo.service.AssetDisposalService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/disposals")
public class AssetDisposalController {

    private final AssetDisposalService disposalService;

    public AssetDisposalController(AssetDisposalService disposalService) {
        this.disposalService = disposalService;
    }

    @PostMapping("/request/{assetId}")
    public AssetDisposal requestDisposal(@PathVariable Long assetId, 
                                         @RequestBody AssetDisposal disposal) {
        return disposalService.requestDisposal(assetId, disposal);
    }

    @PutMapping("/approve/{disposalId}/{adminId}")
    public AssetDisposal approveDisposal(@PathVariable Long disposalId, 
                                         @PathVariable Long adminId) {
        return disposalService.approveDisposal(disposalId, adminId);
    }
}
