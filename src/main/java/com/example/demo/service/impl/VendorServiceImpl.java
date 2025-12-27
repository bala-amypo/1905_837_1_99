package com.example.demo.service.impl;

import com.example.demo.entity.Vendor;
import com.example.demo.repository.VendorRepository;
import com.example.demo.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {
    private final VendorRepository vendorRepository;

    @Override
    public Vendor createVendor(Vendor vendor) {
        if (vendorRepository.findByVendorName(vendor.getVendorName()).isPresent()) {
            throw new IllegalArgumentException("Duplicate Vendor"); // [cite: 774]
        }
        if (vendor.getContactEmail() == null || !vendor.getContactEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid Email"); // [cite: 775]
        }
        return vendorRepository.save(vendor);
    }

    @Override
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    @Override
    public Vendor getVendor(Long id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
    }
}