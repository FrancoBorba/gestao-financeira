package com.franco.gestao_financeira.domain.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.franco.gestao_financeira.domain.exception.BusinessRuleException;
import com.franco.gestao_financeira.infrastructure.repository.BaseCategoryRepository;

@Service
public class PlanningService {

    private final BaseCategoryRepository baseCategoryRepository;

    public PlanningService(BaseCategoryRepository baseCategoryRepository) {
        this.baseCategoryRepository = baseCategoryRepository;
    }

    public void definirOrcamento(String categoria, BigDecimal valor) {
    }

    public BigDecimal calcularSobraDoMes(LocalDate mes) {
      return BigDecimal.ZERO;
    }
}
