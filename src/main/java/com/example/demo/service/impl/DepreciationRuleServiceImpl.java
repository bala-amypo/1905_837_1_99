package com.example.demo.service.impl;

import com.example.demo.entity.DepreciationRule;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.DepreciationRuleRepository;
import com.example.demo.service.DepreciationRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepreciationRuleServiceImpl implements DepreciationRuleService {

    private final DepreciationRuleRepository depreciationRuleRepository;

    @Override
    public DepreciationRule createRule(DepreciationRule rule) {
        if (rule.getUsefulLifeYears() == null || rule.getUsefulLifeYears() <= 0) {
            throw new BadRequestException("Useful life years must be greater than 0");
        }
        if (rule.getSalvageValue() == null || rule.getSalvageValue() < 0) {
            throw new BadRequestException("Salvage value must be greater than or equal to 0");
        }
        if (!"STRAIGHT_LINE".equals(rule.getMethod()) && !"DECLINING_BALANCE".equals(rule.getMethod())) {
            throw new BadRequestException("Method must be STRAIGHT_LINE or DECLINING_BALANCE");
        }

        if (depreciationRuleRepository.findByRuleName(rule.getRuleName()).isPresent()) {
            throw new BadRequestException("Rule name must be unique");
        }

        rule.setCreatedAt(LocalDateTime.now());
        return depreciationRuleRepository.save(rule);
    }

    @Override
    public List<DepreciationRule> getAllRules() {
        return depreciationRuleRepository.findAll();
    }
}
