package com.example.demo.service.impl;

import com.example.demo.entity.Vendor;
import com.example.demo.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl {
    private final VendorRepository repo;

    public Vendor createVendor(Vendor v) {
        if(repo.findByVendorName(v.getVendorName()).isPresent()) throw new IllegalArgumentException("Duplicate Vendor");
        if(!v.getContactEmail().contains("@")) throw new IllegalArgumentException("Invalid Email");
        return repo.save(v);
    }
    public List<Vendor> getAllVendors() { return repo.findAll(); }
    public Vendor getVendor(Long id) { return repo.findById(id).orElseThrow(() -> new RuntimeException("Not found")); }
}