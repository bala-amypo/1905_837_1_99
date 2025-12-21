package com.example.demo.controller;

import com.example.demo.entity.DepreciationRule;
import com.example.demo.service.DepreciationRuleService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rules")
public class DepreciationRuleController {

    private final DepreciationRuleService ruleService;

    public DepreciationRuleController(DepreciationRuleService ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping("/")
    public DepreciationRule createRule(@RequestBody DepreciationRule rule) {
        return ruleService.createRule(rule);
    }

    @GetMapping("/")
    public List<DepreciationRule> getAllRules() {
        return ruleService.getAllRules();
    }
}
