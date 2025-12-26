package com.example.demo.service.impl;

import com.example.demo.entity.Vendor;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.VendorRepository;
import com.example.demo.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    @Override
    public Vendor createVendor(Vendor vendor) {
        if (vendorRepository.findByVendorName(vendor.getVendorName()).isPresent()) {
            throw new BadRequestException("Vendor name must be unique");
        }

        vendor.setCreatedAt(LocalDateTime.now());
        return vendorRepository.save(vendor);
    }

    @Override
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }
}
