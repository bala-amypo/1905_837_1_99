package com.example.demo.service.impl;
import com.example.demo.entity.Vendor;
import com.example.demo.repository.VendorRepository;
import com.example.demo.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VendorServiceImpl implements VendorService {
    @Autowired private VendorRepository repo;
    
    public Vendor createVendor(Vendor vendor) { return repo.save(vendor); }
    public List<Vendor> getAllVendors() { return repo.findAll(); }
}
