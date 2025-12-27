package com.example.demo.controller;
import com.example.demo.entity.DepreciationRule;
import com.example.demo.service.impl.DepreciationRuleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
public class DepreciationRuleController {
    private final DepreciationRuleServiceImpl service;
    @PostMapping public DepreciationRule create(@RequestBody DepreciationRule r) { return service.createRule(r); }
    @GetMapping public List<DepreciationRule> getAll() { return service.getAllRules(); }
}