package com.example.demo.service.impl;

import com.example.demo.entity.DepreciationRule;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.DepreciationRuleRepository;
import com.example.demo.service.DepreciationRuleService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DepreciationRuleServiceImpl implements DepreciationRuleService {

    private final DepreciationRuleRepository ruleRepository;

    public DepreciationRuleServiceImpl(DepreciationRuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    @Override
    public DepreciationRule createRule(DepreciationRule rule) {
        if (rule.getUsefulLifeYears() == null || rule.getUsefulLifeYears() <= 0) {
            throw new BadRequestException("Useful life must be > 0");
        }
        if (rule.getSalvageValue() == null || rule.getSalvageValue() < 0) {
            throw new BadRequestException("Salvage value must be >= 0");
        }
        if (!"STRAIGHT_LINE".equals(rule.getMethod()) && !"DECLINING_BALANCE".equals(rule.getMethod())) {
            throw new BadRequestException("Invalid depreciation method. Use STRAIGHT_LINE or DECLINING_BALANCE");
        }
        
        rule.setCreatedAt(LocalDateTime.now());
        return ruleRepository.save(rule);
    }

    @Override
    public List<DepreciationRule> getAllRules() {
        return ruleRepository.findAll();
    }
}
