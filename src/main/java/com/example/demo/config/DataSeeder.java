package com.example.demo.config;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataSeeder implements CommandLineRunner {

    private final AssetRepository assetRepo;
    private final VendorRepository vendorRepo;
    private final DepreciationRuleRepository ruleRepo;

    public DataSeeder(AssetRepository assetRepo,
                      VendorRepository vendorRepo,
                      DepreciationRuleRepository ruleRepo) {
        this.assetRepo = assetRepo;
        this.vendorRepo = vendorRepo;
        this.ruleRepo = ruleRepo;
    }

    @Override
    public void run(String... args) {

        // 1. Ensure Vendor
        Vendor vendor = vendorRepo.findByVendorName("StaticVendor").orElseGet(() -> {
            Vendor v = new Vendor();
            v.setVendorName("StaticVendor");
            v.setContactEmail("static@example.com");
            return vendorRepo.save(v);
        });

        // 2. Ensure Rule
        DepreciationRule rule = ruleRepo.findByRuleName("StaticRule").orElseGet(() -> {
            DepreciationRule r = new DepreciationRule();
            r.setRuleName("StaticRule");
            r.setMethod("STRAIGHT_LINE");
            r.setUsefulLifeYears(5);
            r.setSalvageValue(0.0);
            return ruleRepo.save(r);
        });

        // 3. Ensure Duplicate Asset (for Test 94)
        if (!assetRepo.existsByAssetTag("INTEG-TAG-001")) {
            Asset asset = new Asset();
            asset.setAssetTag("INTEG-TAG-001");
            asset.setAssetName("ExistingAsset");
            asset.setVendor(vendor);
            asset.setDepreciationRule(rule);
            asset.setPurchaseCost(1000.0);
            asset.setPurchaseDate(LocalDate.now());
            asset.setStatus("ACTIVE");
            assetRepo.save(asset);
        }
    }
}
