package com.example.demo.service.impl;

import com.example.demo.entity.DepreciationRule;
import com.example.demo.repository.DepreciationRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepreciationRuleServiceImpl {
    private final DepreciationRuleRepository repo;

    public DepreciationRule createRule(DepreciationRule r) {
        if(r.getUsefulLifeYears() <= 0) throw new IllegalArgumentException("Life must be > 0");
        if(r.getSalvageValue() < 0) throw new IllegalArgumentException("Salvage >= 0");
        return repo.save(r);
    }
    public List<DepreciationRule> getAllRules() { return repo.findAll(); }
}