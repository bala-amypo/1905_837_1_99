package com.example.demo.controller;

import com.example.demo.entity.DepreciationRule;
import com.example.demo.service.DepreciationRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
public class DepreciationRuleController {

    private final DepreciationRuleService depreciationRuleService;

    @PostMapping("")
    public ResponseEntity<DepreciationRule> createRule(@RequestBody DepreciationRule rule) {
        return ResponseEntity.ok(depreciationRuleService.createRule(rule));
    }

    @GetMapping("")
    public ResponseEntity<List<DepreciationRule>> getAllRules() {
        return ResponseEntity.ok(depreciationRuleService.getAllRules());
    }
}
