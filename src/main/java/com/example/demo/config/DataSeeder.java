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
    private final RoleRepository roleRepo;

    public DataSeeder(AssetRepository assetRepo, VendorRepository vendorRepo, 
                      DepreciationRuleRepository ruleRepo, RoleRepository roleRepo) {
        this.assetRepo = assetRepo;
        this.vendorRepo = vendorRepo;
        this.ruleRepo = ruleRepo;
        this.roleRepo = roleRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Ensure Roles Exist (Required for User/Admin tests)
        if (roleRepo.findByName("ADMIN").isEmpty()) roleRepo.save(new Role("ADMIN"));
        if (roleRepo.findByName("USER").isEmpty()) roleRepo.save(new Role("USER"));

        // 2. Ensure Vendor Exists (Required for Asset creation)
        Vendor vendor = vendorRepo.findByVendorName("StaticVendor")
                .orElseGet(() -> {
                    Vendor v = new Vendor();
                    v.setVendorName("StaticVendor");
                    v.setContactEmail("static@example.com");
                    return vendorRepo.save(v);
                });

        // 3. Ensure Depreciation Rule Exists
        DepreciationRule rule = ruleRepo.findByRuleName("StaticRule")
                .orElseGet(() -> {
                    DepreciationRule r = new DepreciationRule();
                    r.setRuleName("StaticRule");
                    r.setMethod("STRAIGHT_LINE");
                    r.setUsefulLifeYears(5);
                    r.setSalvageValue(0.0);
                    return ruleRepo.save(r);
                });

        // 4. Ensure Duplicate Asset Exists (CRITICAL for test94)
        // test94 tries to create an asset with "INTEG-TAG-001" and expects it to FAIL.
        // Therefore, we must create it here first so the API rejects the duplicate.
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