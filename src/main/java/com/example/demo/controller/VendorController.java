package com.example.demo.controller;
import com.example.demo.entity.Vendor;
import com.example.demo.service.impl.VendorServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vendors")
@RequiredArgsConstructor
public class VendorController {
    private final VendorServiceImpl service;
    @PostMapping public Vendor create(@RequestBody Vendor v) { return service.createVendor(v); }
    @GetMapping public List<Vendor> getAll() { return service.getAllVendors(); }
    @GetMapping("/{id}") public Vendor get(@PathVariable Long id) { return service.getVendor(id); }
}