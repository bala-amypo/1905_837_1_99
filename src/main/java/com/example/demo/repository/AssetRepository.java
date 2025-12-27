package com.example.demo.repository;

import com.example.demo.entity.Asset;
import com.example.demo.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    boolean existsByAssetTag(String assetTag); // [cite: 711]
    List<Asset> findByStatus(String status);   // [cite: 712]
    List<Asset> findByVendor(Vendor vendor);   // [cite: 713]
}